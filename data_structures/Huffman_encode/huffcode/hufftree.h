#ifndef _HUFFTREE_H
#define _HUFFTREE_H

#include "huffnode.h"
#include <QDebug>

class HuffTree
{
private:
    HuffNode* Root;
public:
    HuffTree(quint8 val, long freq) { Root = new HuffNode(val, freq); }
    HuffTree(HuffTree* l, HuffTree* r) { Root = new HuffNode(0, l->weight()+r->weight(), l->root(), r->root()); }
    ~HuffTree() {}

    HuffNode* root() { return Root; }
//    bool operator > (HuffTree* other) { if (this->Root->getWeight() > other->Root->getWeight()) return true; return false; }
    long weight() { return Root->getWeight(); }
};

#endif
