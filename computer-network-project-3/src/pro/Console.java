package pro;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Console extends JFrame {

    private static final long serialVersionUID = 6881374763491695135L;

    private LinkedList<SimulatedHost> hostList = null;
    private BusHub hub = null;
    private JTextArea text = null;
    private JButton setStatus = null;
    private JButton faster = null;
    private JButton slower = null;
    private ShowProcess showProcess = null;
    private int length = 0;
    private int quantity = 0;
    
    Console(int length, int quantity) {
        this.length = length;
        this.quantity = quantity;
        this.setSize(1200, 700);
        this.setLayout(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        
        hub = new BusHub(length, quantity);
        hub.setGap(1000);
        hostList = new LinkedList<SimulatedHost>();
        for (int i = 0; i < quantity; i++) {
            hostList.add(new SimulatedHost(hub, i+1, (i+1)*10));
        }
        for (SimulatedHost host: hostList) {
            host.start();
        }
        
        text = new JTextArea(10, 100);
        text.setLineWrap(true);
        JScrollPane scrollText = new JScrollPane(text);
        scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        showProcess = new ShowProcess(length, hub.getBus(), quantity);
        setStatus = new JButton("开始");
        slower = new JButton("减速");
        faster = new JButton("加速");
        
        this.add(showProcess);
        this.add(scrollText);
        this.add(setStatus);
        this.add(faster);
        this.add(slower);
        
        showProcess.setBounds(0, 0, 1200, 300);
        scrollText.setBounds(0, 300, 1100, 375);
        setStatus.setBounds(1100, 300, 80, 40);
        faster.setBounds(1100, 350, 80, 40);
        slower.setBounds(1100, 400, 80, 40);
        
        setStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hub.getStatus()) {
                    hub.setStatus(false);
                    setStatus.setText("开始");
                }
                else {
                    hub.setStatus(true);
                    setStatus.setText("暂停");
                }
            }
        });
        
        faster.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hub.setGap(hub.getGap()/2);
            }
        });
        
        slower.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hub.setGap(hub.getGap()*2);
            }
        });
    }
    
    public void start() {
        while(true) {
            if (hub.getStatus()) {
                showMessage();
                showProcess.setBus(hub.getBus());
                showProcess.repaint();
                try { Thread.sleep(hub.getGap()); } 
                catch (InterruptedException e) { e.printStackTrace(); }
            }
            else {
                try { Thread.sleep(100); } 
                catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }
    
    private void showMessage() {
        String str = hub.pollMessage();
        if (str != null) {
            text.append(str);
            text.append("\n");
        }
    }
    
    public static void main(String[] a) { 
        Console console = new Console(50, 2);
        console.setVisible(true);
        console.start();
    }
    /*
    public static void main(String[] s) {
        BusHub hub = new BusHub(10);
        for (Integer b: hub.getBus()) {
            System.out.println(b);
        }
        int length = 5;
        LinkedList<SimulatedHost> list = new LinkedList<SimulatedHost>();
        for (int i = 0; i < length; i++) {
            list.add(new SimulatedHost(hub));
        }
        for (SimulatedHost host: list) {
            host.start();
        }
    }*/
}
