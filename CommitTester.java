import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommitTester {
    @BeforeAll
    static void setUpBeforeClass() throws IOException {
        File exampleFile = new File("junit_example_file_data.txt");
        exampleFile.createNewFile();
        PrintWriter pw = new PrintWriter(exampleFile);
        pw.write("test file contents");
        pw.close();

        File objectDirectory = new File("objects");
        if (!objectDirectory.exists())
            objectDirectory.mkdir();
    }

    @AfterAll
    static void tearDownAfterClass() {
        File objectDirectory = new File("objects");
        if (objectDirectory.exists())
            BlobTest.deleteDirectory(objectDirectory);
    }

    @Test
    void testCommit() throws Exception {
        Commit commit = new Commit("f924e482dd33576fd0de90b6376f1671b08b5f52", "Jake Parker", "This is my commit.");
        commit.commit();
        String sCommitSha = commit.getCommitSha();
        File file = new File("./objects/", sCommitSha);
        assertTrue(file.exists());
        assertEquals(
            commit.constructTreeSha()                  + "\n" +
            "f924e482dd33576fd0de90b6376f1671b08b5f52" + "\n" + "\n" +
            "Jake Parker"                              + "\n" +
            "29/09/2023"                               + "\n" +
            "This is my commit."                       ,
            Files.readString(Paths.get("./objects/" + sCommitSha))
        );
    }

    @Test
    void testCommitOtherConstructor() throws Exception {
        Commit commit = new Commit("Jake Parker", "This is my commit.");
        commit.commit();
        String sCommitSha = commit.getCommitSha();
        File file = new File("./objects/", sCommitSha);
        assertTrue(file.exists());
        assertEquals(
            commit.constructTreeSha() + "\n" +
            ""                        + "\n" + "\n" + 
            "Jake Parker"             + "\n" +
            "29/09/2023"              + "\n" +
            "This is my commit."      ,
            Files.readString(Paths.get("./objects/" + sCommitSha))
        );
    }

    @Test
    void testGetDate() throws Exception {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals(Commit.getDate(), sdf.format(cal.getTime()));
    }

    @Test
    void testCreateTree() throws Exception {
        Commit commit = new Commit("Jake Parker", "This is my commit.");
        String sPredictedSha = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
        assertEquals(commit.constructTreeSha(), sPredictedSha);
    }
}
