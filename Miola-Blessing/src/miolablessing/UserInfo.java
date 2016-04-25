package miolablessing;

public class UserInfo {
    private String ID = null;
    private String info = null;
    
    public UserInfo(String id, String info) {
        setID(id);
        setInfo(info);
    }
    
    public void setID(String id) { this.ID = id; }
    public void setInfo(String info) { this.info = info; }
    
    public String getID() { return this.ID; }
    public String getInfo() { return this.info; }
    
}
