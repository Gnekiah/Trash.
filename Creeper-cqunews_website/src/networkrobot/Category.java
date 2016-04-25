package networkrobot;

import java.util.LinkedList;

/**
 * 记录一个新闻类别的属性
 * @return 返回新闻类别属性的链表
 * @author DouBear
 *
 */
public class Category {

    public LinkedList<CategoryData> getAllCategory() {
        LinkedList<CategoryData> data = new LinkedList<CategoryData>();
        data.add(new CategoryData("党团建设", "http://news.cqu.edu.cn/news/article/list.php?catid=52", 52));
        data.add(new CategoryData("科研", "http://news.cqu.edu.cn/news/article/list.php?catid=46", 46));
        data.add(new CategoryData("成果", "http://news.cqu.edu.cn/news/article/list.php?catid=51", 51));
        data.add(new CategoryData("学术", "http://news.cqu.edu.cn/news/article/list.php?catid=47", 47));
        data.add(new CategoryData("招生就业", "http://news.cqu.edu.cn/news/article/list.php?catid=53", 53));
        data.add(new CategoryData("教学", "http://news.cqu.edu.cn/news/article/list.php?catid=48", 48));
        data.add(new CategoryData("人物", "http://news.cqu.edu.cn/news/article/list.php?catid=49", 49));
        data.add(new CategoryData("风采", "http://news.cqu.edu.cn/news/article/list.php?catid=50", 50));
        data.add(new CategoryData("交流", "http://news.cqu.edu.cn/news/article/list.php?catid=54", 54));
        data.add(new CategoryData("视点", "http://news.cqu.edu.cn/news/article/list.php?catid=66", 66));
        data.add(new CategoryData("新闻会客厅", "http://news.cqu.edu.cn/news/article/list.php?catid=57", 57));
        data.add(new CategoryData("人物志", "http://news.cqu.edu.cn/news/article/list.php?catid=59", 59));
        data.add(new CategoryData("风采录", "http://news.cqu.edu.cn/news/article/list.php?catid=55", 55));
        data.add(new CategoryData("广角镜", "http://news.cqu.edu.cn/news/article/list.php?catid=60", 60));
        data.add(new CategoryData("大家说", "http://news.cqu.edu.cn/news/article/list.php?catid=56", 56));
        data.add(new CategoryData("聚光灯", "http://news.cqu.edu.cn/news/article/list.php?catid=58", 58));
        data.add(new CategoryData("人事招聘", "http://news.cqu.edu.cn/news/article/list.php?catid=43", 43));
        data.add(new CategoryData("学术", "http://news.cqu.edu.cn/news/article/list.php?catid=8", 8));
        data.add(new CategoryData("文体", "http://news.cqu.edu.cn/news/article/list.php?catid=41", 41));
        data.add(new CategoryData("公示", "http://news.cqu.edu.cn/news/article/list.php?catid=45", 45));
        data.add(new CategoryData("社团", "http://news.cqu.edu.cn/news/article/list.php?catid=9", 9));
        data.add(new CategoryData("招投标", "http://news.cqu.edu.cn/news/article/list.php?catid=42", 42));
        data.add(new CategoryData("国际", "http://news.cqu.edu.cn/news/article/list.php?catid=11", 11));
        data.add(new CategoryData("国内", "http://news.cqu.edu.cn/news/article/list.php?catid=12", 12));
        data.add(new CategoryData("民生", "http://news.cqu.edu.cn/news/article/list.php?catid=13", 13));
        data.add(new CategoryData("校友", "http://news.cqu.edu.cn/news/article/list.php?catid=63", 63));
        data.add(new CategoryData("校历史", "http://news.cqu.edu.cn/news/article/list.php?catid=64", 64));
        return data;    
    }
}

