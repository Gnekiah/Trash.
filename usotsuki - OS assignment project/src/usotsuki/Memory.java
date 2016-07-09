package usotsuki;

import java.util.LinkedList;
import java.util.ListIterator;

import datastruct.Mapping;

/**
 * 针对全局置换策略
 * 内存模块采用类似计数的机制，用flag保存，flag的值表示程序运行后memory访问的次数
 * 对于FIFO，flag表示add的次数，在add时改变flags对应位的状态
 * 对于LRU，flag表示访问次数， 在get和add的时候改变flags对应位的状态
 * 根据以上两点，可以将FIFO和LRU的置换算法统一起来，即用同一个置换步骤实现两种置换算法
 * 
 * 针对局部置换策略
 * 沿用全局策略下的计数机制，在此基础上，增加了一个二维矩阵，用于记录每个进程拥有的固定内存区域
 * 二维矩阵的元素值对应到内存的页号，同时也对应到相应页号的flags，可以很方便的获取进程持有的
 * 页的flags
 * 
 * 采用多态函数的方式针对不同置换策略分别实现了统一的接口。注意区分使用
 * @author Ekira
 *
 */
public class Memory {
	private int flag = 0;						// 用于表示时间的增长
	private int[] flags = null; 				// 用于标记fifo或lru的置换位
	private int proclen = 0;					// 进程分配的固定内存大小
	private int[][] procs = null;				// 保存每个进程的页框ID
	private LinkedList<Mapping> memory = null;	// memory页表
	private int memorysize = 0;					// memory页框大小
	private int memorylen = 0;					// memory使用长度
	private int algo = 0; 						// 0:FIFO,	1:LRU

	/**
	 * 当选局部置换策略时，必须使用这种初始化方式
	 * @param memorysize - 内存页框数
	 * @param algo - 置换算法
	 * @param proc_amount - 进程数量
	 */
	public Memory(int memorysize, int algo, int proc_amount) {
		this.memorysize = memorysize;
		this.algo = algo;
		memory = new LinkedList<Mapping>();
		flags = new int[memorysize];
		proclen = memorysize/proc_amount;
		procs = new int[proc_amount][proclen + 1];
		for (int i =0; i < proc_amount; i++) 
			for (int j =0; j < proclen+1; j++) 
				procs[i][j] = 0;
	}
	
	/**
	 * 当采用全局策略时，用这种方式初始化
	 * @param memorysize - 内存页框数
	 * @param algo - 置换算法
	 */
	public Memory(int memorysize, int algo) {
		this.memorysize = memorysize;
		this.algo = algo;
		memory = new LinkedList<Mapping>();
		flags = new int[memorysize];
	}

	/**
	 * 获取key，不需要传入进程id，适用于全局策略
	 * @param value - 值
	 * @return - 键 / -1表示未找到该元素
	 */
	public int get(int value) {
		ListIterator<Mapping> it = memory.listIterator();
		int pos = 0;
		while(it.hasNext()) {
			Mapping tmpmap = it.next();
			if (tmpmap.getValue() == value) {
				if (algo == 1) {
					flag += 1;
					flags[pos] = flag;
				}
				return tmpmap.getKey();
			}
			pos += 1;
		}
		return -1;
	}
	
	/**
	 * 获取key，传入id为进程id，适用于局部策略
	 * @param value - 值
	 * @param id - 进程id
	 * @return - 键 / -1表示未找到该元素
	 */
	public int get(int value, int id) {
		for (int i=1; i <= procs[id][0]; i++) {
			if (memory.get(procs[id][i]).getValue() == value) {
				if (algo == 1) {
					flag += 1;
					flags[procs[id][i]] = flag;
				}
				return memory.get(procs[id][i]).getKey();
			}			
		}
		return -1;
	}

	/**
	 * 添加映射，需要传入id，适用于局部策略
	 * @param value - 值
	 * @param id - 进程id
	 * @return pos
	 */
	public int add(int value, int id) {
		flag += 1;
		int key = proclen;
		if (procs[id][0] < proclen) {
			memory.add(new Mapping(key, value));
			flags[memorylen] = flag;
			procs[id][0] += 1;
			procs[id][procs[id][0]] = memorylen;
			memorylen += 1;
			return key;
		}
		int pos = 0;
		int min = flags[procs[id][1]];
		for (int i =1; i < proclen+1; i++) {
			if (flags[procs[id][i]] < min) {
				min = flags[procs[id][i]];
				pos = procs[id][i];
			}
		}
		memory.set(pos, new Mapping(pos, value));
		flags[pos] = flag;
		return pos;
	}
	
	/**
	 * 添加映射关系，适用于全局策略
	 * @param value - 值
	 * @return pos
	 */
	public int add(int value) {
		flag += 1;
		int key = proclen;
		if (memorylen < memorysize) {
			memory.add(new Mapping(key, value));
			flags[memorylen] = flag;
			memorylen += 1;
			return key;
		}
		int pos = 0;
		int min = flags[0];
		for (int i = 0; i < memorysize; i++) {
			if (flags[i] < min) {
				min = flags[i];
				pos = i;
			}
		}
		memory.set(pos, new Mapping(pos, value));
		flags[pos] = flag;
		return pos;
	}
	
	/**
	 * 测试代码
	public void show() {
		ListIterator<Mapping> it = memory.listIterator();
		while(it.hasNext()) {
			Mapping map = it.next();
			System.out.println(map.getKey() + "--" + map.getValue());
		}
		for (int i = 0; i < memorysize; i++) {
			System.out.println(flags[i]);
		}
		for (int i = 0; i< 3; i++) {
			for (int j =0; j < proclen+1; j++)
				System.out.print(procs[i][j]);
			System.out.println();
		}
		
	}
	
	
	public static void main(String[] s) {
		Memory memory = new Memory(10, 1, 3);
		memory.add(10, 10, 0);
		memory.add(41, 41, 1);
		memory.add(52, 52, 2);
		memory.add(90, 90, 0);
		memory.add(100,100,0);
		memory.add(12,12,2);
		memory.add(11,11,1);
		memory.add(32,32,2);
		memory.get(10, 0);
		memory.get(90, 0);
		memory.add(40,40,0);
		memory.add(30,30,0);
		memory.add(21,21,1);
		System.out.println(memory.get(21, 1));
		memory.show();
	}
	
	
	public static void main(String[] s) {
		LinkedList<String> l = new LinkedList<String>();
		l.add("11");
		// l.add("22");
		System.out.println(l.poll());
		System.out.println(l.poll());
	}
	*/
}
