package qwerty;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class GameOrder extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 5556847527216101481L;

    /**
     * 本地游戏窗口实例
     */
    private LocalPanel localPanel;
    
    /**
     * 联机远程窗口实例
     */
    private RemotePanel remotePanel;
    
    /**
     * 下一个方块提示窗口的实例
     */
    private PromptPanel promptPanel;
    
    /**
     * 显示连接的远程主机的ip地址标签
     */
    private JLabel ipLabel;
    
    /**
     * 显示远程主机游戏的分数标签
     */
    private JLabel remoteScore;
    
    /**
     * 显示本地游戏的分数标签
     */
    private JLabel localScore;
    
    /**
     * 游戏逻辑控制实例
     */
    private GameCore gameCore;
    
    /**
     * 调用音乐
     */
    private Voice voice = new Voice();
    /**
     * 创建一个实例
     */
    private GameOrder() {
        super("So cool !");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        /*
         * 创建不同模块的实例
         */
        this.localPanel = new LocalPanel((float)0.015);
        this.remotePanel = new RemotePanel((float)0.008);
        this.promptPanel = new PromptPanel((float)0.015);
        this.ipLabel = new JLabel("192.168.99.122");
        this.localScore = new JLabel("0");
        this.remoteScore = new JLabel("0");
        this.gameCore =new GameCore();
       
        /*
         * 布局管理，使用GridBagLayout
         */
        GridBagLayout gridlayout = new GridBagLayout();
        setLayout(gridlayout);
        GridBagConstraints layout = new GridBagConstraints();
        layout.fill = GridBagConstraints.BOTH;
        this.add(localPanel);
        this.add(remotePanel);
        this.add(promptPanel);
        this.add(ipLabel);
        this.add(localScore);
        this.add(remoteScore);

        /*
         * 远端游戏窗口布局
         */
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 3;
        layout.insets = new Insets(10, 10, 0, 10);
        gridlayout.setConstraints(remotePanel, layout);
        
        /*
         * 远端IP地址显示布局
         */
        layout.gridx = 0;
        layout.gridy = 1;
        layout.insets = new Insets(10, 20, 0, 0);
        gridlayout.setConstraints(ipLabel, layout);
        
        /*
         * 远端分数显示布局
         */
        layout.gridx = 1;
        layout.gridy = 1;
        layout.insets = new Insets(10, 100, 0, 10);
        gridlayout.setConstraints(remoteScore, layout);
        
        /*
         * 预览窗口布局
         */
        layout.gridx = 0;
        layout.gridy = 2;
        layout.gridwidth = 2;
        layout.insets = new Insets(40, 40, 10, 0);
        gridlayout.setConstraints(promptPanel, layout);
        
        /*
         * 本地分数显示布局
         */
        layout.gridx = 2;
        layout.gridy = 2;
        layout.insets = new Insets(40, 5, 10, 0);
        gridlayout.setConstraints(localScore, layout);
        
        /*
         * 本地游戏窗口显示布局
         */
        layout.gridx = 3;
        layout.gridy = 0;
        layout.gridheight = 4;
        layout.insets = new Insets(10, 0, 10, 10);
        gridlayout.setConstraints(localPanel, layout);
        
        /*
         * 监听键盘事件
         * left、right、down对应方块的向左、向右、向下移动
         */
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                case KeyEvent.VK_UP:
                        gameCore.swift();
                        voice.playRotate();
                    break;
                case KeyEvent.VK_LEFT:
                        gameCore.leftWard();
                        voice.playMove();
                    break;
                case KeyEvent.VK_RIGHT:
                        gameCore.rightWard();
                        voice.playMove();
                    break;
                case KeyEvent.VK_DOWN:
                    gameCore.downWard();
                    voice.playMove();
                    break;
                }
                localPanel.updatePanel(gameCore.getContainer());
                super.keyPressed(e);
            }            
        });
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * 开始游戏
     */
    public void startGame() {
        MatrixReceiver r = new MatrixReceiver("receiver");
        MatrixSender s = new MatrixSender("sender");
        r.start();
        s.start();
        Clock clock = new Clock();
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (clock.timeCheckOut()) {
                if(!gameCore.startGame())
                    break;
            }
            s.updateMatrix(gameCore.getContainer(), gameCore.getScore());
            remotePanel.updatePanel(r.getMatrix());
            promptPanel.updatePanel(gameCore.getNextCube());
            localPanel.updatePanel(gameCore.getContainer());
            localScore.setText(String.valueOf(gameCore.getScore()));
            remoteScore.setText(String.valueOf(gameCore.getScore()));
        }
        
    }
    
    public static void main(String[] args) {
        GameOrder gameOrder = new GameOrder();
        gameOrder.startGame();
    }
}
