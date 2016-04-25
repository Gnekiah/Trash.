package networkrobot;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * LinksGather类，用于爬取网页的超链接
 * 提供如下接口：
 * LinkedList<String> getHyperlinksInWebsite(String url, int depth)
 * LinkedList<String> getHyperlinksInWebsite(String url, int depth, String feature)
 * LinkedList<String> getCategoryHyperlinksList(String topUrl)
 * LinkedList<String> getHotnewsHyperlinksList(String topUrl)
 * LinkedList<String> getPhotonewsHyperlinksList(String topUrl)
 * @author DouBear
 *
 */
public class LinksGather {
    /**
     * 一个新闻链接划分的dev的class
     */
    private String newsTagClass = "linews";
    
    /**
     * 热点新闻的dev的class
     */
    private String hotnewsTagClass = "hot";
    
    /**
     * 每个子类别图片新闻的dev的class
     */
    private String photonewsTagClass = "liphoto";
    
    /**
     * 用于从给定的url链接抓取所有超链接，并返回
     * @param url 传入一个url超链接地址
     * @return 返回包含所有超链接内容的Elements类型
     */
    private Elements getAllHyperlinks(String url) {
        return getHyperlinks(url, null);
    }
    
    /**
     * 用于从给定的url链接中，从指定的标签类型tagClass中抓取所有网页
     * @param url 传入一个超链接
     * @param tagClass 传入标签类型
     * @return 返回包含所有超链接的Elements 
     */
    private Elements getHyperlinks(String url, String tagClass) {
        Document doc = null;
        Elements links = new Elements();
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            // e.printStackTrace();
            ErrorLog.errorLog("ERROR 12  Failed to connect to the URL: " + url + ".");
            return links;
        }
        links = tagClass==null ? doc.select("a[href]") : doc.getElementsByClass(tagClass).select("a[href]");  
        return links;
    }
    
    /**
     * 检查搜索出的超链接，并删除非法链接和重复链接
     * @param roundLinks 一层深度爬取的超链接
     * @param hyperLinks 所有合法超链接
     * @param feature 超链接路径过滤，将不是该路径下的超链接删除
     * @return 返回一层深度爬取的合法超链接链表
     */
    private LinkedList<String> checkoutLinks(LinkedList<String> roundLinks, LinkedList<String> hyperLinks, String feature) {
     // 用于记录剔除#后的链接列表，以及用于记录剔除重复链接后的链接列表，名字来源：sharp，repeat -->sharpeat
        LinkedList<String> eliminateSharpeat = new LinkedList<String>();
     // 剔除格式非法的超链接，同时将链接中含有#的链接删去#及后面的字符串
        // 删去#后的结果暂存在eliminateSharpeat中,完成所有#的剔除工作后，再将结果addAll到roundLinks中
        for (Iterator<String> iter = roundLinks.iterator(); iter.hasNext(); ) {
            String item = iter.next();
            if (!item.startsWith(feature)) {
                iter.remove();
            }
            if (item.contains("#")) {
                eliminateSharpeat.add(item.substring(0, item.indexOf('#')));
                iter.remove();
            }
        }
        // 将eliminateSharpeat添加到roundLinks中，并将eliminateSharpeat清空用于保存下一个临时数据
        roundLinks.addAll(eliminateSharpeat);
        eliminateSharpeat.clear();
        // 删除roundLinks中重复的链接
        // 将剔除结果暂存在eliminateSharpeat中
        for (String s: roundLinks) {
            if (Collections.frequency(eliminateSharpeat, s) < 1)
                eliminateSharpeat.add(s);
        }
        // 清空roundLinks，用于保存一轮循环最后得到的数据
        roundLinks.clear();
        // 删除eliminateSharpeat在hyperLinks中重复的超链接,并将结果暂存在roundLinks中
        // 完成比对后将结果addAll到hyperLinks中
        for (String s: eliminateSharpeat) {
            if (Collections.frequency(hyperLinks, s) < 1)
                roundLinks.add(s);
        }
        eliminateSharpeat.clear();
        return roundLinks;
    }
    
    /**
     * 根据传入的链接获取网页上的pages
     * 用于提取新闻网站子模块下的每个页码链接
     * @param url 传入的子版面链接的首页
     * @return 返回所有该版面的页码链接的链表，若抓取失败，则返回空链表
     */
    private LinkedList<String> getPages(String url) {
        Document doc = null;
        Elements links = new Elements();
        LinkedList<String> hyperLinks = new LinkedList<String>();
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            // e.printStackTrace();
            ErrorLog.errorLog("ERROR 13  Failed to connect to the URL: " + url + ".");
            return hyperLinks;
        }
        links = doc.getElementsByClass("pages");
        links = links.select("[value]");
        for (Element link : links) {
            hyperLinks.add(link.attr("abs:value"));
        }
        return hyperLinks;
    }
    
    /**
     * 广度优先
     * 根据给定的url链接爬取链接，默认根据"http"剔除非法URL
     * 每一轮爬取都会遍历上一轮得到的并且没有被爬过的链接，并将一轮爬取的链接保存
     * 爬取深度为depth
     * @param url 给定以爬取入口
     * @param depth 爬虫爬取的深度
     * @return 返回String链表，链表中存储符合要求的链接
     */
    public LinkedList<String> getHyperlinksInWebsite(String url, int depth) {
        return getHyperlinksInWebsite(url, depth, "http");
    }
    
    /**
     * 广度优先
     * 根据给定的url链接爬取链接，并剔除不符合要求的链接
     * 每一轮爬取都会遍历上一轮得到的并且没有被爬过的链接，并将一轮爬取的链接保存
     * 爬取深度为depth
     * @param url 给定以爬取入口
     * @param depth 爬虫爬取的深度
     * @param feature 用于剔除不符合要求的链接
     * @return 返回String链表，链表中存储符合要求的链接
     */
    public LinkedList<String> getHyperlinksInWebsite(String url, int depth, String feature) {
        // 用于保存抓取的所有符合要求的链接
        LinkedList<String> hyperLinks = new LinkedList<String>();
        // 用于新一轮遍历的与hyperLinks不重复的超链接
        LinkedList<String> loopLinks = new LinkedList<String>();
        // 用于保存一轮遍历后得到的超链接
        LinkedList<String> roundLinks = new LinkedList<String>();
        
        // 用于保存爬取一个链接得到的超链接
        Elements links = null;
        hyperLinks.add(url);
        loopLinks.add(url);
        
        for (int i = 0; i < depth; i++) {
            // 根据整个list爬取每个链接中的超链接
            for (Iterator<String> iter = loopLinks.iterator(); iter.hasNext(); ) {
                // 爬取一个链接的超链接
                links = getAllHyperlinks(iter.next());
                for (Element link : links) {
                    roundLinks.add(link.attr("abs:href"));
                }
            }
            // 调用checkoutLinks函数将非法链接和重复链接剔除
            roundLinks = checkoutLinks(roundLinks, hyperLinks, feature);
            // 清空loopLinks
            loopLinks.clear();
            // 将筛选后的超链接加入loopLinks和hyperLinks
            loopLinks.addAll(roundLinks);
            hyperLinks.addAll(roundLinks);
            // 清空roundLinks和eliminateSharpeat
            roundLinks.clear();
            
        }
        return hyperLinks;
    }
    
    /**
     * 抓取一个子类别的超链接
     * @param topUrl 子类别的首页链接
     * @return 返回子类别的新闻条目超链接链表
     */
    public LinkedList<String> getCategoryHyperlinksList(String topUrl) {
        // 用于保存抓取的所有符合要求的链接
        LinkedList<String> hyperLinks = new LinkedList<String>();
        // 抓取的页码保存的链表
        LinkedList<String> pagesList = new LinkedList<String>();
        Elements links = null;
        // 抓取一个栏目下的所有超链接
        pagesList = getPages(topUrl);
        for (String urlLink: pagesList) {
            links = getHyperlinks(urlLink, newsTagClass);
            for (Element link : links) {
                hyperLinks.add(link.attr("abs:href"));
            }
        }
        // 删除含有"list"字符串的链接（即删除页码的超链接）
        for (Iterator<String> iter = hyperLinks.iterator(); iter.hasNext(); ) {
            if (iter.next().contains("list")) {
                iter.remove();
            }
        }
        return hyperLinks;
    }
    
    /**
     * 抓取热点新闻的链接
     * @param url 新闻子类的任意一条有效链接
     * @return 返回热点新闻的链接链表
     */
    public LinkedList<String> getHotnewsHyperlinksList(String url) {
        // 用于保存抓取的所有符合要求的链接
        LinkedList<String> hyperLinks = new LinkedList<String>();
        Elements links = null;
        links = getHyperlinks(url, hotnewsTagClass);
        for (Element link : links) {
            hyperLinks.add(link.attr("abs:href"));
        }
        // 因为热点新闻类标签的第一的链接是“点击查看更多”，而这条连接是无效的，故删去
        hyperLinks.removeFirst();
        return hyperLinks;
    }
    
    /**
     * 抓取一个子类别的图片新闻下的链接
     * @param topUrl 子类别的链接地址
     * @return 返回图片新闻的链接链表
     */
    public LinkedList<String> getPhotonewsHyperlinksList(String topUrl) {
        // 用于保存抓取的所有符合要求的链接
        LinkedList<String> hyperLinks = new LinkedList<String>();
        Elements links = null;
        links = getHyperlinks(topUrl, photonewsTagClass);
        for (Element link : links) {
            hyperLinks.add(link.attr("abs:href"));
        }
        // 删除重复的链接； 由于图片新闻的文字与图片都有一条链接，故需要删除重复链接
        for (int index = hyperLinks.size(); index > 0; index--) {
            hyperLinks.remove(--index);
        }
        return hyperLinks;
    }
    
    
    
    
    /*
    public static void main(String[] a) {
        LinksGather l = new LinksGather();
        LinkedList<String> s = new LinkedList<String>();
        String url = "http://news.cqu.edu.cn/news/article/list.php?catid=8";
        s = l.getPhotonewsHyperlinksList(url);
        for (String b: s) {
            System.out.println(b);
        }
    }*/
    
    /*
    public static void main(String[] a) {
        String url = "http://news.cqu.edu.cn/news/article/list.php?catid=8";
        LinksGather l = new LinksGather();
        LinkedList<String> s = new LinkedList<String>();
        s = l.getCategoryHyperlinksList(url);
        for (String b: s) {
            System.out.println(b);
        }
    }*/
    
    /*
    public static void main(String[] a) {
        GetLinkAddresses getLink = new GetLinkAddresses();
        LinkedList<String> hyperLinks = new LinkedList<String>();
        String url = "http://news.cqu.edu.cn/";
        int depth = 3;
        String feature = url;
        hyperLinks = getLink.getHyperLinksInWebsite(url, depth, feature);
        System.out.println(hyperLinks.size());
        for (Iterator<String> iterHyper = hyperLinks.iterator(); iterHyper.hasNext(); ) {
            System.out.println(iterHyper.next());
            
        }
    }*/
    
    /*
    public static void main(String[] a) {
        GetLinkAddresses getLink = new GetLinkAddresses();
        String url = "http://news.cqu.edu.cn";
        Elements links = getLink.getAllHyperlinkAddresses(url);
        if (links.size() == 0) {
            System.out.println("links is empty");
        }
        else {
            System.out.println(links.size());
            for (Element link : links) {
                System.out.println(link.attr("href"));
            }
        }
        
    }*/

   /*
    public static void main(String[] a) {
        LinkedList<String> x = new LinkedList<String>();
        LinkedList<String> y = new LinkedList<String>();
        x.add("x1");
        x.add("x2");
        x.add("x3");
        x.add("x4");
        x.add("x5");
       // for (Iterator<String> iter = x.iterator(); iter.hasNext(); ) {
          //  y.add(iter.next());
          //  if (iter.next().startsWith("x3")) {
          //      iter.remove();
       //     }
      //  }
        x.clear();
        System.out.println("print x");
        System.out.println(x);
        System.out.println("print y");
        System.out.println(y);
    }*/
    
}
