package usotsuki;

import java.util.LinkedList;
import java.util.ListIterator;

import datastruct.Option;
import datastruct.PCBRequestItem;
import datastruct.RequestItem;

public class Executor {
	
	//private final String MAIN_DIR = "C:/Users/MyPC/Documents/Tencent Files/291255772/FileRecv/";
	private final String MAIN_DIR = "./src/";
	
	private Option option = null;			// 选项
	private RequestForm requestForm = null;	// 请求到达时间表
	private LinkedList<PCB> pcb = null;		// PCB链表模块
	private TLB tlb = null;					// TLB模块
	private Memory memory = null;			// 内存模块
	private Storage storage = null;			// 外存模块
	private int stra = 0;					// 策略
	private int algo = 0;
	private double currtime = 0;			// 当前时间点，单位：毫秒
	private double[] waittimearray = null;	// 进程请求等待总时间
	private double[] runtimearray = null;	// 进程请求运行总时间
	private int[] requestamount = null;		// 进程总请求数
	private int checkMemFault = 0;			// 检测内存命中, 0 - 未命中， 1 - 命中
	private int checkTLBFault = 0;			// 检测TLB命中
	private float[] faultTLBAmount = null;	// TLB缺页数量
	private float[] faultMEMAmount = null;	// 内存缺页数量
	
	public Executor(int algo, int stra) {
		init(algo, stra);
	}
	
	/**
	 * 初始化函数，用于在执行re:0操作后，重置所有模块
	 * @param algo - 算法
	 * @param stra - 策略
	 */
	public void init(int algo, int stra) {
		this.stra = stra;
		this.algo = algo;
		this.currtime = 0;
		option = new Option(MAIN_DIR + "parameter.txt");
		waittimearray = new double[5];
		runtimearray = new double[5];
		requestamount = new int[5];
		faultTLBAmount = new float[5];
		faultMEMAmount = new float[5];
		pcb = new LinkedList<PCB>();
		pcb.add(new PCB(0, 1001, 1, 0, MAIN_DIR + "trace1.txt"));
		pcb.add(new PCB(1, 1001, 1, 1, MAIN_DIR + "trace2.txt"));
		pcb.add(new PCB(2, 1001, 1, 2, MAIN_DIR + "trace3.txt"));
		pcb.add(new PCB(3, 1001, 1, 3, MAIN_DIR + "trace4.txt"));
		pcb.add(new PCB(4, 1001, 1, 4, MAIN_DIR + "trace5.txt"));
		requestForm = new RequestForm();
		ListIterator<PCB> it = pcb.listIterator();
		while(it.hasNext()) {
			PCB tmppcb = it.next();
			requestForm.loadTrace(tmppcb.getRequestTable(), tmppcb.getPID());
		}
		requestForm.sortTable();
		tlb = new TLB(option.getTLBEntry(), algo);
		if (stra == 0)
			memory = new Memory(option.getFrameNumber(), algo);
		else
			memory = new Memory(option.getFrameNumber(), algo, 5);
		storage = new Storage(option.getSeekTime(), option.getRotationalTime(), option.getTransferTime());
		storage.loadMapping(MAIN_DIR + "mapping.txt");
		
		for (int i =0; i < 5; i++) {
			waittimearray[i] = 0;
			runtimearray[i] = 0;
			requestamount[i] = pcb.get(i).getRequestTable().size();
			faultTLBAmount[i] = 0;
			faultMEMAmount[i] = 0;
		}
	}
	
	/**
	 * 执行下一条请求
	 * @return - 执行结果
	 */
	public String runNext() {
		int target = 0;
		double waitedtime = 0; 	// 等待时间,单位：毫秒
		double runnedtime = 0;	// 执行时间，单位：毫秒
		String result = null;
		RequestItem requestItem = requestForm.getNext();
		if (requestItem == null) return result;
		if (requestItem.getReqst() > currtime) currtime = requestItem.getReqst();
		else waitedtime = currtime - requestItem.getReqst();
		
		PCBRequestItem pcbItem = pcb.get(requestItem.getProc()).getNext();
		target = tlb.get(pcbItem.getLogicAddr());
		// TLB未命中
		if (target == -1) {
			runnedtime += (option.getTLBTime() / 1000000.0);
			faultTLBAmount[requestItem.getProc()] += 1;
			checkTLBFault = 0;
			// 全局策略
			if (stra == 0) {
				target = memory.get(pcbItem.getLogicAddr());
				runnedtime += (option.getMemoryTime() / 1000000.0);
				// 内存未命中
				if (target == -1) {
					faultMEMAmount[requestItem.getProc()] += 1;
					checkMemFault = 0;
					target = storage.get(pcbItem.getLogicAddr());
					tlb.add(memory.add(pcbItem.getLogicAddr()), pcbItem.getLogicAddr());
					runnedtime += storage.timeConsumed();
					runnedtime += (option.getMemoryTime() / 1000000.0);
					runnedtime += (option.getTLBTime() / 1000000.0);
				}
				else {
					checkMemFault = 1;
					tlb.add(target, pcbItem.getLogicAddr());
					runnedtime += (option.getMemoryTime() / 1000000.0);
					runnedtime += (option.getTLBTime() / 1000000.0);
				}
			}
			// 局部策略
			else {
				target = memory.get(pcbItem.getLogicAddr(), requestItem.getProc());
				runnedtime += (option.getMemoryTime() / 1000000.0);
				// 内存未命中
				if (target == -1) {
					faultMEMAmount[requestItem.getProc()] += 1;
					checkMemFault = 0;
					target = storage.get(pcbItem.getLogicAddr());
					tlb.add(memory.add(pcbItem.getLogicAddr(), requestItem.getProc()), pcbItem.getLogicAddr());
					runnedtime += storage.timeConsumed();
					runnedtime += (option.getMemoryTime() / 1000000.0);
					runnedtime += (option.getTLBTime() / 1000000.0);
				}
				else {
					checkMemFault = 1;
					tlb.add(target, pcbItem.getLogicAddr());
					runnedtime += (option.getMemoryTime() / 1000000.0);
					runnedtime += (option.getTLBTime() / 1000000.0);
				}
			}
		}
		else {
			checkTLBFault = 1;
			runnedtime += ((option.getTLBTime() + option.getMemoryTime()) / 1000000.0);
		}
		String fault = "";
		if (checkTLBFault == 0) {
			if (checkMemFault == 0) fault = "TLB:F  MEM:F  ";
			else fault = "TLB:F  MEM:T  ";
		}
		else {
			fault = "TLB:T  MEM:-  ";
			
		}
		currtime += runnedtime;
		waittimearray[requestItem.getProc()] += waitedtime;
		runtimearray[requestItem.getProc()] += runnedtime;
		result = "process:" + String.valueOf(requestItem.getProc()) + "\t" + fault + "request_arrive_time:" 
				+ String.valueOf(requestItem.getReqst()) + "\tlogic_addr:" + String.valueOf(pcbItem.getLogicAddr())
				+ "\t" + "wait_time:" + String.valueOf(waitedtime) + "\t" + "run_time:" 
				+ String.valueOf(runnedtime) + "\n";
		return result;
	}
	
	
	public String getAnalysis() {
		double tmpsum1=0, tmpsum2=0, tmpsum3=0;
		String result = "algo:" + String.valueOf(algo) + ", stra:" + String.valueOf(stra) + "\n";
		for (int i = 0; i < 5; i++) {
			tmpsum1 += waittimearray[i];
			tmpsum2 += runtimearray[i];
			tmpsum3 += requestamount[i];
			result += "process:" + String.valueOf(i) 
					+ "\ttlb page fault:" + String.valueOf(faultTLBAmount[i] / requestamount[i]) + "\t" 
					+ "memory page fault:" + String.valueOf(faultMEMAmount[i] / requestamount[i]) + "\t" 
					+  "\taverage waitting time:" + String.valueOf(waittimearray[i] / requestamount[i]) + "\t" 
					+ "average running time:" + String.valueOf(runtimearray[i] / requestamount[i]) + "\n";
		}
		
		
		result += "AVERAGE: WAITTING TIME:" + String.valueOf(tmpsum1 / tmpsum3) + "\n" 
				+ "AVERAGE: RUNNED TIME:" + String.valueOf(tmpsum2 / tmpsum3) + "\n";  
		return result;
	}
	
	/*
	public static void main(String[] s) {
		Executor exe = new Executor(0,0);
		String result = exe.runNext();
		System.out.println(result);
		for (int i = 0; i < 50; i++) {
			result = exe.runNext();
			System.out.println(result);
		}
	}
	*/

}
