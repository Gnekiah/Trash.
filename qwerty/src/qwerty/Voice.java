package qwerty;

import java.io.*;
import sun.audio.*;

public class Voice {
    
    /**
     * 播放消除行的音乐
     */
    public void playLinefilled() {
        play("D:\\Eclipse\\workspace\\qwerty\\resource\\linefilled.wav");
    }
    
    /**
     * 播放单元移动音乐
     */
    public void playMove() {
        play("D:\\Eclipse\\workspace\\qwerty\\resource\\move.wav");
    }
    
    /**
     * 播放单元格旋转音乐
     */
    public void playRotate() {
        play("D:\\Eclipse\\workspace\\qwerty\\resource\\rotate.wav");
    }
    
    /**
     * 播放游戏开始音乐
     */
    public void playStart() {
        play("D:\\Eclipse\\workspace\\qwerty\\resource\\start.wav");
    }
    
    /**
     * 播放跌落音乐
     */
    public void playDown() {
        play("D:\\Eclipse\\workspace\\qwerty\\resource\\down.wav");
    }
    
    /**
     * 播放游戏结束音乐
     */
    public void playGameOver() {
        play("D:\\Eclipse\\workspace\\qwerty\\resource\\gameover.wav");
    }
    
    /**
     * 根据路径播放游戏音乐，供内部函数调用
     * @param path 传入的路径
     */
    private void play(String path) {
        try {
            AudioPlayer.player.start(new AudioStream(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}
