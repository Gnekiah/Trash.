package pro;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JPanel;

public class ShowProcess extends JPanel {

    private LinkedList<JPanel> panels = null;
    private int length = 0;
    private int quantity = 0;
    private int[] bus = null;
    
    ShowProcess(int length, int[] bus, int quantity) {
        this.bus = bus;
        this.length = length;
        this.quantity = quantity;
        this.setLayout(null);
        
        panels = new LinkedList<JPanel>();
        for (int i = 0; i < length; i++) {
            panels.add(new JPanel());
        }
        int i = 0;
        for (JPanel p: panels) {
            p.setBounds((1200/length)*i, 150, 1200/length-2, 1200/length-2);

            this.add(p);
            i++;
            
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        int i = 0;
        for (JPanel p: panels) {
            if (bus[i] == 0) {
                p.setBackground(new Color(240,240, 240));
            }
            else if (bus[i] == -1) {
                p.setBackground(new Color(0, 0, 0));
                
            }
            else {
                p.setBackground(new Color(0, 200*bus[i]%255, 100*bus[i]%255));
            }
            g.drawRect((1200/length)*i-1, 150-1, 1200/length-1, 1200/length-1);
            i++;
        }
        for (int j = 0; j < quantity; j++) {
            g.drawRect((1200/length)*(j+1)*10, 120, 1200/length-1, 1200/length-1);
        }
        
    }
    
    public void setBus(int[] bus) {
        this.bus = bus;
    }
    
}
