package networkrobot;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 内容整理类，用于对给定链接的网页内容抓取并按照要求保存到本地
 * 主要抓取内容为：网页标题、新闻内容(根据<div id="zoom">抓取)、新闻内容图片(根据<div id="zoom">标签内的<img>标签抓取)
 * 其中新闻内容按照html一个<div>标签的格式保存，图片将以ID形式重命名，同时更改新闻内容中图片的资源路径为实际资源路径
 * 该类提供一个接口：
 * int[] arrangeContent(String url, String imgDirPath, String textDirPath, int txtID, int imgID)
 * 函数接受4个参数，其中：
 * url 表示要抓取的目标链接
 * imgDirPath 表示图片保存的目录
 * textDirPath 表示新闻文本保存的目录
 * txtID 表示文本内容的ID
 * imgID 表示分配给下一个抓取到的图片的ID
 * 接口返回一个int[2]，int[0]为文本内容ID，int[1]为图片ID
 * @author DouBear
 *
 */
public class ContentArrangement {
    
    /**
     * 连接延时
     */
    private final int TIMEOUT = 10000;
    
    /**
     * 图片目录保存路径
     */
    private String imgDirPath = null;
    
    /**
     * 图片大小的下限，用于过滤小于该值的图片，单位byte
     */
    private int IMAGE_SIZE_MIN = 1024;
    
    /**
     * 爬取网页内容，返回Document，若链接无效，则返回null
     * @param url 传入的链接
     * @return 返回Document类型
     */
    private Document getContentByHtml(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            // e.printStackTrace();
            ErrorLog.errorLog("ERROR 04  Failed to connect to the URL: " + url + ".");
            return doc;
        }
        return doc;
    }
    
    /**
     * 获取网页标题，若无标题，则返回空串
     * @param doc 传入的网页内容
     * @return 网页标题
     */
    private String getTitle(Document doc) {
        return doc.title();
    }
    /**
     * 获取网页新闻内容,若无内容，则返回null
     * @param doc 传入的网页
     * @return 网页新闻内容
     */
    private Element getContent(Document doc) {
        return doc.getElementById("zoom");
    }
    
    /**
     * 获取网页新闻的图片链接
     * @param content 传入的网页新闻内容
     * @return 返回图片链接的列表
     */
    private LinkedList<String> getImageAddr(Element content) {
        Elements img = content.getElementsByTag("img");
        LinkedList<String> imageAddr = new LinkedList<String>();
        String s = null;
        for (Element e : img) {
            s = e.attr("abs:src");
            imageAddr.add(s);
        }
        return imageAddr;
    }
    
    /**
     * 根据传入的图片地址表，抓取网页上新闻的图片
     * @param imageAddr 图片地址链表
     * @param count 新的图片ID的起始，例如图片库末尾ID为123，则传入124
     */
    private int[] getImages(LinkedList<String> imageAddr, int imgID) {
        // http连接的返回代码
        int httpCode = 0;
        // 存储网络图片的网络地址
        String imgPath = null;
        // 存储网络图片的字节流
        byte[] imgByte = null;
        // 建立http连接
        HttpURLConnection connect = null;
        // 网络图片输入流
        InputStream inputStream = null;
        // 图片文件输出流
        FileOutputStream outputStream = null;
        // 图片文件路径
        String imgFilePath = null;
        // 定义一个数组存储图片ID，若图片抓取失败，则相应的图片链接的ID为-1，若图片抓取成功，则相应位置保存ID
        int[] imgIDs = new int[imageAddr.size()];
        // 用于记录数组的位置
        int arrayPos = 0;
        // 检测目录是否存在，不存在则新建目录
        if (imgDirPath != null) {
            File imgFileDir = new File(imgDirPath);
            if (!imgFileDir.exists()) {
                imgFileDir.mkdirs();
            }
        }
        
        for (Iterator<String> iter = imageAddr.iterator(); iter.hasNext(); ) {
            imgPath = iter.next();
            // 建立连接
            try {
                connect = (HttpURLConnection) new URL(imgPath).openConnection();
                connect.setRequestMethod("GET");
                connect.setReadTimeout(TIMEOUT);
                httpCode = connect.getResponseCode();
            } catch (IOException | ClassCastException e1) {
                // e1.printStackTrace();
                ErrorLog.errorLog("ERROR 05  Connection timeout or Error to change URLConnection to HttpURLConnection: " + imgPath + ".");
                // 图片抓取失败，该位置ID为-1
                imgIDs[arrayPos] = -1;
                arrayPos += 1;
                continue; // 若抛出错误，则跳过这一轮循环，进入下一轮
            }
            
            // 判断连接成功
            if (httpCode == 200) {
                try {
                    inputStream = connect.getInputStream();
                } catch (IOException e1) {
                    // e1.printStackTrace();
                    ErrorLog.errorLog("ERROR 06  Failed to connect to: " + imgPath + ".");
                    imgIDs[arrayPos] = -1;
                    arrayPos += 1;
                    continue;
                }
                imgByte = readImgStream(inputStream);
                // 判断是否大于预定义的图片的最小值
                if (imgByte.length > IMAGE_SIZE_MIN) {
                    imgFilePath = imgDirPath + Integer.toString(imgID) + ".jpg";
                    // 图片抓取成功，ID自加1，并将ID加入ID数组
                    imgIDs[arrayPos] = imgID;
                    imgID += 1;
                    arrayPos += 1;
                    try {
                        outputStream = new FileOutputStream(imgFilePath);
                        outputStream.write(imgByte);
                        outputStream.close();
                    } catch (IOException e) {
                        // e.printStackTrace();
                        ErrorLog.errorLog("ERROR 07  Failed to write file (resource from URL): " + imgPath + ".");
                    }
                }
                else { // 图片小于最小值，不抓取，图片ID为-1
                    imgIDs[arrayPos] = -1;
                    arrayPos += 1;
                }
            }
            else { // 连接失败，图片ID为-1
                imgIDs[arrayPos] = -1;
                arrayPos += 1;
            }
        }
        return imgIDs;
    }
    
    /**
     * 读取图片的字节流
     * @param inputStream 网络图片输入流
     * @return 返回图片文件字节数组
     */
    private byte[] readImgStream(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while((len = inputStream.read(buffer)) !=-1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            // e.printStackTrace();
            ErrorLog.errorLog("ERROR 08  Failed to read bytes (ContentArrangement.readImgStream).");
        }
        try {
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            // e.printStackTrace();
            ErrorLog.errorLog("ERROR 09  Failed to close stream (ContentArrangement.readImgStream).");
        }
        return outputStream.toByteArray();
    }

  
    /**
     * 判断文件是否存在，若存在，则在文件名后面加上序号后再测试，直到文件名唯一
     * @param name 传入待测试的文件名
     * @return 返回目录中唯一的文件名
     */
/*    
    private String makeDifferentFilename(String name) {
        for (int i = 2; (new File(name).exists()); ) {
            name = name + Integer.toString(i) + ")";
        }
        return name;
    }
*/    
    
    /**
     * 整理网页新闻内容，包括图片、新闻内容、新闻标题
     * 新闻内容为标签<div id="zoom">内的内容，其中的图片地址会替换成图片保存在本地的路径
     * @param url 链接地址
     * @param imgDirPath 图片的保存目录
     * @param textDirPath 新闻文本的保存路径
     * @param txtID 分配给下一个文本的ID
     * @param imgID 分配给下一个图片的ID
     * @return 返回可分配的下一个新闻内容的ID和下一个图片的ID
     */
    public int[] arrangeContent(String url, String imgDirPath, String textDirPath, int txtID, int imgID) {
        this.imgDirPath = imgDirPath;
        // 网页新闻内容的图片ID
        int[] imgIDs = null;
        // 返回用的ID
        int[] lastID = new int[2];
        // 最新的用于分配图片和文本ID的ID，用于记录函数的返回值
        lastID[0] = txtID;
        lastID[1] = imgID;
        String imgSrcAddr = null;
        Document doc = getContentByHtml(url);
        if (doc != null) {
            // 网页标题
            String title = getTitle(doc);
            // 网页新闻内容
            Element content = getContent(doc);
            // 若无内容，则返回
            if (content == null) {
                return lastID;
            }
            // 获取网页新闻内容里的图片地址
            LinkedList<String> imgAddrs = getImageAddr(content);
            if (imgAddrs.size() != 0) {
                imgIDs = getImages(imgAddrs, imgID);
                // 再次获取所有图片链接，并替换content内容中的图片链接为本地的图片链接
                Elements imgSrc = content.getElementsByTag("img");
                int cnt = 0; // ID数组计数器
                for (Element e : imgSrc) {
                    if (imgIDs[cnt] != -1) {
                        lastID[1] = imgIDs[cnt] + 1;
                    }
                    imgSrcAddr = imgDirPath + Integer.toString(imgIDs[cnt++]) + ".jpg";
                    e.attr("src", imgSrcAddr);
                }
            }
            // 新建保存路径，并将新闻内容保存到本地存储设备
            if (textDirPath != null) {
                File textFileDir = new File(textDirPath);
                if (!textFileDir.exists()) {
                    textFileDir.mkdirs();
                }
            }
            // 根据内容ID建立文本文件名
            String textAddr = textDirPath==null ? Integer.toString(lastID[0]) : textDirPath+Integer.toString(lastID[0]);
            // 创建字节流和字符流，将文本内容写入磁盘
            FileOutputStream fileOutputStream;
            BufferedWriter bufferedWriter;
            try {
                fileOutputStream = new FileOutputStream(textAddr);
            } catch (FileNotFoundException e) {
                // e.printStackTrace();
                ErrorLog.errorLog("ERROR 10  File not found: " + textAddr + ".");
                return lastID;
            }  
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(textAddr));
                lastID[0]++;
                bufferedWriter.write(title);
                bufferedWriter.newLine();
                bufferedWriter.write(content.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                fileOutputStream.close();
            } catch (IOException e) {
                // e.printStackTrace();
                ErrorLog.errorLog("ERROR 11  Failed to write text to file: " + textAddr + ".");
            }
        }
        return lastID;
    }
    

    
    /*
    public static void main(String[] a) {
        LinksGather link = new LinksGather();
        String url = "http://news.cqu.edu.cn";
        int depth = 1;
        LinkedList<String> links = new LinkedList<String>();
        links = link.getHyperLinksInWebsite(url, depth, url);
        ContentArrangement con = new ContentArrangement();
        String asd = null;
        int imgID = 1;
        for (Iterator<String> iter = links.iterator(); iter.hasNext(); ) {
            asd = iter.next();
            imgID = con.arrangeContent(asd, "imgpaththree\\", "textpaththree\\", imgID);
            System.out.println(asd);
        }
    }*/
    
    /*
    public static void main(String [] a) {
        //System.out.println(System.currentTimeMillis());
        String dirs = "aa\\";
        File f = new File(dirs);
      //  f.mkdirs();
        if (f.exists()) {
            System.out.println("yes");
        }
    }*/
    
    /*
    public static void main(String[] a) {
        ContentArrangement x = new ContentArrangement();
        String url = "http://news.cqu.edu.cn/news/article/article_71348.html";
        Element content = x.getContent(x.getContentByHtml(url));
        int[] y = x.getImages(x.getImageAddr(content), 10001);
        System.out.println(content);
        
        Elements img = content.getElementsByTag("img");
        int i = 0;
        String addr = "dewedwd";
        for (Element e : img) {
            e.attr("src", addr += Integer.toString(y[i++]));
        }
        
        System.out.println(content.toString());
    }*/
    
    /*
    public static void main(String[] a) {
        ContentArrangement as = new ContentArrangement();
        if (as.imgFilePath != null) {
        File file = new File(as.imgFilePath);
        if (!file.isDirectory()) {
            file.mkdir();
        }
        else {
            System.out.println("ssssssssssss");
        }
        }
    }*/
    
    /*
    public static void main(String[] s) throws MalformedURLException, IOException {
        HttpURLConnection connect = (HttpURLConnection) new URL("http://news.cqu.edu.cn/news/article/uploadfile/201511/20151124052706505.jpg").openConnection();
        connect.setRequestMethod("GET");
        connect.setReadTimeout(10000);
        if (connect.getResponseCode() == 200) {
            InputStream inputStream = connect.getInputStream();
            byte[] data = null;
            try {
                data = readStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if(data.length>(1024*10)){
              //  File file = new File(".\\20151124052706505.jpg");
               // file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(".\\20151124052706505.jpg");
                outputStream.write(data);
                System.err.println("图片下载成功");
                outputStream.close();
            }
        }
    }*/
    
    /*
    public static void main(String[] a) {
        ContentArrangement cont = new ContentArrangement();
        String url = "http://news.cqu.edu.cn/news/article/article_71281.html";
        Document doc = cont.getContentByHtml(url);
        if (doc != null) {
            
            System.out.println("Title: " + cont.getTitle(doc));
            System.out.println("Content: " + cont.getContent(doc));
            //Elements img = doc.getElementById("zoom").getElementsByTag("img");
            System.out.println(cont.getImageAddr(cont.getContent(doc)));
            //for (Element e : img) {
            //    String s = e.attr("src");
            //    System.out.println(s);
            //}
           // System.out.println(img.select("src"));
        }
    }*/
}
