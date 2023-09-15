import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IndexTest {
    @BeforeAll
    static void setUpBeforeClass() throws IOException{
        File exampleFile = new File("junit_example_file_data.txt");
        exampleFile.createNewFile();
        PrintWriter pw = new PrintWriter(exampleFile);
        pw.write("test file contents");
        pw.close();

        File indexFile = new File("index");
        File objectDirectory = new File("objects");
        
        if(indexFile.exists()){
            indexFile.delete();
        }

        if(objectDirectory.exists()){
            deleteDirectory(objectDirectory);
        }
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        File indexFile = new File("index");
        File objectDirectory = new File("objects");
        
        if(indexFile.exists()){
            indexFile.delete();
        }

        if(objectDirectory.exists()){
            deleteDirectory(objectDirectory);
        }
    }
    
    @Test
    void testAddBlob() {

    }

    @Test
    void testRemoveBlob() {

    }

    public static void deleteDirectory(File file)
    {
        // store all the paths of files and folders present
        // inside directory
        for (File subfile : file.listFiles()) {
 
            // if it is a subfolder,e.g Rohan and Ritik,
            //  recursively call function to empty subfolder
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
 
            // delete files and empty subfolders
            subfile.delete();
        }
    }
}
