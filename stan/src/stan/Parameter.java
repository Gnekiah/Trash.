package stan;

public class Parameter {
    public static void usage() throws Exception{
        System.out.print("stan - file manager on terminal.\n"
                + "Usage: stan [OPERATION]... [OPTION]... [OBJECT]...\n\n"
                + "       stan cd [ |TARGET/PATH]\n"
                + "                 - change directory.\n"
                + "       stan cp [ |r] [SOURCE/PATH] [TARGET/PATH]\n"
                + "                 - copy file(s).\n"
                + "              -r - copy directory(s)\n"
                + "       stan crypt [d|e] [FILE]\n"
                + "              -d - decrypt file(s)\n"
                + "              -e - encrypt file(s)\n"
                + "       stan ls [ |TARGET/PATH]\n"
                + "                 - list all files and directorys of TARGET directory.\n"
                + "       stan mkdir [TARGET/PATH]\n"
                + "                 - make a new directory.\n"
                + "       stan rm [ |r|f] [TARGET/PATH]\n"
                + "                 - remove file(s).\n"
                + "              -r - remove directory(s).\n"
                + "              -f - force to remove file(s) or directory(s).\n"
                + "       stan tar [c|x] [TARGET/PATH] [FILE|DIRECTORY...]\n"
                + "              -c - create a compress package.\n"
                + "              -x - express a package.\n"
                + "       stan [help|usage]\n"
                + "                 - to get usage of stan.\n");
    }
    
    
    public boolean switchCode(String[] args) throws Exception{
    	if(args[0].equals("exit"))
    		return false;
    	else{
    	switch(args[0]) {
	    	case "cd":
	    		if(args.length==2||args.length==1)
	    			FileOperation.cd(args);
	    		else
	    			usage();
	    		break;
	    	case "cp":
	    		if(args.length==3||args.length==4)
	    			FileOperation.cp(args);
	    		else
	    			usage();
	    		break;
	    	case "crypt":
	    		if(args.length==3)
	    			Encryption.switchEncrypt(args);
	    		else{
	    			 System.out.println("Error with parameter.");
	    			usage();
	    		}
	    		break;
	    	case "ls":
	    		FileOperation.list(args);
	    		break;
	    	case "mkdir":
	    		if(args.length==2)
	    			FileOperation.mkdir(args);
	    		else
	    			usage();
	    		break;
	    	case "rm":
	    		if(args.length==2||args.length==3)
	    			FileOperation.rm(args);
	    		else
	    			usage();
	    		break;
	    	case "pwd":
	    		FileOperation.pwd(args);
	    		break;
	    	case "tar":
	    		if(args.length>=3)
	    			Compression.switchCompress(args);
	    		else
	    			usage();
	    		break;
	    	case "help":
	    	case "usage":
	    	default:
	    		usage();
	    		break;
    		}
    	return true;
        }
    }
}
