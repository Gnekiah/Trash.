package usotsuki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import datastruct.PCBRequestItem;

public class PCB {
	// ����ID
	private int PID = 0;
	// �������� 0 - root, 1-999 - system, 1000+ - user
	private int feature = 0;
	// ����״̬ 0 - running, -1 - stopped, >1 - waiting 
	private int status = 0;
	// ָ�����ҳ��ָ��
	private int pPage = 0;
	// ���̷������󵽴�ʱ���Լ��������
	private LinkedList<PCBRequestItem> requestTable = null;
	
	
	public PCB(int PID, int feature, int status, int pPage) {
		this.PID = PID;
		this.feature = feature;
		this.status = status;
		this.pPage = pPage;
		requestTable = new LinkedList<PCBRequestItem>();
	}
	
	public PCB(int PID, int feature, int status, int pPage, String filename) {
		this.PID = PID;
		this.feature = feature;
		this.status = status;
		this.pPage = pPage;
		requestTable = new LinkedList<PCBRequestItem>();
		loadTrace(filename);
	}
	
	/**
	 * ����trace�ļ�
	 * @param filename - trace�ļ�·��
	 * @return true - ���سɹ�
	 */
	public boolean loadTrace(String filename) {
		File file = new File(filename);
		// ���ڱ�Ƕ�ȡ��trace�ļ������Ƿ����쳣����ֵ��ʾ�쳣���ݵ���Ŀ��
		int flag = 0;
		if (!file.exists()) return false;
		BufferedReader br = null;
		try {
			String tmpLine = null;
			br = new BufferedReader(new FileReader(file));
			while ((tmpLine = br.readLine()) != null) {
				String[] tmpItem = tmpLine.split(" ");
				if (tmpItem.length != 2) {
					flag += 1;
					continue;
				}
				requestTable.add(new PCBRequestItem(Double.valueOf(tmpItem[0]), Integer.valueOf(tmpItem[1])));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (flag != 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * ��ȡ��һ�����������
	 * @return
	 */
	public PCBRequestItem getNext() { return requestTable.poll(); }
	
	public void setPID(int PID) { this.PID = PID; }
	public void setFeature(int feature) { this.feature = feature; }
	public void setStatus(int status) { this.status = status; }
	public void setPage(int pPage) { this.pPage = pPage; }
	
	public int getPID() { return PID; }
	public int getFeature() { return feature; }
	public int getStatus() { return status; }
	public int getPage() { return pPage; }
	public LinkedList<PCBRequestItem> getRequestTable() { return requestTable; }
	
	/*
	public static void main(String[] s) {
		PCB rf = new PCB(1,1,1,1);
		if (rf.loadTrace("C:/Users/Ekira/Desktop/����ϵͳ�γ����/trace1.txt") 
		 && rf.loadTrace("C:/Users/Ekira/Desktop/����ϵͳ�γ����/trace2.txt")) {
			PCBRequestItem ri;
			while((ri=rf.getNext()) != null) {
				System.out.println(ri.getRequestTime() + "   "+ ri.getLogicAddr());
			}
		}
	}
	*/

}
