package miolablessing;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceive extends Thread {

    public void run() {
        ServerSocket server = null;
        DataInputStream dis = null;
        Socket client = null;
        File saveFile = null;
        try {
            server = new ServerSocket(Miola.filereceiveport);
        } catch (IOException e) {
            return;
        }
        while (true) {
            try {
                client = server.accept();
                dis = new DataInputStream(client.getInputStream());
               
                File qw = new File("D:\\aaaaaaaa\\");
                if (!qw.exists()) {
                    qw.mkdir();
                }
                
                saveFile = new File("D:\\aaaaaaaa\\" + System.currentTimeMillis());
                doAccept(dis, saveFile);
            } catch (Exception e) {
                return;
            } finally {
                try {
                    client.close();
                    dis.close();
                } catch (IOException e) {
                    return;
                }
            }
        }
    }

    private void doAccept(DataInputStream dis, File saveFile) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(saveFile);
            byte[] sendBytes = new byte[1024 * 64];
            while (true) {
                int read = 0;
                read = dis.read(sendBytes);
                if (read == -1)
                    break;
                fos.write(sendBytes, 0, read);
                fos.flush();
            }
        } catch (Exception e) {
            return;
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                return;
            }
        }
    }
}
