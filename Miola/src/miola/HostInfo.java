package miola;

public class HostInfo {
    /*
     * 超时检测，单位毫秒
     * 用于判断客户端是否离线
     */
    private final int TIMEOUT = 5000;
    
    private String ID = null;
    private String IP = null;
    private int port = 0;
    private String info = null;
    private long stamp = 0;
    
    public HostInfo(String ID, String IP, int port, String info) {
        setID(ID);
        setIP(IP);
        setPort(port);
        setInfo(info);
        this.stamp = System.currentTimeMillis();
    }
    
    public void setID(String ID) { this.ID = ID; }
    public void setIP(String IP) { this.IP = IP; }
    public void setPort(int port) { this.port = port; }
    public void setInfo(String info) { this.info = info; }
    
    public String getID() { return this.ID; }
    public String getIP() { return this.IP; }
    public int getPort() { return this.port; }
    public String getInfo() { return this.info; }
    
    public void updateStamp() { stamp = System.currentTimeMillis(); }
    public boolean timeout() {
        return (System.currentTimeMillis() - stamp) > TIMEOUT ? true : false; 
    }
}
