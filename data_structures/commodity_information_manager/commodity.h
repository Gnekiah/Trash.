/* This file is part of CIMS
   Copyright 2014, Doubear
   Fri Nov 14 22:30:20 2014
   A class of commodity information
*/


#ifndef COMMODITY_H
#define COMMODITY_H
#include <string>

using namespace std;

class commodity {
	private:
		string name;
		string type;
		unsigned stock;
		float bid;
		float sell;
	public:
		commodity();
		commodity(string name, string type, unsigned stock, float bid, float sell);
		string getName(void);
		string getType(void);
		unsigned getStock(void);
		float getBid(void);
		float getSell(void);
};

#endif //COMMODITY_H

