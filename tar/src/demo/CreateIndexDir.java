package demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * 建立索引文件
 * 
 * @author chinaxxren
 * 
 */
public class CreateIndexDir {
    // 搜索源文件
    private static String INDEX_DIR = "C:\\Documents and Settings\\admin\\桌面\\cs";
    // 建立生成索引文件夹
    private static String DATA_DIR = "C:\\Documents and Settings\\admin\\桌面\\test";

    public static void main(String[] args) throws Exception {
        long start = new Date().getTime();
        int numIndexed = index(new File(INDEX_DIR), new File(DATA_DIR));
        long end = new Date().getTime();

        System.out.println("Indexing " + numIndexed + " files took "
                + (end - start) + " milliseconds");
    }

    /**
     * 
     * @param srcDir
     *            查询的源文件
     * @param saveDir
     *            创建保存索引的文件目录
     * @return 返回匹配的记录总数
     */
    public static int index(File srcDir, File saveDir) {
        Analyzer analyzer = new StandardAnalyzer(); // 创建一个语法分析器
        IndexWriter writer = null;
        // 文件目录
        Directory directory = null;
        int numIndexed = 0;
        try {
            // 索引文件可放的位置：索引可以存放在两个地方1.硬盘，2.内存；
            // 放在硬盘上可以用FSDirectory()，放在内存的用RAMDirectory()不过一关机就没了
            directory = FSDirectory.open(saveDir); // 把索引文件存储到磁盘目录
            // 创建一个IndexWriter(存放索引文件的目录,分析器,Field的最大长度)
            System.out.println(IndexWriter.MaxFieldLength.UNLIMITED);
            // 可见构造它需要一个索引文件目录，一个分析器(一般用标准的这个)，一个参数是标识是否清空索引目录
            writer = new IndexWriter(directory, analyzer, true,
                    IndexWriter.MaxFieldLength.UNLIMITED);
            // 索引合并因子
            // 一、SetMergeFactor（合并因子）
            // SetMergeFactor是控制segment合并频率的，其决定了一个索引块中包括多少个文档，当硬盘上的索引块达到多少时，
            // 将它们合并成一个较大的索引块。当MergeFactor值较大时，生成索引的速度较快。MergeFactor的默认值是10，建议在建立索引前将其设置的大一些。
            writer.setMergeFactor(100);
            // 二、SetMaxBufferedDocs（最大缓存文档数）
            // SetMaxBufferedDocs是控制写入一个新的segment前内存中保存的document的数目，
            // 设置较大的数目可以加快建索引速度，默认为10。
            writer.setMaxMergeDocs(1000);
            // 三、SetMaxMergeDocs（最大合并文档数）
            // SetMaxMergeDocs是控制一个segment中可以保存的最大document数目，值较小有利于追加索引的速度，默认Integer.MAX_VALUE，无需修改。
            // 在创建大量数据的索引时，我们会发现索引过程的瓶颈在于大量的磁盘操作，如果内存足够大的话，
            // 我们应当尽量使用内存，而非硬盘。可以通过SetMaxBufferedDocs来调整，增大Lucene使用内存的次数。
            indexDirectory(writer, srcDir);
            numIndexed = writer.numDocs();
            // SetUseCompoundFile这个方法可以使Lucene在创建索引库时，会合并多个 Segments 文件到一个 .cfs
            // 中。
            // 此方式有助于减少索引文件数量，对于将来搜索的效率有较大影响。
            // 压缩存储（True则为复合索引格式）
            writer.setUseCompoundFile(true);
            // 对索引进行优化
            writer.optimize();
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close(); // 关闭IndexWriter时,才把内存中的数据写到文件
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (directory != null) {
                try {
                    directory.close(); // 关闭索引存放目录
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return numIndexed;
    }

    /**
     * 递归文件
     * 
     * @param writer
     * @param dir
     * @throws IOException
     */
    private static void indexDirectory(IndexWriter writer, File srcDir)
            throws IOException {

        File[] files = srcDir.listFiles();
        for (File src : files) {
            if (src.isDirectory()) {
                // 如果是文件继续递归
                indexDirectory(writer, src); // recurse
                // 如果是txt结尾的文件则处理
            } else if (src.getName().endsWith(".txt")) {
                indexFile(writer, src);
            }
        }
    }

    /**
     * 建立索引表
     * 
     * @param writer
     * @param f
     * @throws IOException
     */
    private static void indexFile(IndexWriter writer, File src)
            throws IOException {
        // 如果文件时隐藏或者文件不存在或则文件不能读，则返回
        if (src.isHidden() || !src.exists() || !src.canRead()) {
            return;
        }
        // 显示读取的文件的路径
        String path = src.getCanonicalPath();
        System.out.println("" + path);
        // 显示读取的文件内容
        String text = loadFileToString(src);
        // Document相当于数据库中的一行记录。
        Document doc = new Document();
        // Field(String name, byte[] value, Field.Store store)
        // Field(String name, Reader reader)
        // Field(String name, Reader reader, Field.TermVector termVector)
        // Field(String name, String value, Field.Store store, Field.Index
        // index)
        // Field(String name, String value, Field.Store store, Field.Index
        // index, Field.TermVector termVector)
        //      
        // 在Field中有三个内部类：Field.Index,Field.Store,Field.termVector，而构造函数也用到了它们。
        // 参数说明：
        // Field.Store：
        // Field.Store.NO：表示该Ｆield不需要存储。
        // Field.Store.Yes：表示该Ｆield需要存储。
        // Field.Store.COMPRESS：表示使用压缩方式来存储。
        // Field.Index：
        // Field.Index.NO：表示该Ｆield不需要索引。
        // Field.Index.TOKENIZED：表示该Ｆield先被分词再索引。
        // Field.Index.UN_TOKENIZED：表示不对该Ｆield进行分词，但要对其索引。
        // Field.Index.NO_NORMS：表示该Ｆield进行索引，但是要对它用Analyzer，同时禁止它参加评分，主要是为了减少内在的消耗。
        // 增加文档 Field相当于增加数据库字段一样
        //检索,获取都需要的内容,直接放index中,不过这样会增大index,保存文件的txt内容
        doc.add(new Field("contents", text, Field.Store.YES,
                Field.Index.ANALYZED));
        //大段文本内容,会用来检索,但是检索后不需要从index中取内容,可以根据url去load真实的内容 
        doc.add(new Field("contents", new FileReader(src)));
        doc.add(new Field("filename", path, Field.Store.YES,
                Field.Index.ANALYZED));
        writer.addDocument(doc);
    }

    /**
     * 将文件读出来转化为字符串
     * 
     * @param file
     *            源文件，不能是文件夹
     * @return
     */
    private static String loadFileToString(File file) {
        BufferedReader br = null;
        try {
            // 字符缓冲流，是个装饰流，提高文件读取速度
            br = new BufferedReader(new FileReader(file));
            StringBuffer sb = new StringBuffer();
            String line = br.readLine();
            while (null != line) {
                sb.append(line);
                line = br.readLine();
                System.out.println(line);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在!");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                System.out.println("关闭流出现异常");
                e.printStackTrace();
            }
        }
    }
}

