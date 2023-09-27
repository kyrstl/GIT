import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class testerTree {
    public static void main (String[] args) throws IOException {
        Tree tree = new Tree();
        createTest();


    }

    public static void createTest() throws IOException {
        String dirName = "./testPath/";
        File dir = new File (dirName);//create this directory (File class java)
        dir.mkdir();
        
        File file = new File (dir, "hello.txt");
        if(!file.exists()) {
            file.createNewFile();
        }
        addFileContents("hello.txt", "derpderpderp");


        File file2 = new File (dir, "blob.txt");
        if(!file2.exists()) {
            file2.createNewFile();
        }
        addFileContents("blob.txt", "hi");

        String subName = "./subpath/";
        File subdir = new File (dir,subName);
        subdir.mkdir();
        
        File file3 = new File (subdir, "BLOB.txt");
        if(!file3.exists()) {
            file3.createNewFile();
        }
        addFileContents("BLOB.txt", "hello");

    }

    public static void addFileContents(String fileName, String contents) throws IOException {
        PrintWriter pw = new PrintWriter(fileName);
        pw.print(contents);
        pw.close();
    }

}
