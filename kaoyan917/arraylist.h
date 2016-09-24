#include<stdlib.h>

#ifndef ARRAYLIST_H_

#define BASETYPE        int
#define SUCCESS         0
#define FALSE           0
#define TRUE            1
#define NULL_POINT      -1
#define OVER_FLOW       -2
#define NONE_ELEM       -3

typedef struct {
    BASETYPE *elem;
    int length;
    int size;
} arraylist;

int ListInit(arraylist *L, int size);
int ListDestroy(arraylist *L);
int ListClear(arraylist *L);
int ListIsEmpty(arraylist *L);
int ListLength(arraylist *L);
int ListGetElem(arraylist *L, int pos, BASETYPE *elem);
int ListLocateElem(arraylist *L, BASETYPE elem, int (*func)(BASETYPE t1, BASETYPE t2));
int ListInsert(arraylist *L, int pos, BASETYPE elem);
int ListPriorElem(arraylist *L, BASETYPE curElem, BASETYPE *preElem);
int ListNextElem(arraylist *L, BASETYPE curElem, BASETYPE *nextElem);
int ListDelete(arraylist *L, int pos, BASETYPE *elem);

#endif // ARRAYLIST_H_

