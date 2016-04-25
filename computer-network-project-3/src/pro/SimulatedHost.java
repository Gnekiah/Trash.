package pro;

import java.util.Random;

public class SimulatedHost extends Thread {
    // bushub的对象
    private BusHub hub = null;
    // 记录当前“主机”在bus上的位置
    private int pos = 0;
    // 主机ID，从1开始计数
    private int id = 0;
    // 二进制指数退避的尝试次数
    private int failed = 0;
    // 随机值
    private Random random = null;
    
    SimulatedHost(BusHub hub, int id, int pos) {
        random = new Random();
        this.hub = hub;
        this.id = id;
        this.pos = pos;
    }

    
    public void run() {
        // 用于响应暂停事件
        while(true) {
            // 当程序状态设置为阻塞时，执行sleep，否则执行working
            if (hub.getStatus()) {
                working();
            }
            else {
                try { Thread.sleep(100); } 
                catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }
    
    /**
     * 当检测到总线上无数据时，进入选择发送支线，根据有无待发数据决定发送或继续监听
     * 否则sleep一个间隔后继续执行
     */
    private void working() {
        if (doMonitor()) {
            for (int i = (int) ((random.nextInt()) % 100); i > 0; i--) {
                hub.setSubbus(id, pos, 0);
                doGap();
            }
            hub.offerMessage("主机："+this.id+"  即将发送数据；");
            
            if (doMonitor()) {
                if (!sendData()) {
                    hub.offerMessage("主机："+this.id+"  发送失败，执行二进制指数退避算法重试发送；");
                    doBinExpBakOff();
                }
            }
            else {
                hub.offerMessage("主机："+this.id+"  发送前检测到信道繁忙，进入等待状态；");
            }
        }
        else {
            doGap();
        }
    }
    
    /**
     * 监听总线上的空闲状态
     * @return 若有数据通过，则返回false，若总线空闲，则返回true
     */
    private boolean doMonitor() {
        return (hub.getMonitor(pos) == 0) ? true : false;
    }
    
    /**
     * 检验在发送数据的过程中是否产生冲突
     * @return
     */
    private boolean checkMonitor() {
        return (hub.getMonitor(pos) == id) || (hub.getMonitor(pos) == 0) ? true : false;
    }
    
    /**
     * 发送数据
     * @return 发送成功则返回true，失败返回false
     */
    private boolean sendData() {
        hub.offerMessage("主机："+this.id+"  开始发送数据，数据大小为包含主机ID的64字节；");
        for (int i = 0; i < 64; i++) {
            hub.setSubbus(id, pos, id);
            doGap();
            if (!checkMonitor()) {
                hub.offerMessage("主机："+this.id+"  发送过程中检测到冲突，发送10字节的阻塞信息；");
                // 发送阻塞信息
                for (int j = 0; j < 10; j++) {
                    hub.setSubbus(id, pos, -1);
                    doGap();
                }
                hub.setSubbus(id, pos, 0);
                return false;
            }
        }
        hub.offerMessage("主机："+this.id+"  数据发送成功；");
        for (int i = 0; i < 30; i++) {
            hub.setSubbus(id, pos, 0);
            doGap();
        }
        return true;
    }
    
    // 发送间隔
    private void doGap() {
        try { Thread.sleep(hub.getGap()); } 
        catch (InterruptedException e) { e.printStackTrace(); }
    }
    
    // 二进制指数退避
    private void doBinExpBakOff() {
        failed = failed>16 ? 2 : failed+1;
        for (int i = (int) ((new Random().nextInt()) % Math.pow(2, failed)); i > 0; i--) {
            hub.setSubbus(id, pos, 0);
            doGap();
        }
    }
}
