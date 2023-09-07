import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Blob {
    private String sha1 = "";
    public Blob (String fileName) throws IOException, NoSuchAlgorithmException {
        //File myFile = new File(fileName);

        //reading file contents
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String str = "";

        while(br.ready()) {
            str = str + ((char) br.read());
        }

        br.close();

        //converting to sha1
        //String test1 = "1010";

        sha1 = encryptPassword(str);
        System.out.println("TEST BYTE: " + str);
        
        System.out.println("SHA : " + sha1);

        //printing to objects folder
        String dirName = "./objects/";
        File dir = new File (dirName);//create this directory (File class java)
        //dir.mkdir();
        File file = new File (dir, sha1);

        PrintWriter pw = new PrintWriter(file);

        pw.print(str);

        pw.close();

        System.out.println("Reading contents of " + fileName + ": " + str);

        System.out.println("\nCreating new blob " + " from content: " + sha1);
    }

      private static String encryptPassword(String password)
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

    private String byteToHex(final byte[] hash)
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

    public String getSha1() {
        return sha1;
    }

}