package usotsuki;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class MainUI extends JFrame{

	private static final long serialVersionUID = 6937164097231259797L;
	public final static String APPNAME = "USOTSUKI";
	private final int APPSIZE_WIDTH = 800;
	private final int APPSIZE_HEIGHT = 510;
	
	private final int TEXT_STATUS_WIDTH = 600;
	private final int TEXT_STATUS_HEIGHT = 300;
	private final int TEXT_ANALYSIS_WIDTH = 600;
	private final int TEXT_ANALYSIS_HEIGHT = 160;
	private final int BUTTON_WIDTH = 105;
	private final int BUTTON_HEIGHT = 35;
	private final int RADIOBUTTON_WIDTH = 100;
	private final int RADIOBUTTON_HEIGHT = 30;

	private JButton runOne = null;
	private JButton runTen = null;
	private JButton runAll = null;
	private JButton re0 = null;
	
	private JTextArea showStatus = null;
	private JTextArea showAnalysis = null;
	
	private ButtonGroup g_algo = null;
	private ButtonGroup g_stra = null;;
	
	private JRadioButton algo_fifo = null;
	private JRadioButton algo_lru = null;
	private JRadioButton stra_whole = null;
	private JRadioButton stra_local = null;

	private Executor exec = null;
	
	public MainUI() {
		super(APPNAME);
		this.setLayout(null);
		this.setSize(APPSIZE_WIDTH, APPSIZE_HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);

		runOne = new JButton("Run one");
		runTen = new JButton("Run ten");
		runAll = new JButton("Run all");
		re0 = new JButton("RE:0");
		showStatus = new JTextArea();
		showAnalysis = new JTextArea();
		g_algo = new ButtonGroup();
		g_stra = new ButtonGroup();
		algo_fifo = new JRadioButton("FIFO");
		algo_lru = new JRadioButton("LRU");
		stra_whole = new JRadioButton("whole");
		stra_local = new JRadioButton("local");
		
		showStatus.setEditable(false);
		showAnalysis.setEditable(false);
		
		g_algo.add(algo_fifo);
		g_algo.add(algo_lru);
		g_stra.add(stra_whole);
		g_stra.add(stra_local);
		
		algo_fifo.setSelected(true);
		algo_lru.setSelected(false);
		stra_whole.setSelected(true);
		stra_local.setSelected(false);
		
		runOne.setBounds(630, 280, BUTTON_WIDTH, BUTTON_HEIGHT);
		runTen.setBounds(630, 330, BUTTON_WIDTH, BUTTON_HEIGHT);
		runAll.setBounds(630, 380, BUTTON_WIDTH, BUTTON_HEIGHT);
		re0.setBounds(630, 430, BUTTON_WIDTH, BUTTON_HEIGHT);
		// showStatus.setBounds(5, 5, TEXT_STATUS_WIDTH, TEXT_STATUS_HEIGHT);
		// showAnalysis.setBounds(5, 315, TEXT_ANALYSIS_WIDTH, TEXT_ANALYSIS_HEIGHT);
		algo_fifo.setBounds(630, 40, RADIOBUTTON_WIDTH, RADIOBUTTON_HEIGHT);
		algo_lru.setBounds(630, 75, RADIOBUTTON_WIDTH, RADIOBUTTON_HEIGHT);
		stra_whole.setBounds(630, 150, RADIOBUTTON_WIDTH, RADIOBUTTON_HEIGHT);
		stra_local.setBounds(630, 180, RADIOBUTTON_WIDTH, RADIOBUTTON_HEIGHT);
		
		JScrollPane jsp1 = new JScrollPane(showStatus);
		JScrollPane jsp2 = new JScrollPane(showAnalysis);
		jsp1.setBounds(5, 5, TEXT_STATUS_WIDTH, TEXT_STATUS_HEIGHT);
		jsp2.setBounds(5, 315, TEXT_ANALYSIS_WIDTH, TEXT_ANALYSIS_HEIGHT);
		
		this.add(runOne);
		this.add(runTen);
		this.add(runAll);
		this.add(re0);
		this.add(jsp1);
		this.add(jsp2);
		this.add(algo_fifo);
		this.add(algo_lru);
		this.add(stra_whole);
		this.add(stra_local);
		this.setVisible(true);
	}
	
	public void start() {
		int algo, stra;
		if (algo_fifo.isSelected())	algo = 0;
		else algo = 1;
		if (stra_whole.isSelected()) stra = 0;
		else stra = 1;	
		exec = new Executor(algo, stra);
		
		runOne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String result = exec.runNext();
				if (result == null) {
					result = "run over.\n";
					showAnalysis.append(exec.getAnalysis());
				}
				showStatus.append(result);
			}
		});
		
		runTen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i =0; i < 10; i++) {
					String result = exec.runNext();
					if (result == null) {
						showStatus.append("run over.\n");
						showAnalysis.append(exec.getAnalysis());
						return;
					}
					showStatus.append(result);
				}
			}
		});
		
		runAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				while(true) {
					String result = exec.runNext();
					if (result == null) {
						showStatus.append("run over.\n");
						showAnalysis.append(exec.getAnalysis());
						return;
					}
					showStatus.append(result);
				}
			}
		});
		
		re0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int algo, stra;
				if (algo_fifo.isSelected())	algo = 0;
				else algo = 1;
				if (stra_whole.isSelected()) stra = 0;
				else stra = 1;	
				exec.init(algo, stra);
				showStatus.setText("");
			}
		});
	}
	
	
	private static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) { UIManager.put(key, fontRes); }
        }
    }
	
	public static void main(String[] s) {
		InitGlobalFont(new Font("Consolas", Font.PLAIN, 18));
		MainUI mainui = new MainUI();
		mainui.start();
	}
}
