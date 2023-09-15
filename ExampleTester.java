import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

//import org.jcp.xml.dsig.internal.dom.Utils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExampleTester {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        File exampleFile = new File("junit_example_file_data.txt");
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
    @DisplayName("[8] Test if initialize and objects are created correctly")
    void testInitialize() throws Exception {

        // Run the person's code
        Index ind = new Index();
        // TestHelper.runTestSuiteMethods("testInitialize");

        // check if the file exists
        File file = new File("index");
        Path path = Paths.get("objects");

        assertTrue(file.exists());
        assertTrue(Files.exists(path));
    }

    @Test
    @DisplayName("[15] Test if adding a blob works.  5 for sha, 5 for file contents, 5 for correct location")
    void testCreateBlob() throws Exception {

        try {

            // Manually create the files and folders before the 'testAddFile'
            // MyGitProject myGitClassInstance = new MyGitProject();
            // myGitClassInstance.init();

            // TestHelper.runTestSuiteMethods("testCreateBlob", file1.getName());

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        // Check blob exists in the objects folder
       // File file_junit1 = new File("objects/" + file1.methodToGetSha1());
        //assertTrue("Blob file to add not found", file_junit1.exists());

        // Read file contents
        //String indexFileContents = MyUtilityClass.readAFileToAString("objects/" + file1.methodToGetSha1());
       // assertEquals("File contents of Blob don't match file contents pre-blob creation", //indexFileContents,
                //file1.getContents());
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
