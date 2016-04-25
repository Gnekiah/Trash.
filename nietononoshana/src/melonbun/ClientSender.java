package melonbun;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientSender {
    private static final String TARGET_IP = "127.0.0.1";
    private static final int TARGET_PORT = 20113;

    public static FileInfo doSend(File file, boolean isPublicKey, String filename) {
        Socket client = null;
        FileInputStream fis = null;
        DataOutputStream dos = null;
        try {
            client = new Socket(TARGET_IP, TARGET_PORT);
            fis = new FileInputStream(file);
            dos = new DataOutputStream(client.getOutputStream());
            dos.writeUTF(filename);
            dos.flush();
            if (isPublicKey) {
                dos.writeLong(-2);
            }
            else {
                dos.writeLong(file.length());
            }
            dos.flush();
            byte[] sendBytes = new byte[1024];
            int length = 0;
            while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                dos.write(sendBytes, 0, length);
                dos.flush();
            }
        } catch (Exception e) {
            return null;
        } finally {
            try {
                fis.close();
                dos.close();
                client.close();
            } catch (IOException e) {
                return null;
            }
        }
        return new FileInfo(filename, file.length());
    }

    public static void doSend(String message, String filename) {
        Socket client = null;
        DataOutputStream dos = null;
        try {
            client = new Socket(TARGET_IP, TARGET_PORT);
            dos = new DataOutputStream(client.getOutputStream());
            dos.writeUTF(filename + ".rsasign");
            dos.flush();
            dos.writeLong(-1);
            dos.flush();
            byte[] sendBytes = message.getBytes();
            dos.write(sendBytes, 0, sendBytes.length);
            dos.flush();
            
        } catch (Exception e) {
            return;
        } finally {
            try {
                dos.close();
                client.close();
            } catch (IOException e) {
                return;
            }
        }
    }
}
