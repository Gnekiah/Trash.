package qwerty;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MatrixReceiver extends Thread {
    
    /**
     * 端口号
     */
    private int port = 11111;
    
    /**
     * 记录分数
     */
    private int score = 0;
    
    /**
     * 记录矩阵
     */
    private int[][] matrix = new int[35][20];
    
    /**
     * 构造函数
     * @param name
     */
    public MatrixReceiver(String name) {
        super(name);
        for (int i = 0; i < 35; i++)
            for (int j = 0; j < 20; j++)
                matrix[i][j] = 0;
    }

    /**
     * 接受矩阵
     */
    public void receiveMatrix() {
        byte[] linerByte = new byte[21];
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();

            InputStream in = socket.getInputStream();
            in.read(linerByte);
            int liner = (int)((linerByte[0]) & 0xFF);
            score = (int)(linerByte[21] & 0xFF);
            for (int i = 0; i < 20; i++) {
                matrix[liner][i] = (int)((linerByte[i+1]) & 0xFF);
            }
            serverSocket.close();
           
        } catch(IOException e) { e.printStackTrace(); }
    }
    
    /**
     * 获取矩阵
     * @return
     */
    public int[][] getMatrix() {
        return matrix;
    }
    
    /**
     * 获取分数
     * @return
     */
    public int getScore() {
        return score;
    }

    /**
     * 运行线程
     */
    public void run() {
        while(true) {
            receiveMatrix();
        }
    }

}

