/* This file is part of CIMS
   Copyright 2014, Doubear
   Fri Nov 14 22:48:02 2014
   A function to search by name or type 
*/


#include <iostream>
#include <cstdlib>
#include <vector>
#include <string>
#include <iomanip>
#include "commodity.h"
#include "searchInfo.h"

using namespace std;

void searchInfo(vector<commodity> &tmp)
{
	char YorN;
	string name, type;
	vector<commodity>::iterator iter;
	while (true) {
		system("clear");
		cout<< "\033[31m" 
			<< " ___________________________ \n" 
			<< "|        Search By:         |\n" 
			<< "|  n --> Name   t --> Type  |\n" 
			<< "|  b --> Back to menu       |\n" 
			<< "|___________________________|\033[0m" << endl;
		cin.clear();
		cin.sync();
		YorN = cin.get();
		if (YorN == 'n') {
			while (true) {
				cout << "Name: ";
				cin >> name;
				for (iter = tmp.begin(); iter != tmp.end(); ++iter) {
					if (iter->getName() == name) {
						cout << "Name: " << iter->getName() << "    "
						    << "Type: " << iter->getType() << "    " 
						    << "Stock: " << iter->getStock() << "    "
						    << "Bid: " << iter->getBid() << "    "
						    << "Sell: " << iter->getSell() << endl;
					    cout << "Continue?  [Y/n]:";
						cin.get();  // clear ostream
						while (true) {
							YorN = cin.get();
							cin.clear();
							cin.sync();
							if (YorN == 'y'||YorN == 'n')
							    break;
						}
						if (YorN == 'n'||YorN == 'y')
							break;
					
					}
				}
				if (iter == tmp.end()) {
					cout << "No record named: " << "\033[31m" << name << "\033[0m" << endl;
					cout << "Continue?  [Y/n]:";
					cin.get();
					while (true) {
						YorN = cin.get();
						cin.clear();
						cin.sync();
						if (YorN == 'y'||YorN == 'n')
							break;
					}
				}
				if (YorN == 'n')
					break;
			}
		}

		else if (YorN == 't') {
			while (true) {
				unsigned counter = 0;
				cout << "Type: ";
				cin >> type;
		        cout << setw(20) << setiosflags(ios::right) << "Commodity Name"
		            << setw(20) << setiosflags(ios::right) << "Commodity Type"
		            << setw(15) << setiosflags(ios::right) << "Stock"
		            << setw(15) << setiosflags(ios::right) << setprecision(2) << "Bid"
		            << setw(15) << setiosflags(ios::right) << setprecision(2) << "Sell" << endl;

				for (iter = tmp.begin(); iter != tmp.end(); ++iter) {
					if (iter->getType() == type) {
			            cout << setw(20) << setiosflags(ios::right) << iter->getName()
				            << setw(20) << setiosflags(ios::right) << iter->getType()
				            << setw(15) << setiosflags(ios::right) << iter->getStock()
				            << setw(15) << setiosflags(ios::right) << setprecision(2) << iter->getBid()
							<< setw(15) << setiosflags(ios::right) << setprecision(2) << iter->getSell() << endl;
						counter++;
					}
				}
				if (!counter) 
					cout << "No record of the type: " << "\033[31m" << type << "\033[0m" << endl;
				cout << "Continue?  [Y/n]:";
				cin.get();
				while (true) {
					YorN = cin.get();
					cin.clear();
				    cin.sync();
					if (YorN == 'y'||YorN == 'n')
						break;       
				}
				if (YorN == 'n')
					break;
			}
		}

		else if (YorN =='b') 
			break;
	}
}


