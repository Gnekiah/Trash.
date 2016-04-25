package miolablessing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JTable;

public class Blessing extends Thread {
    
    private JTable usertable = null;
    
    private final int tcplistenport = Miola.TCPPORT;
    private final int udplistenport = 10088;
    private DatagramSocket client = null;
    
    public Blessing(JTable usertable) {
        this.usertable = usertable;
        try {
            client = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        BlessingListener bl = new BlessingListener(usertable, udplistenport);
        bl.start();
    }

    public void run() {
        while (true) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listening();
        }
    }

    private void listening() {        
        String sendStr = Miola.ID + "|" + Miola.TCPPORT + "|" + Miola.info + "|" + udplistenport;
        byte[] sendBuff = sendStr.getBytes();
        DatagramPacket sendPacket;
        try {
            sendPacket = new DatagramPacket(sendBuff, sendBuff.length, InetAddress.getByName(Miola.serverIp), Miola.serverPort);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
            return;
        }
        try {
            client.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
