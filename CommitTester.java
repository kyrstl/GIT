import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommitTester {
    @BeforeEach
    void setUpBeforeClass() throws Exception {
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
        //File index = new File("index");

        File file1 = new File("file1.txt");
        if(!file1.exists()) {
            file1.createNewFile();
        }
        ind.addBlob("file1.txt");


        //create  previous com
    }

    @AfterEach
    void tearDownAfterClass() throws IOException, NoSuchAlgorithmException {
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

        File file4 = new File("file4.txt");
        if(file4.exists()) {
            file4.delete();
        }

        File file3 = new File("file3.txt");
        if(file3.exists()) {
            file3.delete();
        }
        File file2 = new File("file2.txt");
        if(file2.exists()) {
            file2.delete();
        }
        File file1 = new File("file1.txt");
        if(file1.exists()) {
            file1.delete();
        }
        
        File subfolder = new File("subpath");
        if(subfolder.exists()) {
            BlobTest.deleteDirectory(subfolder);
        }

        File file5 = new File("file5.txt");
        if(file5.exists()) {
            file5.delete();
        }

        File file6 = new File("file6.txt");
        if(file6.exists()) {
            file6.delete();
        }

        File subfolder2 = new File("subpath2");
        if(subfolder2.exists()) {
            BlobTest.deleteDirectory(subfolder2);
        }

        File file7 = new File("file7.txt");
        if(file7.exists()) {
            file7.delete();
        }

        File file8 = new File("file8.txt");
        if(file8.exists()) {
            file8.delete();
        }
    }

    @Test
    void testCommit() throws Exception {
        Commit commit = new Commit("", "Jake Parker", "This is my commit.");
        //commit.commit();
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

        //assertEquals(date,"10/06/23");
    }

    @Test
    void testCommitOtherConstructor() throws Exception {
        Commit commit = new Commit("Jake Parker", "This is my commit.");
        //commit.commit();
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
        Index ind = new Index();
        ind.init();

        File file2 = new File("file2.txt");
        if(!file2.exists()) {
            file2.createNewFile();
        }
        ind.addBlob("file2.txt");


        Commit commit = new Commit("","Jake Parker", "This is my commit.");
        //commit.commit();
        String sCommitSha = commit.getCommitSha();

        String date1 = commit.getDate();
        String commitContents = 
            "7420abea04daa61be92b66d8b2004f38c4144269" + "\n" +
            ""                                         + "\n" + //parentSha
            "Jake Parker"                              + "\n" +
            date1                                       + "\n" +
            "This is my commit."                       ;
        String myExpectedCommitSha = Blob.encryptPassword(commitContents);

        assertEquals(myExpectedCommitSha, sCommitSha);

        File file = new File("./objects/", sCommitSha);
        assertTrue(file.exists());

        String tSha = commit.getCurrentTreeSha();
        String expectedSha = "7420abea04daa61be92b66d8b2004f38c4144269";
        assertEquals(expectedSha, tSha);

        String parentSha = "";
        String nextSha = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        for(int i=1; i<=6; i++) {
            String str1 = br.readLine();
            if(i==2) {
                parentSha = str1;
            }
            else if(i==3) {
                nextSha = str1;
            }
        }
        br.close();
        assertEquals(parentSha, "");
        assertEquals(nextSha, "");//because only one commit

        //how do u check previous shas??
        
    }

    @Test
    @DisplayName("test 2 commits w/ 1 folder")
    void testCommit2() throws Exception {
        Index ind = new Index();
        ind.init();

        //first commit
        File file2 = new File("file2.txt");
        if(!file2.exists()) {
            file2.createNewFile();
        }
        ind.addBlob("file2.txt");

        Commit commit = new Commit("","Jake Parker", "This is my commit.");
        //commit.commit();
        String sCommitSha = commit.getCommitSha();

        File first = new File("./objects/", sCommitSha);
        assertTrue(first.exists());

        String tSha = commit.getCurrentTreeSha();
        String expectedSha = "7420abea04daa61be92b66d8b2004f38c4144269";
        assertEquals(expectedSha, tSha);


        //adding second commit

        File file3 = new File("file3.txt");
        if(!file3.exists()) {
            file3.createNewFile();
        }
        ind.addBlob("file3.txt");

        File file4 = new File("file4.txt");
        if(!file4.exists()) {
            file4.createNewFile();
        }
        ind.addBlob("file4.txt");
        
        File subfolder = new File("subpath");
        subfolder.mkdirs();
        
        ind.addBlob("subpath");

        assertTrue(subfolder.isDirectory());

        
        Commit commit2 = new Commit(sCommitSha,"Jingjing Duan", "This is a second commit.");
        //commit2.commit();
        String sCommit2Sha = commit2.getCommitSha();
        commit.setNextSha(sCommit2Sha);

        //commit2.setParentSha(sCommitSha);
        String parentSha = commit2.getParentSha();

        File second = new File("./objects/", sCommit2Sha);
        assertTrue(second.exists());

        String tSha2 = commit2.getCurrentTreeSha();
        String expectedSha2 = "6c7e58a2700e3a97b8e295133521e7eb9077408e";//wrong this is if blob
        assertEquals(expectedSha2, tSha2);


        assertTrue(commit.getNextSha().equals(sCommit2Sha));
        assertTrue(commit2.getParentSha().equals(sCommitSha));


        /*String tSha = commit.getCurrentTreeSha();
        String expectedSha = "7420abea04daa61be92b66d8b2004f38c4144269";
        assertEquals(expectedSha, tSha);

        String parentSha = "";
        String nextSha = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        for(int i=1; i<=6; i++) {
            String str1 = br.readLine();
            if(i==2) {
                parentSha = str1;
            }
            else if(i==3) {
                nextSha = str1;
            }
        }
        br.close();
        assertEquals(parentSha, "");
        assertEquals(nextSha, "");//because only one commit

        //how do u check previous shas??*/
        
    }

    @Test
    @DisplayName("test 4 commits w/ each 2 files + 2/4 have 1 folder")
    void testCommit3() throws Exception {
        Index ind = new Index();
        ind.init();

        File file2 = new File("file2.txt");
        if(!file2.exists()) {
            file2.createNewFile();
        }
        writeContents(file2,"2");
        ind.addBlob("file2.txt");

        Commit commit = new Commit("","Jake Parker", "This is my commit.");
        //commit.commit();
        String sCommitSha = commit.getCommitSha();

        File first = new File("./objects/", sCommitSha);
        assertTrue(first.exists());

        String tSha = commit.getCurrentTreeSha();
        String expectedSha = "dcfbe813de81fc88916bf2fa135fa81cc912ffbe";
        assertEquals(expectedSha, tSha);


        //second commit
        File file3 = new File("file3.txt");
        if(!file3.exists()) {
            file3.createNewFile();
        }
        writeContents(file3,"3");
        ind.addBlob("file3.txt");

        File file4 = new File("file4.txt");
        if(!file4.exists()) {
            file4.createNewFile();
        }
        writeContents(file4,"4");
        ind.addBlob("file4.txt");
        
        File subfolder = new File("subpath");
        subfolder.mkdirs();
        ind.addBlob("subpath");

        assertTrue(subfolder.isDirectory());

        
        Commit commit2 = new Commit(sCommitSha,"Jingjing Duan", "This is a second commit.");
        //commit2.commit();
        String sCommit2Sha = commit2.getCommitSha();
        String parentSha = commit2.getParentSha();
        commit.setNextSha(sCommit2Sha);

        File second = new File("./objects/", sCommit2Sha);
        assertTrue(second.exists());

        String tSha2 = commit2.getCurrentTreeSha();
        String expectedSha2 = "d071722ee024c422d1165bd72f6bd9df50f3eba8";//wrong this is if blob
        assertEquals(expectedSha2, tSha2);

        assertTrue(commit.getNextSha().equals(sCommit2Sha));
        assertTrue(commit2.getParentSha().equals(sCommitSha));
        
        //third commit
        File file5 = new File("file5.txt");
        if(!file5.exists()) {
            file5.createNewFile();
        }
        writeContents(file5,"5");
        ind.addBlob("file5.txt");

        File file6 = new File("file6.txt");
        if(!file6.exists()) {
            file6.createNewFile();
        }
        writeContents(file6,"6");
        ind.addBlob("file6.txt");

        Commit commit3 = new Commit(sCommit2Sha,"Jingjing Duan", "This is a third commit.");
        //commit3.commit();
        String sCommit3Sha = commit3.getCommitSha();
        String parentSha3 = commit3.getParentSha();
        commit2.setNextSha(sCommit3Sha);

        File third = new File("./objects/", sCommit3Sha);
        assertTrue(third.exists());

        String tSha3 = commit3.getCurrentTreeSha();
        String expectedSha3 = "8800643c6fc16632633a2967831740e683e55553";//wrong this is if blob
        assertEquals(expectedSha3, tSha3);

        assertTrue(commit2.getNextSha().equals(sCommit3Sha));
        assertTrue(commit3.getParentSha().equals(sCommit2Sha));
        //assertTrue(commit3.getParentSha().equals("23f7ef6fca69383b92f6bec4d5fec06bcc79de5b"));


        //fourth commit
        File file7 = new File("file7.txt");
        if(!file7.exists()) {
            file7.createNewFile();
        }
        writeContents(file7,"7");
        ind.addBlob("file7.txt");

        File file8 = new File("file8.txt");
        if(!file8.exists()) {
            file8.createNewFile();
        }
        writeContents(file8,"8");
        ind.addBlob("file8.txt");
        
        File subfolder2 = new File("subpath2");
        subfolder2.mkdirs();
        ind.addBlob("subpath2");

        assertTrue(subfolder2.isDirectory());

        Commit commit4 = new Commit(sCommit3Sha,"Jingjing Duan", "This is a third commit.");
        //commit4.commit();
        String sCommit4Sha = commit4.getCommitSha();
        String parentSha4 = commit4.getParentSha();
        commit3.setNextSha(sCommit4Sha);

        File fourth = new File("./objects/", sCommit4Sha);
        assertTrue(fourth.exists());

        String tSha4 = commit4.getCurrentTreeSha();
        String expectedSha4 = "6d0790f43373ce52717d130959dd14e6f1d7cde0";//wrong this is if blob
        assertEquals(expectedSha4, tSha4);

        assertTrue(commit3.getNextSha().equals(sCommit4Sha));
        assertTrue(commit4.getParentSha().equals(sCommit3Sha));

        String date4 = commit4.getDate();
        String commit4contents = 
            "6d0790f43373ce52717d130959dd14e6f1d7cde0" + "\n" +
            "f111c08094c2d7b23e7ca52ab19c41c1af77fd12" + "\n" + //parentSha
            "\n" +
            "Jingjing Duan"                             + "\n" +
            date4                                       + "\n" +
            "This is a third commit."                       ;

        assertEquals(commit4contents, Files.readString(Paths.get("./objects/" + sCommit4Sha)));

    }

    @Test
    @DisplayName("test 2 commits w/ 1 folder w/ a file inside")
    void testCommit4() throws Exception {
        Index ind = new Index();
        ind.init();

        //first commit
        File file2 = new File("file2.txt");
        if(!file2.exists()) {
            file2.createNewFile();
        }
        ind.addBlob("file2.txt");

        Commit commit = new Commit("","Jake Parker", "This is my commit.");
        //commit.commit();
        String sCommitSha = commit.getCommitSha();

        File first = new File("./objects/", sCommitSha);
        assertTrue(first.exists());

        String tSha = commit.getCurrentTreeSha();
        String expectedSha = "7420abea04daa61be92b66d8b2004f38c4144269";
        assertEquals(expectedSha, tSha);


        //adding second commit

        File file3 = new File("file3.txt");
        if(!file3.exists()) {
            file3.createNewFile();
        }
        ind.addBlob("file3.txt");

        File subfolder = new File("subpath");
        subfolder.mkdirs();
        assertTrue(subfolder.isDirectory());

        File file4 = new File(subfolder,"file4.txt");
        if(!file4.exists()) {
            file4.createNewFile();
        }
        ind.addBlob("subpath");
        ind.addBlob("./subpath/" + "file4.txt");//should we be able to add files that arent in the main folder to index?
        //WOULD WE HAVE TO CREATE A TREE IN INDEX???
        //ind.addBlobWithPath(subfolder,"subpath");

        
        Commit commit2 = new Commit(sCommitSha,"Jingjing Duan", "This is a second commit.");
        //commit2.commit();
        String sCommit2Sha = commit2.getCommitSha();
        commit.setNextSha(sCommit2Sha);

        //commit2.setParentSha(sCommitSha);
        String parentSha = commit2.getParentSha();

        File second = new File("./objects/", sCommit2Sha);
        assertTrue(second.exists());

        String tSha2 = commit2.getCurrentTreeSha();
        String expectedSha2 = "dd67edc2d5822156513404d20ea574333d6b0571";//wrong this is if blob
        assertEquals(expectedSha2, tSha2);


        assertTrue(commit.getNextSha().equals(sCommit2Sha));
        assertTrue(commit2.getParentSha().equals(sCommitSha));
        
    }

    @Test
    @DisplayName("test 5 commits w/ deleted and edited files")
    void testCommitWithEditedDeleted() throws Exception {
        Index ind = new Index();
        ind.init();

        File file2 = new File("file2.txt");
        if(!file2.exists()) {
            file2.createNewFile();
        }
        writeContents(file2,"2");
        ind.addBlob("file2.txt");

        Commit commit = new Commit("","Jake Parker", "This is my commit.");
        //commit.commit();
        String sCommitSha = commit.getCommitSha();

        File first = new File("./objects/", sCommitSha);
        assertTrue(first.exists());

        String tSha = commit.getCurrentTreeSha();
        String expectedSha = "dcfbe813de81fc88916bf2fa135fa81cc912ffbe";
        assertEquals(expectedSha, tSha);


        //second commit
        File file3 = new File("file3.txt");
        if(!file3.exists()) {
            file3.createNewFile();
        }
        writeContents(file3,"3");
        ind.addBlob("file3.txt");

        File file4 = new File("file4.txt");
        if(!file4.exists()) {
            file4.createNewFile();
        }
        writeContents(file4,"4");
        ind.addBlob("file4.txt");
        
        File subfolder = new File("subpath");
        subfolder.mkdirs();
        ind.addBlob("subpath");

        assertTrue(subfolder.isDirectory());

        
        Commit commit2 = new Commit(sCommitSha,"Jingjing Duan", "This is a second commit.");
        //commit2.commit();
        String sCommit2Sha = commit2.getCommitSha();
        String parentSha = commit2.getParentSha();
        commit.setNextSha(sCommit2Sha);

        File second = new File("./objects/", sCommit2Sha);
        assertTrue(second.exists());

        String tSha2 = commit2.getCurrentTreeSha();
        String expectedSha2 = "d071722ee024c422d1165bd72f6bd9df50f3eba8";//wrong this is if blob
        assertEquals(expectedSha2, tSha2);

        assertTrue(commit.getNextSha().equals(sCommit2Sha));
        assertTrue(commit2.getParentSha().equals(sCommitSha));
        
        //third commit
        File file5 = new File("file5.txt");
        if(!file5.exists()) {
            file5.createNewFile();
        }
        writeContents(file5,"5");
        ind.addBlob("file5.txt");

        File file6 = new File("file6.txt");
        if(!file6.exists()) {
            file6.createNewFile();
        }
        writeContents(file6,"6");
        ind.addBlob("file6.txt");

        Commit commit3 = new Commit(sCommit2Sha,"Jingjing Duan", "This is a third commit.");
        //commit3.commit();
        String sCommit3Sha = commit3.getCommitSha();
        String parentSha3 = commit3.getParentSha();
        commit2.setNextSha(sCommit3Sha);

        File third = new File("./objects/", sCommit3Sha);
        assertTrue(third.exists());

        String tSha3 = commit3.getCurrentTreeSha();
        String expectedSha3 = "8800643c6fc16632633a2967831740e683e55553";//wrong this is if blob
        assertEquals(expectedSha3, tSha3);

        assertTrue(commit2.getNextSha().equals(sCommit3Sha));
        assertTrue(commit3.getParentSha().equals(sCommit2Sha));
        //assertTrue(commit3.getParentSha().equals("23f7ef6fca69383b92f6bec4d5fec06bcc79de5b"));


        //fourth commit
        File file7 = new File("file7.txt");
        if(!file7.exists()) {
            file7.createNewFile();
        }
        writeContents(file7,"7");
        ind.addBlob("file7.txt");

        File file8 = new File("file8.txt");
        if(!file8.exists()) {
            file8.createNewFile();
        }
        writeContents(file8,"8");
        ind.addBlob("file8.txt");
        
        File subfolder2 = new File("subpath2");
        subfolder2.mkdirs();
        ind.addBlob("subpath2");

        assertTrue(subfolder2.isDirectory());

        Commit commit4 = new Commit(sCommit3Sha,"Jingjing Duan", "This is a 4th commit.");
        //commit4.commit();
        String sCommit4Sha = commit4.getCommitSha();
        String parentSha4 = commit4.getParentSha();
        commit3.setNextSha(sCommit4Sha);

        File fourth = new File("./objects/", sCommit4Sha);
        assertTrue(fourth.exists());

        String tSha4 = commit4.getCurrentTreeSha();
        String expectedSha4 = "6d0790f43373ce52717d130959dd14e6f1d7cde0";//wrong this is if blob
        assertEquals(expectedSha4, tSha4);

        assertTrue(commit3.getNextSha().equals(sCommit4Sha));
        assertTrue(commit4.getParentSha().equals(sCommit3Sha));

        //5th commit has a deleted file THIS TEST WORKS
        /*ind.deleteFile("file7.txt");
        Commit commit5 = new Commit(sCommit4Sha,"Jingjing Duan", "This is a 5th commit.");
        String sCommit5Sha = commit5.getCommitSha();

        String contents = commit5.getFileContents(sCommit5Sha);

        String tSha5 = commit5.getCurrentTreeSha();
        String expectedSha5 = "5423d1d226bd36c0d5c3d3c8aab478714269f474";
        assertEquals(expectedSha5, tSha5);*/


        //fifth commit
        ind.deleteFile("file3.txt");
        ind.deleteFile("file6.txt");
        Commit commit5 = new Commit(sCommit4Sha,"Jingjing Duan", "This is a 5th commit.");
        String sCommit5Sha = commit5.getCommitSha();

        String contents = commit5.getFileContents(sCommit5Sha);

        String tSha5 = commit5.getCurrentTreeSha();
        String expectedSha5 = "0ceaccf92c1704d1dcdfc35ba3d2e97d976f05dd";
        assertEquals(expectedSha5, tSha5);

        //writeContents(file4,"4");
        //ind.addBlob("file4.txt");
        /*Commit commit6 = new Commit(sCommit5Sha,"Jingjing Duan", "This is a 5th commit.");
        String sCommit6Sha = commit6.getCommitSha();

        String contents6 = commit6.getFileContents(sCommit6Sha);

        String tSha6 = commit6.getCurrentTreeSha();
        String expectedSha6 = "5423d1d226bd36c0d5c3d3c8aab478714269f474";
        assertEquals(expectedSha6, tSha6);*/


    }

    public void writeContents(File file, String contents) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(file);
        pw.print(contents);
        pw.close();
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
