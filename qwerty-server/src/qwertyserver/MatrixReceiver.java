package qwertyserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MatrixReceiver extends Thread {
    
    private int port = 11112;
    
    
    private int[][] matrix = new int[35][20];
    
    public MatrixReceiver(String name) {
        super(name);
        for (int i = 0; i < 35; i++)
            for (int j = 0; j < 20; j++)
                matrix[i][j] = 0;
    }


    public void receiveMatrix() {
        byte[] linerByte = new byte[21];
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();

            InputStream in = socket.getInputStream();
            in.read(linerByte);
            int liner = (int)((linerByte[0]) & 0xFF);
            for (int i = 0; i < 20; i++) {
                matrix[liner][i] = (int)((linerByte[i+1]) & 0xFF);
            }
            serverSocket.close();
           
        } catch(IOException e) { e.printStackTrace(); }
    }
    
    public int[][] getMatrix() {
        return matrix;
    }

    public void run() {
        while(true) {
            receiveMatrix();
        }
    }

}
