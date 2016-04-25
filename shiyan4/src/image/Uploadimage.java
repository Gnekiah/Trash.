package image;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class Uploadimage
 */
@WebServlet("/Uploadimage")
public class Uploadimage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Uploadimage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	    DiskFileItemFactory imgfile = new DiskFileItemFactory();
        imgfile.setSizeThreshold(4000000);
        imgfile.setRepository(new File("D:/temp"));
        ServletFileUpload sfup = new ServletFileUpload(imgfile);
        sfup.setFileSizeMax(4000000);
        Map<String, List<FileItem>> fileList = new HashMap<String, List<FileItem>>();
        try {
            fileList = sfup.parseParameterMap(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        for (List<FileItem> fi : fileList.values()) {
            for (FileItem f : fi) {
                File newFile = new File("D:/shiyan4/" + request.getParameter("album") + "/" + f.getName());
                try {
                    f.write(newFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        File file = new File("D:/shiyan4/" + request.getParameter("album"));
        request.setAttribute("viewAlbum", file);
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
