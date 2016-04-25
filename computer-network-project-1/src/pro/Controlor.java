package pro;

import java.awt.FlowLayout;

import javax.swing.JFrame;

public class Controlor extends JFrame {
    private static final long serialVersionUID = 1L;
    SenderPanel senderPanel = null;
    ReceiverPanel receiverPanel = null;
    int serverPort = 10086;
    Controlor() {
        this.setSize(1200, 700);
        this.setLayout(new FlowLayout());
        
        senderPanel = new SenderPanel();
        receiverPanel = new ReceiverPanel();
        this.add(senderPanel);
        this.add(receiverPanel);
    }

    public void starting() {
        receiverPanel.starting(serverPort);
    }
    
    public static void main(String[] a) {
        Controlor c = new Controlor();
        c.setVisible(true);
        c.starting();
    }
}
