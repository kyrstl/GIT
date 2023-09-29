import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class blobtester {
    public static void main (String[] args) throws NoSuchAlgorithmException, IOException {
        String dirName = "./objects/";
        File dir1 = new File (dirName);//create this directory (File class java)
        if(!dir1.exists()) {
            dir1.mkdir();
        }

        File example = new File(dir1,"example.txt");
        if(!example.exists()) {
            example.createNewFile();
        }
        Blob test = new Blob("example.txt");
        System.out.println("SHA: " + test.getSha1());

        /*Index test1 = new Index();
        System.out.println("\nADDED? " + test1.addBlob("example.txt"));
        System.out.println("\nADDED? " + test1.addBlob("example2.txt"));
        System.out.println("\nREMOVED? " + test1.removeBlob("example.txt"));
        System.out.println("\nADDED? " + test1.addBlob("example3.txt"));*/

        Tree tree = new Tree();
        tree.add("blob : da39a3ee5e6b4b0d3255bfef95601890afd80709 : example.txt");
        //tree.writeToFile();
        String sha = tree.getSha();
        System.out.println("TREE SHA: " + sha);
        String treeStr = tree.getTreeContents();
        System.out.println("TREE FILE: " + treeStr);
    }
}