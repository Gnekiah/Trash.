/* This file is part of CIMS
   Copyright 2014, Doubear
   Fri Nov 14 22:43:15 2014
   A function to update data file and make up a backup 
*/


#include <iostream>
#include <fstream>
#include <vector>
#include <cstdlib>
#include <string>
#include "commodity.h"
#include "logOff.h"

using namespace std;

void logOff(vector<commodity> &tmp)
{
	vector<commodity>::iterator iter;
	system("rm .datafile.backup");
	ofstream backup(".datafile.backup");
	cout << "Making up a backup...." << endl;
	for (iter = tmp.begin(); iter != tmp.end(); iter++) {
		backup << iter->getName() << " "
			<< iter->getType() << " "
			<< iter->getStock() << " "
			<< iter->getBid() << " "
			<< iter->getSell() << endl;
	}
	backup.close();
	cout << "BackUp Successful!" << endl;
	cout << "Updating \033[31mdatafile\033[0m...." << endl;

	system("rm datafile");
	ofstream update("datafile");
	for (iter = tmp.begin(); iter != tmp.end(); iter++) {
		update << iter->getName() << " "
			<< iter->getType() << " "
			<< iter->getStock() << " "
			<< iter->getBid() << " "
			<< iter->getSell() << endl;
	}
	update.close();
	cout << "UpDate \033[31mdatafile\033[0m Successful!" << endl;
}

