package melonbun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TabForB extends JPanel {

    private final int BW = 122;
    private final int BH = 40;
    private final int TW = 500;
    private final int TH = 40;
    private final int PX = 10;
    private final int PY = 520;
    private JTextField inputAesPasswd = null;
    private JButton passwdButton = null;
    private JTextField showSign = null;
    private JTextField showRsaPasswd = null;
    private JTextField showHash = null;
    private JTextField checkSign = null;
    
    private Queue<FileInfo> queue = null;
    private ServerListener server = null;
    
    private String RSASign = null; 
    private String RSA_KEY_PATH = ServerListener.PUBLIC_KEY_FILE;
    private String hashCode = null;
    private String receivedFile = null;
    
    public TabForB() {
        queue = new LinkedList<FileInfo>();
        inputAesPasswd = new JTextField();
        passwdButton = new JButton("CheckSign");
        showSign = new JTextField("显示RSA签名");
        showSign.setEditable(false);
        showRsaPasswd = new JTextField("显示RSA公钥");
        showRsaPasswd.setEditable(false);
        showHash = new JTextField("显示hash结果");
        showHash.setEditable(false);
        checkSign = new JTextField("数字签名校验");
        checkSign.setEditable(false);
        inputAesPasswd.setBounds(PX, 10, TW, TH);
        passwdButton.setBounds(PY, 10, BW, BH);
        showSign.setBounds(PX, 10+(BH+10), TW+BW+10, TH);
        showRsaPasswd.setBounds(PX, 20+(2*BH+10), TW+BW+10, TH);
        showHash.setBounds(PX, 30+(3*BH+10), TW+BW+10, TH);
        checkSign.setBounds(PX, 40+(4*BH+10), TW+BW+10, TH);
        
        this.setSize(BW+PY, 5*(BH+10));
        this.setLayout(null);
        this.add(inputAesPasswd);
        this.add(passwdButton);
        this.add(showSign);
        this.add(showRsaPasswd);
        this.add(showHash);
        this.add(checkSign);
        this.setVisible(true);
        
        passwdButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (receivedFile == null || !new File(receivedFile).exists()) {
                    inputAesPasswd.setText("未接收到远端传送的文件");
                    return;
                }
                File decrypt = CipherDesu.decryptAESFile(new File(receivedFile), ".unaestmp", inputAesPasswd.getText());
                decrypt.renameTo(new File(receivedFile + ".unaes"));
                if (!new File(RSA_KEY_PATH).exists()) {
                    inputAesPasswd.setText("未找到远端传送的公钥");
                    return;
                }
                hashCode = CipherDesu.RSADecrypt(RSASign, RSA_KEY_PATH);
                showHash.setText(hashCode);
                String plainHash = CipherDesu.SHA256Encrypt(new File(receivedFile + ".unaes"));
                if (plainHash.equals(hashCode)) {
                    checkSign.setText("Hash校验成功");
                }
                else {
                    showHash.setText("Hash校验失败");
                    checkSign.setText("解密后的文件Hash值为：" + plainHash);
                }
            }});
    }
    
    public void doStart() {
        server = new ServerListener(queue);
        server.start();
        while(true) {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            if (!queue.isEmpty()) {
                FileInfo f = queue.poll();
                FileInputStream is = null;
                try {
                    is = new FileInputStream(f.getSign());
                    byte[] b = new byte[1024 * 4];
                    int codeLength = is.read(b);
                    byte[] b1 = new byte[codeLength];
                    for (int i = 0; i < codeLength; i++) {
                        b1[i] = b[i];
                    }
                    RSASign = new String(b1);
                    is.close();
                } catch (IOException e) { }
                showSign.setText(RSASign);
                if (new File(RSA_KEY_PATH).exists()) {
                    showRsaPasswd.setText("RSA公钥保存路径为：" + RSA_KEY_PATH);
                }
                receivedFile = f.getPath();
                inputAesPasswd.setText("接收到文件，保存路径为：" + receivedFile + "  文件长度：" + f.getLength() + "请输入AES算法密钥进行验证");
            }
        }
    }
}
