import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

        commit();
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

        createHead();
        System.out.println("COMMIT FILE:\n" + sCommit.toString());
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
        /*ArrayList<String> indexFiles = new ArrayList<String>();
        BufferedReader br0 = new BufferedReader(new FileReader("index"));
        while(br0.ready()) {
            String line = br0.readLine();
            String fileName = "";
            if(line.contains("*deleted*") || line.contains("*edited*")) {
                fileName = line.substring(10);
                //PROBABLY NEED TO RENAME THE PREVIOUS COMMIT???
                //WOULD I NEED TO DO AN ERROR IF THERES NO PARENT SHA??????
                String prevTreeSha = getTreeSha(sParentSha);
                String treeSha = tree.findDeletedFileTree(prevTreeSha,fileName);//if it even has a parentsha
                indexFiles.add("tree : " + treeSha);//bug???????
                addAllFilesToTree(indexFiles, treeSha, prevTreeSha, fileName);//BUGBUGBUGBUGBUGBUG

                if(line.contains("*edited*")) {
                    Blob editedFile = new Blob(fileName);
                    String editedSha = editedFile.getSha1();
                    indexFiles.add("blob : " + editedSha + " : " + fileName);
                }

                //need to remove the file afterwwards
                index.removeLine(line);
            }
        }
        br0.close();*/





        //puts deleted and edited files into an arraylist
        if(counter>0) {
            ArrayList<String> allDeleteEditFiles = new ArrayList<String>();
            ArrayList<String> tempDeletedEditedFiles = new ArrayList<String>();
            ArrayList<String> deletedFiles = new ArrayList<String>();
            ArrayList<String> editedFiles = new ArrayList<String>();
            BufferedReader br0 = new BufferedReader(new FileReader("index"));
            while(br0.ready()) {
                String line = br0.readLine();
                if(line.contains("*deleted*")) {
                    deletedFiles.add(line);
                    allDeleteEditFiles.add(line);
                    tempDeletedEditedFiles.add(line);
                    index.removeLine(line);//removes the line from index
                }
                else if (line.contains("*edited*")) {
                    editedFiles.add(line);
                    allDeleteEditFiles.add(line);
                    tempDeletedEditedFiles.add(line);
                    index.removeLine(line);
                }
            }
            br0.close();


            ArrayList<String> indexFiles = new ArrayList<String>();
            String prevTreeSha = getTreeSha(sParentSha);
            //ArrayList<String> tempDeletedEditedFiles = allDeleteEditFiles;
            String newLinkTreeSha = tree.findDeletedFileTree(prevTreeSha, tempDeletedEditedFiles);
            if(!newLinkTreeSha.equals("")) {
                tree.add("tree : " + newLinkTreeSha);//first line added!
            }


            //deletedEditedFilesLength = allDeleteEditFiles.size();
            //int tempLength = tempDeletedEditedFiles.size();

            addAllFilesToIndexList(indexFiles, newLinkTreeSha, prevTreeSha);
            //ADD ARRAYLIST STUFF INTO TREE

            int deletedFilesLength = deletedFiles.size();
            int deletedEditedFilesLength = allDeleteEditFiles.size();


            for(int i=0; i<indexFiles.size(); i++) {
                String line = indexFiles.get(i);
                String fileName = line.substring(50);
                if(!isDeletedFile(allDeleteEditFiles, fileName)) {
                    tree.add(line);
                }
                else if(isEditedFile(editedFiles, fileName)) {
                    Blob editedFile = new Blob(fileName);
                    String editedSha = editedFile.getSha1();
                    tree.add("blob : " + editedSha + " : " + fileName);
                }
            }
        }

        

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

    private void addAllFilesToIndexList(ArrayList<String> indexFiles, String stopSha, String currentTSha) throws Exception {
        //fileName = the name of file deleted/edited
        //File treeFile = new File(currentTSha);
        //String contents = getFileContents(currentTSha);
        String firstLine = getFirstLine(currentTSha);//get first line of the file thing (but this might not exist!!)

        if(currentTSha.equals(stopSha)) { }//????????
        else {
            //String treeContents = getFileContents(currentTSha);
            addFileContentsToIndexList(currentTSha, indexFiles);
            if(firstLine.length()==47) {//previous tree's sha!
                String prevTreeSha = getFirstLine(currentTSha).substring(7);
                addAllFilesToIndexList(indexFiles,stopSha,prevTreeSha);
            }
            else {
                throw new Exception ("File not found");
            }
        }

    }

    private void addFileContentsToIndexList(String fromFileName, ArrayList<String> indexFiles) throws IOException, NoSuchAlgorithmException {
        String dirName = "./objects/";
        File dir = new File (dirName);
        File file = new File(dir,fromFileName);

        //Index index = new Index();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String firstLine = getFirstLine(fromFileName);
        if(firstLine.length() == 47) {
            br.readLine();
        }
        while(br.ready()) {
            String str = br.readLine();
            // String type = str.substring(0,4);
            // if(type.equals("tree")) {
            //     //String entryName = str.substring(50);
            //     indexFiles.add(str);//wouldn't this add all things in that directory? but those same files would be in Index too? that would be duplicated? OH, TREE CANT DO DUPLICATE ok got it
            // }
            // else if (type.equals("blob")) {
            //     indexFiles.add(str);//why cant i just do this? whats the point of addDirecotry?
            // }//this literally still adds it into it.......KFJLDSHFAKDSJCHSDKCFJHSDFKJSDHFLKDSJFHLKSDJFH
            indexFiles.add(str);//is this local??? to method?????????
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
        File commFile = new File("./objects/" + commitSha);
        BufferedReader br = new BufferedReader(new FileReader(commFile));
        String treeSha = br.readLine();
        br.close();
        if(treeSha.length()==40) {
            return treeSha;
        }
        else {
            throw new Exception("No tree sha");
        }
    }

    public String getFileContents(String fileName) throws IOException {
        String dirName = "./objects/";
        File dir = new File (dirName);
        File file = new File(dir,fileName);

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
        String dirName = "./objects/";
        File dir = new File (dirName);
        File file = new File(dir,fileName);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String firstLine = br.readLine();
        br.close();
        return firstLine;
    }

    private boolean isDeletedFile(ArrayList<String> deletedFiles, String fileName) {
        for(int i=0; i<deletedFiles.size(); i++) {
            String entry = deletedFiles.get(i);
            if(entry.contains(fileName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEditedFile(ArrayList<String> editedFiles, String fileName) {
        for(int i=0; i<editedFiles.size(); i++) {
            if(editedFiles.get(i).contains(fileName)) {
                return true;
            }
        }
        return false;
    }

    public void createHead() throws IOException {
        String dirName = "./objects/";
        File dir = new File (dirName);
        File head = new File(dir,"HEAD");
        if(!head.exists()) {
            head.createNewFile();
        }
        PrintWriter pw = new PrintWriter(new FileWriter(head));
        String currentCommitSha = getCommitSha();
        pw.print(currentCommitSha);
        pw.close();
    }

    public void checkout(String commitSha) throws Exception {
        String currentTreeSha = getCurrentTreeSha();
        String dirName = "./objects/";
        File dir = new File (dirName);
        File currentTreeFile = new File(dir,currentTreeSha);

        String oldTreeSha = getTreeSha(commitSha);
        String oldContents = getFileContents(oldTreeSha);
        PrintWriter pw = new PrintWriter(new FileWriter(currentTreeFile));
        pw.print(oldContents);
        pw.close();
        //sTreeSha = oldTreeSha;
        sTreeSha = Blob.encryptPassword(oldContents);
        commit();
    }
}
