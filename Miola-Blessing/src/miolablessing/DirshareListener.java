package miolablessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTable;
import javax.swing.JTextArea;

public class DirshareListener extends Thread {

private JTable sharetable = null;
    
    private ServerSocket listener = null;
    private Socket socket = null;
    
    public DirshareListener(JTable sharetable) {
        this.sharetable = sharetable;
        try {
            listener = new ServerSocket(Miola.sharePort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void run() {
        while(true) {
            try {
                socket = listener.accept();
                System.out.println("client sharedir get message");
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
                String xc = br.readLine();
                if (xc.equals(null)) {
                    return;
                }
                String[] msg = xc.split("\\|");

                if (msg[0].equals("request")) {
                    File f = new File(msg[1]);
                    if (f.isDirectory()) {
                        if (!Miola.shareip.equals(null)) {
                            SendMessage.dirshare(msg[1], Miola.shareip);
                        }
                    }
                    else if (f.isFile()) {
                        SendMessage.sendFile(msg[1], Miola.shareip);
                    }
                    else { }
                }
                else {
                    sharetable.removeAll();
                    int i = 0;
                    for (String s : msg) {
                        sharetable.setValueAt(s, i, 0);
                        i++;
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
