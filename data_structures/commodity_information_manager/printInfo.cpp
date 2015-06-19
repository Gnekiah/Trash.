/* This file is part of CIMS
   Copyright 2014, Doubear
   Fri Nov 14 22:46:36 2014
   A function to print all information  
*/


#include <string>
#include <vector>
#include <cstdlib>
#include <iostream>
#include <iomanip>
#include "commodity.h"
#include "printInfo.h"
#define LINE 30

using namespace std;

void printInfo(vector<commodity> &tmp)
	
{
	unsigned dLine = 2 * LINE; // control if back to last page or not
	string x;
	char YorN;
	int count = 0;
	vector<commodity>::iterator iter = tmp.begin();
	while (iter != tmp.end()) {
		count = 0;
		cout << setw(30) <</* setiosflags(ios::right) <<*/ "\033[31mCommodity Name"
			<< setw(20) << /*setiosflags(ios::right) <<*/ "Commodity Type"
			<< setw(15) <</* setiosflags(ios::right) <<*/ "Stock"
			<< setw(15) <</* setiosflags(ios::right) <<*/ setprecision(2) << "Bid"
			<< setw(15) <</* setiosflags(ios::right) <<*/ setprecision(2) << "Sell\033[0m" << endl;

		for  ( ; iter != tmp.end(); ++iter) {
			cout << setw(25) << setiosflags(ios::left) << iter->getName()
				<< setw(20) << setiosflags(ios::left) << iter->getType()
				<< setw(15) << setiosflags(ios::left) << iter->getStock()
				<< setw(15) << setiosflags(ios::left) << setprecision(2) << iter->getBid()
				<< setw(15) << setiosflags(ios::left) << setprecision(2) << iter->getSell() << endl;
			++count;
			if (count >= LINE) {
				++iter;
				break;
			}
		}
		cout << "\n\n             Enter(Next Page)  L(Last Page)  Q(Quit)" << endl;
		YorN = cin.get();
		if (YorN == 'q'||YorN == 'Q') 
			break;
		if (YorN == 'l'||YorN == 'L') {
			if ((iter-LINE) == tmp.begin()) 
				iter -= LINE;
			else
				iter -= dLine;
			cin.get();
		}
		cin.clear();
		cin.sync();
		system("clear");
	}
}
