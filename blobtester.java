import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class blobtester {
    public static void main (String[] args) throws NoSuchAlgorithmException, IOException {
        //Blob test = new Blob("example.txt");

        Index test1 = new Index();
        System.out.println("\nADDED? " + test1.addBlob("example.txt"));
        System.out.println("\nADDED? " + test1.addBlob("example2.txt"));
        System.out.println("\nREMOVED? " + test1.removeBlob("example.txt"));
        System.out.println("\nADDED? " + test1.addBlob("example3.txt"));
    }
}