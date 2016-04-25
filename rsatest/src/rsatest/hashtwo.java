package rsatest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class hashtwo {
    /**
     * 适用于上G大的文件
     */
     public static String getFileSha1(String path) throws OutOfMemoryError,IOException {
         File file=new File(path);
         FileInputStream in = new FileInputStream(file);
        MessageDigest messagedigest;
       try {
           messagedigest = MessageDigest.getInstance("SHA-1");
       
        byte[] buffer = new byte[1024 * 1024 * 10];
        int len = 0;
        
        while ((len = in.read(buffer)) >0) {
       //该对象通过使用 update（）方法处理数据
         messagedigest.update(buffer, 0, len);
        }
       
      //对于给定数量的更新数据，digest 方法只能被调用一次。在调用 digest 之后，MessageDigest 对象被重新设置成其初始状态。
        return byte2hex(messagedigest.digest());
       }   catch (NoSuchAlgorithmException e) {
           NQLog.e("getFileSha1->NoSuchAlgorithmException###", e.toString());
               e.printStackTrace();
           }
       catch (OutOfMemoryError e) {
           
           NQLog.e("getFileSha1->OutOfMemoryError###", e.toString());
               e.printStackTrace();
               throw e;
           }
       finally{
            in.close();
       }
        return null;
     }
}
