package stan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
/**
 * 文件操作类
 * @author k_lee9575 李超
 *
 */
public class FileOperation {
    private static String relative_path="";
    static String getPath(){
    	return relative_path;
    }
    /**
     * 当前目录
     * @param operations
     * @return
     */
    static void pwd(String[] operations){
    	System.out.println(System.getProperty("user.dir")+relative_path);
    }
    /**
     * 列出目录下的文件
     * @param operations
     * @return
     */
    static void list(String[] operations){
        File dirFile = new File(System.getProperty("user.dir")+relative_path);
        if(dirFile.exists()){
	        String[] files = dirFile.list();
	        if(operations.length>1&&operations[1].equals("-a")){
	            System.out.print(".  ");
	            System.out.print("..  ");
	            for(int i = 0; i < files.length; i++)
	                System.out.print(files[i]+"  ");
	        }
	        else{
	            for(int i = 0; i < files.length; i++){
	                if(files[i].charAt(0)!='.')
	                    System.out.print(files[i]+"  ");
	            }
	        }
	        System.out.println();
        }
    }
    /**
     * 进入目录
     * @param operations
     * @return
     */
    static void cd(String[] operations)  {
        String temp_path=relative_path;
        if(operations.length==1)
            temp_path="";
        else{
            if(operations[1].equals("..")){
                int i;
                for(i=temp_path.length()-1;i>=0;i--){
                    if(temp_path.charAt(i)==File.separatorChar)
                        break;
                }
                if(i<0)
                    i=0;
                temp_path=temp_path.substring(0,temp_path.length()-(temp_path.length()-i));
            }
            else if(!operations[1].equals(".")){
                if(operations[1].endsWith(File.separator)){
                    operations[1]=operations[1].substring(0,operations[1].length()-1); 
                }
                temp_path+=File.separator+operations[1];
            }
        }
        File file=new File(System.getProperty("user.dir")+temp_path);
        if(file.exists()){
            relative_path=temp_path;
        }else{
        	System.out.println("bash: cd: "+operations[1]+": 没有那个文件或目录");
        }
    }
    /**
     * 创建文件夹
     * @param operations
     */
    static void mkdir(String[] operations){
        if(operations[1].endsWith(File.separator)){
            operations[1]=operations[1].substring(0,operations[1].length()-1); 
        }
        String[] path=operations[1].split(File.separator);
        String temp_path="";
        for(int i=0;i<path.length-1;i++)
            temp_path+=File.separator+path[i];
        File temp_file=new File(System.getProperty("user.dir")+relative_path+temp_path);
        if(temp_file.exists()) {
            File file = new File(System.getProperty("user.dir")+relative_path+temp_path+File.separator+path[path.length-1]) ;
            if(file.exists()){
            }
            else
                file.mkdir() ;
        }
        else{
            System.out.println("mkdir: 无法创建目录"+temp_path+path[path.length-1]+": 没有那个文件或目录");
        }
    }
    /**
     * 删除文件或文件夹
     * @param operations
     */
    static void rm(String[] operations){
        if(operations.length > 1 && operations[1].equals("-a")){
            if(operations[2].endsWith(File.separator)){
                operations[2]=operations[2].substring(0,operations[2].length()-1); 
            }
            File path=new File(System.getProperty("user.dir")+relative_path+File.separator+operations[2]);
            File[] filelist=path.listFiles(); 
            int listlen=filelist.length;  
            for(int   i=0;i<listlen;i++){ 
                if(filelist[i].isDirectory()){
                    String newString="";
                    if(filelist[i].toString().endsWith(File.separator)){
                        newString=filelist[i].toString().substring(0,filelist[i].toString().length()-1); 
                    }
                    else
                        newString=filelist[i].toString();
                    String[] temp_path=newString.split(File.separator);
                    String[]  temp_operations=operations;
                    temp_operations[2]+=File.separator+temp_path[temp_path.length-1];
                    rm(temp_operations); 
                  } 
                else { 
                    filelist[i].delete(); 
                } 
            }
            path.delete();
        }
        else if (operations.length == 1){
              File path=new File(System.getProperty("user.dir")+relative_path+File.separator+operations[0]);
              path.delete(); 
        }
    }
    /**
     * 复制文件和文件夹
     * @param operations
     * @throws Exception
     */
    static void cp(String[] operations) throws Exception{
 //   	if (operations.length != 4) {
 //           System.out.println("cp: 在"+operations[1]+" 后缺少了要操作的目标文件\nTry 'cp --help' for more information.\n");
 //           Parameter.usage();
 //           return;
 //       }
        if(operations[1].equals("-r")){
            try{
                if(operations[2].endsWith(File.separator)){
                    operations[2]=operations[2].substring(0,operations[2].length()-1); 
                }
                if(operations[3].endsWith(File.separator)){
                    operations[3]=operations[3].substring(0,operations[3].length()-1); 
                }
                (new File(System.getProperty("user.dir")+relative_path+File.separator+operations[3])).mkdirs();
                File a=new File(System.getProperty("user.dir")+relative_path+'/'+operations[2]);
                String[] file=a.list();
                File temp=null;
                for(int i= 0;i<file.length;i++){
                    temp=new File(System.getProperty("user.dir")+relative_path+'/'+operations[2]+File.separator+file[i]);   
                    if(temp.isFile()){
                        FileInputStream input=new FileInputStream(temp);
                        FileOutputStream output = new FileOutputStream(System.getProperty("user.dir")+relative_path+File.separator+operations[3]+File.separator+file[i]);
                        byte[] b=new byte[1024*5];
                        int len;
                        while((len=input.read(b))!= -1) {
                            output.write(b,0,len);
                        }
                        output.flush();
                        output.close();
                        input.close();
                    }
                    if(temp.isDirectory()){
                        String[] temp_operations=operations;
                        temp_operations[2]+=File.separator+file[i];
                        temp_operations[3]+=File.separator+file[i];
                        cp(temp_operations);
                    }
                }
            }
            catch(Exception e){
                System.out.println("复制整个文件夹内容操作出错");
                e.printStackTrace();
            }
        }  
        else{
            File in = new File(System.getProperty("user.dir")+relative_path+File.separator+operations[1]);
            File out = new File(System.getProperty("user.dir")+relative_path+File.separator+operations[2]);
            FileInputStream inFile = new FileInputStream(in);
            FileOutputStream outFile = new FileOutputStream(out);
            byte[] buffer = new byte[1024];
            int i = 0;
            while ((i = inFile.read(buffer)) != -1) {
                outFile.write(buffer, 0, i);
            }
            inFile.close();
            outFile.close();
        }
    }
}
