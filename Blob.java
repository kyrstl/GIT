import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Blob {
    public Blob (String fileName) throws IOException, NoSuchAlgorithmException {
        File myFile = new File(fileName);

        //System.out.println("FILE NAME : " + fileName);

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String str = "";

        while(br.ready()) {
            str = str + ((char) br.read());
            //System.out.println("READ: " + str);
        }

        //System.out.println("READ FILE NAME : " + str);

        br.close();

        /*MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] messageDigest = md.digest(str.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        String sha1 = bigInt.toString(6);*/

        String test1 = "1010";
        /*byte[] b = test1.getBytes(Charset.forName("UTF-8"));

        String sha1 = byteArrayToHexString(b);*/
        String sha1 = encryptPassword(test1);
        System.out.println("TEST BYTE: " + test1);
        
        System.out.println("SHA : " + sha1);

        String dirName = ".\\Git\\"+fileName;
        File dir = new File (dirName);
        File file = new File (dir, sha1);

        /*OutputStream os = new FileOutputStream(myFile);*/
        PrintWriter pw = new PrintWriter(file);

        pw.print(sha1);

        pw.close();

        System.out.println("Reading contents of " + fileName + ": " + str);

        System.out.println("\nCreating new blob " + " from content: " + sha1);
    }

    /*public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i=0; i < b.length; i++) {
          result +=
                Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }*/

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

    private static String byteToHex(final byte[] hash)
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

}