package networkrobot;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * ContentFilter类，用于筛选符合指定规则的超链接
 * @author DouBear
 *
 */
public class ContentFilter {
    /**
     * 超链接起始部分匹配筛选，指定一个超链接的起始规则，返回符合这个规则的超链接
     * @param hyperLinksList 传入超链接链表
     * @param feature 指定超链接的规则
     * @return 返回符合指定规则的超链接链表
     */
    public LinkedList<String> filterByFeature(LinkedList<String> hyperLinksList, String feature) {
        LinkedList<String> hyperLinks = new LinkedList<String>();
        String tmp;
        for (Iterator<String> iter = hyperLinksList.iterator(); iter.hasNext(); ) {
            tmp = iter.next();
            if (tmp.startsWith(feature)) {
                hyperLinks.add(tmp);
            }
        }
        return hyperLinks;
    }
    
    /*
    public LinkedList<String> filterInvalidLinks(LinkedList<String> hyperLinksList) {
        LinkedList<String> hyperLinks = new LinkedList<String>();
        String tmp;
        for (Iterator<String> iter = hyperLinksList.iterator(); iter.hasNext(); ) {
            tmp = iter.next();
            
        }
        return hyperLinks;
    }*/
    /*
    public static void main(String[] a) {
        LinksGather l = new LinksGather();
        LinkedList<String> hyperLinks = new LinkedList<String>();
        String url = "http://news.cqu.edu.cn/";
        int depth = 1;
        //String feature = "http://";
        hyperLinks = l.getHyperLinksInWebsite(url, depth);
        ContentFilter f = new ContentFilter();
        System.out.println("length of hyperlinks: " + hyperLinks.size());
        //feature = "http://news.cqu.edu.cn/news/article/";
        //hyperLinks = f.filterByFeature(hyperLinks, feature);
        System.out.println("length of latter hyperlinks: " + hyperLinks.size());
    }*/

}
