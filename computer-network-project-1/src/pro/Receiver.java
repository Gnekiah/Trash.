package pro;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver extends Thread {

    private Data data = null;
    
    private int port = 0;
    
    public Receiver(int port, Data data) {
        this.port = port;
        this.data = data;
    }

    @SuppressWarnings("resource")
    public void run() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream reader = null;

        byte[] s = new byte[1024*1024];
        
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        while(true) {
            try {
                socket = serverSocket.accept();
                reader = socket.getInputStream();
                int length = reader.read(s);
                byte[] b = new byte[length];
                for (int i = 0; i < length; i++) {
                    b[i] = s[i];
                }
                data.setData(b);
                data.setCheck(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}