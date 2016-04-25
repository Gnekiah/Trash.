package stan;

import java.io.*;
import java.util.zip.*;

public class Compression {
    static final int BUFFER = 2048;
    public static void switchCompress(String[] args) throws Exception {
        String argv[] = new String[args.length - 2];
        for (int i = 2; i < args.length; i++) {
            argv[i-2] = args[i];
        }
        switch (args[1]) {
        case "c":
            Compress(argv);
            break;
        case "x":
            Extract(argv);
            break;
        default:
            System.out.println("Error with parameter.");
            Parameter.usage();
            System.exit(0);
        }
    }
    
    public static void Extract(String[] args) {
        try {                          
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(args[0]);
            ZipOutputStream out = new ZipOutputStream(dest);
            byte data[] = new byte[BUFFER];
            File f = new File(".");
            String files[] = f.list();
            for (int i = 0; i < files.length; i++) {
                System.out.println("Adding: " + files[i]);
                FileInputStream fi = new FileInputStream(files[i]);
                origin =new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(files[i]);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void Compress(String[] args) {
        try {
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(args[0]);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println("Extracting: " + entry);
                int count;
                byte data[] = new byte[BUFFER];
                FileOutputStream fos = new FileOutputStream(entry.getName());
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            }
            zis.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
