package image;

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Changeimagename
 */
@WebServlet("/Changeimagename")
public class Changeimagename extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Changeimagename() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	    File file = new File("D:/shiyan4/" + request.getParameter("album") + "/" + request.getParameter("name"));
        if (file.exists() && file.renameTo(new File("D:/shiyan4/" + request.getParameter("album") + "/" + request.getParameter("fname")))) {}
        request.setAttribute("album", request.getParameter("album"));
        File file1 = new File("D:/shiyan4/" + request.getParameter("album"));
        request.setAttribute("viewAlbum", file1);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/viewalbum.jsp");
        rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
