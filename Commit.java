import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

        //adding prevTree sha
        if(sParentSha != null && !sParentSha.equals("")) {
            File commFile = new File("./objects/" + sParentSha);
            BufferedReader br1 = new BufferedReader(new FileReader(commFile));
            String prevTreeSha = br1.readLine();
            br1.close();
            String entry = "tree : " + prevTreeSha;
            tree.add(entry);
        }
        

        //adding file contents
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

    public String getCurrentTreeSha() {
        return sTreeSha;
    }

    public String getCommitSha() {
        return this.sCommitSha;
    }

    public String getTreeSha(String commitSha) throws IOException {
        File commFile = new File(commitSha);
        BufferedReader br = new BufferedReader(new FileReader(commFile));
        String treeSha = br.readLine();
        br.close();
        return treeSha;
    }
}
