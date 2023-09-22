import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
        Commit(String sParentSha, String sAuthor, String sSummary)
    }

    @Test
    void testCommitOtherConstructor() throws Exception {
        Commit(String sAuthor, String sSummary)
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
        String sPredictedSha = "59e083093fb3c613480ce987129f51216c385183";
        assertEquals(commit.constructTreeSha(), sPredictedSha);
    }
}
