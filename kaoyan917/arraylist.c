#include"arraylist.h"


int ListInit(arraylist *L, int size) {
    if (L) {
        L->elem = (BASETYPE*)malloc(size * sizeof(BASETYPE));
        if (!L->elem) return NULL_POINT;
        L->length = 0;
        L->size = size;
        return SUCCESS;
    }
    return NULL_POINT;
}


int ListDestroy(arraylist *L) {
    if (L) {
        if (L->elem) {
            free(L->elem);
            L->elem = NULL;
        }
        free(L);
        L = NULL;
        return SUCCESS;
    }
    return NULL_POINT;
}


int ListClear(arraylist *L) {
    if (L) {
        if (!L->elem) return NULL_POINT;
        for (int i=0; i < L->length; i++) {
            L->elem[i] = 0;
        }
        L->length = 0;
        return SUCCESS;
    }
    return NULL_POINT;
}


int ListIsEmpty(arraylist *L) {
    if (L) {
        if (L->length) return FALSE;
        return TRUE; 
    }
    return NULL_POINT;
}


int ListLength(arraylist *L) {
    if (L) {
        return L->length;
    }
    return NULL_POINT;
}


int ListGetElem(arraylist *L, int pos, BASETYPE *elem) {
    if (L) {
        if (pos < L->length) {
            *elem = L->elem[pos];
            return SUCCESS;
        }
        return OVER_FLOW;
    }
    return NULL_POINT;
}


int ListLocateElem(arraylist *L, BASETYPE elem, int (*func)(BASETYPE t1, BASETYPE t2)) {
    if (L) {
        for (int i=0; i < L->length; i++) {
            if (func(elem, L->elem[i])) {
                return i;
            }
        }
        return NONE_ELEM;
    }
    return NULL_POINT;
}


int ListInsert(arraylist *L, int pos, BASETYPE elem) {
    if (L) {
        if (pos < L->size && L->length < L->size && pos >= 0) {
            for (int i=L->length; i > pos; i--) {
                L->elem[i] = L->elem[i-1];
            }
            L->elem[pos] = elem;
            L->length += 1;
            return SUCCESS;
        }
        return OVER_FLOW;
    }
    return NULL_POINT;
}


int ListPriorElem(arraylist *L, BASETYPE curElem, BASETYPE *preElem) {
    if (L) {
        for (int i=0; i < L->length; i++) {
            if (L->elem[i] == curElem) {
                if (i == 0) {
                    return NONE_ELEM; 
                }
                *preElem = L->elem[i-1];
                return SUCCESS;
            }
        }
        return NONE_ELEM;
    }
    return NULL_POINT;
}


int ListNextElem(arraylist *L, BASETYPE curElem, BASETYPE *nextElem) {
    if (L) {
        for (int i=0; i < L->length; i++) {
            if (L->elem[i] == curElem) {
                if (i+1 == L->length) {
                    return NONE_ELEM; 
                }
                *nextElem = L->elem[i+1];
                return SUCCESS;
            }
        }
        return NONE_ELEM;
    }
    return NULL_POINT;
}


int ListDelete(arraylist *L, int pos, BASETYPE *elem) {
    if (L) {
        if (pos < L->length && pos >= 0) {
            *elem = L->elem[pos];
            L->length -= 1;
            for (int i=pos; i < L->length; i++) {
                L->elem[i] = L->elem[i+1];
            }
            return SUCCESS;
        }
        return OVER_FLOW;
    }
    return NULL_POINT;
}

