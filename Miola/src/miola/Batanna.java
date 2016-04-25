package miola;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;

public class Batanna extends Thread {

    private LinkedList<HostInfo> hostInfo = null;
    private final int udplistenport = 10086;
    private final int buffSize = 1000;
    private byte[] recvBuff = null;
    private DatagramSocket server = null;
    private DatagramPacket recvPacket = null;

    public Batanna(LinkedList<HostInfo> hostInfo) {
        this.hostInfo = hostInfo;
        recvBuff = new byte[buffSize];
        try {
            server = new DatagramSocket(udplistenport);
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
        System.out.println("server udp get message");
        String recvStr = new String(recvPacket.getData(), 0, recvPacket.getLength());
        String[] strs = recvStr.split("\\|");
        if (strs.length != 4)
            return;

        System.out.println(strs[0]);
        System.out.println(strs[1]);
        System.out.println(strs[2]);
        System.out.println(strs[3]);
        System.out.println("Info size=" + hostInfo.size());
        
        Iterator iter = hostInfo.iterator();
        boolean flag = false;
        while (iter.hasNext()) {
            HostInfo tmpHost = (HostInfo) iter.next();
            if (tmpHost.getIP().equals(recvPacket.getAddress().getHostAddress())) {
                
                tmpHost.updateStamp();
                flag = true;
                break;
            }
        }
        if (!flag) {
            hostInfo.add(
                    new HostInfo(strs[0], recvPacket.getAddress().getHostAddress(), Integer.valueOf(strs[1]).intValue(), strs[2]));
        }
        cleanTimeout();
        String sendStr = "";
        iter = hostInfo.iterator();
        flag = false;
        while (iter.hasNext()) {
            HostInfo tmpHost = (HostInfo) iter.next();
            sendStr += flag ? "|" : "";
            flag = true;
            sendStr += tmpHost.getID() + "&" + tmpHost.getIP() + "&" + tmpHost.getPort() + "&" + tmpHost.getInfo();
        }

        byte[] sendBuff = sendStr.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuff, sendBuff.length, recvPacket.getAddress(),
                Integer.valueOf(strs[3]).intValue());
        try {
            server.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cleanTimeout() {
        Iterator itert = hostInfo.iterator();
        while (itert.hasNext()) {
            HostInfo tmpHost = (HostInfo) itert.next();
            if (tmpHost.timeout()) {
                itert.remove();
            }
        }
    }
}
