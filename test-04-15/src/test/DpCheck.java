/**
 * README
 * 修改1：添加账号-密码认证
 * 修改2：动态口令验证时，使用3个连续且间隔为1分钟的时间戳进行哈希运算，生成3个运算结果，以增加时间容错性
 * 
 *      DpCheck类提供的接口：
 * boolean passwdCheckOut(String name, String passwd);
 *      传入ID和密码，返回true表示验证通过，false表示验证失败
 * boolean dpCheckOut(String name, String passwd);
 *      传入ID和动态口令，返回true表示验证通过，false表示验证失败
 *      
 * 部署：将idsn.txt，passwd.txt，seed.txt拷贝到D盘根目录
 * 在服务器初始化时，直接new DpCheck()，之后即可通过调用两个接口实现验证
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

public class DpCheck {
    private final static String IDSN_FILE = "D:/idsn.txt";
    private final static String SEED_FILE = "D:/seed.txt";
    private final static String PASSWD_FILE = "D:/passwd.txt";
    
    private HashMap<String, String> idsnmap = null;
    private HashMap<String, String> seedmap = null;
    private HashMap<String, String> passwdmap = null;
    
    public DpCheck() {
        idsnmap = new HashMap<String, String>();
        seedmap = new HashMap<String, String>();
        passwdmap = new HashMap<String, String>();
        init();
    }
    
    private void init() {
        File idsnFile = new File(IDSN_FILE);
        File seedFile = new File(SEED_FILE);
        File passwdFile = new File(PASSWD_FILE);
        if (!(idsnFile.exists() && seedFile.exists() && passwdFile.exists())) {
            System.out.println("ERROR: Not found idsn file or seed file.");
            System.exit(1);
        }
        String tmp = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(idsnFile));
            while((tmp=reader.readLine()) != null) {
                String[] splitTmp = tmp.split(" ");
                idsnmap.put(splitTmp[0], splitTmp[1]);
            }
            reader.close();
            reader = new BufferedReader(new FileReader(seedFile));
            while((tmp=reader.readLine()) != null) {
                String[] splitTmp = tmp.split("  ");
                seedmap.put(splitTmp[0], splitTmp[1]);
            }
            reader.close();
            reader = new BufferedReader(new FileReader(passwdFile));
            while((tmp=reader.readLine()) != null) {
                String[] splitTmp = tmp.split(" ");
                passwdmap.put(splitTmp[0], splitTmp[1]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean passwdCheckOut(String name, String passwd) {
        return passwd.equals(passwdmap.get(name));
    }
    
    public boolean dpCheckOut(String name, String dps) {
        String seed = seedmap.get(idsnmap.get(name));
        if (seed == null) {
            return false;
        }
        long timestamp = System.currentTimeMillis() / 60000 + 480;
        String shaed1 = SHA1(seed + String.valueOf(timestamp));
        String shaed2 = SHA1(seed + String.valueOf(timestamp+1));
        String shaed3 = SHA1(seed + String.valueOf(timestamp-1));
        return (Integer.valueOf(shaed1.substring(0, 6), 16).toString().substring(0, 6).equals(dps) ||
                Integer.valueOf(shaed2.substring(0, 6), 16).toString().substring(0, 6).equals(dps) || 
                Integer.valueOf(shaed3.substring(0, 6), 16).toString().substring(0, 6).equals(dps));
    }
    
    
    private String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    private String SHA1(String text) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            byte[] sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    /*
    public static void main(String[] args) {
        DpCheck dp = new DpCheck();
        Scanner in = new Scanner(System.in);
        System.out.println("Input: ");
        String name = in.nextLine();
        String dps = in.nextLine();
        if (dp.dpCheckOut(name, dps))
            System.out.println("ok");
        else
            System.out.println("error");
    }
    */
}