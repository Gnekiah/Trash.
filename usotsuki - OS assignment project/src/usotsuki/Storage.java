package usotsuki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.ListIterator;

import datastruct.Mapping;

/**
 * ���ģ��
 * @author Ekira
 *
 */
public class Storage {
	private LinkedList<Mapping> storage = null;	// �߼��������ӳ���
	private int currPos = 0;					// ��ͷ��ǰλ��
	private int lastPos = 0;					// ��ͷ��һ��λ��
	private int stackConsumed = 0;				// ��λ�ŵ�Ѱַʱ��,��λ������
	private int sectionConsumed = 0;			// ��λ����Ѱַʱ��
	private int transferConsumed = 0;			// ����һ��ҳ��ʱ��
	
	/**
	 * ��ʼ��ʱ��Ҫ����ŵ���������Ѱַʱ��
	 * @param stackComsumed - �ŵ�Ѱַʱ��
	 * @param sectionConsumed - ����Ѱַʱ��
	 */
	public Storage(int stackComsumed, int sectionConsumed, int transferConsumed) {
		storage = new LinkedList<Mapping>();
		this.stackConsumed = stackComsumed;
		this.sectionConsumed = sectionConsumed;
		this.transferConsumed = transferConsumed;
	}
	
	/**
	 * ��ʼ������ӳ������
	 * @param filename - ӳ���λ��
	 * @param stackComsumed - �ŵ�Ѱַʱ��
	 * @param sectionConsumed - ��ͷѰַʱ��
	 */
	public Storage(String filename, int stackComsumed, int sectionConsumed, int transferConsumed) {
		storage = new LinkedList<Mapping>();
		loadMapping(filename);
		this.stackConsumed = stackComsumed;
		this.sectionConsumed = sectionConsumed;
		this.transferConsumed = transferConsumed;
	}
	
	/**
	 * ����mapping
	 * @param filename - mapping·��
	 * @return - true ���سɹ�
	 */
	public boolean loadMapping(String filename) {
		File file = new File(filename);
		// ���ڱ�Ƕ�ȡ��mapping�ļ������Ƿ����쳣����ֵ��ʾ�쳣���ݵ���Ŀ��
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
	 * �����߼���ַ��ȡ�����ַ
	 * @param key - �߼���ַ
	 * @return �����ַ  / -1��ʾû�ҵ��߼��������ַ��ӳ���ϵ
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
	 * ����ʱ������, ��λ������
	 * @return - ���ĵ�ʱ��
	 */
	public double timeConsumed() {
		int stack = (((currPos & 0xff00) >>> 8) - ((lastPos & 0xff00) >>> 8));
		int section = ((currPos & 0x00ff) - (lastPos & 0x00ff));
		return (Math.abs(stack) * stackConsumed + Math.abs(section) * sectionConsumed + transferConsumed) / 1000000.0;
	}
	
	
	/**
	 * ���Դ���
	public void show() {
		ListIterator<Mapping> it = storage.listIterator();
		while(it.hasNext()) {
			Mapping tmpmap = it.next();
			System.out.println(tmpmap.getKey() + "--" + tmpmap.getValue());
		}
	}
	
	
	public static void main(String[] s) {
		Storage storage = new Storage("C:/Users/Ekira/Desktop/����ϵͳ�γ����/mapping.txt", 0.03, 0.05);
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
