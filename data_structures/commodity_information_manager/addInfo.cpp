/* This file is part of CIMS
   Copyright 2014, Doubear
   Fri Nov 14 22:14:40 2014
   A function to add on info 
*/


#include <string>
#include <vector>
#include <iostream>
#include "commodity.h"
#include "addInfo.h"

using namespace std;

void addInfo(vector<commodity> &tmp)
{
	int YorN; // choose yes or no
	bool counter = false;
	string name, type;
	unsigned stock;
	float bid, sell;
	vector<commodity>::iterator iter;
	while (true) {

		while (true) { // judge if the name of input is altreay existed
			cout << "Name: ";
			cin >> name;
			counter = false;
			for (iter = tmp.begin(); iter != tmp.end(); ++iter) 
				if (iter->getName() == name) {
					counter = true;
					break;
				}

			if (counter) {
				cout << "\033[31m" << name << "\033[0m" << " is already existed!" << endl;
				cout << "Name: " << iter->getName() << "    "
					<< "Type: " << iter->getType() << "    "
					<< "Stock: " << iter->getStock() << "    "
					<< "Bid: " << iter->getBid() << "    "
					<< "Sell: " << iter->getSell() << endl;
				cout << "Replace? [Y/n]:";
				cin.get();
				while (true) {			
					YorN = cin.get();
					cin.clear();
					cin.sync();
					if (YorN == 'y'||YorN == 'n')
						break;
				}
				if (YorN == 'y')
					break;
			}
			else
				break;
		}
					
		cout << "Type: ";
		cin >> type;
		cout << "Stock: ";
		cin >> stock;
		cout << "Bid: ";
		cin >> bid;
		cout << "Sell: ";
		cin >> sell;
		commodity midTmp(name, type, stock, bid, sell);
		tmp.push_back(midTmp); // add an info
		cout << "Continue? [Y/n]: ";

		while (true) { // get a char
			cin.clear();
			cin.sync();
			YorN = cin.get();
			if (YorN=='y'||YorN=='n')
				break;
		}

		if (YorN == 'n')
			break;
	}
}
