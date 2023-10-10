import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;  
import java.util.Date;  

public class Commit {
    private String sTreeSha, sParentSha, sChildSha, sAuthor, sDate, sSummary, sCommitSha;
    public Commit(String sParentSha, String sAuthor, String sSummary) throws Exception {
        this.sParentSha = sParentSha;
        this.sTreeSha = constructTreeSha();
        this.sChildSha = "";
        this.sAuthor = sAuthor;
        this.sDate = getDate();
        this.sSummary = sSummary;
    }

    public Commit(String sAuthor, String sSummary) throws Exception {
        this("", sAuthor, sSummary);
    }

    public void commit() throws Exception {
        StringBuilder sCommit = new StringBuilder(
            this.sTreeSha   + "\n" +
            this.sParentSha + "\n" +
            this.sAuthor    + "\n" +
            this.sDate      + "\n" +
            this.sSummary
        );

        this.sCommitSha = Blob.encryptPassword(sCommit.toString());

        sCommit.insert(sCommit.indexOf("\n", sCommit.indexOf("\n") + 1), this.sChildSha + "\n");

        FileWriter f_writer = new FileWriter("objects/" + this.sCommitSha);
        f_writer.write(sCommit.toString());
        f_writer.close();
    }

    public void setParentSha(String parentSha) {
        this.sParentSha = parentSha;
    }
    
    public String getParentSha() {
        return sParentSha;
    }

    public void setNextSha(String nextSha) {
        this.sChildSha = nextSha;
    }
    
    public String getNextSha() {
        return sChildSha;
    }

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
        Date date = new Date();  
        return sdf.format(date);  
    }

    public String constructTreeSha() throws Exception {
        Tree tree = new Tree();
        Index index = new Index();//???
        //File indexFile = new File("index");

        int counter = numDeletedEdited();
        //adding prevTree sha
        if(counter==0) {
            if(sParentSha != null && !sParentSha.equals("")) {
                File commFile = new File("./objects/" + sParentSha);
                BufferedReader br1 = new BufferedReader(new FileReader(commFile));
                String prevTreeSha = br1.readLine();
                br1.close();
                String entry = "tree : " + prevTreeSha;
                tree.add(entry);
            }
        }

        //check deleted files? but how would u know?? unless u looped through index already?? but u do that below??????????
        BufferedReader br0 = new BufferedReader(new FileReader("index"));
        while(br0.ready()) {
            String line = br0.readLine();
            String fileName = "";
            if(line.contains("*deleted*") || line.contains("*edited*")) {
                fileName = line.substring(10);
                //PROBABLY NEED TO RENAME THE PREVIOUS COMMIT???
                //WOULD I NEED TO DO AN ERROR IF THERES NO PARENT SHA??????
                String treeSha = tree.findDeletedFileTree(sParentSha,fileName);//if it even has a parentsha
                tree.add("tree : " + treeSha);
                addAllFilesToTree(tree, treeSha, sParentSha, fileName);

                if(line.contains("*edited*")) {
                    Blob editedFile = new Blob(fileName);
                    String editedSha = editedFile.getSha1();
                    tree.add("blob : " + editedSha + " : " + fileName);
                }

                //need to remove the file afterwwards
                index.removeLine(line);
            }
        }
        br0.close();

        //adding file contents
        //BUT if there is an asterisk before it u should not add it? what do u do for edit?
        BufferedReader br = new BufferedReader(new FileReader("index"));
        while(br.ready()) {
            String str = br.readLine();
            String type = str.substring(0,4);
            if(type.equals("tree")) {
                //String entryName = str.substring(50);
                tree.add(str);//wouldn't this add all things in that directory? but those same files would be in Index too? that would be duplicated? OH, TREE CANT DO DUPLICATE ok got it
            }
            else {
                tree.add(str);//why cant i just do this? whats the point of addDirecotry?
            }
            String fileName = str.substring(50);
            index.removeBlob(fileName);//fix file not found
        }
        br.close();

        return tree.getSha();
    }

    private void addAllFilesToTree(Tree tree, String stopSha, String currentTSha, String fileName) throws Exception {
        //fileName = the name of file deleted/edited
        //File treeFile = new File(currentTSha);
        //String contents = getFileContents(currentTSha);
        String prevTreeSha = getFirstLine(currentTSha);//get first line of the file thing (but this might not exist!!)

        if(currentTSha.equals(stopSha)) { }
        else {
            //String treeContents = getFileContents(currentTSha);
            addFileContentsToTree(currentTSha, tree);
            if(prevTreeSha.length()!=47) {
                addAllFilesToTree(tree,stopSha,prevTreeSha,fileName);
            }
            else {
                throw new Exception ("File not found");
            }
        }

    }

    private void addFileContentsToTree(String fromFileName, Tree tree) throws IOException, NoSuchAlgorithmException {
        //Index index = new Index();
        BufferedReader br = new BufferedReader(new FileReader(fromFileName));
        String firstLine = getFirstLine(fromFileName);
        if(firstLine.length() == 47) {
            br.readLine();
        }
        while(br.ready()) {
            String str = br.readLine();
            String type = str.substring(0,4);
            if(type.equals("tree")) {
                //String entryName = str.substring(50);
                tree.add(str);//wouldn't this add all things in that directory? but those same files would be in Index too? that would be duplicated? OH, TREE CANT DO DUPLICATE ok got it
            }
            else if (type.equals("blob")) {
                tree.add(str);//why cant i just do this? whats the point of addDirecotry?
            }
        }
        br.close();
    }

    private int numDeletedEdited() throws IOException {
        BufferedReader br0 = new BufferedReader(new FileReader("index"));
        int counter = 0;
        while(br0.ready()) {
            String line = br0.readLine();
            if(line.contains("*deleted*") || line.contains("*edited*")) {
                counter++;
            }
        }
        br0.close();
        return counter;
    }

    public String getCurrentTreeSha() {
        return sTreeSha;
    }

    public String getCommitSha() {
        return this.sCommitSha;
    }

    public String getTreeSha(String commitSha) throws Exception {
        File commFile = new File(commitSha);
        BufferedReader br = new BufferedReader(new FileReader(commFile));
        String treeSha = br.readLine();
        br.close();
        if(treeSha.length()==47) {
            return treeSha;
        }
        else {
            throw new Exception("No tree sha");
        }
    }

    public String getFileContents(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String contents = "";
        while(br.ready()) {
            contents += br.readLine() + "\n";
        }
        br.close();
        contents = contents.trim();
        return contents;
    }

    private String getFirstLine(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String firstLine = br.readLine();
        br.close();
        return firstLine;
    }
}
