package networkrobot;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.filechooser.FileFilter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class BreadthFirstRobot {
    
    public void creeper(String topUrl, String typeName) {
        LinksGather linksGather = new LinksGather();
        ContentArrangement contentArrangement = new ContentArrangement();
        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList = linksGather.getCategoryHyperlinksList(topUrl);
        int imgID = 1;
        for (String s: linkedList) {
            System.out.println("1:" + imgID+ ": " + s );
            imgID = contentArrangement.arrangeContent(s, ".\\"+typeName+"imgDirCat\\", ".\\"+typeName+"textDirCat\\", imgID);
        }
        linkedList.clear();
        linkedList = linksGather.getHotnewsHyperlinksList(topUrl);
        imgID = 1;
        for (String s: linkedList) {
            System.out.println("2:" + imgID + ": " + s);
            imgID = contentArrangement.arrangeContent(s, ".\\"+typeName+"imgDirHot\\", ".\\"+typeName+"textDirHot\\", imgID);
        }
        linkedList.clear();
        linkedList = linksGather.getPhotonewsHyperlinksList(topUrl);
        imgID = 1;
        for (String s: linkedList) {
            System.out.println("3:" + imgID + ": " + s);
            imgID = contentArrangement.arrangeContent(s, ".\\"+typeName+"imgDirPho\\", ".\\"+typeName+"textDirPho\\", imgID);
        }
        linkedList.clear();
    }
    
    public static void main(String[] args) {
        LinkedList<CategoryData> data = new LinkedList<CategoryData>();
        data.add(new CategoryData("党团建设", "http://news.cqu.edu.cn/news/article/list.php?catid=52"));
        data.add(new CategoryData("科研", "http://news.cqu.edu.cn/news/article/list.php?catid=46"));
        data.add(new CategoryData("成果", "http://news.cqu.edu.cn/news/article/list.php?catid=51"));
        data.add(new CategoryData("学术", "http://news.cqu.edu.cn/news/article/list.php?catid=47"));
        data.add(new CategoryData("招生就业", "http://news.cqu.edu.cn/news/article/list.php?catid=53"));
        data.add(new CategoryData("教学", "http://news.cqu.edu.cn/news/article/list.php?catid=48"));
        data.add(new CategoryData("人物", "http://news.cqu.edu.cn/news/article/list.php?catid=49"));
        data.add(new CategoryData("风采", "http://news.cqu.edu.cn/news/article/list.php?catid=50"));
        data.add(new CategoryData("交流", "http://news.cqu.edu.cn/news/article/list.php?catid=54"));
        data.add(new CategoryData("视点", "http://news.cqu.edu.cn/news/article/list.php?catid=66"));
        data.add(new CategoryData("新闻会客厅", "http://news.cqu.edu.cn/news/article/list.php?catid=57"));
        data.add(new CategoryData("人物志", "http://news.cqu.edu.cn/news/article/list.php?catid=59"));
        data.add(new CategoryData("风采录", "http://news.cqu.edu.cn/news/article/list.php?catid=55"));
        data.add(new CategoryData("广角镜", "http://news.cqu.edu.cn/news/article/list.php?catid=60"));
        data.add(new CategoryData("大家说", "http://news.cqu.edu.cn/news/article/list.php?catid=56"));
        data.add(new CategoryData("聚光灯", "http://news.cqu.edu.cn/news/article/list.php?catid=58"));
        data.add(new CategoryData("人事招聘", "http://news.cqu.edu.cn/news/article/list.php?catid=43"));
        data.add(new CategoryData("学术", "http://news.cqu.edu.cn/news/article/list.php?catid=8"));
        data.add(new CategoryData("文体", "http://news.cqu.edu.cn/news/article/list.php?catid=41"));
        data.add(new CategoryData("公示", "http://news.cqu.edu.cn/news/article/list.php?catid=45"));
        data.add(new CategoryData("社团", "http://news.cqu.edu.cn/news/article/list.php?catid=9"));
        data.add(new CategoryData("招投标", "http://news.cqu.edu.cn/news/article/list.php?catid=42"));
        data.add(new CategoryData("国际", "http://news.cqu.edu.cn/news/article/list.php?catid=11"));
        data.add(new CategoryData("国内", "http://news.cqu.edu.cn/news/article/list.php?catid=12"));
        data.add(new CategoryData("民生", "http://news.cqu.edu.cn/news/article/list.php?catid=13"));
        data.add(new CategoryData("校友", "http://news.cqu.edu.cn/news/article/list.php?catid=63"));
        data.add(new CategoryData("校历史", "http://news.cqu.edu.cn/news/article/list.php?catid=64"));

        BreadthFirstRobot robot = new BreadthFirstRobot();
        for (CategoryData c: data) {
            robot.creeper(c.url, c.name);
        }      
    }
    /*
    public static void main(String[] a) {
        File dir = new File(".\\");
        File[] list = dir.listFiles();
        int size = 0;
        for(File f: list) {
            if (f.getName().contains("img")) {
                size += f.list().length;
            }
        }
        // text 36750
        // ing  37017
        System.out.println(size);
    }*/
}
