#include<stdio.h>
#include<stdlib.h>

typedef struct {
    char a;
    char b;
} zx;


int main(int argc, char* argv[]) {
    zx* a = (zx*)malloc(sizeof(zx));
    a->a = 'a';
    a->b = 'b';
    free(a);

    zx m;
    m.a = 'a';
    m.b = 'b';

    int *p;
    int q = 100;
    p = &q;
    printf("%d, %d, %d\n", p, q, *p);

    return 0;
}
