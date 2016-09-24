#include<stdio.h>
#include"arraylist.h"

int main(int argc, char* argv[]) {
    arraylist* L = (arraylist*)malloc(sizeof(arraylist));
    ListInit(L, 10);
    ListInsert(L, 0, 1);
    ListInsert(L, 1, 10);
    ListLength(L);

    int elem;
    ListNextElem(L, 1, &elem);
    ListGetElem(L, 0, &elem);
    printf("%d\n", elem);
    for (int i=0; i < L->length; i++) {
        printf("%d, ", L->elem[i]);
    }
    printf("\n");
    return SUCCESS;
}



