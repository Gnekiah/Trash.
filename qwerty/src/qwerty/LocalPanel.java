package qwerty;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import javax.swing.JPanel;

/**
 * 当前玩家的游戏区域
 * @author DouBear
 *
 */
public class LocalPanel extends JPanel{
    /**
     *
     */
    private static final long serialVersionUID = 2170144588248128283L;

    /**
     * 获取屏幕尺寸
     */
    public final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    
    /**
     * 游戏区宽上的单元格数量
     */
    protected final int WIDTH_COUNT = 20;
    
    /**
     * 游戏区实际高度的单元格数
     */
    protected final int HEIGHT_COUNT = 35;  
    
    /**
     * 单元格方块宽度占屏幕宽度的比例
     * == 需要在构造函数中初始化
     */
    protected float UNIT_PERCENT = 0;
    
    /**
     * 一个方块的阴影宽度占单元格的比例
     */
    protected final float UNIT_SHADOW_PERCENT = (float)0.2;
    
    /**
     * 游戏区上方被隐藏的单元格数量
     */
    protected final int TOP_HIDED_COUNT = 3;
    
    /**
     * 游戏区高度方向可见的单元格数量
     */
    protected final int VISIBLE_HEIGHT_COUNT = HEIGHT_COUNT - TOP_HIDED_COUNT;

    /**
     * 一个单元格的宽度
     * ==需要在构造函数中初始化
     */
    protected int UNIT_WIDTH = 0;
    
    /**
     * 单元格阴影的实际宽度
     * ==需要在构造函数中初始化
     */
    protected int UNIT_SHADOW_WIDTH = 0;
    
    /**
     * 游戏区边框宽度
     */
    protected final int BORDER_WIDTH = 4;
    
    /**
     * 面板宽度
     * ==需要在构造函数中初始化
     */
    protected int PANEL_WIDTH = 0;
    
    /**
     * 面板高度
     * ==需要在构造函数中初始化
     */
    protected int PANEL_HEIGHT = 0;
    
    /**
     * 定义矩阵存储俄罗斯方块游戏的区块
     */
    protected int[][] matrix;
    
    /**
     * 空矩阵，用于初始化matrix
     */
    private int[][] empty;
   
    /**
     * 大字体
     */
    protected final Font BIG_FONT = new Font("Consolas", Font.BOLD, 24);
    
    /**
     * 创建一个本地游戏显示区域面板的实例
     * @param width 面板宽度的单元格数量
     * @param height 面板高度的单元格数量
     * @param unitPerc 单元格占屏幕的比例
     * @param shadowPerc 阴影占单元格的比例
     */
    public LocalPanel(float unitPerc) {
        // 初始化参数
        UNIT_PERCENT = unitPerc;
        empty = new int[HEIGHT_COUNT][WIDTH_COUNT];
        matrix = empty;
        UNIT_WIDTH = (int) (UNIT_PERCENT * SCREEN_SIZE.width);
        UNIT_SHADOW_WIDTH = (int) (UNIT_SHADOW_PERCENT * UNIT_WIDTH);
        PANEL_WIDTH = WIDTH_COUNT * UNIT_WIDTH + BORDER_WIDTH * 2;
        PANEL_HEIGHT = VISIBLE_HEIGHT_COUNT * UNIT_WIDTH + BORDER_WIDTH * 2;
        
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
    }
    
    /**
     * 更新面板
     * @param matrix
     */
    public void updatePanel(int[][] matrix) {
        this.matrix = matrix;
        repaint();
    }

    /**
     * 绘制图像
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        int xUnitPos, yUnitPos;
        super.paint(g2d);
        for (int i = TOP_HIDED_COUNT; i < HEIGHT_COUNT; i++) {
            for (int j = 0; j < WIDTH_COUNT; j++) {
                xUnitPos = j * UNIT_WIDTH + BORDER_WIDTH;
                yUnitPos =(i-TOP_HIDED_COUNT) * UNIT_WIDTH + BORDER_WIDTH;
                   
                switch(matrix[i][j]) {
                case 0:
                    break;
                case 1:
                    paintUnit(g2d, xUnitPos, yUnitPos, Color.CYAN);
                    break;
                case 2:
                    paintUnit(g2d, xUnitPos, yUnitPos, Color.BLUE);
                    break;
                case 3:
                    paintUnit(g2d, xUnitPos, yUnitPos, Color.ORANGE);
                    break;
                case 4:
                    paintUnit(g2d, xUnitPos, yUnitPos, Color.RED);
                    break;
                case 5:
                    paintUnit(g2d, xUnitPos, yUnitPos, Color.PINK);
                    break;
                case 6:
                    paintUnit(g2d, xUnitPos, yUnitPos, Color.YELLOW);
                    break;
                case 7:
                    paintUnit(g2d, xUnitPos, yUnitPos, Color.GRAY);
                    break;
                }
            }
        }           
    }
    
    /**
     * 绘制每个单元格
     * @param g2d
     * @param xUnitPos 
     * @param yUnitPos
     * @param color
     */
    private void paintUnit(Graphics2D g2d, int xUnitPos, int yUnitPos, Color color) {
        g2d.setColor(color);
        g2d.fillRect(xUnitPos, yUnitPos, UNIT_WIDTH, UNIT_WIDTH);
        g2d.setColor(color.darker());
        g2d.fillRect(xUnitPos + UNIT_WIDTH - UNIT_SHADOW_WIDTH, yUnitPos, UNIT_SHADOW_WIDTH, UNIT_WIDTH);
        g2d.fillRect(xUnitPos, yUnitPos + UNIT_WIDTH - UNIT_SHADOW_WIDTH, UNIT_WIDTH, UNIT_SHADOW_WIDTH);
    }

}

