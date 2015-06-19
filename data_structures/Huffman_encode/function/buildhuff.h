#include "huffcode/hufftree.h"
#include "datastore/minheap.h"
#include <QDebug>
#include <QList>

HuffTree* buildHuff(long (&charsStore)[256])
{
    QList<HuffTree*> treeVector;
    treeVector.append(new HuffTree(0, charsStore[0]));
    charsStore[0] = 0;
    for (int i = 1; i < 256; ++i) {
        if (charsStore[i] != 0) {
            int flag = 0;
            for (int j = 0; j < treeVector.length(); ++j) {
                if (treeVector.value(j)->weight() > charsStore[i]) {
                    treeVector.insert(j, new HuffTree(i, charsStore[i]));
                    flag = 1;
                    break;
                }
            }
            if (flag==0)
                treeVector.append(new HuffTree(i, charsStore[i]));
            charsStore[i] = 0;
        }
    }

    HuffTree *temp1, *temp2, *temp3 = NULL;
    while (treeVector.length() > 1) {
        temp1 = treeVector.first();
        treeVector.removeFirst();
        temp2 = treeVector.first();
        treeVector.removeFirst();
        temp3 = new HuffTree(temp1, temp2);
        int flag = 0;
        for (int j = 0; j < treeVector.length(); ++j) {
            if (treeVector.value(j)->weight() > temp3->weight()) {
                treeVector.insert(j, temp3);
                flag = 1;
                break;
            }
        }
        if (flag==0)
            treeVector.append(temp3);
        delete temp1;
        delete temp2;
    }
    return temp3;
 }
