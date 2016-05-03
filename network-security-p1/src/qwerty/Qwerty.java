package qwerty;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Qwerty {
    private final static String passwdfilename = "E:/passwd.txt";

    public Qwerty() { }

    public void testfor1() {
        String passwd = null;
        String hexwd = null;
        // String x0 = "them, understand";
        // String h0 =
        // "59A36D7241CAE3D89BB8F43155CD11E337B64D35F481798EB55C2096B59A06A9";
        String x = "000 This is a be";
        String h = "89D737584B3E104ECCE17AF490E25621D7D04DFBBE31882601D563B8DAFCE160";
        for (int i = 0; i < 1000000; i++) {
            passwd = String.valueOf(i);
            if (passwd.length() < 6) {
                for (int m = passwd.length(); m < 6; m++) {
                    passwd = "0" + passwd;
                }
            }
            try {
                hexwd = byte2HexStr(encrypt(x, passwd));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (h.equals(hexwd)) {
                System.out.println("Success::" + passwd);
                return;
            }
        }
    }
    
    public void testfor2() {
        byte[] passwd = {'0','0','0','0','0','0'};
        String hexwd = null;
        String x = "000 This is a be";
        String h = "86A50B9B33E20C40D3D9D2A22231BA57AB4CBB691ECCD859D7D409403D5190B4";
        while(true) {
            int flag = 0;
            for (int i = 0; i < 6; i++) {
                if (passwd[i] == 'z') {
                    flag++;
                }
            }
            if (flag==6) {
                break;
            }
            carry(passwd, 5);
            try {
                hexwd = byte2HexStr(encrypt(x, new String(passwd)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (h.equals(hexwd)) {
                System.out.println("Success::" + passwd);
                return;
            }
        }
    }
    
    public void carry(byte[] passwd, int i) {
        passwd[i] = (byte) (passwd[i]=='9' ? 'a' : (passwd[i]=='z'?'0':passwd[i]+1));
        if (passwd[i] == '0') {
            carry(passwd, i-1);
        }
    }
    
    
    
    public void testfor3() {
        byte[] passwd = {'0','0','0','0','0','0'};
        String hexwd = null;
        String x = "000 This is a be";
        String h = "B09650CDE80D23E65A6E8545F0D6B66CD156FA8FA4ADECAE43EDAFAF11D782BF";
        while(true) {
            int flag = 0;
            for (int i = 0; i < 6; i++) {
                if (passwd[i] == ']') {
                    flag++;
                }
            }
            if (flag==6) {
                break;
            }
            carryv2(passwd, 5);
            try {
                hexwd = byte2HexStr(encrypt(x, new String(passwd)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (h.equals(hexwd)) {
                System.out.println("Success::" + passwd);
                return;
            }
        }
    }
    
    public void carryv2(byte[] passwd, int i) {
        byte[] alphabet = {'@','#','%','&','+','-','*','/','[',']'};
        if (passwd[i] == '9') {
            passwd[i] = 'a';
        }
        else if (passwd[i] == 'z') {
            passwd[i] = alphabet[0];
        }
        else if (passwd[i] == ']') {
            passwd[i] = '0';
            carry(passwd, i-1);
        }
        else if ((passwd[i] >= '0' && passwd[i] <= '9') || (passwd[i] >= 'a' && passwd[i] <= 'z')) {
            passwd[i]++;
        }
        else {
            for (int k = 0; k < 9; k++) {
                if (alphabet[k] == passwd[i]) {
                    passwd[i] = alphabet[k+1];
                    break;
                }
            }
        }
    }
    
    /**
     * 以下代码用于测试
    public void test2() {
        String x = " them, understan";
        String passwd = "123456";
        byte[] hexwd = encrypt(x, passwd);
        System.out.println(new String(hexwd));
        byte[] re = decrypt(hexwd, passwd);
        System.out.println(new String(re));
    }

    public void test3() {
        String x = "123xyz";
        System.out.println(byte2HexStr(x.getBytes()));
    }

    public void test4() {
        String x = "000test This is ";
        String passwd = "123456";
        byte[] z = passwd.getBytes();
        for (int i = 0; i < z.length; i++) {
            System.out.print(z[i]);
        }
        String hexwd = byte2HexStr(encrypt(x, passwd));
        System.out.println("::" + hexwd);
    }

    public void test5() {
        String h = "59A36D7241CAE3D89BB8F43155CD11E337B64D35F481798EB55C2096B59A06A9";
        byte[] b = hexStr2Byte(h);
        byte[] z = null;
        String passwd = null;
        for (int i = 0; i < 1000000; i++) {
            passwd = String.valueOf(i);
            if (passwd.length() < 6) {
                for (int m = passwd.length(); m < 6; m++) {
                    passwd = "0" + passwd;
                }
            }
            try {
                z = decrypt(b, passwd);
                System.out.println(z.length);
                System.out.println(new String(z));
            } catch (Exception e) {
            }
        }
    }
    **/

    /**
     * 生成6位数纯数字的密码
     */
    public void passwdGenerator() {
        File f = new File(passwdfilename);
        BufferedWriter bw = null;
        if (!f.exists()) {
            try {
                f.createNewFile();
                bw = new BufferedWriter(new FileWriter(f));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        String passwd = null;
        for (int i = 0; i < 1000000; i++) {
            passwd = String.valueOf(i);
            if (passwd.length() < 6) {
                for (int m = passwd.length(); m < 6; m++) {
                    passwd = "0" + passwd;
                }
            }
            try {
                bw.write(passwd + "\n");
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] decrypt(byte[] content, String password) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(password.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(content);
        return result;
    }

    public static byte[] encrypt(String content, String password) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(password.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        byte[] byteContent = content.getBytes("utf-8");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(byteContent);
        return result;
    }

    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    public static byte[] hexStr2Byte(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static void main(String[] args) {
        Qwerty q = new Qwerty();
        q.passwdGenerator();
        q.testfor1();
        q.testfor2();
        q.testfor3();
    }
}
