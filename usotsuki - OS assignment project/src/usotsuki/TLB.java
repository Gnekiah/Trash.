package usotsuki;

import datastruct.Mapping;


/**
 * TLB模块，采用类似计数的机制，用flag保存，flag的值表示程序运行后TLB访问的次数
 * 对于FIFO，flag表示add的次数，在add时改变flags对应位的状态
 * 对于LRU，flag表示访问次数， 在get和add的时候改变flags对应位的状态
 * 根据以上两点，可以将FIFO和LRU的置换算法统一起来，即用同一个置换步骤实现两种置换算法
 * @author Ekira
 *
 */
public class TLB {
	private int flag = 0;			// 用于表示时间的增长
	private int[] flags = null; 	// 用于标记fifo或lru的置换位
	private Mapping[] tlb = null;	// tlb表
	private int tlbsize = 0;		// tlb大小
	private int tlblen = 0;			// tlb使用长度
	private int algo = 0; 			// 0:FIFO,	1:LRU
	
	/**
	 * 初始化TLB时，需要传入TLB的大小以及采用的置换算法
	 * @param tlbsize - tlb大小
	 * @param algo - 算法，0:FIFO,	1:LRU
	 */
	public TLB(int tlbsize, int algo) {
		this.tlbsize = tlbsize;
		tlb = new Mapping[tlbsize];
		flags = new int[tlbsize];
		this.algo = algo;
	}
	
	/**
	 * 从TLB中查找需要的mapping关系
	 * @param value - 根据value查找key
	 * @return -1： 查找失败，没有该元素  ；否则返回key值
	 */
	public int get(int value) {
		if (tlblen == 0) return -1;
		for (int i = 0; i < tlblen; i++) {
			if (tlb[i].getValue() == value) {
				if (algo == 1) {
					flag += 1;
					flags[i] = flag;
				}
				return tlb[i].getKey();
			}
		}
		return -1;
	}
	
	public void add(Mapping map) {
		add(map.getKey(), map.getValue());
	}
	
	/**
	 * 向TLB中添加mapping，若TLB为满，则根据算法决定相应的置换
	 * @param key - 键
	 * @param value - 值
	 */
	public void add(int key, int value) {
		flag += 1;
		if (tlblen < tlbsize) {
			tlb[tlblen] = new Mapping();
			tlb[tlblen].setMapping(key, value);
			flags[tlblen] = flag;
			tlblen += 1;
			return;
		}
		int pos = 0;
		int min = flags[0];
		for (int i = 0; i < tlbsize; i++) {
			if (flags[i] < min) {
				min = flags[i];
				pos = i;
			}
		}
		tlb[pos].setMapping(key, value);
		flags[pos] = flag;
	}
	
	/**
	 * 测试代码
	 * 
	public void print() {
		for (int i = 0; i < tlblen; i++) {
			System.out.println(flags[i] + "--" + tlb[i].getValue());
		}
	}
	
	public static void main(String[] s) {
		TLB tlb = new TLB(10, 0);
		tlb.add(1, 11);
		tlb.add(2, 22);
		tlb.add(3,33);
		tlb.add(4, 44);
		tlb.add(5, 55);
		tlb.add(6, 66);
		tlb.get(44);
		tlb.get(44);
		tlb.get(11);
		tlb.add(7, 77);
		tlb.add(8, 88);
		tlb.add(9, 99);
		tlb.add(110, 1110);
		tlb.get(1110);
		tlb.add(111, 1111);
		tlb.add(112, 1112);
		tlb.get(22);
		tlb.add(99, 999);
		tlb.add(888, 8888);
		tlb.print();
	}
	*/
}
