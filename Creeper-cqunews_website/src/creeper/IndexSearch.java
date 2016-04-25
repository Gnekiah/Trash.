package creeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 检索类，对文档建立索引，并将索引保存在指定目录
 * 提供一个函数用于检索：
 * LinkedList<String> doSearch(String querystr)
 * 输入关键字，返回包含该关键字的文本所在路径的String链表
 * @author DouBear
 *
 */
public class IndexSearch {
    
    /**
     * 用于检索的资源目录 
     */
    private final String SOURCE_DIR = "D:/creepersource/news/";
    
    /**
     * 建立的索引所保存的目录
     */
    private final String INDEX_DIR = "D:/creepersource/index/";
    
    StandardAnalyzer analyzer;
    Directory index; 
    
    /**
     * 在构造函数中初始化，若未建立索引，则建立，否则直接从目录读取索引
     */
    IndexSearch() {
        analyzer = new StandardAnalyzer();
        if (!new File(INDEX_DIR).exists()) {
            createIndex();
            return;
        }
        try {
            index = FSDirectory.open(FileSystems.getDefault().getPath(INDEX_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 向索引添加新条目
     * @param content 文本内容
     * @param path 路径信息
     * @return 返回Document类型用于向index添加内容
     */
    private Document addDoc(String content, String path) {
        Document doc = new Document();
        doc.add(new TextField("content", content, Field.Store.YES));
        doc.add(new StringField("path", path, Field.Store.YES));
        return doc;
    }
    
    /**
     * 获取资源目录及子目录下的所有文件路径
     * @return 返回String型的路径链表
     */
    private LinkedList<String> getAllSrcPath() {
        LinkedList<String> pathList = new LinkedList<String>();
        File[] typeList = null;
        File[] tmpList = null;
        File[] fileList = null;
        typeList = new File(SOURCE_DIR).listFiles();
        for (int i = 0; i < typeList.length; i++) {
            tmpList = typeList[i].listFiles();
            for (int j = 0; j < tmpList.length; j++) {
                fileList = tmpList[j].listFiles();
                for (int k = 0; k < fileList.length; k++) 
                    pathList.add(fileList[k].getAbsolutePath());
            }
        }
        return pathList;
    }
    
    /**
     * 读取文本文件的内容，用于向index添加内容
     * @param filePath 文件路径
     * @return 返回String的内容
     */
    private String getContent(String filePath) {
        String content = null;
        BufferedReader bufferReader = null;
        char[] s = new char[1024*1024+1];
        int cnt = 0;
        try {
            bufferReader = new BufferedReader(new FileReader(new File(filePath)));
            cnt = bufferReader.read(s, 0, 1024*1024);
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        content = String.valueOf(s, 0, cnt);
        return content;
    }
    
    /**
     * 创建索引，在构造函数中检测是否已创建过，若没有创建过，则调用该函数创建索引
     * @return 判断创建是否成功
     */
    private boolean createIndex() {
        try {
            index = FSDirectory.open(FileSystems.getDefault().getPath(INDEX_DIR));
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = null;
        LinkedList<String> sourcePath = null;
        try {
            indexWriter = new IndexWriter(index, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sourcePath = getAllSrcPath();
        try {
            for (String s: sourcePath) {
                indexWriter.addDocument(addDoc(getContent(s), s));
            }
            indexWriter.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    /**
     * 进行检索操作
     * @param querystr 传入的关键字
     * @return 返回包含该关键字的文件的路径
     */
    public LinkedList<String> doSearch(String querystr) {
        
        int hitsPerPage = 10;
        LinkedList<String> linkedList = new LinkedList<String>();
        Document doc;
        IndexReader reader;
        try {
            Query q = new QueryParser("content", analyzer).parse(querystr);
            reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            
            for (int i = 0; i < hits.length; i++) {
                doc = searcher.doc(hits[i].doc);
                linkedList.add(doc.get("path"));
            }
            reader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }   
        return linkedList;
    }
    
    /*
    public static void main(String [] a) {
        IndexSearch search = new IndexSearch();
      //  search.createIndex();
        LinkedList<String> link = search.doSearch("熊");
        for (String s: link) {
            System.out.println(s);
        }
    }*/
   
}
