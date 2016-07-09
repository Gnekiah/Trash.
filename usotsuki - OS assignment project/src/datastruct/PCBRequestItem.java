package datastruct;

public class PCBRequestItem {
	private double requestTime = 0;
	private int logicAddr = 0;
	
	public PCBRequestItem(double requestTime, int logicAddr) {
		this.requestTime = requestTime;
		this.logicAddr = logicAddr;
	}
	
	public void setRequestTime(double requestTime) { this.requestTime = requestTime; }
	public void setLogicAddr(int logicAddr) { this.logicAddr = logicAddr; }
	
	public double getRequestTime() { return requestTime; }
	public int getLogicAddr() { return logicAddr; }

}
