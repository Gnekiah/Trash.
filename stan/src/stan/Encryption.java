package stan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Encryption {
    public static void switchEncrypt(String[] args) throws Exception {
        File in = new File(args[2]);
        switch (args[1]) {
        case "d":
            decrypt(in);
            break;
        case "e":
            encrypt(in);
            break;
        default:
            System.out.println("Error with parameter.");
            Parameter.usage();
            System.exit(0);
        }
    }

	public static void encrypt(File fileIn){
		try {
			FileInputStream fis = new FileInputStream(fileIn);
			Scanner scan = new Scanner(System.in);
			System.out.println("input the new file name(including its path,like:/home/zzw/xxx.xxx) ");
			String name = scan.next();
			File outf=new File(name);
			
			FileOutputStream fos = new FileOutputStream(outf);
			
			byte[] bytIn = new byte[1240];
			int len=0;
			while((len=fis.read(bytIn))!=-1){
				for(int i=0;i<len;i++)
					bytIn[i]=(byte) ~bytIn[i];
				fos.write(bytIn,0, len);
			}
			fis.close();
			fos.close();System.out.println("encrypt ok");
			scan.close();
			}
		catch (Exception e) {
			e.printStackTrace();			
		}
		
		
	}
	public static void decrypt(File fileIn){
		try {
			FileInputStream fis = new FileInputStream(fileIn);
			Scanner scan = new Scanner(System.in);
			System.out.println("input the new file name(including its path,like:/home/zzw/xxx.xxx) ");
			String name = scan.next();
			File outf=new File(name);
			
			FileOutputStream fos = new FileOutputStream(outf);
			
			byte[] bytIn = new byte[1240];
			int len=0;
			while((len=fis.read(bytIn))!=-1){
				for(int i=0;i<len;i++)
					bytIn[i]=(byte) ~bytIn[i];
				fos.write(bytIn,0, len);
			}
			fis.close();
			fos.close();System.out.println("decrypt ok");
			scan.close();
			}
		catch (Exception e) {
			e.printStackTrace();			
		}
		
	}
}
