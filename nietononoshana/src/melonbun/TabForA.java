package melonbun;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TabForA extends JPanel {

    private boolean IS_PUBLIC_KEY_SENDED = false; 
    
    private final int BW = 122;
    private final int BH = 40;
    private final int TW = 500;
    private final int TH = 40;
    private final int PX = 10;
    private final int PY = 520;
    
    private JButton chooseButton = null;
    private JTextField showChoose = null;
    private JButton encrpytButton = null;
    private JTextField inputAesPasswd = null;
    private JButton getHashButton = null;
    private JTextField showHash = null;
    private JButton getSignButton = null;
    private JTextField inputRsaPasswd = null;
    private JTextField showSign = null;
    private JButton sendButton = null;
    
    private File sendName = null;
    private File tmpFile = null;
    private String hashCode = null;
    private String RSASign = null;
    
    public TabForA() {
        chooseButton = new JButton("选择...");
        showChoose = new JTextField("请输入文件路径");
        encrpytButton = new JButton("AES加密");
        inputAesPasswd = new JTextField("请输入AES算法密钥");
        getHashButton = new JButton("计算Hash");
        showHash = new JTextField("显示hash结果");
        showHash.setEditable(false);
        getSignButton = new JButton("RSA签名");
        inputRsaPasswd = new JTextField("显示RSA的密钥对路径");
        inputRsaPasswd.setEditable(false);
        showSign = new JTextField("显示RSA签名结果");
        showSign.setEditable(false);
        sendButton = new JButton("发送");

        showChoose.setBounds(PX, 10, TW, TH);
        chooseButton.setBounds(PY, 10, BW, BH);
        inputAesPasswd.setBounds(PX, 10+(BH+10), TW, TH);
        encrpytButton.setBounds(PY, 10+(BH+10), BW, BH);
        showHash.setBounds(PX, 20+(2*BH+10), TW, TH);
        getHashButton.setBounds(PY, 20+(2*BH+10), BW, BH);
        inputRsaPasswd.setBounds(PX, 30+(3*BH+10), TW, TH);
        getSignButton.setBounds(PY, 30+(3*BH+10), BW, BH);
        showSign.setBounds(PX, 40+(4*BH+10), TW, TH);
        sendButton.setBounds(PY, 40+(4*BH+10), BW, BH);
        
        this.setSize(BW+PY, 5*(BH+10));
        this.setLayout(null);
        this.add(showChoose);
        this.add(chooseButton);
        this.add(inputAesPasswd);
        this.add(encrpytButton);
        this.add(showHash);
        this.add(getHashButton);
        this.add(inputRsaPasswd);
        this.add(getSignButton);
        this.add(showSign);
        this.add(sendButton);
        this.setVisible(true);
        
        chooseButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                if(chooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    showChoose.setText(chooser.getSelectedFile().getAbsolutePath());
                }
                inputAesPasswd.setText("请输入AES算法密钥");
                showHash.setText("显示hash结果");
                inputRsaPasswd.setText("显示RSA的密钥对路径");
                showSign.setText("显示RSA签名结果");
            }});
        
        encrpytButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (inputAesPasswd.getText() == null || inputAesPasswd.getText() == "" || inputAesPasswd.getText().equals("请输入AES算法密钥")) {
                    inputAesPasswd.setText("请输入AES算法密钥");
                    return;
                }
                tmpFile = CipherDesu.encryptAESFile(new File(showChoose.getText()), ".aestmp", inputAesPasswd.getText());
                sendName = new File(showChoose.getText());
                inputAesPasswd.setText("加密完成");
            }});
        
        getHashButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) { 
                if (tmpFile == null) {
                    showHash.setText("请选择要操作的文件");
                    return;
                }
                hashCode = CipherDesu.SHA256Encrypt(sendName);
                showHash.setText(hashCode);
            }});
        
        getSignButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) { 
                if (hashCode == null) {
                    inputRsaPasswd.setText("请先计算hash值");
                    return;
                }
                RSASign = CipherDesu.RSAEncrypt(hashCode);
                inputRsaPasswd.setText("数字签名完成");
                showSign.setText(RSASign);
            }});
        
        sendButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) { 
                if (!IS_PUBLIC_KEY_SENDED) {
                    ClientSender.doSend(new File(CipherDesu.PUBLIC_KEY_FILE), true, "");
                }
                FileInfo info = ClientSender.doSend(tmpFile, false, sendName.getName());
                ClientSender.doSend(RSASign, sendName.getName());
                showSign.setText("发送成功，发送的文件名：" + info.getPath() + "  文件大小：" + info.getLength());
                sendName = null;
                tmpFile = null;
                hashCode = null;
                RSASign = null;
            }});
    }
}
