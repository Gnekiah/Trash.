package networkrobot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 嗯，因为系统输出的异常信息太长了，严重影响美观，故把错误输出自定义了。。。
 * 如果有时间就把系统异常捕获的信息输出到文件吧。。。
 * 异常捕获貌似大概应该是都捕获了
 * 现在只提供一个函数：
 * void errorLog(String error)
 * 输入的是错误的信息。。。
 * @author DouBear
 *
 */
public class ErrorLog {
    
    /**
     * 错误日志输出的路径及文件名
     */
    private final static String ERRORLOGFILENAME = "D:/creepersource/robot_error.log";
    
    /**
     * 写入错误日志
     * @param error 错误提示。。
     */
    public static void errorLog(String error) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        error = format.format(new Date()) + "    " + error;
        
        File file = new File(ERRORLOGFILENAME);
        // 新建错误日志
        if (!file.exists()) { try { file.createNewFile(); } catch (IOException e) { return; }}
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write(error);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {}
    }
}
