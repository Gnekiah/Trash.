package qwertyserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MatrixSender extends Thread {
    
    /**
     * 目标IP
     */
    private String ip = "192.168.99.122";
    
    /**
     * 目标IP的端口
     */
    private int port = 11112;
    
    /**
     * 连通检测延时
     */
    private int timeOut = 3000;
    
    private int[][] matrix = new int[35][20];
    
    /**
     * 初始化实例
     * @param name 传入的线程名称
     */
    public MatrixSender(String name) {
        super(name);
        for (int i = 0; i < 35; i++)
            for (int j = 0; j < 20; j++)
                matrix[i][j] = 0;
    }

    /**
     * 测试目标ip是否能够连通
     * @return 能够连通，返回true
     */
    public boolean checkConnected() {
        Socket checkSocket= new Socket();
        try {
            checkSocket.connect(new InetSocketAddress(ip, port), timeOut);
            boolean check = checkSocket.isConnected(); 
            return check;
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally { 
            try { checkSocket.close(); }
            catch(IOException e) { e.printStackTrace(); }
        }
        return false;
    }

    /**
     * 发送矩阵
     * @param matrix 矩阵，int[][]型
     * @return 返回true表示发送成功
     */
    public boolean sendMatrix() {
        try {
            Socket sendSocket = new Socket(ip, port);
            OutputStream out = sendSocket.getOutputStream();
            for (int i = 0; i < 35; i++)
                out.write(translateIntToBytes(matrix[i], i));
            out.flush();
            sendSocket.close();
            return true;
        } catch(IOException e) { e.printStackTrace(); }
        return false;
    }

    /**
     * 将int型数组转换成byte型数组
     * @param matrix 传入int矩阵的行
     * @id 矩阵的第x行
     * @return 返回byte矩阵的行，byte长度为矩阵行数+1，其中byte[0]为矩阵的第x行
     */
    private byte[] translateIntToBytes(int[] matrix, int id) {
        byte[] tmpByte = new byte[21];
        tmpByte[0] = (byte)(id & 0xFF);
        for (int j = 1; j < 21; j++) {
            System.out.println(matrix[j-1]);
            tmpByte[j] = (byte)(matrix[j-1] & 0xFF);
        }
        return tmpByte;
    }
    
    public void updateMatrix(int[][] matrix) {
        for (int i = 0; i < 35; i++)
            for (int j = 0; j < 20; j++)
                this.matrix[i][j] = matrix[i][j];
    }
    

    public void run() {
        while(true) {
            if (checkConnected()) 
                while (sendMatrix()) {
                    try { sleep(100); } 
                    catch (InterruptedException e) { e.printStackTrace(); }
                }
            try { sleep(1000); } 
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
 