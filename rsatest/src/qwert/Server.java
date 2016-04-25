package qwert;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Server extends Thread {

    private static final int PORT = 20113;
    private static final String ACCEPT_FILE_PATH = "D:/ACCEPT_PATH/";
    private static String PUBLIC_KEY_FILE = ACCEPT_FILE_PATH + "PublicKey";
    
    private Queue<FileInfo> queue = null;
    private String filename = null;
    private long filelength = -1;
    private String sign = null;
    
    public Server(Queue<FileInfo> queue) {
        this.queue = queue;
        File f = new File(ACCEPT_FILE_PATH);
        if (!f.exists() || !f.isDirectory()) {
            f.mkdir();
        }
    }
    
    public void run() {
        ServerSocket server = null;
        DataInputStream dis = null;
        Socket client = null;
        String fileName = null;
        File saveFile = null;
        long fileLength = 0;
        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            return;
        }
        while (true) {
            try {
                client = server.accept();
                dis = new DataInputStream(client.getInputStream());
                fileName = dis.readUTF();
                fileLength = dis.readLong();
                if (fileLength == -2) {
                    saveFile = new File(PUBLIC_KEY_FILE);
                } else {
                    saveFile = new File(ACCEPT_FILE_PATH + fileName);
                }
                
                for (int i = 1; saveFile.exists(); i++) {
                    saveFile.renameTo(new File(ACCEPT_FILE_PATH + fileName + "(" + i + ")"));
                }
                doAccept(dis, saveFile);
                
                if (fileLength == -1) {
                    sign = saveFile.getAbsolutePath();
                }
                if (fileLength > -1) {
                    filename = saveFile.getAbsolutePath();
                    filelength = fileLength;
                }
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
            if (filename != null && sign != null) {
                queue.offer(new FileInfo(filename, filelength, sign));
                filename = null;
                sign = null;
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


    public static void main(String[] args) throws Exception {
        Queue<FileInfo> queue = new LinkedList<FileInfo>();
        Server server = new Server(queue);
        server.start();
        while(true) {
            Thread.sleep(1000);
            if (!queue.isEmpty()) {
                FileInfo f = queue.poll();
                System.out.println(f.getPath());
                System.out.println(f.getSign());
                System.out.println(f.getLength());
            }
        }
    }
}