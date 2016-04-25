package miolablessing;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;


public class SendMessage {
    
    public static void Send(String recvID, String message) {
        String sendMsg = Miola.ID + "|" + recvID + "|" + message;
        Socket send;
        try {
            send = new Socket(Miola.serverIp, Miola.servertcpport);
            System.out.println("I will send message:" + sendMsg);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(send.getOutputStream())); 
            bw.write(sendMsg);
            bw.flush();
            bw.close();
            send.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void dirshare(String path, String ip) {
        File f = new File(path);
        LinkedList<String> ss = new LinkedList<String>();
        if (f.isDirectory()) {
            File[] ff = f.listFiles();
            for (File fff:ff) {
                ss.add(fff.getAbsolutePath());
            }
        }
        else {
            ss.add(path);
        }
        String sendMsg = "";
        Iterator iter = ss.iterator();
        boolean falg = false;
        while (iter.hasNext()) {
            if (falg == false) {
                falg = true;
            }
            else {
                sendMsg += "|";
            }
            sendMsg += (String) iter.next();
        }
        
        Socket send;
        try {
            send = new Socket(ip, Miola.sharePort);
            System.out.println("I will send message:" + sendMsg);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(send.getOutputStream())); 
            bw.write(sendMsg);
            bw.flush();
            bw.close();
            send.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendFile(String filename, String ip) {
        Socket client = null;
        FileInputStream fis = null;
        DataOutputStream dos = null;
        try {
            client = new Socket(ip, Miola.filereceiveport);
            fis = new FileInputStream(filename);
            dos = new DataOutputStream(client.getOutputStream());
          
            byte[] sendBytes = new byte[1024];
            int length = 0;
            while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                dos.write(sendBytes, 0, length);
                dos.flush();
            }
        } catch (Exception e) {
            return ;
        } finally {
            try {
                fis.close();
                dos.close();
                client.close();
            } catch (IOException e) {
                return ;
            }
        }
    }

}
