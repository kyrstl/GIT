import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

    public boolean addBlob(String origFileName) throws NoSuchAlgorithmException, IOException {

        Blob bl = new Blob(origFileName);
        String newFileName = bl.getSha1();

        String newEntry = origFileName + " : " + newFileName;

        if(!entryExists(newEntry, ind)) {
            PrintWriter pw = new PrintWriter(new FileWriter(ind, true));
            pw.append(newEntry + "\n");
            pw.close();
            return true;
        }
        return false;
    }

    private boolean entryExists(String newEntry, File index) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(index));
        while(br.ready()) {
            String str = br.readLine();
            if(str.equals(newEntry)) {
                br.close();
                return true;
            }
        }
        br.close();
        return false;
    }

    public boolean removeBlob(String origFileName) throws NoSuchAlgorithmException, IOException {
        Blob bl = new Blob(origFileName);
        String newFileName = bl.getSha1();

        String newEntry = origFileName + " : " + newFileName;
        
        File inputFile = new File("index");
        File tempFile = new File("myTempFile.txt");
        
        if(!entryExists(newEntry, ind)) {
            return false;
        }

        //removing line
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        
        String lineToRemove = newEntry;
        String currentLine;
        
        while((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close(); 
        reader.close(); 
        boolean successful = tempFile.renameTo(inputFile);
        return successful;
    }

}