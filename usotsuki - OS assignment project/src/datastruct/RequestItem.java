package datastruct;

/**
 * ����ÿ����������ݣ���������Ľ���ID�������ʱ��
 * @author Ekira
 *
 */
public class RequestItem {
	private int proc = 0;
	private double reqst = 0;
	
	public RequestItem(int proc, double reqst) {
		this.proc = proc;
		this.reqst = reqst;
	}
	
	public void setProc(int proc) { this.proc = proc; }
	public void setReqst(double reqst) { this.reqst = reqst; }
	
	public int getProc() { return proc; }
	public double getReqst() { return reqst; }
}
