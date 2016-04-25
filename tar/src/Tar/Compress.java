package Tar;
import java.io.*;
import java.util.zip.*;

/**
 * 压缩类，调用Compress(String targetZipName, String inputSourcePath)函数处理压缩
 * 其中，传入的两个参数分别表示压缩后的zip文件的保存路径和待压缩的文件或文件夹路径
 *
 */
public class Compress {
    /**
     * 定义返回值，0表示操作成功；
     * 1表示传入的目标文件路径或待操作文件路径不正确；
     * 2表示没有待操作文件的读权限或没有目标文件路径的写权限
     */
    private int SUCCESS = 0;
    private int PARAMETER_ERROR = 1;
    private int COMPRESS_ERROR = 2;
    
    /**
     * 压缩操作
     * @param targetZipName 压缩后的zip文件存放的路径
     * @param sourcePath 待压缩的文件或目录的路径
     * @return 0表示操作成功；1表示传入的目标文件路径或待操作文件路径不正确；2表示没有待操作文件的读权限或没有目标文件路径的写权限
     */
    public int Compress(String targetZipName, String inputSourcePath) {
      
        File sourcePath;
        ZipOutputStream targetZipStream;
        BufferedOutputStream targetBufferedStream;
        try {
            sourcePath = new File(inputSourcePath);
            targetZipStream = new ZipOutputStream(new FileOutputStream(targetZipName));
            targetBufferedStream = new BufferedOutputStream(targetZipStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return PARAMETER_ERROR;
        }
        try {
            startCompressFile(targetZipStream, sourcePath, sourcePath.getName(), targetBufferedStream);
            targetBufferedStream.close();
            targetZipStream.close(); 
        } catch (Exception e) {
            e.printStackTrace();
            return COMPRESS_ERROR;
        }
        return SUCCESS;
    }

    /**
     * 对每个文件进行压缩
     * 只供内部类调用
     * @param targetZipStream 压缩的目标文件 
     * @param sourcePath 输入的操作文件/文件夹的路径
     * @param filePath 文件的路径
     * @param targetBufferedStream 压缩的目标的缓冲区
     * @throws Exception
     */
    private void startCompressFile(ZipOutputStream targetZipStream, File sourcePath, String filePath,
            BufferedOutputStream targetBufferedStream) throws Exception {
        
        // 对目录和文件进行分类操作
        if (sourcePath.isDirectory()) {
            File[] fileList = sourcePath.listFiles(); // 保存目录下的文件列表
            if (fileList.length == 0) {
                targetZipStream.putNextEntry(new ZipEntry(filePath + "/")); 
            }
            for (int i = 0; i < fileList.length; i++) {
                // 递归的对一个目录下的所有文件压缩
                startCompressFile(targetZipStream, fileList[i], filePath + "/" + fileList[i].getName(), targetBufferedStream); 
            }
        } 
        // 对单个文件进行压缩
        else {
            targetZipStream.putNextEntry(new ZipEntry(filePath)); 
            FileInputStream fileToCompress = new FileInputStream(sourcePath);
            BufferedInputStream bufferToCompress = new BufferedInputStream(fileToCompress);
            int flag;
            while ((flag = bufferToCompress.read()) != -1) {
                targetBufferedStream.write(flag); 
            }
            bufferToCompress.close();
            fileToCompress.close(); 
        }
    }
    
    
  /*  public static void main(String[] args) {
        Compress compress = new Compress();
        String targetZipName = "E:\\zbc.zip";
        String sourcePath = "E:\\.fbtermrc";
        try {
            if (compress.Compress(targetZipName, sourcePath) == 0) {
                System.out.println("Success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}

