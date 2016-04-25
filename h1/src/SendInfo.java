

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class SendInfo {
	public SendInfo() {
		HandleInfo hd=new HandleInfo(0);
		hd.connectToServer();
	}
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		
//	}
	
}

class HandleInfo implements Runnable {
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	private String host = "localhost";
	private String Message;
	private int ID;
	public HandleInfo(int id) {
		this.ID=id;
	}
	public void connectToServer() {
		try {
			Socket socket;
			//连接H2
			socket = new Socket("192.168.137.125", 9000);
			//"172.24.10.226""192.168.253.15"
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
			System.out.println("链接成功");
		} catch (Exception ex) {
			System.err.println(ex);
		}
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while(true){
		try {
			//System.out.println("MIngS");
			//int id = fromServer.readInt();
			String str1=ConstantItems.SE_destination.getText();
			String str=ConstantItems.SE_content.getText();
			System.out.println(str+"             1");
			if(str!=null&&(str1.equals("H2")||str1.equals("H3"))){
				if(!str.equals(Message)){
				    System.out.println("H1"+str1+str);
					toServer.writeUTF("H1"+str1+str);
					Message=str;
					Thread.currentThread().sleep(10000);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}

}