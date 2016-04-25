package rsatest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha {
    public static final String SHA_ALGORITHM = "SHA-256";
    public static String SHA256Encrypt(File file) {
        FileInputStream in = null;
        MessageDigest messagedigest = null;
        try {
            in = new FileInputStream(file);
            messagedigest = MessageDigest.getInstance(SHA_ALGORITHM);
            byte[] buffer = new byte[1024 * 1024];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                messagedigest.update(buffer, 0, len);
            }
            in.close();
        }
        catch (Exception e) {
            return null;
        }
        return getDigestStr(messagedigest.digest());
    }

    private static String getDigestStr(byte[] origBytes) {
        String tempStr = null;
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < origBytes.length; i++) {
            tempStr = Integer.toHexString(origBytes[i] & 0xff);
            if (tempStr.length() == 1) {
                stb.append("0");
            }
            stb.append(tempStr);
        }
        return stb.toString();
    }

    public static void main(String[] args) throws IOException {
        File f = new File("C:/Users/DouBear/Desktop/待删除/code.exe");
        System.out.println(SHA256Encrypt(f));
    }
}
