package rsatest;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

public class M {
    public String RSA(String S) {
        byte[] b = S.getBytes();
        KeyPair keys;
        byte[] encrypted = null;
        try {
            keys = KeyPairGenerator.getInstance("RSA").generateKeyPair();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, keys.getPublic());
            encrypted = cipher.doFinal(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(encrypted);
    }
    
    public String UnRSA(String S) {
        byte[] b = S.getBytes();
        KeyPair keys;
        byte[] encrypted = null;
        try {
            keys = KeyPairGenerator.getInstance("RSA").generateKeyPair();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, keys.getPrivate());
            encrypted = cipher.doFinal(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(encrypted);
    }
    
    public static void main(String[] a) {
        M m = new M();
        String s = "qawxscthfv7b5";
        String result = m.RSA(s);
        System.out.println(result);
        System.out.println(m.UnRSA(result));
    }
}
