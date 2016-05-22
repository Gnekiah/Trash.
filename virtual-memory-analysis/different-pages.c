#include <stdio.h>
#include <stdlib.h>

// length of 1 fragement
const int FRAGEMENT = 500;
const int page_length[5] = {100, 500, 1000, 2000, 5000};

int lrufunc(FILE*, const int);

int main(int argc, char* argv[]) {
    FILE* fp;
    int size = sizeof(page_length)/sizeof(int);
    if (argc != 2) {
        printf("Exit ERROR, Usage - da [FILE]\n");
        return -1;
    }
    if ((fp = fopen(argv[1], "r")) == NULL) {
        printf("Exit ERROR, file [%s] is not exist or have no permition to open it.\n", argv[1]);
        return -1;
    }
    for (int i = 0; i < size; i++) {
        lrufunc(fp, page_length[i]);
        fseek(fp, 0L, SEEK_SET);
    }
    fclose(fp);
    return 0;
}

int lrufunc(FILE* fp, const int size) {
    // an array to save pages
    int* pages = NULL;
    // page fault counter.
    int pagefault = 0;
    // all of pages.
    int pagecount = 0;
    // activity page.
    int tmp = 0;
    // point to oldest page.
    int oldest = 0;
    // point to current free page frame.
    int currpage = 0;
    // flag=0, current pages have activity page.
    // flag=1, current pages have no activity page.
    int flag = 1;
    // each of bits connect to pages.
    int* timers = (int*)malloc(size * sizeof(int));
    // page frames.
    pages = (int*)malloc(size * sizeof(int));
    for (int k=0; k<size; k++) {
        timers[k] = 0;
    }
    fscanf(fp, "%d", &tmp);
    while(!feof(fp)) {
        flag = 1;
        // search from existed pages.
        for (int i=0; i<currpage; i++) {
            if (tmp == pages[i]) {
                for (int k=0; k<size; k++) {
                    timers[k] += 1;
                }
                timers[i] = 0;
                flag = 0;
                break;
            }
        }
        // load activity page.
        if (flag) {
            // page fault.
            if (currpage == size) {
                int cnt = 0;
                int pit = 0;
                for (int k=0; k<size; k++) {
                    if (timers[k] > cnt) {
                        cnt = timers[k];
                        pit = k;
                    }
                }
                pages[pit] = tmp;
                for (int k=0; k<size; k++) {
                    timers[k] += 1;
                }
                timers[pit] = 0;
                pagefault++;
            }
            // add activity page to current free page frame.
            else {
                pages[currpage] = tmp;
                for (int k=0; k<size; k++) {
                    timers[k] += 1;
                }
                timers[currpage] = 0;
                currpage++;
            }
        }
        pagecount++;
        if (!(pagecount%FRAGEMENT)) {
            printf("%d,%f\n", pagecount, pagefault*1.0/pagecount);
        }
        fscanf(fp, "%d", &tmp);
    }
    free(pages);
    free(timers);
    printf("\n\n");
    return pagefault;
}
