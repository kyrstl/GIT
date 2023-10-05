import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommitTester {
    @BeforeAll
    static void setUpBeforeClass() throws IOException, NoSuchAlgorithmException {
        File exampleFile = new File("junit_example_file_data.txt");
        exampleFile.createNewFile();
        PrintWriter pw = new PrintWriter(exampleFile);
        pw.write("test file contents");
        pw.close();

        File objectDirectory = new File("objects");
        if (!objectDirectory.exists())
            objectDirectory.mkdir();

        //create index file
        Index ind = new Index();
        ind.init();
        File index = new File("index");
        index.createNewFile();

        File file1 = new File("file1.txt");
        if(!file1.exists()) {
            file1.createNewFile();
        }
        ind.addBlob("file1.txt");


        //create  previous com
    }

    @AfterAll
    static void tearDownAfterClass() throws IOException, NoSuchAlgorithmException {
        File objectDirectory = new File("objects");
        if (objectDirectory.exists())
            BlobTest.deleteDirectory(objectDirectory);

        File index = new File("index");
        if(index.exists()) {
            index.delete();
        }

        /*Index ind = new Index();
        File file1 = new File("file1.txt");
        if(file1.exists()) {
            ind.removeBlob("file1.txt");
        }
        File file2 = new File("file2.txt");
        if(file2.exists()) {
            ind.removeBlob("file2.txt");
        }*/
    }

    @Test
    void testCommit() throws Exception {
        Commit commit = new Commit("", "Jake Parker", "This is my commit.");
        commit.commit();
        String sCommitSha = commit.getCommitSha();
        String date = commit.getDate();
        File file = new File("./objects/", sCommitSha);
        assertTrue(file.exists());
        assertEquals(
            "968d81f0c547460786e34543bc3f5b5b68ee5151" + "\n" +
            ""                                         + "\n" + //parentSha
            "\n"                                       +        //childSha
            "Jake Parker"                              + "\n" +
            date                                       + "\n" +
            "This is my commit."                       ,
            Files.readString(Paths.get("./objects/" + sCommitSha))
        );
    }

    @Test
    void testCommitOtherConstructor() throws Exception {
        Commit commit = new Commit("Jake Parker", "This is my commit.");
        commit.commit();
        String sCommitSha = commit.getCommitSha();
        String date = commit.getDate();
        File file = new File("./objects/", sCommitSha);
        assertTrue(file.exists());
        assertEquals(
            "968d81f0c547460786e34543bc3f5b5b68ee5151" + "\n" +
            ""                                         + "\n" + //parentSha
            "\n"                                       +        //childSha
            "Jake Parker"                              + "\n" +
            date                                       + "\n" +
            "This is my commit."                       ,
            Files.readString(Paths.get("./objects/" + sCommitSha))
        );
    }

    @Test
    @DisplayName("test 1 commit")
    void testCommit1() throws Exception {
        /*Index ind = new Index();
        ind.init();

        File file2 = new File("file2.txt");
        if(!file2.exists()) {
            file2.createNewFile();
        }
        ind.addBlob("file2.txt");*/


        Commit commit = new Commit("Jake Parker", "This is my commit.");
        commit.commit();
        String sCommitSha = commit.getCommitSha();

        File file = new File("./objects/", sCommitSha);
        assertTrue(file.exists());

        String tSha = commit.getCurrentTreeSha();
        String expectedSha = "7420abea04daa61be92b66d8b2004f38c4144269";
        assertEquals(expectedSha, tSha);

        //how do u check previous shas??
        
    }

    @Test
    @DisplayName("test 2 commits w/ 1 folder")
    void testCommit2() throws Exception {
        Commit commit = new Commit("Jake Parker", "This is my commit.");
        commit.commit();
        String sCommitSha = commit.getCommitSha();
        String date = commit.getDate();
        File file = new File("./objects/", sCommitSha);
        assertTrue(file.exists());
        
    }

    @Test
    @DisplayName("test 4 commits w/ each 2 files + 2/4 have 1 folder")
    void testCommit3() throws Exception {
        Commit commit = new Commit("Jake Parker", "This is my commit.");
        commit.commit();
        String sCommitSha = commit.getCommitSha();
        String date = commit.getDate();
        File file = new File("./objects/", sCommitSha);
        assertTrue(file.exists());
        
    }

    @Test
    void testGetDate() throws Exception {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals(Commit.getDate(), sdf.format(cal.getTime()));
    }

    @Test
    void testCreateTree() throws Exception {
        //commit with no parent commit
        Commit commit = new Commit("","Jake Parker", "This is my commit.");
        String sPredictedSha = "968d81f0c547460786e34543bc3f5b5b68ee5151";
        String treeSha = commit.getCurrentTreeSha();
        assertEquals(sPredictedSha, treeSha);
    }
}
