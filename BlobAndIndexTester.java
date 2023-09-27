import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

//import org.jcp.xml.dsig.internal.dom.Utils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BlobAndIndexTester {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
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
    @DisplayName("[8] Test if initialize and objects are created correctly")
    void testInitialize() throws Exception {

        // Run the person's code
        Index ind = new Index();
        ind.init();

        // check if the file exists
        File file = new File("index");
        Path path = Paths.get("objects");

        assertTrue(file.exists());
        assertTrue(Files.exists(path));
    }

    @Test
    @DisplayName("[15] Test if adding a blob works.")
    void testCreateBlob() throws Exception {
        Path path = Path.of("junit_example_file_data.txt");
        String contents = Files.readString(path);
        String hash = encryptPassword(contents);

        Blob blob = new Blob("junit_example_file_data.txt");
        File file_junit1 = new File("./objects/" + hash);
        Path testPath = Path.of("./objects/" + hash);
        String testContents = Files.readString(testPath);

        assertTrue(file_junit1.exists());
        assertEquals(contents, testContents);

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

    public static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
