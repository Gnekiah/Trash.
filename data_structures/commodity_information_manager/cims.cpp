/* This file is part of CIMS
   Copyright 2014, Doubear
   Fri Nov 14 22:24:08 2014
   Main function, control all function
*/


#include <iostream>
#include <cstdlib>
#include <string>
#include <vector>
#include "loadInfo.h"
#include "commodity.h"
#include "menu.h"
#include "printInfo.h"
#include "addInfo.h"
#include "deleteInfo.h"
#include "searchInfo.h"
#include "help.h"
#include "logOff.h"

using namespace std;

int main()
{
	vector<commodity> Info;
	loadInfo(Info); // load infomation
	char choose;

	while (true) {
		choose = menu();
		switch(choose) {
			case 'p':
				system("clear");
				printInfo(Info); // print all infomation
				cin.clear();
				cin.sync();
				cin.get();
				system("clear");
				break;
	
			case 'a':
				system("clear");
				addInfo(Info); // add an information
				cin.clear();
				cin.sync();
				cin.get();
				system("clear");
				break;

			case 's':
				searchInfo(Info); // search information
				cin.clear();
				cin.sync();
				cin.get();
				system("clear");
				break;
	
			case 'd':
				system("clear");
				deleteInfo(Info); // delete information
				cin.clear();
				cin.sync();
				cin.get();
				system("clear");
				break;
	
			case 'h':
				system("clear");
				help(); // help document
				system("clear");
				break;
	
			case 'l':
				system("clear");
				logOff(Info); // updata information on disk and make up a backup
				cin.clear();
				cin.sync();
				cout << "Press anykey to continue...." << endl;
				cin.get();
				system("clear");
				break;

			case 'q':
				logOff(Info);
				cin.clear();
				cin.sync();
				cout << "Press Enter to continue...." << endl;
				cin.get();
				system("clear");
				exit(0);

			default:
				break;
		}
	}
	return 0;
}
