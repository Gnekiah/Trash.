package networkrobot;

/**
 * 用于记录新闻网子栏目的名字及其对应的链接
 * @author DouBear
 *
 */
public class CategoryData {
    public String name = null;
    public String url = null;
    public CategoryData(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
