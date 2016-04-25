package qwert;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class Youlast {
    private static final String RSA_ALGORITHM = "RSA";
    private static final int RSA_KEYSIZE = 1024;
    private static String PUBLIC_KEY_FILE = "PublicKey";
    private static String PRIVATE_KEY_FILE = "PrivateKey";

    private static void generateKeyPair() {
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e1) {
            return;
        }
        keyPairGenerator.initialize(RSA_KEYSIZE, secureRandom);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        Key publicKey = keyPair.getPublic();
        Key privateKey = keyPair.getPrivate();

        ObjectOutputStream oos1 = null;
        ObjectOutputStream oos2 = null;
        try {
            oos1 = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));
            oos2 = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILE));
            oos1.writeObject(publicKey);
            oos2.writeObject(privateKey);
            oos1.close();
            oos2.close();
        } catch (Exception e) {
            return;
        }
    }

    public static String RSAEncrypt(String source) {
        generateKeyPair();
        Key privateKey;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
            privateKey = (Key) ois.readObject();
            ois.close();
        } catch (Exception e) {
            return null;
        }
        Cipher cipher = null;
        BASE64Encoder encoder = null;
        byte[] b = null;
        try {
            cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            b = cipher.doFinal(source.getBytes());
            encoder = new BASE64Encoder();
        } catch (Exception e) {
            return null;
        }
        return encoder.encode(b);
    }

    public static String RSADecrypt(String cryptograph) {
        Key publicKey;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
            publicKey = (Key) ois.readObject();
            ois.close();
        } catch (Exception e) {
            return null;
        }
        Cipher cipher = null;
        byte[] b = null;
        try {
            cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            BASE64Decoder decoder = new BASE64Decoder();
            b = cipher.doFinal(decoder.decodeBuffer(cryptograph));
        } catch (Exception e) {
            return null;
        }
        return new String(b);
    }

    public static void main(String[] args) throws Exception {
        String source = "恭喜发财fffffffffff!";// 要加密的字符串
        System.out.println("准备用公钥加密的字符串为：" + source);

        String cryptograph = RSAEncrypt(source);// 生成的密文
        System.out.print("用公钥加密后的结果为:" + cryptograph);
        System.out.println();

        String target = RSADecrypt(cryptograph);// 解密密文
        System.out.println("用私钥解密后的字符串为：" + target);
        System.out.println();
    }
}