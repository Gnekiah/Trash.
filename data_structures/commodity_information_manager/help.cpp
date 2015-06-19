/* This file is part of CIMS
   Copyright 2014, Doubear
   Fri Nov 14 22:40:07 2014
   A function to print help document
*/


#include <iostream>
#include "help.h"

using namespace std;

void help(void)
{
	cout << "        Commodity Information Management System\n" << endl;
	cout << "\033[31mPrint\033[0m:  Print all commodity information to screen." << endl;
	cout << "\033[31mAdd\033[0m:    Add commodity information to datafile." << endl;
	cout << "\033[31mSearch\033[0m: Search information by name or type." << endl;
	cout << "\033[31mDelete\033[0m: Search by name and then choose delete it or not." << endl;
	cout << "\033[31mLogOff\033[0m: Save information and set up a BACKUP.\n" << endl;
	cout << "Press any key to continue." << endl;
	cin.clear();
	cin.sync();
	cin.get();
}
