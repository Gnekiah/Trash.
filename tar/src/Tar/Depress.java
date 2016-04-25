package Tar;
import java.io.*;
import java.util.zip.*;
/**
 * 解压缩类，调用Depress(String zipFileName, String targetPath)函数处理解压缩操作
 * 其中zipFileName为传入的zip文件的路径
 * targetPath为将解压后保存的路径
 * 如果没有输入解压缩的保存路径，则调用Depress(String zipFileName)函数解压缩
 * 解压后的文件保存在当前目录（程序运行目录）
 *
 */
public class Depress {
    /**
     * 定义返回的错误类型
     * 0：操作成功
     * 1：zip文件未找到或没有读权限
     * 2：列表中的文件未找到
     * 3：输出错误或没有保存路径的写权限
     */
    private int SUCCESS = 0;
    private int ZIP_FILEPATH_ERROR = 1;
    private int FILE_NOT_FOUND = 2;
    private int OUTPUT_ERROR = 3;
    
    /**
     * 解压缩操作
     * @param zipFileName zip文件的路径
     * @param targetPath 解压后保存的路径
     * @return 0：操作成功；1：zip文件未找到或没有读权限；2：列表中的文件未找到；3：输出错误或没有保存路径的写权限
     */
    public int Depress(String zipFileName, String targetPath) {
        ZipInputStream zipInputStream = null; // 用于打开zip文件的文件流
        BufferedInputStream bufferInputStream = null; // zip文件流的缓冲区流
        File outputFilePath; // 输出路径
        ZipEntry entry; // zip文件的输入区块
        // 测试文件路径的合法性
        try {
            zipInputStream=new ZipInputStream(new FileInputStream(zipFileName));
            bufferInputStream=new BufferedInputStream(zipInputStream);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return ZIP_FILEPATH_ERROR;
        }
        // 对文件解压缩操作
        try {
            while((entry = zipInputStream.getNextEntry()) != null && !entry.isDirectory()) {
                outputFilePath=new File(targetPath, entry.getName());
                // 判断输出路径是否合法
                if(!outputFilePath.exists()){
                    (new File(outputFilePath.getParent())).mkdirs();
                }
                // 文件输出
                FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                int flag;
                while((flag = bufferInputStream.read()) != -1){
                    bufferedOutputStream.write(flag);
                }
                bufferedOutputStream.close();
                fileOutputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return FILE_NOT_FOUND;
        } catch (IOException e) {
            e.printStackTrace();
            return OUTPUT_ERROR;
        }
        try {
            bufferInputStream.close();
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return SUCCESS;
        }   
        return SUCCESS;
    }
    
    
    /**
     * 解压缩函数，用于处理没有输入保存路径时候的解压缩操作，此时默认保存在当前目录
     * @param zipFileName
     * @return
     */
    public int Depress(String zipFileName) {
        return Depress(zipFileName, ".");
    }
    
    
    /*public static void main(String[] args) {
        Depress depress = new Depress();
        String zipFileName = "E:\\zbc.zip";
       // String targetPath = "E:\\.putout\\";
        if (depress.Depress(zipFileName, targetPath) == 0) {
            System.out.println("success");
        }
    }*/
}
