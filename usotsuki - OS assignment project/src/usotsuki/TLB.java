package usotsuki;

import datastruct.Mapping;


/**
 * TLBģ�飬�������Ƽ����Ļ��ƣ���flag���棬flag��ֵ��ʾ�������к�TLB���ʵĴ���
 * ����FIFO��flag��ʾadd�Ĵ�������addʱ�ı�flags��Ӧλ��״̬
 * ����LRU��flag��ʾ���ʴ����� ��get��add��ʱ��ı�flags��Ӧλ��״̬
 * �����������㣬���Խ�FIFO��LRU���û��㷨ͳһ����������ͬһ���û�����ʵ�������û��㷨
 * @author Ekira
 *
 */
public class TLB {
	private int flag = 0;			// ���ڱ�ʾʱ�������
	private int[] flags = null; 	// ���ڱ��fifo��lru���û�λ
	private Mapping[] tlb = null;	// tlb��
	private int tlbsize = 0;		// tlb��С
	private int tlblen = 0;			// tlbʹ�ó���
	private int algo = 0; 			// 0:FIFO,	1:LRU
	
	/**
	 * ��ʼ��TLBʱ����Ҫ����TLB�Ĵ�С�Լ����õ��û��㷨
	 * @param tlbsize - tlb��С
	 * @param algo - �㷨��0:FIFO,	1:LRU
	 */
	public TLB(int tlbsize, int algo) {
		this.tlbsize = tlbsize;
		tlb = new Mapping[tlbsize];
		flags = new int[tlbsize];
		this.algo = algo;
	}
	
	/**
	 * ��TLB�в�����Ҫ��mapping��ϵ
	 * @param value - ����value����key
	 * @return -1�� ����ʧ�ܣ�û�и�Ԫ��  �����򷵻�keyֵ
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
	 * ��TLB�����mapping����TLBΪ����������㷨������Ӧ���û�
	 * @param key - ��
	 * @param value - ֵ
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
	 * ���Դ���
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
