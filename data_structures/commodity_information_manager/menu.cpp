/* This file is part of CIMS
   Copyright 2014, Doubear
   Fri Nov 14 22:44:52 2014
   A function to provide user interface 
*/


#include <iostream>
#include <cstdlib>
#include <cstdio>
#include "menu.h"

using namespace std;

char menu(void)
{
	char choose;
	cout<< "\033[31m"
		<< " _______________________________________ \n"  
		<< "|              Welcome!                 |\n"  
 		<< "|_______________________________________|\n" 
		<< "|                                       |\n" 
		<< "|    p --> Print      a --> Add         |\n" 
		<< "|    s --> Search     d --> Delete      |\n" 
		<< "|    h --> Help       l --> LogOff      |\n" 
		<< "|    q --> Quit                         |\n" 
		<< "|_______________________________________|\033[0m" << endl;
	 
	while (true) {
		cin.clear();
		cin.sync();
		choose = cin.get();
		if (choose=='l'||choose=='a'||choose=='s'||choose=='d'||choose=='h'||choose=='p'||choose=='q') {
			cin.get();
			return choose;
		}
	 }
}


