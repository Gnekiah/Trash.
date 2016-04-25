package pro;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ReceiverPanel extends JPanel {
    // 输入框长宽
    private int WIDTH = 5;
    private int HEIGHT = 40;
    private byte tcpID = 0x06;
    private byte udpID = 0x11;
    private byte[] ipsData = new byte[1024 * 8];
    private int ipsDataLength = 0;
    private JTextArea inputField = null;
    private JLabel transType = null;
    private JTextArea appDataField = null;
    private JTextArea transDataField = null;
    private JTextArea netDataField = null;
    private JTextArea macDataField = null;
    private JTextArea phyDataField = null;
    ReceiverPanel() {
        inputField = new JTextArea(WIDTH, HEIGHT);
        inputField.setLineWrap(true);
        transType = new JLabel("tcp");
        appDataField = new JTextArea(WIDTH, HEIGHT);
        appDataField.setLineWrap(true);
        transDataField = new JTextArea(WIDTH, HEIGHT);
        transDataField.setLineWrap(true);
        netDataField = new JTextArea(WIDTH, HEIGHT);
        netDataField.setLineWrap(true);
        macDataField = new JTextArea(WIDTH, HEIGHT);
        macDataField.setLineWrap(true);
        phyDataField = new JTextArea(WIDTH, HEIGHT);
        phyDataField.setLineWrap(true);
            
        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(1));
        p1.add(new JLabel("Input:"));
        JScrollPane s1 = new JScrollPane(inputField);
        s1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        p1.add(s1);
        
        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout(1));
        p3.add(new JLabel("AppData:"));
        JScrollPane s3 = new JScrollPane(appDataField);
        s3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        p3.add(s3);
        
        JPanel p4 = new JPanel();
        p4.setLayout(new FlowLayout(1));
        p4.add(transType);
        JScrollPane s4 = new JScrollPane(transDataField);
        s4.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        p4.add(s4);
        
        JPanel p5 = new JPanel();
        p5.setLayout(new FlowLayout(1));
        p5.add(new JLabel("NetData:"));
        JScrollPane s5 = new JScrollPane(netDataField);
        s5.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        p5.add(s5);
        
        JPanel p6 = new JPanel();
        p6.setLayout(new FlowLayout(1));
        p6.add(new JLabel("MacData:"));
        JScrollPane s6 = new JScrollPane(macDataField);
        s6.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        p6.add(s6);
        
        JPanel p7 = new JPanel();
        p7.setLayout(new FlowLayout(1));
        p7.add(new JLabel("PhyData:"));
        JScrollPane s7 = new JScrollPane(phyDataField);
        s7.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        p7.add(s7);
        
        this.setLayout(new GridLayout(6, 1));
        this.add(p7);
        this.add(p6);
        this.add(p5);
        this.add(p4);
        this.add(p3);
        this.add(p1);
    }
    
    public void starting(int serverPort) {
        Data data = new Data();
        Receiver receiver = new Receiver(serverPort, data);
        receiver.start();
        while (true) {
            try {Thread.sleep(100);}catch(Exception e){}
            if (!data.getCheck()) {
                doUpdate(data.getData());
                data.setCheck(true);
            }
        }
    }
    
    private void doUpdate(byte[] macData) {
        UnpackProtocol up = new UnpackProtocol();
        // 将物理层的二进制显示到界面
        phyDataField.setText(up.getPhy(macData));
        String adf = "";
        // 将mac层数据显示到界面
        for (int i = 0; i < macData.length; i++) {
            String tmp = Integer.toHexString(macData[i]);
            adf += tmp.length()==1 ? "0"+tmp : tmp;
        }
        macDataField.setText(adf);
        // 显示网络层数据包
        byte[] netData = up.unPackMac(macData);
        adf = "";
        for (int i = 0; i < netData.length; i++) {
            String tmp = Integer.toHexString(netData[i]);
            adf += tmp.length()==1 ? "0"+tmp : tmp;
        }
        netDataField.setText(adf);

        // 获得传输层数据包
        byte[] transData = up.unPackIp(netData);
        
        if ((netData[6] & 0x20) == 0x20) {
            for (int i = 0; i < transData.length; i++) {
                ipsData[i+ipsDataLength] = transData[i];
            }
            ipsDataLength += transData.length;
        }
        else {
            for (int i = 0; i < transData.length; i++) {
                ipsData[i+ipsDataLength] = transData[i];
            }
            ipsDataLength += transData.length;
            // 获得IP数据包内的数据上层协议ID,并根据协议获得应用层数据包
            byte upperID = up.getProIdInIpData(netData);
            byte[] trueData = new byte[ipsDataLength];
            for (int i = 0; i < ipsDataLength; i++) {
                trueData[i] = ipsData[i];
            }
            merge(trueData, up, upperID);
            ipsDataLength = 0;
        }
    }
    
    private void merge(byte[] transData, UnpackProtocol up, byte upperID) {
        
        String adf = "";
        for (int i = 0; i < transData.length; i++) {
            String tmp = Integer.toHexString(transData[i]);
            adf += tmp.length()==1 ? "0"+tmp : tmp;
        }
        transDataField.setText(adf);
        transType.setText(upperID==tcpID?"tcpData":"udpData");
        byte[] appData = upperID == tcpID ? up.unPackTcp(transData) : up.unPackUdp(transData);
        adf = "";
        for (int i = 0; i < appData.length; i++) {
            String tmp = Integer.toHexString(appData[i]);
            adf += tmp.length()==1 ? "0"+tmp : tmp;
        }
        appDataField.setText(adf);
        // 获得对方用户数据
        long timeStamp = up.getTimeStamp(appData);
        byte[] sourceData = up.getSourceData(appData);
        adf = "time stamp: " + Long.toString(timeStamp) + ": ";
        Charset cs = Charset.forName ("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(sourceData.length);
        bb.put (sourceData);
        bb.flip ();
        CharBuffer cb = cs.decode (bb);
        char[] chars = cb.array();
        for (int i = 0; i < chars.length; i++) {
            adf += chars[i];
        }
        inputField.setText(adf);
    }
}
