package pro;

public class Data {
    
    private byte[] s = null;
    
    private boolean check = true;
    
    public void setData(byte[] s) {
        this.s = s;
    }

    public byte[] getData() {
        return s;
    }
    
    /**
     * 数据接收线程每轮修改数据后，直接将check置为false
     * 数据处理线程首先检查check是否为false，若为false，则读取数据并将check置为true
     * @return
     */
    
    public boolean getCheck() {
        return check;
    }
    
    public void setCheck(boolean check) {
        this.check = check;
    }

}
