package control;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

import org.apache.catalina.startup.WebAnnotationSet;

import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.sun.xml.internal.bind.v2.runtime.Name;

import dao.AlbumDao;
import dao.PictureDao;
import model.Album;
import model.picture;

public class Controller {
    private String nameString;
    public Controller() {
        // TODO Auto-generated constructor stub
    }
    
    public String getNameString() {
        return nameString;
    }

    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

    
    
    public List<Album> getAlbums() throws ClassNotFoundException, SQLException{
        List<Album> list = new ArrayList<Album>();
        list = new AlbumDao().getAlbums();
        return list;
    }
    public void createalbum(String nameString,String description,PrintWriter outPrintWriter){
        File file = new File("/tomcat/shiyan-test/WebContent/image/"+nameString);
        if(file.exists()){
            outPrintWriter.println("<script language='javascript'>alert('相册已经存在');</script>");
        }else{
            file.mkdirs();
            Album album = new Album();
            album.setDescription(description);
            album.setName(nameString);
            try {
                new AlbumDao().Createalbum(album);
            } catch (ClassNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            outPrintWriter.print("dasdasdadds");
        }
    }
    
    public void updatealbum(String nameString,String description,PrintWriter outPrintWriter,String oldname){
        try {
            File file = new File("/tomcat/shiyan-test/WebContent/image/"+oldname);
            File newFile = new File("/tomcat/shiyan-test/WebContent/image/"+nameString);
            if(newFile.exists()){
                outPrintWriter.println("<script language='javascript'>alert('相册名已被使用');</script>");
                return;
            }else{
                file.renameTo(newFile);
            }       
            new AlbumDao().updateAlbum(nameString,description,oldname);
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void deletealbum(String nameString){
        try{
            File file = new File("/tomcat/shiyan-test/WebContent/image/"+nameString);
            if(file.exists()){
                deleteFile(file);
            }
            new AlbumDao().deleteAlbum(nameString);
        }catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /*
     * 照片方法
     */
    public List<picture> getPictures(String name) throws ClassNotFoundException, SQLException{
        List<picture > listPictures = new ArrayList<picture>();
        listPictures = new PictureDao().getPictures(name);
        return listPictures;
    }
    
    public String addpicture(HttpServletRequest request,HttpServletResponse response,Servlet servlet) {
        try{
            SmartUpload su = new SmartUpload();//事例话上传下载
            su.setCharset("UTF-8");
            JspFactory _jspxFactory = null;
            PageContext pageContext = null;
            _jspxFactory = JspFactory.getDefaultFactory();
            pageContext = _jspxFactory.getPageContext(servlet,request,response,"",true,8192,true);
            su.initialize(pageContext);//初始化上传操作//初始化环境 pageContext为内置对象
//          su.setMaxFileSize(1024*1024);//设置最大的文件大小
//          su.setTotalMaxFileSize(5*1024*1024);//设置总的文件大小
            su.setAllowedFilesList("jpg,gif,png");
            su.upload();
        
            Request req=su.getRequest();
            System.out.print(req.getParameter("photo_name")+"\n");
            System.out.print(req.getParameter("albumname")+"\n");
            com.jspsmart.upload.File myFile = su.getFiles().getFile(0);
            myFile.saveAs("/tomcat/shiyan-test/WebContent/image/"+req.getParameter("albumname")+"/"+req.getParameter("photo_name"));
            
            picture pic = new picture();
            pic.name = req.getParameter("photo_name");
            pic.description = req.getParameter("photo_description");
            pic.image = req.getParameter("photo_name");
            try {
                new PictureDao().addpicture(pic,req.getParameter("albumname"));
            } catch (ClassNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return req.getParameter("albumname");
        }catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SmartUploadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public void deletepic(String albumname,String photoname) throws ClassNotFoundException, SQLException {
        File file = new File("/tomcat/shiyan-test/WebContent/image/"+albumname+"/"+photoname);
        file.delete();
        new PictureDao().deletepic(albumname,photoname);
    }
    
    public void picedit(HttpServletRequest request,HttpServletResponse response) throws ClassNotFoundException, SQLException{
        String albumname = request.getParameter("albumname");
        String description = request.getParameter("photo_description");
        String oldname = request.getParameter("oldname");
        String photoname = request.getParameter("photo_name");
        File file = new File("/tomcat/shiyan-test/WebContent/image/"+albumname+"/"+oldname);
        File file2 = new File("/tomcat/shiyan-test/WebContent/image/"+albumname+"/"+photoname);
        file.renameTo(file2);
        new PictureDao().picedit(albumname,photoname,description,oldname);
    }
    private void deleteFile(File file){ 
        if(file.exists()){                    //判断文件是否存在
            if(file.isFile()){                    //判断是否是文件
                file.delete();                       //delete()方法 你应该知道 是删除的意思;
            }else if(file.isDirectory()){              //否则如果它是一个目录
                File files[] = file.listFiles();               //声明目录下所有的文件 files[];
                for(int i=0;i<files.length;i++){            //遍历目录下所有的文件
                    this.deleteFile(files[i]);             //把每个文件 用这个方法进行迭代
                } 
            } 
            file.delete(); 
        }else{ 
            System.out.println("所删除的文件不存在！"+'\n'); 
        } 
    } 
    
    
}
