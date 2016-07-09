package usotsuki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

import datastruct.PCBRequestItem;
import datastruct.RequestItem;

/**
 * ���ڴ�����ĵ���ʱ�䣬��֯��һ������
 * ���������ݺ�����Ӧ�����򣬼�����sortTable
 * @author Ekira
 *
 */
public class RequestForm {
	/**
	 * �������󵽴�ʱ�䡢����Ľ���ID������
	 */
	private LinkedList<RequestItem> requestTable = null;
	
	public RequestForm() {
		requestTable = new LinkedList<RequestItem>();
	}
	
	public RequestForm(String filename, int proc) {
		requestTable = new LinkedList<RequestItem>();
		loadTrace(filename, proc);
	}
	
	public RequestForm(LinkedList<PCBRequestItem> PCBTable, int PID) {
		requestTable = new LinkedList<RequestItem>();
		loadTrace(PCBTable, PID);
	}
	
	/**
	 * ����trace�ļ�
	 * @param filename �ļ���
	 * @param proc ����ID
	 * @return true - �ɹ�
	 */
	public boolean loadTrace(String filename, int proc) {
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
				requestTable.add(new RequestItem(proc, Double.valueOf(tmpItem[0])));
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
	 * ����pcb�ṹ�б������Ϣ��ȡ����ʱ��
	 * @param PCBTable pcb�б�������ʱ���Ŀ�������
	 * @param PID ����ID
	 */
	public void loadTrace(LinkedList<PCBRequestItem> PCBTable, int PID) {
		ListIterator<PCBRequestItem> it = PCBTable.listIterator();
		while(it.hasNext()) {
			requestTable.add(new RequestItem(PID, Double.valueOf(it.next().getRequestTime())));
		}
	}
	
	/**
	 * ��ȡ��һ�����������
	 * @return
	 */
	public RequestItem getNext() { return requestTable.poll(); }
	
	/**
	 * �����Զ��������������
	 * �����ǰ������󵽴�ʱ����Ⱥ�˳������
	 */
	@SuppressWarnings("unchecked")
	public void sortTable() {
		Collections.sort(requestTable, new RequestComparator());
	}
	
	/**
	 * �Զ���Ƚ���������ʱ��Ĵ�С��������
	 * @author Ekira
	 *
	 */
	@SuppressWarnings("rawtypes")
	static class RequestComparator implements Comparator { 
		public int compare(Object obj1, Object obj2) {
			RequestItem ri1 = (RequestItem)obj1;
			RequestItem ri2 = (RequestItem)obj2;
			return new Double(ri1.getReqst()).compareTo(ri2.getReqst());
		}
	}
	
	/*
	public static void main(String[] s) {
		RequestForm rf = new RequestForm();
		if (rf.loadTrace("C:/Users/Ekira/Desktop/����ϵͳ�γ����/trace1.txt", 1) 
		 && rf.loadTrace("C:/Users/Ekira/Desktop/����ϵͳ�γ����/trace2.txt", 2)) {
			RequestItem ri;
			rf.sortTable();
			while((ri=rf.getNext()) != null) {
				System.out.println(ri.getProc() + "   "+ ri.getReqst());
			}
		}
	}
	
	
	public static void main(String[] s) {
		PCB pcb = new PCB(1,1,1,1);
		RequestForm rf = new RequestForm();
		if (pcb.loadTrace("C:/Users/Ekira/Desktop/����ϵͳ�γ����/trace1.txt") 
		 &&pcb.loadTrace("C:/Users/Ekira/Desktop/����ϵͳ�γ����/trace2.txt")) {
			
			rf.loadTrace(pcb.getRequestTable(), pcb.getPID());
			
			RequestItem oi;
			while((oi=rf.getNext() )!= null) {
				System.out.println(oi.getProc() + "  "+oi.getReqst()+"  ");
			}
			
			PCBRequestItem ri;
			while((ri=pcb.getNext()) != null) {
				System.out.println(ri.getRequestTime() + "   "+ ri.getLogicAddr());
			}
		}
	}
	*/
}
