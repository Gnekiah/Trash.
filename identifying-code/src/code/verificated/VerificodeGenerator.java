package code.verificated;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class VerificodeGenerator
 */
@WebServlet("/VerificodeGenerator")
public class VerificodeGenerator extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	/**
	 * 图片的宽度
	 */
	public static final int WIDTH = 90;
	
	/**
	 * 图片的高度
	 */
	public static final int HEIGHT = 40;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerificodeGenerator() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // 设置图片的格式
	    response.setContentType("image/jpeg");
	    OutputStream outputStream = response.getOutputStream();
	    BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);  
	    // 绘图
        Graphics g = image.getGraphics();
        Color c = g.getColor();
        g.fillRect(0, 0, WIDTH, HEIGHT);  
        // 显示的26个字母和10个数字
        char[] ch = "QWERTYUIOPASDFGHJKLZXCVBNM0123456789".toCharArray();
        // 合法的字符数量
        int length = ch.length;
        // 用于保存生成的随机验证码
        String sRand = "";
        // 开始绘图
        Random random = new Random();  
        for (int i = 0; i < 4; i++) {  
            g.setFont(new Font("Consolas",Font.PLAIN ,15));  
            String rand = new Character(ch[random.nextInt(length)]).toString();  
            sRand += rand;
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));  
            g.drawString(rand, 20 * i + 6, 25);  
        }  
        // 生成干扰元素
        for (int i = 0; i < 20; i++) {  
            g.drawOval(random.nextInt(WIDTH), random.nextInt(HEIGHT), 2, 2);
        }
        g.setColor(c);
        g.dispose();
        // 放入会话区
        request.getSession().setAttribute("safecode", sRand);
        // 发送如片
        ImageIO.write(image, "JPEG", outputStream);
        
		// response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
