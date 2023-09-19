import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TreeTest {
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        File dir  = new File("objects");
        dir.mkdir();

        File exampleFile = new File("junit_example_file_data.txt");
        exampleFile.createNewFile();
        PrintWriter pw = new PrintWriter(exampleFile);
        pw.write("test file contents");
        pw.close();
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        File dir  = new File("objects");
        deleteDirectory(dir);
    }

    @Test
    @DisplayName("Test if adding to tree works")
    void testAdd() throws IOException {
        //add blob and initialize
        Tree tree = new Tree();
        String exampleBlob = "blob : cbaedccfded0c768295aae27c8e5b3a0025ef340 : junit_example_file_data.txt";
        
        //run their code
        tree.add(exampleBlob);
        tree.writeToFile();

        //test if blob was added correctly
        File treeFile = new File("./objects/94eb3ffa9b13aef9097430513a0401557f36b79b");
        assertTrue(treeFile.exists());
        Path treePath = Path.of("./objects/94eb3ffa9b13aef9097430513a0401557f36b79b");
        String treeContents = Files.readString(treePath);
        assertEquals(treeContents, exampleBlob);

        //test if duplicates will be ignored
        tree.add(exampleBlob);
        String newTreeContents = Files.readString(treePath);
        assertEquals(newTreeContents, exampleBlob);
    }

    @Test
    void testRemoveBlob() {

    }

    @Test
    void testRemoveTree() {

    }

    @Test
    void testWriteToFile() {

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
