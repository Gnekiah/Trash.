/* This file is part of CIMS
   Copyright 2014, Doubear
   Fri Nov 14 22:29:09 2014
   A class of commodity information 
*/


#include <string>
#include "commodity.h"

using namespace std;

commodity::commodity() {
	name = "-";
	type = "-";
	stock = 0;
	bid = 0.0;
	sell = 0.0;
}

commodity::commodity(string name, string type, unsigned stock, float bid, float sell) {
	this->name = name;
	this->type = type;
	this->stock = stock;
	this->bid = bid;
	this->sell = sell;
}

string commodity::getName(void) {
	return name;
}

string commodity::getType(void) {
	return type;
}

unsigned commodity::getStock(void) {
	return stock;
}

float commodity::getBid(void) {
	return bid;
}

float commodity::getSell(void) {
	return sell;
}

