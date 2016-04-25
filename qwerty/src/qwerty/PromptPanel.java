package qwerty;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JPanel;

/**
 * 展示即将出现的方块
 * @author DouBear
 *
 */
public class PromptPanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 6037362308321129865L;
    
    /**
     * 获取屏幕尺寸
     */
    public final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * 下一个俄罗斯方块预览区的单元格边长数
     */
    protected final int SQUARE_COUNT = 5;
    
    /**
     * 单元格方块宽度占屏幕宽度的比例
     * == 需要在构造函数中初始化
     * == 比例应该与localPanel的比例相等
     */
    protected float UNIT_PERCENT = 0;
    
    /**
     * 一个方块的阴影宽度占单元格的比例
     */
    protected final float UNIT_SHADOW_PERCENT = (float)0.2;

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
    protected final int BORDER_WIDTH = 5;
    
    /**
     * 面板边长宽度
     * ==需要在构造函数中初始化
     */
    protected int PANEL_SQUARE = 0;
    
    /**
     * 用于存储矩阵
     */
    private int[][] matrix;
    
    /**
     * 空矩阵，用于初始化matrix矩阵
     */
    private int[][] empty;

    /**
     * 构造函数，初始化一个实例
     * @param unitPerc 预览区占屏幕的比例， （应该等于LocalPanel）
     */
    public PromptPanel(float unitPerc) {
        // 初始化参数
        UNIT_PERCENT = unitPerc;
        empty = new int[5][5];
        matrix = empty;
        UNIT_WIDTH = (int) (UNIT_PERCENT * SCREEN_SIZE.width);
        UNIT_SHADOW_WIDTH = (int) (UNIT_SHADOW_PERCENT * UNIT_WIDTH);
        PANEL_SQUARE = SQUARE_COUNT * UNIT_WIDTH + BORDER_WIDTH * 2;
        setPreferredSize(new Dimension(PANEL_SQUARE, PANEL_SQUARE));
        setBackground(Color.BLACK);
    }
    
    /**
     * 更新矩阵，用于绘图
     * @param matrix 传入的矩阵对象
     */
    public void updatePanel(int[][] matrix) {
        this.matrix = matrix;
        repaint();
    }

    /**
     * 绘制画面
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        int xUnitPos, yUnitPos;
        super.paint(g2d);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                xUnitPos = j * UNIT_WIDTH + BORDER_WIDTH;
                yUnitPos =i * UNIT_WIDTH + BORDER_WIDTH;
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
     * 绘制每个单元格，供paint调用
     * @param g2d 
     * @param xUnitPos x位置
     * @param yUnitPos y位置
     * @param color 绘制方块的颜色
     */
    private void paintUnit(Graphics2D g2d, int xUnitPos, int yUnitPos, Color color) {
        g2d.setColor(color);
        g2d.fillRect(xUnitPos, yUnitPos, UNIT_WIDTH, UNIT_WIDTH);
        g2d.setColor(color.darker());
        g2d.fillRect(xUnitPos + UNIT_WIDTH - UNIT_SHADOW_WIDTH, yUnitPos, UNIT_SHADOW_WIDTH, UNIT_WIDTH);
        g2d.fillRect(xUnitPos, yUnitPos + UNIT_WIDTH - UNIT_SHADOW_WIDTH, UNIT_WIDTH, UNIT_SHADOW_WIDTH);
    }
    

}
