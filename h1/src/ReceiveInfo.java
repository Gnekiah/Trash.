

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveInfo {
	public ReceiveInfo() {
		try {
//			ConstantItems.RE_content.setText("Ming");
//			System.out.println(ConstantItems.SE_content.getText());
			ServerSocket serverSocket = new ServerSocket(8000);

			int sessionNo = 1;
			//new DataOutputStream(player1.getOutputStream()).writeInt(2);
			Socket player1 = serverSocket.accept();
			String ip= player1.getInetAddress().getHostAddress();
//			int id=0;
//			
//			if(ip.equals("127.0.0.1")){
//				id=3;
//			}else {
//				id=2;
//			}
			HandleASession toPlay1=new HandleASession(player1);
			Thread thread1=new Thread(toPlay1);
			thread1.start();

			Socket player2 = serverSocket.accept();
//			ip= player2.getInetAddress().getHostAddress();
//			if(ip.equals("127.0.0.1")){
//				id=3;
//			}else {
//				id=2;
//			}
			//new DataOutputStream(player2.getOutputStream()).writeInt(3);
			HandleASession toPlay2=new HandleASession(player2);
			Thread thread2=new Thread(toPlay2);
			thread2.start();
			System.out.println("１连接客户端成功");
			

		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}

class HandleASession implements Runnable {
	private Socket player;
	//private int ID;

	private DataInputStream fromPlayer;
	private DataOutputStream toPlayer;

	private boolean continueToPlay = true;

	/** Construct a thread */
	public HandleASession(Socket play) {
		System.out.println(ConstantItems.SE_content.getText());
		this.player = play;
//		this.ID=id;
	}

	public void run() {
		while(true){
		try {
			//System.out.println("Ming");
			fromPlayer = new DataInputStream(player.getInputStream());
			toPlayer = new DataOutputStream(player.getOutputStream());
			
			//toPlayer.writeInt(1);
			String str=fromPlayer.readUTF();
			System.out.println(str);
			String from=ConstantItems.RE_from.getText();
			//if(ID==3&&!from.equals("H3")){
				//ConstantItems.RE_from.setText("H3");
			//}else if(!from.equals("H2")){
			//	ConstantItems.RE_from.setText("H2");
			//}
			ConstantItems.RE_from.setText(str.substring(0, 2));
			ConstantItems.RE_destination.setText(str.substring(2, 4));
			ConstantItems.RE_content.setText(str.substring(4));

		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	}
}
