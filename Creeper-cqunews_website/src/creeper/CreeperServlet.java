package creeper;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CreeperServlet")
public class CreeperServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 服务器端处理的请求类型
	 */
	String sh = null;
	
	/**
	 * 返回给客户端的html内容
	 */
	String revert = null;
	
	/**
	 * 声明一个辅助类，用于对不同请求进行响应
	 */
	TextReader textReader = null;
	
    public CreeperServlet() {
        super();
        textReader = new TextReader();
        System.out.println("初始化完成");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		sh = request.getParameter("sh");
		if (sh != null) {
		    // 主页
		    if (sh.equals("index"))
		        revert = textReader.getIndex();
		    // 新闻内容页面
		    else if (sh.equals("txt")) 
		        revert = textReader.getText(request.getParameter("type"), request.getParameter("tab"), request.getParameter("id"));
		    // 翻页请求、目录
		    else if (sh.equals("page")) 
		        revert = textReader.getPage(request.getParameter("type"), request.getParameter("page"));
		    // 页面未找到
		    else 
		        revert = textReader.notFound(request.getRequestURI());
		}
		// 搜索请求
		else {
		    long startTime = System.currentTimeMillis();
		    if (request.getParameter("search") != null) 
                revert = textReader.getSearch(request.getParameter("search"));
		    else
		        revert = textReader.notFound(request.getRequestURI());
		    long endTime = System.currentTimeMillis();
		    System.out.println("Searching: " + (endTime - startTime) + "msec");
		}
	    
		response.setContentType("text/html; charset=utf-8");
        BufferedWriter out = new BufferedWriter(response.getWriter());
        out.write(revert);
        out.flush();
        out.close();
	}

}
