import java.io.FileWriter;
import java.text.SimpleDateFormat;  
import java.util.Date;  

public class Commit {
    private String sTreeSha, sParentSha, sChildSha, sAuthor, sDate, sSummary;

    public Commit(String sParentSha, String sAuthor, String sSummary) {
        this.sTreeSha = constructTreeSha();
        this.sParentSha = sParentSha;
        this.sChildSha = "";
        this.sAuthor = sAuthor;
        this.sDate = getDate();
        this.sSummary = sSummary;
    }

    public Commit(String sAuthor, String sSummary) {
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

        String sCommitSha = Blob.encryptPassword(sCommit.toString());

        sCommit.insert(sCommit.indexOf("\n", sCommit.indexOf("\n") + 1), this.sChildSha + "\n");

        FileWriter f_writer = new FileWriter("objects/" + sCommitSha);
        f_writer.write(sCommit.toString());
        f_writer.close();
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
        Date date = new Date();  
        return sdf.format(date);  
    }

    public String constructTreeSha() {
        Tree tree = new Tree();
        return tree.getSha();
    }
}
