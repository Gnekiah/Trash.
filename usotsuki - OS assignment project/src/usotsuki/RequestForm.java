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
 * 对内存请求的到达时间，组织成一张链表
 * 加载完数据后，首先应该排序，即调用sortTable
 * @author Ekira
 *
 */
public class RequestForm {
	/**
	 * 保存请求到达时间、请求的进程ID的链表
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
	 * 加载trace文件
	 * @param filename 文件名
	 * @param proc 进程ID
	 * @return true - 成功
	 */
	public boolean loadTrace(String filename, int proc) {
		File file = new File(filename);
		// 用于标记读取的trace文件数据是否有异常，数值表示异常数据的条目数
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
	 * 根据pcb结构中保存的信息获取请求时间
	 * @param PCBTable pcb中保存请求时间和目标的链表
	 * @param PID 进程ID
	 */
	public void loadTrace(LinkedList<PCBRequestItem> PCBTable, int PID) {
		ListIterator<PCBRequestItem> it = PCBTable.listIterator();
		while(it.hasNext()) {
			requestTable.add(new RequestItem(PID, Double.valueOf(it.next().getRequestTime())));
		}
	}
	
	/**
	 * 获取下一个请求的内容
	 * @return
	 */
	public RequestItem getNext() { return requestTable.poll(); }
	
	/**
	 * 按照自定义规则整理链表
	 * 这里是按照请求到达时间的先后顺序排序
	 */
	@SuppressWarnings("unchecked")
	public void sortTable() {
		Collections.sort(requestTable, new RequestComparator());
	}
	
	/**
	 * 自定义比较器，按照时间的大小进行排序
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
		if (rf.loadTrace("C:/Users/Ekira/Desktop/操作系统课程设计/trace1.txt", 1) 
		 && rf.loadTrace("C:/Users/Ekira/Desktop/操作系统课程设计/trace2.txt", 2)) {
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
		if (pcb.loadTrace("C:/Users/Ekira/Desktop/操作系统课程设计/trace1.txt") 
		 &&pcb.loadTrace("C:/Users/Ekira/Desktop/操作系统课程设计/trace2.txt")) {
			
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
