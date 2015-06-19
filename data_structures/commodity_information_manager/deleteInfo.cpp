/* This file is part of CIMS
   Copyright 2014, Doubear
   Fri Nov 14 22:37:31 2014
   A function to delete information that inputted 
*/


#include <iostream>
#include <vector>
#include <string>
#include "commodity.h"
#include "deleteInfo.h"

using namespace std;

void deleteInfo(vector<commodity> &tmp)
{
	char YorN;
	string name;
	vector<commodity>::iterator iter;
	cout << "Enter the \033[31mname\033[0m of commodity which you want to delete!" << endl;
	while (true) {
		cout << "Name: ";
		cin >> name;
		YorN = '0';
		for (iter = tmp.begin(); iter != tmp.end(); ++iter) {
			if (iter->getName() == name) {
				cout << "Name: " << iter->getName() << "    "
					<< "Type: " << iter->getType() << "    " 
					<< "Stock: " << iter->getStock() << "    "
					<< "Bid: " << iter->getBid() << "    "
					<< "Sell: " << iter->getSell() << endl;
				cout << "Delete?  [Y/n]:";
				cin.get();
				while (true) {
					cin.clear();
					cin.sync();
					YorN = cin.get();
					if (YorN == 'y'||YorN == 'n') 
						break;
				}
			}
			if (YorN =='y') {
				tmp.erase(iter);
				cout << "\033[31m" << name << "\033[0m" << " is deleted!" << endl;
				break;
			}
			if (YorN == 'n')
				break;
		}
		if (iter == tmp.end()) 
			cout << "No record named: " << "\033[31m" << name << "\033[0m" << endl;
		cout << "Continue?  [Y/n]: ";
		cin.get();
        while (true) {
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
