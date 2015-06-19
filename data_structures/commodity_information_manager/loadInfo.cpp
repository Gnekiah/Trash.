/* This file is part of CIMS
   Copyright 2014, Doubear
   Fri Nov 14 22:41:49 2014
   A function to load information from disk 
*/


#include <vector>
#include <iostream>
#include <cstdlib>
#include <fstream>
#include <string>
#include "commodity.h"
#include "loadInfo.h"

using namespace std;

void loadInfo(vector<commodity> &tmp) 
{
	system("clear");
	cout << "Prepare to load data from \033[31mdatafile\033[0m...." << endl;
	ifstream load("datafile");
	if (!load) {
		cout << "Fail to open \033[31mdatafile\033[0m." << endl;
		exit(0);
	}
	cout << "Loading...." << endl;

	string name, type;
	unsigned stock;
	float bid, sell;
	for (; !load.eof(); ) {
		load >> name >> type >> stock >> bid >> sell;
		commodity midTmp(name, type, stock, bid, sell);
		tmp.push_back(midTmp);
	}
	tmp.pop_back();

	cout << "Loading Successful!" << endl;
	load.close();
	cout << "Press Enter to continue...." << endl;
	cin.get();
	system("clear");
}
