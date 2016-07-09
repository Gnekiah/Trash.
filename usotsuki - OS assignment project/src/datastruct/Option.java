package datastruct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class Option {
	private int frame_number = 20;
	private int tlb_entry = 10;
	private int tlb_time = 5;				// 单位，纳秒
	private int memory_time = 100;
	private int seek_time = 100;
	private int rotational_time = 50;
	private int transfer_time = 30000;
	private int replacement_algorithm = 0;	// 0:FIFO,	1:LRU
	private int replacement_strategy = 0;	// 0:global,1:local
	
	/**
	 * 初始化时传入设置文件的路径
	 * @param filename - parameter.txt
	 */
	public Option(String filename) {
		File file = new File(filename);
		if (!file.exists()) return;
		BufferedReader br = null;
		try {
			String tmpLine = null;
			br = new BufferedReader(new FileReader(file));
			while ((tmpLine = br.readLine()) != null) {
				try {
					setOption(tmpLine); // 存在由于类型错误导致的异常
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置条目
	 * @param item - 从设置文件中读取的一行
	 * @return - true- 设置成功
	 * @throws Exception - 抛出类型转换异常
	 */
	private boolean setOption(String item) throws Exception {
		String items[] = item.split("#");
		if (items.length == 0 || items[0] == null) return false;
		String params[] = items[0].replaceAll(" ", "").replaceAll(";", "").split("=");
		if (params.length != 2) return false;
		Opt op = Opt.valueOf(params[0]);
		switch (op) {
		case frame_number:
			setFrameNumber(Integer.valueOf(params[1])); break;
		case TLB_entry:
			setTLBEntry(Integer.valueOf(params[1])); break;
		case TLB_time:
			setTLBTime(Integer.valueOf(params[1])); break;
		case memory_time:
			setMemoryTime(Integer.valueOf(params[1])); break;
		case seek_time:
			setSeekTime(Integer.valueOf(params[1])); break;
		case rotational_time:
			setRotationalTime(Integer.valueOf(params[1])); break;
		case transfer_time:
			setTransferTime(Integer.valueOf(params[1])); break;
		case replacement_algorithm:
			setReplaceAlgorithm(Integer.valueOf(params[1])); break;
		case replacement_strategy:
			setReplaceStrategy(Integer.valueOf(params[1])); break;
		default:
			return false;
		}
		return true;
	}
	
	
	public enum Opt {
		frame_number,
		TLB_entry,
		TLB_time,
		memory_time,
		seek_time,
		rotational_time,
		transfer_time,
		replacement_algorithm,
		replacement_strategy, 
	  }
	
	public void setFrameNumber(int fm) { this.frame_number = fm; }
	public void setTLBEntry(int tlbe) { this.tlb_entry = tlbe; }
	public void setTLBTime(int tlbt) { this.tlb_time = tlbt; }
	public void setMemoryTime(int memt) { this.memory_time = memt; }
	public void setSeekTime(int seekt) { this.seek_time = seekt; }
	public void setRotationalTime(int rt) { this.rotational_time = rt; }
	public void setTransferTime(int tft) { this.transfer_time = tft; }
	public void setReplaceAlgorithm(int algo) { this.replacement_algorithm = algo; }
	public void setReplaceStrategy(int stra) { this.replacement_strategy = stra; }
	
	public int getFrameNumber() { return this.frame_number; }
	public int getTLBEntry() { return this.tlb_entry; }
	public int getTLBTime() { return this.tlb_time; }
	public int getMemoryTime() { return this.memory_time; }
	public int getSeekTime() { return this.seek_time; }
	public int getRotationalTime() { return this.rotational_time; }
	public int getTransferTime() { return this.transfer_time; }
	public int getReplaceAlgorithm() { return this.replacement_algorithm; }
	public int getReplaceStrategy() { return this.replacement_strategy; }
	
	
	/*
	public static void main(String[] s) {
		Option op = new Option("C:/Users/Ekira/Desktop/操作系统课程设计/parameter.txt");
	}
	*/
}
