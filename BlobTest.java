import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BlobTest {
    @BeforeAll
    static void setUpBeforeClass() throws IOException {
        File exampleFile = new File("junit_example_file_data.txt");
        exampleFile.createNewFile();
        PrintWriter pw = new PrintWriter(exampleFile);
        pw.write("test file contents");
        pw.close();

        File objectDirectory = new File("objects");
        if(!objectDirectory.exists()){
            objectDirectory.mkdir();
        }
    }

    @AfterAll
    static void tearDownAfterClass() {
        File objectDirectory = new File("objects");
        if(objectDirectory.exists()){
            deleteDirectory(objectDirectory);
        }   
    }

    @Test
    @DisplayName("Tests if the sha1 hash is being created correctly")
    void testEncryptPassword() {
        String testStr = "test file contents";
        String correctHash = "cbaedccfded0c768295aae27c8e5b3a0025ef340";

        //compares the generated hash to the correct hash
        assertEquals(correctHash, Blob.encryptPassword("test file contents"));
    }

    @Test
    @DisplayName("Test if adding a blob works.")
    void testCreateBlob() throws NoSuchAlgorithmException, IOException {
        Blob blob = new Blob("junit_example_file_data.txt");
        String correctHash = "cbaedccfded0c768295aae27c8e5b3a0025ef340";
        File file_junit1 = new File("./objects/" + correctHash);

        //tests if a correct file name (based on hash) exists in the objects folder
        assertTrue(file_junit1.exists());

        //tests if the file has the correct contents
        Path path = Path.of("./objects/" + correctHash);
        String contents = Files.readString(path);
        assertEquals("test file contents", contents);
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
