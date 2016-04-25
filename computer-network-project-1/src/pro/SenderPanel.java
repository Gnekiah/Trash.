package pro;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SenderPanel extends JPanel {
    // 输入框长宽
    private int WIDTH = 5;
    private int HEIGHT = 40;
    private int SECTION = 8 * 6;
    private JTextArea inputField = null;
    private JTextField ipField = null;
    private JTextField portField = null;
    private JRadioButton tcpButton = null;
    private JRadioButton udpButton = null;
    private JButton send = null;
    private JTextArea appDataField = null;
    private JTextArea transDataField = null;
    private JTextArea netDataField = null;
    private JTextArea macDataField = null;
    private JTextArea phyDataField = null;

    SenderPanel() {
        inputField = new JTextArea(WIDTH, HEIGHT);
        inputField.setLineWrap(true);
        ipField = new JTextField(10);
        ipField.setText("127.0.0.1");
        portField = new JTextField(4);
        portField.setText("10086");
        tcpButton = new JRadioButton("tcp");
        udpButton = new JRadioButton("udp");
        send = new JButton("Send");
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

        ButtonGroup group = new ButtonGroup();
        group.add(tcpButton);
        group.add(udpButton);
        tcpButton.setSelected(true);

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(1));
        p1.add(new JLabel("Input:"));
        JScrollPane s1 = new JScrollPane(inputField);
        s1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        p1.add(s1);

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout(1));
        p2.add(new JLabel("IP:"));
        p2.add(ipField);
        p2.add(new JLabel("Port:"));
        p2.add(portField);
        p2.add(tcpButton);
        p2.add(udpButton);
        p2.add(send);

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout(1));
        p3.add(new JLabel("AppData:"));
        JScrollPane s3 = new JScrollPane(appDataField);
        s3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        p3.add(s3);

        JPanel p4 = new JPanel();
        p4.setLayout(new FlowLayout(1));
        p4.add(new JLabel("TransData:"));
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

        this.setLayout(new GridLayout(7, 1));
        this.add(p1);
        this.add(p2);
        this.add(p3);
        this.add(p4);
        this.add(p5);
        this.add(p6);
        this.add(p7);
        
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    sendMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendMessage() throws Exception {
        // 选择的传输模式 tcp/udp
        byte upperID = (byte) (tcpButton.isSelected() ? 0x06 : 0x11);  
        byte[] sendData = null;
        Protocol p = new Protocol();
        String sIp = InetAddress.getLocalHost().getHostAddress();
        String tIp = ipField.getText();
        String sPort = "12459";
        String tPort = portField.getText();
        String[] sIps = sIp.split("\\.");
        String[] tIps = tIp.split("\\.");
        
        if (sIps.length != 4 || tIps.length != 4) return;
        // 转换IP地址
        byte[] sourceIp = new byte[4];
        byte[] targetIp = new byte[4];
        byte[] sourcePort = new byte[2];
        byte[] targetPort = new byte[2];
        for (int i = 0; i < 4; i++) {
            int k = Integer.valueOf(sIps[i]);
            sourceIp[i] = (byte) k;
            k = Integer.valueOf(tIps[i]); 
            targetIp[i] = (byte) k;
        }
        // 转换端口
        int k = Integer.valueOf(sPort);
        sourcePort[0] = (byte) (k >> 8);
        sourcePort[1] = (byte) k;
        k = Integer.valueOf(tPort);
        targetPort[0] = (byte) (k >> 8);
        targetPort[1] = (byte) k;
        
        // 转换原始数据
        sendData = inputField.getText().getBytes("UTF-8");
        // 封装原始数据将并显示在界面
        byte[] appData = p.packApp(sendData);
        String adf = "";
        for (int i = 0; i < appData.length; i++) {
            String tmp = Integer.toHexString(appData[i]);
            adf += tmp.length()==1 ? "0"+tmp : tmp;
        }
        appDataField.setText(adf);
        // 根据选择的传输协议进行封装
        byte[] transData = upperID == 0x06 ? p.packTcp(sourcePort, targetPort, appData, sourceIp, targetIp) : p.packUdp(sourcePort, targetPort, appData, sourceIp, targetIp);
        adf = "";
        for (int i = 0; i < transData.length; i++) {
            String tmp = Integer.toHexString(transData[i]);
            adf += tmp.length()==1 ? "0"+tmp : tmp;
        }
        transDataField.setText(adf);
        
        // 分段大小
        int fec = transData.length / SECTION + ((transData.length % SECTION == 0) ? 0 : 1);
        // 分段发送数据
        for (int m = 0; m < fec; m++) {
            Thread.sleep(1000);
            // 封装ip数据包
            byte[] secTransData = null;
            if (fec - m > 1)
                secTransData = new byte[SECTION];
            else 
                secTransData = new byte[transData.length - (fec-1) * SECTION];
            // 拆分数据包
            for (int i = 0; i < secTransData.length; i++) {
                secTransData[i] = transData[i+m*SECTION];
            }
            byte[] ipData = p.packIp(sourceIp, targetIp, secTransData, upperID, m, false, (fec-m > 1));            
            adf = "";
            for (int i = 0; i < ipData.length; i++) {
                String tmp = Integer.toHexString(ipData[i]);
                adf += tmp.length()==1 ? "0"+tmp : tmp;
            }
            netDataField.setText(adf);
            // 封装mac帧
            byte[] macData = p.packMac(ipData);
            adf = "";
            for (int i = 0; i < macData.length; i++) {
                String tmp = Integer.toHexString(macData[i]);
                adf += tmp.length()==1 ? "0"+tmp : tmp;
            }
            macDataField.setText(adf);
            // 显示物理层二进制数据
            phyDataField.setText(p.getPhy(macData));
            Sender send = new Sender(ipField.getText(), Integer.valueOf(portField.getText()));
            send.send(macData);
        }
        
        
    }
}
