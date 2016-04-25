package creeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import networkrobot.Category;
import networkrobot.CategoryData;

/**
 * IO操作类，用于对servlet的请求的回应，提供如下函数
 * String getText(String type, String tab, String id) 获取链接指向的页面内容
 * String getIndex() 获取主页
 * String getPage(String type, String page) 获取分页 
 * String getSearch(String keywords)  获取检索结果
 * String notFound(String url)  当链接失效的返回页
 * @author DouBear
 *
 */
public class TextReader {
    
    /**
     * 一页显示的链接数量
     */
    private int ITEMS_QUANTITY = 70;
    
    private IndexSearch index = null;
    /**
     * 初始化索引
     */
    TextReader() {
        index = new IndexSearch(); 
    }
    
    /**
     * 将标题整合成html格式的head
     * @param title 网页标题
     * @return html格式的head
     */
    private String getHead(String title) {
        return "<!DOCTYPE html> <html> <head> "
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"> "
                + "<title>" + title + "</title> </head>";
    }
    
    /**
     * 整合网页的body
     * @param content 传入的body内容
     * @return html格式的body
     */
    private String getBody(String content) {
        return "<body> " + content + " </body> </html>";
    }
    
    /**
     * 获取新闻内容，返回一个html格式的String字符串
     * @param type 某个分类
     * @param tab 分类下的属性，有“所有新闻”、“热点新闻”、“图片新闻”
     * @param id 分类下的ID
     * @return html格式的String字符串
     */
    public String getText(String type, String tab, String id) {
        // 返回一个html内容的String
        String revert = null;
        // html的head
        String head = null;
        // html的body
        String body = null;
        BufferedReader bufferReader;
        // 缓冲区
        char[] s = new char[1024*1024+1];
        // 新闻内容的资源路径
        String txtPath = "D:/creepersource/news/" + type + "/" + tab + "/" + id;
        File file = new File(txtPath);
        // 若资源存在则执行读取操作，否则返回一个Not Found提示
        if (file.exists()) {
            try {
                bufferReader = new BufferedReader(new FileReader(file));
                // 文本存储的格式为：第一行是标题，后面全部是内容
                head = bufferReader.readLine();
                int cnt = bufferReader.read(s, 0, 1024*1024);
                bufferReader.close();
                body = String.valueOf(s, 0, cnt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            return notFound("/" + type + "/" + tab + "/" + id);
        }
        body = "<div style=\"width:1100px; border:2px solid #f00\">" + body + getIndexList() + "</div>";
        revert = getHead(head) + getBody(body);
        return revert;
    }
    
    /**
     * 获取主页html内容
     * @return 返回主页html内容
     */
    public String getIndex() {
        String revert = null;
        revert = "<h1 align=\"center\">" + "Creeper - cqu news website" + "</h1>"
                + "" + getIndexList();
        revert = getHead("Creeper - cqu news website") + getBody(revert);
        return revert;
    }
    
    /**
     * 获取栏目导航
     * @return 栏目html的String
     */
    private String getIndexList() {
        String list = "<div style=\"position:absolute; top:30px; left:85%; border:2px solid #ffc0cb\">";
        list += "<div><form>Search:<input type=\"text\" name=\"search\"/><input type=\"submit\" value=\"Submit\"/></form></div>";
        LinkedList<CategoryData> data = new Category().getAllCategory();
        for (CategoryData cate: data) {
            list = list + "<h2> <li> <a href=\"" + "/Creeper-cqunews_website/CreeperServlet?sh=page&type="
                    + Integer.toString(cate.id) + "&page=1\" title=\"" + cate.name + "\"target=\"_blank\">"
                    + cate.name + "</a> </li> </h2>";
        }
        list += "</div>";
        return list;
    }
    
    /**
     * 获取不同页码的目录
     * @param type 新闻网类型
     * @param page 第page页
     * @return 返回页面结果
     */
    public String getPage(String type, String page) {
        // 资源目录的路径
        String txtPath = "D:/creepersource/news/" + type + "/cat/";
        // int类型的page
        int ipage;
        // HTML格式的String
        String revert = null;
        // 内容的标题
        String title = null;
        // 若page不为数字，则跳转not found页面
        try { ipage = Integer.parseInt(page); }
        catch (NumberFormatException e) { return notFound("/" + type + "/cat/" + page); }
        
        revert = "<ol start=\"" + ((ipage-1)*ITEMS_QUANTITY+1) +"\"> ";
        // 将目录下存在的属于该page的条目列到html文本中
        for (int i = (ipage-1) * ITEMS_QUANTITY + 1; i < ipage * ITEMS_QUANTITY + 1; i++) {
            if ((new File(txtPath + Integer.toString(i))).exists()) {
                
                // 读取内容的标题
                File file = new File(txtPath + Integer.toString(i));
                BufferedReader bufferReader;
                try {
                    bufferReader = new BufferedReader(new FileReader(file));
                    title = bufferReader.readLine();
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    title = "no title";
                }
                // 将链接写入String
                revert = revert + "<li> <a href=\"" 
                        + "/Creeper-cqunews_website/CreeperServlet?sh=txt&type=" + type + "&tab=cat&id=" + i 
                        + "\" title=\"" + title  + "\" target=\"_blank\">" + title +"</a> </li> ";
            }
        }
        revert += "</ol>";
        if (Integer.valueOf(page)-1 < 1) {
            revert += "<li> <a href=\""+ "/Creeper-cqunews_website/CreeperServlet?sh=page&type=" + type + "&page=" + (Integer.valueOf(page)+1) 
                    + "\">下一页 </a></li>";
        }
        else {
            revert += "<li> <a href=\"" + "/Creeper-cqunews_website/CreeperServlet?sh=page&type=" + type + "&page=" + (Integer.valueOf(page)-1) 
                    + "\">上一页</a>";
            revert += "<a href=\"" + "/Creeper-cqunews_website/CreeperServlet?sh=page&type=" + type + "&page=" + (Integer.valueOf(page)+1) 
                    + "\">下一页</a> </li>";
        }
        revert = "<div style=\"width:1100px; border:2px solid #f00\">" + revert + getIndexList() + "</div>";  
        revert = getHead(title) + getBody(revert);
        return revert;
    }
    
    /**
     * 根据关键字从本地内容中搜索并返回搜索结果
     * @param keywords 关键字
     * @return 返回搜索结果
     */
    public String getSearch(String keywords) {
        String revert = null;
        String title = null;
        String[] split = null;
        LinkedList<String> path = index.doSearch(keywords);
        if (path.size() == 0)
            return notFound(keywords);
        
        revert = "<ol start=1> ";
        
        for (String txtPath: path) {
            // 读取内容的标题
            File file = new File(txtPath);
            BufferedReader bufferReader;
            try {
                bufferReader = new BufferedReader(new FileReader(file));
                title = bufferReader.readLine();
                bufferReader.close();
            } catch (IOException e) {
                e.printStackTrace();
                title = "no title";
            }
            split = txtPath.split("\\\\"); // 该死的巨硬该死的温斗屎，真是反人类的设计为毛要用四个反斜杠我擦，折腾了我大半个下午他喵的度娘谷歌都要死了终于在stackoverflow上找到原因。。。。。。。。。。。。。。。
            if (split.length != 6) {
                return notFound(keywords);
            }
            // 将链接写入String
            revert = revert + "<li> <a href=\"" 
                    + "/Creeper-cqunews_website/CreeperServlet?sh=txt&type=" + split[3] + "&tab=" + split[4] + "&id=" + split[5]
                    + "\" title=\"" + title  + "\" target=\"_blank\">" + title +"</a> </li> ";
        }
        revert += "</ol>";
        revert = "<div style=\"width:1100px; border:2px solid #f00\">" + revert + getIndexList() + "</div>";  
        revert = getHead(title) + getBody(revert);
        return revert;
    }
    
    /**
     * 当链接无效时返回not found页面
     * @param url 无效的网页链接
     * @return html格式的String
     */
    public String notFound(String url) {
        String revert = "<h1 align=\"center\">You fall out of the world.</h1>";
        revert += "<p>" + url + "</p>";
        revert = getHead("Not Found 404") + getBody(revert);
        return revert;
    }
}

