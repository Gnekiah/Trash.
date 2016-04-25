



import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel nameL;
	
//	public static void main(String[] args) {
//		new MainFrame();
//	}
	public MainFrame() {
		// TODO Auto-generated constructor stub\]
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setTitle("Client");
		setVisible(true);
		
		
		nameL=new JLabel("计算机１");
		nameL.setBounds(0, 0, 60, 20);
		add(nameL);
		
		
		
		//路由表
		JPanel rJPanel=new JPanel();
		rJPanel.setBounds(40, 50, 300, 110);
		rJPanel.setBorder(BorderFactory.createTitledBorder("路由表"));		
		rJPanel.setLayout(null);
		add(rJPanel);
		
		JLabel label2=new JLabel("H1:");
		label2.setBounds(10, 20, 40, 20);
		rJPanel.add(label2);
		JTextField textField2=new JTextField();
		textField2.setText("---");
		textField2.setEditable(false);
		textField2.setBounds(65, 20, 150, 20);
		rJPanel.add(textField2);
		
		JLabel label3=new JLabel("H2:");
		label3.setBounds(10, 50,40, 20);
		rJPanel.add(label3);
		JTextField textField3=new JTextField();
		textField3.setText("R2");
		textField3.setBounds(65,50,150, 20);
		rJPanel.add(textField3);
		
		
		JLabel label8=new JLabel("H3:");
		label8.setBounds(10,80,40, 20);
		rJPanel.add(label8);
		JTextField textField8=new JTextField();
		textField8.setText("R2");
		textField8.setBounds(65,80,150, 20);
		rJPanel.add(textField8);
		
		
		//接收消息
		JPanel receivePanel=new JPanel();
		receivePanel.setBounds(40, 180,400, 110);
		receivePanel.setBorder(BorderFactory.createTitledBorder("接收消息"));		
		receivePanel.setLayout(null);
		add(receivePanel);
		
		JLabel label4=new JLabel("来源：");
		label4.setBounds(10, 20, 40, 20);
		receivePanel.add(label4);
		
		ConstantItems.RE_from.setBounds(65, 20, 150, 20);
		receivePanel.add(ConstantItems.RE_from);
		
		JLabel label5=new JLabel("目的地：");
		label5.setBounds(10, 50,60, 20);
		receivePanel.add(label5);
		
		ConstantItems.RE_destination.setBounds(65,50,150, 20);
		receivePanel.add(ConstantItems.RE_destination);
		
		
		JLabel label10=new JLabel("内容：");
		label10.setBounds(10, 80,40, 20);
		receivePanel.add(label10);
		
		ConstantItems.RE_content.setBounds(65,80,200, 20);
		receivePanel.add(ConstantItems.RE_content);
		
		
		
		//发送消息
		
		JPanel sendPanel=new JPanel();
		sendPanel.setBounds(40, 300,400, 130);
		sendPanel.setBorder(BorderFactory.createTitledBorder("发送消息"));		
		sendPanel.setLayout(null);
		add(sendPanel);
		
		JLabel label6=new JLabel("目的地：");
		label6.setBounds(10, 20, 60, 20);
		sendPanel.add(label6);
		ConstantItems.SE_destination.setBounds(65, 20, 150, 20);
		sendPanel.add(ConstantItems.SE_destination);
		
		JLabel label7=new JLabel("内容：");
		label7.setBounds(10, 50,60, 20);
		sendPanel.add(label7);
		ConstantItems.SE_content.setBounds(65,50,200, 20);
		//ConstantItems.SE_content.setText("Ming");
		sendPanel.add(ConstantItems.SE_content);
		
		JButton button=new JButton("发　送");
		button.setBounds(150, 95,90, 20);
		sendPanel.add(button);
		
	}
}
