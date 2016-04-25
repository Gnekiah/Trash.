package stan;

import java.util.Scanner;

public class Main {
    private static String[] names;
    private static Scanner scan;
    public static void main(String[] args) throws Exception{
		Parameter para = new Parameter();
		scan = new Scanner(System.in);
		names = null;
		while (true) {	    
			System.out.print("JAVA@stan:~"+FileOperation.getPath()+"$ ");
            String input = scan.nextLine();
            if(input.equals(""))
            	continue;
            names = input.split(" ");
             if(!para.switchCode(names))  
            	 break;
		}
	}
}