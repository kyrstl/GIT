import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

public class Index {
    private File ind = new File("index");
    public Index () throws IOException {
        //File ind = new File("index");

        if(!ind.exists()) {
            ind.createNewFile();
        }

        String dirName = "./objects/";
        File dir = new File (dirName);
        dir.mkdir();

    }

    public void addBlob(String origFileName) throws NoSuchAlgorithmException, IOException {
        Blob bl = new Blob(origFileName);
        String newFileName = bl.getSha1();

        PrintWriter pw = new PrintWriter(ind);
        String newEntry = origFileName + " : " + newFileName;
        pw.println(newEntry);
        pw.close();
    }

    

}