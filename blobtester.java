import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class blobtester {
    public static void main (String[] args) throws NoSuchAlgorithmException, IOException {
        //Blob test = new Blob("example.txt");

        Index test2 = new Index();
        System.out.println("\nADDED? " + test2.addBlob("example.txt"));
        System.out.println("\nREMOVED? " + test2.removeBlob("example.txt"));
    }
}