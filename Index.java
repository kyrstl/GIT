import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

public class Index {
    private File ind = new File("index");
    public Index () throws IOException {
        
    }

    public void init() throws IOException{
        //File ind = new File("index");

        if(!ind.exists()) {
            ind.createNewFile();
        }

        String dirName = "./objects/";
        File dir = new File (dirName);
        dir.mkdir();
    }
    
    public boolean addBlob(String origFileName) throws Exception {
        File entry = new File(origFileName);

        String newEntry = "";
        String shaFileName = "";
        if(entry.isDirectory()) {
            Tree tree = new Tree();
            //shaFileName = tree.getSha();
            shaFileName = tree.addDirectory(origFileName);
            newEntry = "tree : " + shaFileName + " : " + origFileName;
        }
        else {
            Blob blob = new Blob(origFileName);
            shaFileName = blob.getSha1();

            newEntry = "blob : " + shaFileName + " : " + origFileName;
        }
        if(!entryExists(newEntry, ind)) {
                PrintWriter pw = new PrintWriter(new FileWriter(ind, true));
                pw.append("\n" + newEntry);//might have new line problem
                pw.close();

                String trimmedEntry = "";
                BufferedReader br = new BufferedReader(new FileReader(ind));
                while(br.ready()) {
                    trimmedEntry += br.readLine() + "\n";
                }
                br.close();
                trimmedEntry = trimmedEntry.trim();

                PrintWriter pw2 = new PrintWriter(new FileWriter(ind));
                pw2.append(trimmedEntry);//might have new line problem
                pw2.close();
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
        String shaFileName = "";
        String newEntry = "";
        File entry = new File(origFileName);
        
        if(entry.isDirectory()) {
            Tree tree = new Tree();
            shaFileName = tree.getSha();
            newEntry = "tree : " + shaFileName + " : " + origFileName;
        }
        else {
            Blob blob = new Blob(origFileName);
            shaFileName = blob.getSha1();

            newEntry = "blob : " + shaFileName + " : " + origFileName;
        }
        
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

    public void deleteFile(String fileName) throws IOException {
        PrintWriter pw = new PrintWriter("index");
        if(isEmpty()) {
            pw.print("\n");
        }
        pw.print("*deleted* " + fileName);
        pw.close();//what??
    }

    public void editFile(String fileName) throws IOException {
        PrintWriter pw = new PrintWriter("index");
        if(isEmpty()) {
            pw.print("\n");
        }
        pw.print("*edited* " + fileName);
        pw.close();//what??
    }

    public boolean isEmpty() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("index"));
        String contents = "";
        while (br.ready()) {
            contents+=br.readLine();
        }
        br.close();
        if(contents.length()>0) {
            return false;
        }
        return true;
    }

}