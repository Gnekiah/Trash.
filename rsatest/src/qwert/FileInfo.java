package qwert;

public class FileInfo {
    private String fileName = null;
    private long fileLength = 0;
    private String sign = null;
    public FileInfo(String name, long length) { setPath(name); setLength(length); }
    public FileInfo(String name, long length, String sign) { setPath(name); setLength(length); setSign(sign); }
    
    public void setPath(String fileName) { this.fileName = fileName; }
    public void setLength(long length) { this.fileLength = length; }
    public void setSign(String sign) { this.sign = sign; }
    
    public String getPath() { return this.fileName; }
    public long getLength() { return this.fileLength; }
    public String getSign() { return this.sign; }
}
