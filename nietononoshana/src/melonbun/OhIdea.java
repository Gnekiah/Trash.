package melonbun;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class OhIdea extends JFrame {
    private static final String tabaname = "this is student a";
    private static final String tabbname = "this is student b";
    private static final String appname = "Eeee... I don't know how to named the project...";
    private TabForA taba = new TabForA();
    private TabForB tabb = new TabForB();
    private final int BW = 122;
    private final int BH = 40;
    private final int PY = 520;
    private OhIdea() {
        super(appname);
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.add(tabaname, taba);
        tabs.add(tabbname, tabb);
        JPanel apptitle = new JPanel();
        apptitle.add(new JLabel(appname));
        apptitle.setBounds(10, 10, BW+PY, 50);
        tabs.setBounds(10, 60, BW+PY+20, 6*(BH+10));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setSize(BW+PY+50, 8*(BH+10));
        this.add(apptitle);
        this.add(tabs);
        this.setResizable(false);
        this.setVisible(true);
    }
    
    public void doStart() {
        tabb.doStart();
    }
    
    private static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) { UIManager.put(key, fontRes); }
        }
    }
    
    public static void main(String[] args) {
        InitGlobalFont(new Font("微软雅黑", Font.PLAIN, 20));
        OhIdea ohidea = new OhIdea();
        ohidea.doStart();
    }
}
