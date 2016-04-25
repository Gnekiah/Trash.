package pro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class BusHub {
    
    // 发送数据每个bit的时间间隔
    private int gap = 0;
    // 运行状态，false表示暂停，true表示运行
    private boolean status = false;
    // 数据线路总线，当位的数据位
    //private ArrayList<Integer> bus = null;
    // 总线上的数据数组，0表示无数据，正数表示单数据源发送，-1表示数据碰撞结果
    private int[] bus = null;
    // 每个线程在总线上传送数据的数据
    private int[][] subbus = null;
    // 主机（子线程）的消息队列
    private Queue<String> message = null;
    
    
    BusHub(int length, int quantity) {
        //bus = new ArrayList<Integer>(Collections.nCopies(length, new Integer(0)));
        bus = new int[length];
        subbus = new int[quantity][length];
        message = new LinkedList<String>();
    }
    
    
    public void setGap(int gap) { this.gap = gap; }
    public void setStatus(boolean status) { this.status = status; }
    //public void setBus(int hostid) { addHostIdToBus(hostid); }
    
    public void offerMessage(String msg) { message.offer(msg); }
    public String pollMessage() { return message.poll(); }
    
    public int getGap() { return gap; }
    public boolean getStatus() { return status; }
    
    // 获取总线监听点的数据
    public int getMonitor(int pos) { mergeSubbus(); return bus[pos]; }
    // 获取总线数据
    public int[] getBus() { mergeSubbus(); testpoint();return bus; }
    
    /**
     * 传入数据发送点与内容，根据发送点模拟数据广播的形式，通过内容模拟数据发送
     * @param id 线程id
     * @param pos 发送点位置
     * @param data 数据内容
     */
    public void setSubbus(int id, int pos, int data) {
        id -= 1; // 将id转换为数组行数的映射
        for (int i = 0; i < pos; i++) {
            subbus[id][i] = subbus[id][i+1];
        }
        for (int i = subbus[id].length-1; i > pos; i--) {
            subbus[id][i] = subbus[id][i-1];
        }
        subbus[id][pos] = data;
    }
    
    /**
     * 合并各线程数据到总线上
     * int flag：用于记录subbus中是否存在多个并行数据，
     * 如存在多条数据，则flag为-1
     * 若只有单行数据，则设为数据的内容
     * 若无数据，则设为0
     */
    private void mergeSubbus() {
        for (int i = 0; i < subbus[0].length; i++) {        
            int flag = -1;
            int cnt = 0;
            for (int j = 0; j < subbus.length; j++) {
                cnt += subbus[j][i]==0?1:0;
                flag = (subbus[j][i]>flag) ? subbus[j][i] : flag;
            }
            bus[i] = cnt < subbus.length-1?-1: flag;
        }
    }
    
    
    
    private void testpoint() {
        for (int i = 0; i < subbus.length; i++) {
            for (int j = 0; j < subbus[0].length; j++) {
                System.out.print(subbus[i][j]);
            }
            System.out.println();
        }
    }
    
}
