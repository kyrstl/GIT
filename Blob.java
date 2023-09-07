import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob {
    public Blob (String fileName) throws IOException, NoSuchAlgorithmException {
        File myFile = new File(fileName);

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String str = "";

        while(br.ready()) {
            str += br.readLine();
        }

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] messageDigest = md.digest(str.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        String sha1 = bigInt.toString(6);
        
        
        OutputStream os = new FileOutputStream(myFile);
        PrintWriter pw = new PrintWriter(os);

        pw.print(sha1);

        pw.close();

        System.out.println("Reading contents of " + fileName + ": " + str);

        System.out.println("\nCreating new blob " + " from content: " + sha1);
    }

}