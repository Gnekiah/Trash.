package qwerty;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * 联机的远程计算机玩家的游戏窗口在本地的展示窗口
 * @author DouBear
 *
 */
public class RemotePanel extends LocalPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 4063768506431559643L;
    
    /**
     * 创建一个远程主机游戏面板在本地的映射面板的实例
     * @param width 面板宽度的单元格数量
     * @param height 面板高度的单元格数量
     * @param unitPerc 单元格占屏幕的比例
     * @param shadowPerc 阴影占单元格的比例
     * @throws Exception 
     */
    public RemotePanel(float unitPerc) {
        super(unitPerc);
      
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
    }

    /**
     * 更新面板
     */
    public void updatePanel(int[][] matrix) {
        this.matrix = matrix;
     //   System.out.println(matrix[10][10]);
        repaint();
    }

    /**
     * 绘制面板
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
     * @param xUnitPos x位置
     * @param yUnitPos y位置
     * @param color 单元格颜色
     */
    private void paintUnit(Graphics2D g2d, int xUnitPos, int yUnitPos, Color color) {
        g2d.setColor(color);
        g2d.fillRect(xUnitPos, yUnitPos, UNIT_WIDTH, UNIT_WIDTH);
        g2d.setColor(color.darker());
        g2d.fillRect(xUnitPos + UNIT_WIDTH - UNIT_SHADOW_WIDTH, yUnitPos, UNIT_SHADOW_WIDTH, UNIT_WIDTH);
        g2d.fillRect(xUnitPos, yUnitPos + UNIT_WIDTH - UNIT_SHADOW_WIDTH, UNIT_WIDTH, UNIT_SHADOW_WIDTH);
    }
}
