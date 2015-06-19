#ifndef _HUFFNODE_H
#define _HUFFNODE_H

#include <QDataStream>

class HuffNode {
private:
        quint8 it;
        long weight;
        HuffNode* lc;
        HuffNode* rc;
public:
        HuffNode(quint8 a, long b, HuffNode* c=0, HuffNode* d=0) {it=a; weight=b; lc=c; rc=d;}
        quint8 getValue() { return it; }
        long getWeight() { return weight; }
        HuffNode* getLeft() { return lc; }
        HuffNode* getRight() { return rc; }
        void setLeft(HuffNode* a) { lc = a; }
        void setRight(HuffNode* a) { rc = a; }
};

#endif
