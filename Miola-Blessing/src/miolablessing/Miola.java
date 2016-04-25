package miolablessing;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Miola extends JFrame {
    
    public static final String serverIp = "127.0.0.1";
    public static final int serverPort = 10086;
    public static final int servertcpport = 10087;
    
    public static final int sharePort = 34345;
    public static String shareip = null;
    public static final int filereceiveport = 44343;
    
    public static final int TCPPORT = 19080;
    public static String ID = null;
    public static String info = null;
    private static final String appname = "eeeeeeeeee";
    
    private JTextField newid = null;
    private JTextField newInfo = null;
    private JButton startrun = null;
    
    private JTable usertable = null;
    private JTextArea chat = null;
    private JTextField chatitem = null;
    private JButton sendbutton = null;
    private JButton sharedir = null;
    private JButton shareview = null;
    private JTable sharetable = null;
    private JButton download = null;
    private JButton returnback = null;
    
    private Blessing bl = null;
    private MessageListener ml = null;
    private DirshareListener dl = null;
    private String dirpath = null;
    private FileReceive fr = null;
    
    public Miola() {
        super(appname);
        this.setLayout(null);
        this.setSize(800, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        newid = new JTextField("请输入ID");
        newInfo = new JTextField("请输入自我描述信息");
        startrun = new JButton("确定");
        
        newid.setBounds(300, 100, 200, 50);
        newInfo.setBounds(300, 170, 200, 50);
        startrun.setBounds(340, 240, 120, 50);
        
        this.add(newid);
        this.add(newInfo);
        this.add(startrun);
        
        usertable = new JTable(30, 3);
        JScrollPane sp = new JScrollPane(usertable);
        chat = new JTextArea();
        JScrollPane sp1 = new JScrollPane(chat);
        chatitem = new JTextField();
        sendbutton = new JButton("发送");
        sharedir = new JButton("共享目录给对方");
        shareview = new JButton("查看对方共享目录");
        sharetable = new JTable(100, 1);
        download = new JButton("进入/下载");
        JScrollPane sp2 = new JScrollPane(sharetable);
        returnback = new JButton("返回");
        
        sp.setBounds(0, 0, 300, 500);
        sp1.setBounds(310, 0, 470, 300);
        chatitem.setBounds(310, 310, 400, 30);
        sendbutton.setBounds(715, 310, 60, 30);
        sharedir.setBounds(310, 370, 150, 30);
        shareview.setBounds(470, 370, 150, 30);
        sp2.setBounds(0, 0, 780, 400);
        download.setBounds(100, 420, 100, 30);
        returnback.setBounds(220, 420, 100, 30);
        
        sp.setVisible(false);
        sp1.setVisible(false);
        chatitem.setVisible(false);
        sendbutton.setVisible(false);
        sharedir.setVisible(false);
        shareview.setVisible(false);
        sp2.setVisible(false);
        download.setVisible(false);
        returnback.setVisible(false);
        
        this.add(sp);
        this.add(sp1);
        this.add(chatitem);
        this.add(sendbutton);
        this.add(sharedir);
        this.add(shareview);
        this.add(sp2);
        this.add(download);
        this.add(returnback);
        this.setVisible(true);
        
        bl = new Blessing(usertable);
        ml = new MessageListener(chat);
        dl = new DirshareListener(sharetable);
        fr = new FileReceive();
        
        startrun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ID = newid.getText();
                info = newInfo.getText();
                newid.setVisible(false);
                newInfo.setVisible(false);
                startrun.setVisible(false);
                sp.setVisible(true);
                sp1.setVisible(true);
                sendbutton.setVisible(true);
                chatitem.setVisible(true);
                sharedir.setVisible(true);
                shareview.setVisible(true);
                bl.start();
                ml.start();
                dl.start();
                fr.start();
            }
        });
        
        sendbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    SendMessage.Send(usertable.getValueAt(usertable.getSelectedRow(), 0).toString(), chatitem.getText());
                }
                catch(Exception e) {
                    return;
                }
                chat.append(ID + "\n" + chatitem.getText() + "\n");
                chatitem.removeAll();
            }
        });
        
        sharedir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if(chooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    dirpath = chooser.getSelectedFile().getAbsolutePath();
                }
                try {
                SendMessage.dirshare(dirpath, usertable.getValueAt(usertable.getSelectedRow(), 2).toString());
                } catch(Exception e) {
                    return;
                }
            }
        });
        
        shareview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                sp.setVisible(false);
                sp1.setVisible(false);
                chatitem.setVisible(false);
                sendbutton.setVisible(false);
                sharedir.setVisible(false);
                shareview.setVisible(false);
                sp2.setVisible(true);
                download.setVisible(true);
                returnback.setVisible(true);
                try{
                shareip = usertable.getValueAt(usertable.getSelectedRow(), 2).toString();}
                catch(Exception e) {
                    return;
                }
            }
        });
        
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    SendMessage.dirshare("request|" + sharetable.getValueAt(sharetable.getSelectedRow(), 0).toString(), shareip);
                    } catch(Exception e) {
                        return;
                    }
            }
        });
        
        returnback.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                sp.setVisible(true);
                sp1.setVisible(true);
                chatitem.setVisible(true);
                sendbutton.setVisible(true);
                sharedir.setVisible(true);
                shareview.setVisible(true);
                sp2.setVisible(false);
                download.setVisible(false);
                returnback.setVisible(false);
                sharetable.removeAll();
                shareip = null;
            }
        });
    }
    
    public static void main(String[] s) {
        Miola miola = new Miola();
    }
}
