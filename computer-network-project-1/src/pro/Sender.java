package pro;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Sender {
    
    private String ip = null;
    
    private int port = 0;
    
    Sender(String ip, int port) {
        setIpPort(ip, port);
    }
    
    public void setIpPort(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    /**
     * 发送数据
     * @param b 字节数组
     */
    public void send(byte[] s) {
        try {
            Socket socket = new Socket(ip, port);
            OutputStream out = socket.getOutputStream();
            out.write(s);            
            out.flush();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
}
