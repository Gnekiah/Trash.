package networkrobot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

/**
 * README
 * 
 * 广度优先爬虫，提供一个函数：
 * void creeper(CategoryData topUrlData)
 * 传入一个参数：新闻的类别
 * 我们的爬虫先生会爬取该类别下的“图片新闻”，“热点新闻”，“新闻”，并保存在新闻类别下定义好的路径下
 * 爬虫先生在windows 7 环境下运行正常
 * 若需要在Linux平台运行，请将CategoryData类下路径的双反斜杠换成斜杠
 * @author DouBear
 *
 */
public class BreadthFirstRobot {
    /**
     * 用于保存已经爬取过的超链接集合的文件
     */
    private final String HYPERLINKSLIST = "D:/creepersource/hyperlinkslist.lst";
    
    /**
     * 计数器，爬取链接的数量
     */
    public int count;
    
    /**
     * 爬取内容成功的数量
     */
    public int txtcount;
    
    /**
     * 爬取到的图片数量
     */
    public int imgcount;
    
    /**
     * 记录抓取链接的历史记录
     */
    public LinkedList<String> historyList = new LinkedList<String>();
    
    /**
     * 构造函数，用于初始化读取历史链接记录
     */
    private BreadthFirstRobot() {
        count = 0;
        txtcount = 0;
        imgcount = 0;
        historyList = readHyperlinksList();
    }
    
    /**
     * 读取已爬取过的超链接记录文件
     * @return 返回超链接链表
     */
    private LinkedList<String> readHyperlinksList() {
        LinkedList<String> hyperLinks = new LinkedList<String>();
        BufferedReader bufferReader;
        File file = new File(HYPERLINKSLIST);
        if (file.exists()) {
            try {
                bufferReader = new BufferedReader(new FileReader(file));
                String s = null;
                while((s = bufferReader.readLine()) != null)
                    hyperLinks.add(s);
                bufferReader.close();
            } catch (IOException e) {
                // e.printStackTrace();
                ErrorLog.errorLog("ERROR 01  Failed to read history file: " + HYPERLINKSLIST + ".");
            }
        }
        return hyperLinks;
    }
    
    /**
     * 将新的链接写入记录文件
     * @param linkedList 待写入的数据
     */
    private void writeHyperlinksList(LinkedList<String> linkedList) {
        File file = new File(HYPERLINKSLIST);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // e.printStackTrace();
                ErrorLog.errorLog("ERROR 02  Failed to create file: " + HYPERLINKSLIST + ".");
                return;
            }
        }
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            for (String s: linkedList) {
                bufferedWriter.write(s);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            // e.printStackTrace();
            ErrorLog.errorLog("ERROR 03  Failed to write history information to: " + HYPERLINKSLIST + ".");
        }
    }
    
    /**
     * 将爬取的一个类别下的链接进行去重，并将新链接写入日志
     * @param linkedList 爬取的新闻链接
     * @return 返回未爬过的新链接
     */
    private LinkedList<String> removeRepeatLinks(LinkedList<String> linkedList) {
     // 用于保存删去重复链接的新链接
        LinkedList<String> newList = new LinkedList<String>();
     // 对读取的一个类别的网页链接进行去重处理
        if (historyList.size() > 0) {
            for (String s: linkedList) {
                if (Collections.frequency(historyList, s) < 1)
                    newList.add(s);
            }
        }
        else {
            newList.addAll(linkedList);
        }
        // 将最新的链接写入记录文件
        writeHyperlinksList(newList);
        return newList;
    }
    
    /**
     * 根据传入的新闻子分类的顶层链接获取分类下面的所有链接的内容和图片
     * @param topUrlData 传入的顶层链接
     */
    public void creeper(CategoryData topUrlData) {
        LinksGather linksGather = new LinksGather();
        ContentArrangement contentArrangement = new ContentArrangement();
        // 文本和图片的首位ID
        int[] ID = new int[2];
        ID[0] = ID[1] = 1;
        // 用于保存抓取的新闻链接
        LinkedList<String> linkedList = new LinkedList<String>();
        // 从新闻类别的首页获取所有该类别的链接
        linkedList = linksGather.getCategoryHyperlinksList(topUrlData.url);
        // 去重并将新链接写入日志
        linkedList = removeRepeatLinks(linkedList);
        // 爬取网页新闻内容和图片
        for (String s: linkedList) {
            System.out.println((++count) + ": " + s );
            ID = contentArrangement.arrangeContent(s, topUrlData.imgCatPath, topUrlData.txtCatPath, ID[0], ID[1]);
        }
        txtcount += ID[0] - 1;
        imgcount += ID[1] - 1;
        // 读取一个新闻分类下的热点新闻内容和图片
        linkedList.clear();
        linkedList = linksGather.getHotnewsHyperlinksList(topUrlData.url);
        // 去重并将新链接写入日志
        linkedList = removeRepeatLinks(linkedList);
        // 重置图片ID
        ID[0] = ID[1] = 1;
        for (String s: linkedList) {
            System.out.println((++count) + ": " + s );
            ID = contentArrangement.arrangeContent(s, topUrlData.imgHotPath, topUrlData.txtHotPath, ID[0], ID[1]);
        }
        txtcount += ID[0] - 1;
        imgcount += ID[1] - 1;
        // 读取一个新闻分类下的图片新闻的内容和图片
        linkedList.clear();
        linkedList = linksGather.getPhotonewsHyperlinksList(topUrlData.url);
        // 去重并将新链接写入日志
        linkedList = removeRepeatLinks(linkedList);
        // 重置图片ID
        ID[0] = ID[1] = 1;
        for (String s: linkedList) {
            System.out.println((++count) + ": " + s );
            ID = contentArrangement.arrangeContent(s, topUrlData.imgPhoPath, topUrlData.txtPhoPath, ID[0], ID[1]);
        }
        txtcount += ID[0] - 1;
        imgcount += ID[1] - 1;
    }
    
    
    public static void main(String[] a) {
        System.out.println("Creeper: \"I'm creeper, I'm going to grab Steven....\"");
        long startTime = System.currentTimeMillis();
        Category category = new Category();
        BreadthFirstRobot robot = new BreadthFirstRobot();
        LinkedList<CategoryData> data = category.getAllCategory();
        robot.creeper(data.getFirst());
        for (CategoryData c: data) {
            robot.creeper(c);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("历史爬取链接数量: " + robot.historyList.size());
        System.out.println("本次爬取新链接数量: " + robot.count);
        System.out.println("本次爬取新内容数量: " + robot.txtcount);
        System.out.println("本次爬取新图片数量: " + robot.imgcount);
        System.out.println("本次爬取用时: " + (endTime - startTime) / 1000.0 + " sec");
    }
}
