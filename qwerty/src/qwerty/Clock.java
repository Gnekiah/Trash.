package qwerty;

/**
 * 时钟类，用于控制区块的下落时间间隔
 * @author DouBear
 *
 */
public class Clock {

    /**
     * 记录上一次的时间
     */
    private long prevTime;
    
    /**
     * 记录当前时间
     */
    private long currTime;
    
    /**
     * 默认的基础时间间隔
     */
    private float BASIC_INTERVAL = 1000;
    
    /**
     * 记录游戏时间，根据游戏时间调整游戏难度
     */
    private long allTime;
    
    /**
     * 记录两个时间的时间间隔
     */
    private float interval;
    
    /**
     * 构造函数
     */
    public Clock() {
        init();
    }
    
    /**
     * 对属性初始化
     */
    public void init() {
        prevTime = System.currentTimeMillis();
        allTime = 0;
        currTime = prevTime;
        interval = 1;
    }
    
    /**
     * 判断时间是否超过设置的时间间隔
     * @return true表示超过时间间隔，重新计时
     */
    public boolean timeCheckOut() {
        currTime = System.currentTimeMillis();
        if (currTime - prevTime > BASIC_INTERVAL * interval) {
            allTime += currTime - prevTime;
            if (allTime > 10000)
                setInterval((float)0.9);
            if (allTime > 20000)
                setInterval((float)0.8);
            if (allTime > 30000)
                setInterval((float)0.7);
            if (allTime > 40000)
                setInterval((float)0.6);
            if (allTime > 50000)
                setInterval((float)0.5);
            if (allTime > 60000)
                setInterval((float)0.4);
            if (allTime > 70000)
                setInterval((float)0.3);
            if (allTime > 80000)
                setInterval((float)0.2);
            if (allTime > 90000)
                setInterval((float)0.1);
            
            prevTime = currTime;
            return true;
        }
        return false;
    }
    
    /**
     * 设置时间间隔的百分比，实际时间间隔为interval * 初始时间间隔
     * @param interval 初始时间间隔的比例
     */
    public void setInterval(float interval) {
        this.interval = interval;
    }
}
