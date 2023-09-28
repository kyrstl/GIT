import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TreeTest {
    @BeforeEach
    void setUpBeforeClass() throws Exception {
        File dir  = new File("objects");
        dir.mkdir();

        File exampleFile = new File("junit_example_file_data.txt");
        exampleFile.createNewFile();
        PrintWriter pw = new PrintWriter(exampleFile);
        pw.write("test file contents");
        pw.close();
    }

    @AfterEach
    void tearDownAfterClass() throws Exception {
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

        //test if blob was added correctly
        String treeContents = tree.getTreeContents();
        assertEquals(treeContents, exampleBlob);

        //test if duplicates will be ignored
        tree.add(exampleBlob);
        String newTreeContents = tree.getTreeContents();
        assertEquals(newTreeContents, exampleBlob);
    }

    @Test
    @DisplayName("Test if removing blob works")
    void testRemoveBlob() throws FileNotFoundException {
        String blobStr = "blob : cbaedccfded0c768295aae27c8e5b3a0025ef340 : junit_example_file_data.txt";

        //run their code
        Tree tree = new Tree();
        tree.add(blobStr);
        tree.removeBlob("junit_example_file_data.txt");
        String treeContents = tree.getTreeContents();
        assertEquals("", treeContents);
    }

    @Test
    @DisplayName("Test if removing tree works")
    void testRemoveTree() throws FileNotFoundException {
        String treeStr = "tree : cbaedccfded0c768295aae27c8e5b3a0025ef340";

        //run their code
        Tree tree = new Tree();
        tree.add(treeStr);
        tree.removeTree("cbaedccfded0c768295aae27c8e5b3a0025ef340");
        String treeContents = tree.getTreeContents();
        assertEquals("", treeContents);
    }

    @Test
    @DisplayName("Test if writing to file works")
    void testWriteToFile() throws FileNotFoundException {
        String blobStr = "blob : cbaedccfded0c768295aae27c8e5b3a0025ef340 : junit_example_file_data.txt";

        //run their code
        Tree tree = new Tree();
        tree.add(blobStr);
        tree.writeToFile();

        File file = new File("./objects/94eb3ffa9b13aef9097430513a0401557f36b79b");
        assertTrue(file.exists());
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

    @Test
    @DisplayName("Test if adding files and folders to directory works")
    void addDirectory() throws IOException {
        Tree tree = new Tree();
        createTest();

        File file = new File("hello.txt");
        assertTrue(file.exists());
        String cont = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        while(br.ready()) {
            cont += br.readLine();
        }
        br.close();

        assertTrue(cont.equals("derpderpderp"));

        String dirName = "./testPath/";
        tree.addDirectory(dirName);

        String sha = tree.getSha();
        assertTrue(sha.equals("0a6b515b8f17fd7e8996127ccbd4d0de6bc0eb02"));
    }

    public static void createTest() throws IOException {
        String dirName = "./testPath/";
        File dir = new File (dirName);//create this directory (File class java)
        dir.mkdir();
        
        File file1 = new File (dir, "hello.txt");
        if(!file1.exists()) {
            file1.createNewFile();
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
