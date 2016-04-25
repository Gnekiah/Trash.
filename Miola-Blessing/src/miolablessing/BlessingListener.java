package miolablessing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Iterator;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class BlessingListener extends Thread {
    
    private JTable usertable = null;
    private final int buffSize = 1000;
    private byte[] recvBuff = null;
    private DatagramSocket server = null;
    private DatagramPacket recvPacket = null;
    
    public BlessingListener(JTable usertable, int port) {
        this.usertable = usertable;
        recvBuff = new byte[buffSize];
        try {
            server = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        recvPacket = new DatagramPacket(recvBuff , recvBuff.length);
    }
    
    public void run() {
        while (true) {
            listening();
        }
    }

    private void listening() {
        try {
            server.receive(recvPacket);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("Client udp get message");
        String recvStr = new String(recvPacket.getData(), 0, recvPacket.getLength());
        String[] strs = recvStr.split("\\|");
        usertable.removeAll();
        int i = 0;
        for (String s : strs) {
            String[] ss = s.split("&");
            usertable.setValueAt(ss[0], i, 0);
            usertable.setValueAt(ss[3], i, 1);
            usertable.setValueAt(ss[1], i, 2);
            i++;
            System.out.println(ss[0]);
        }
    }
}
