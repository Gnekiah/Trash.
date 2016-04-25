package miolablessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

import javax.swing.JTextArea;

public class MessageListener extends Thread {
    
    private JTextArea chat = null;
    
    private ServerSocket listener = null;
    private Socket socket = null;
    
    public MessageListener(JTextArea chat) {
        this.chat = chat;
        try {
            listener = new ServerSocket(Miola.TCPPORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void run() {
        while(true) {
            try {
                socket = listener.accept();
                System.out.println("client tcp get message");
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
                String[] msg = br.readLine().split("\\|");
                if (msg.length != 3) {
                    continue;
                }
                chat.append(msg[1] + "\n" + msg[2] + "\n");
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
