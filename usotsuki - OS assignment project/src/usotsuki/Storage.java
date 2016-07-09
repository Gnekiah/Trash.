package usotsuki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.ListIterator;

import datastruct.Mapping;

/**
 * 外存模块
 * @author Ekira
 *
 */
public class Storage {
	private LinkedList<Mapping> storage = null;	// 逻辑与物理的映射表
	private int currPos = 0;					// 磁头当前位置
	private int lastPos = 0;					// 磁头上一次位置
	private int stackConsumed = 0;				// 单位磁道寻址时间,单位：纳秒
	private int sectionConsumed = 0;			// 单位扇区寻址时间
	private int transferConsumed = 0;			// 传输一个页的时间
	
	/**
	 * 初始化时需要传入磁道与扇区的寻址时间
	 * @param stackComsumed - 磁道寻址时间
	 * @param sectionConsumed - 扇区寻址时间
	 */
	public Storage(int stackComsumed, int sectionConsumed, int transferConsumed) {
		storage = new LinkedList<Mapping>();
		this.stackConsumed = stackComsumed;
		this.sectionConsumed = sectionConsumed;
		this.transferConsumed = transferConsumed;
	}
	
	/**
	 * 初始化即将映射表读入
	 * @param filename - 映射表位置
	 * @param stackComsumed - 磁道寻址时间
	 * @param sectionConsumed - 磁头寻址时间
	 */
	public Storage(String filename, int stackComsumed, int sectionConsumed, int transferConsumed) {
		storage = new LinkedList<Mapping>();
		loadMapping(filename);
		this.stackConsumed = stackComsumed;
		this.sectionConsumed = sectionConsumed;
		this.transferConsumed = transferConsumed;
	}
	
	/**
	 * 加载mapping
	 * @param filename - mapping路径
	 * @return - true 加载成功
	 */
	public boolean loadMapping(String filename) {
		File file = new File(filename);
		// 用于标记读取的mapping文件数据是否有异常，数值表示异常数据的条目数
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
				storage.add(new Mapping(Integer.valueOf(tmpItem[0]), Integer.valueOf(tmpItem[1])));
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
	 * 根据逻辑地址获取物理地址
	 * @param key - 逻辑地址
	 * @return 物理地址  / -1表示没找到逻辑与物理地址的映射关系
	 */
	public int get(int key) {
		ListIterator<Mapping> it = storage.listIterator();
		while(it.hasNext()) {
			Mapping tmpmap = it.next();
			if (tmpmap.getKey() == key) {
				lastPos = currPos;
				currPos = tmpmap.getValue();
				return tmpmap.getValue();
			}
		}
		return -1;
	}
	
	/**
	 * 计算时间消耗, 单位：毫秒
	 * @return - 消耗的时间
	 */
	public double timeConsumed() {
		int stack = (((currPos & 0xff00) >>> 8) - ((lastPos & 0xff00) >>> 8));
		int section = ((currPos & 0x00ff) - (lastPos & 0x00ff));
		return (Math.abs(stack) * stackConsumed + Math.abs(section) * sectionConsumed + transferConsumed) / 1000000.0;
	}
	
	
	/**
	 * 测试代码
	public void show() {
		ListIterator<Mapping> it = storage.listIterator();
		while(it.hasNext()) {
			Mapping tmpmap = it.next();
			System.out.println(tmpmap.getKey() + "--" + tmpmap.getValue());
		}
	}
	
	
	public static void main(String[] s) {
		Storage storage = new Storage("C:/Users/Ekira/Desktop/操作系统课程设计/mapping.txt", 0.03, 0.05);
		System.out.println(storage.get(173));
		System.out.println(storage.timeConsumed());
		System.out.println(storage.get(173));
		System.out.println(storage.timeConsumed());
		System.out.println(storage.get(27179));
		System.out.println(storage.timeConsumed());
		storage.show();
	}
	*/
}
