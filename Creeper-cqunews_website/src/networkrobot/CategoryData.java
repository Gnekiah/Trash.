package networkrobot;

/**
 * 用于记录新闻网子栏目的名字及其对应的链接和id
 * @author DouBear
 *
 */
public class CategoryData {
    public int id;
    public String name = null;
    public String url = null;
    public String imgCatPath = null;
    public String txtCatPath = null;
    public String imgHotPath = null;
    public String txtHotPath = null;
    public String imgPhoPath = null;
    public String txtPhoPath = null;
    public CategoryData(String name, String url, int id) {
        this.name = name;
        this.url = url;
        this.id = id;
        this.imgCatPath = "D:/creepersource/imgs/" + id + "/cat/";
        this.txtCatPath = "D:/creepersource/news/" + id + "/cat/";
        this.imgHotPath = "D:/creepersource/imgs/" + id + "/hot/";
        this.txtHotPath = "D:/creepersource/news/" + id + "/hot/";
        this.imgPhoPath = "D:/creepersource/imgs/" + id + "/pho/";
        this.txtPhoPath = "D:/creepersource/news/" + id + "/pho/";
    }
}
