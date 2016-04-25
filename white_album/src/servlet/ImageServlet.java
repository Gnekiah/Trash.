package servlet;

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

@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String imageRoot = "/whitealbum/";
    private static final int MAX_SIZE = 5 * 1024 * 1024;
    
    public ImageServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String op = request.getParameter("op");
        switch(op) {
        case "addAlbum":
            addAlbum(request, response);
            break;
        case "modifyAlbum":
            modifyAlbum(request, response);
            break;
        case "deleteAlbum":
            deleteAlbum(request, response);
            break;
        case "viewAlbum":
            viewAlbum(request, response);
            break;
        case "upload":
            upload(request, response);
            break;
        case "viewImage":
            viewImage(request, response);
            break;
        case "modifyImage":
            modifyImage(request, response);
            break;
        case "deleteImage":
            deleteImage(request, response);
            break;
        }
    }
    
    private void addAlbum(HttpServletRequest request, HttpServletResponse response) {
        File file = new File(imageRoot + request.getParameter("fname"));
        if (file.exists()) {
            request.setAttribute("addAlbum", false);
        }
        else {
            file.mkdir();
            request.setAttribute("addAlbum", true);
        }
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/addAlbum.jsp");
        try {
            rd.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void modifyAlbum(HttpServletRequest request, HttpServletResponse response) {
        File file = new File(imageRoot + request.getParameter("name"));
        if (file.exists() && file.renameTo(new File(imageRoot + request.getParameter("fname")))) {
            request.setAttribute("modifyAlbum", true);
        }
        else {
            request.setAttribute("modifyAlbum", false);
        }
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/modifyAlbum.jsp");
        try {
            rd.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void deleteAlbum(HttpServletRequest request, HttpServletResponse response) {
        File file = new File(imageRoot + request.getParameter("name"));
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length < 1) {
                file.delete();
            }
            else {
                for (File f : files) {
                    f.delete();
                }
                file.delete();
            }
            request.setAttribute("deleteAlbum", true);
        }   
        else {
            request.setAttribute("deleteAlbum", false);
        }
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/deleteAlbum.jsp");
        try {
            rd.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void viewAlbum(HttpServletRequest request, HttpServletResponse response) {
        File file = new File(imageRoot + request.getParameter("album"));
        request.setAttribute("viewAlbum", file);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/viewAlbum.jsp");
        try {
            rd.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void upload(HttpServletRequest request, HttpServletResponse response) {
        DiskFileItemFactory imgfile = new DiskFileItemFactory();
        imgfile.setSizeThreshold(MAX_SIZE);
        imgfile.setRepository(new File("D:/temp"));
        ServletFileUpload sfup = new ServletFileUpload(imgfile);
        sfup.setFileSizeMax(MAX_SIZE);
        Map<String, List<FileItem>> fileList = new HashMap<String, List<FileItem>>();
        try {
            PrintWriter out = response.getWriter();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            fileList = sfup.parseParameterMap(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        for (List<FileItem> fi : fileList.values()) {
            for (FileItem f : fi) {
                File newFile = new File(imageRoot + request.getParameter("album") + "/" + f.getName());
                try {
                    f.write(newFile);
                    request.setAttribute("upload", true);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("upload", false);
                }
            }
        }
        request.setAttribute("album", request.getParameter("album"));
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/upload.jsp");
        try {
            rd.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
   
    private void viewImage(HttpServletRequest request, HttpServletResponse response) {
        File file = new File(imageRoot + request.getParameter("album") + "/" + request.getParameter("name"));
        request.setAttribute("imgFile", file);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/viewImage.jsp");
        try {
            rd.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void modifyImage(HttpServletRequest request, HttpServletResponse response) {
        File file = new File(imageRoot + request.getParameter("album") + "/" + request.getParameter("name"));
        if (file.exists() && file.renameTo(new File(imageRoot + request.getParameter("album") + "/" + request.getParameter("fname")))) {
            request.setAttribute("modifyImage", true);
        }
        else {
            request.setAttribute("modifyImage", false);
        }
        request.setAttribute("album", request.getParameter("album"));
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/modifyImage.jsp");
        try {
            rd.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void deleteImage(HttpServletRequest request, HttpServletResponse response) {
        File file = new File(imageRoot + request.getParameter("album") + "/" + request.getParameter("name"));
        if (file.exists() && file.delete()) {
            request.setAttribute("deleteImage", true);
        }
        else {
            request.setAttribute("deleteImage", false);
        }
        request.setAttribute("album", request.getParameter("album"));
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/deleteImage.jsp");
        try {
            rd.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
