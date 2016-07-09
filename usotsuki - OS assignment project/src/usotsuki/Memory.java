package usotsuki;

import java.util.LinkedList;
import java.util.ListIterator;

import datastruct.Mapping;

/**
 * ���ȫ���û�����
 * �ڴ�ģ��������Ƽ����Ļ��ƣ���flag���棬flag��ֵ��ʾ�������к�memory���ʵĴ���
 * ����FIFO��flag��ʾadd�Ĵ�������addʱ�ı�flags��Ӧλ��״̬
 * ����LRU��flag��ʾ���ʴ����� ��get��add��ʱ��ı�flags��Ӧλ��״̬
 * �����������㣬���Խ�FIFO��LRU���û��㷨ͳһ����������ͬһ���û�����ʵ�������û��㷨
 * 
 * ��Ծֲ��û�����
 * ����ȫ�ֲ����µļ������ƣ��ڴ˻����ϣ�������һ����ά�������ڼ�¼ÿ������ӵ�еĹ̶��ڴ�����
 * ��ά�����Ԫ��ֵ��Ӧ���ڴ��ҳ�ţ�ͬʱҲ��Ӧ����Ӧҳ�ŵ�flags�����Ժܷ���Ļ�ȡ���̳��е�
 * ҳ��flags
 * 
 * ���ö�̬�����ķ�ʽ��Բ�ͬ�û����Էֱ�ʵ����ͳһ�Ľӿڡ�ע������ʹ��
 * @author Ekira
 *
 */
public class Memory {
	private int flag = 0;						// ���ڱ�ʾʱ�������
	private int[] flags = null; 				// ���ڱ��fifo��lru���û�λ
	private int proclen = 0;					// ���̷���Ĺ̶��ڴ��С
	private int[][] procs = null;				// ����ÿ�����̵�ҳ��ID
	private LinkedList<Mapping> memory = null;	// memoryҳ��
	private int memorysize = 0;					// memoryҳ���С
	private int memorylen = 0;					// memoryʹ�ó���
	private int algo = 0; 						// 0:FIFO,	1:LRU

	/**
	 * ��ѡ�ֲ��û�����ʱ������ʹ�����ֳ�ʼ����ʽ
	 * @param memorysize - �ڴ�ҳ����
	 * @param algo - �û��㷨
	 * @param proc_amount - ��������
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
	 * ������ȫ�ֲ���ʱ�������ַ�ʽ��ʼ��
	 * @param memorysize - �ڴ�ҳ����
	 * @param algo - �û��㷨
	 */
	public Memory(int memorysize, int algo) {
		this.memorysize = memorysize;
		this.algo = algo;
		memory = new LinkedList<Mapping>();
		flags = new int[memorysize];
	}

	/**
	 * ��ȡkey������Ҫ�������id��������ȫ�ֲ���
	 * @param value - ֵ
	 * @return - �� / -1��ʾδ�ҵ���Ԫ��
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
	 * ��ȡkey������idΪ����id�������ھֲ�����
	 * @param value - ֵ
	 * @param id - ����id
	 * @return - �� / -1��ʾδ�ҵ���Ԫ��
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
	 * ���ӳ�䣬��Ҫ����id�������ھֲ�����
	 * @param value - ֵ
	 * @param id - ����id
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
	 * ���ӳ���ϵ��������ȫ�ֲ���
	 * @param value - ֵ
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
	 * ���Դ���
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
