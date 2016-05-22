#include <stdio.h>
#include <stdlib.h>

// quantity of page frames
const int PAGE_LENGTH = 1000;
// length of 1 fragement
const int FRAGEMENT = 500;
// an array to save pages
int* pages = NULL;

int fifofunc(FILE*);
int clockfunc(FILE*);
int lrufunc(FILE*);

int main(int argc, char* argv[]) {
    FILE* fp;
    if (argc != 2) {
        printf("Exit ERROR, Usage - da [FILE]\n");
        return -1;
    }
    if ((fp = fopen(argv[1], "r")) == NULL) {
        printf("Exit ERROR, file [%s] is not exist or have no permition to open it.\n", argv[1]);
        return -1;
    }
    fifofunc(fp);
    fseek(fp, 0L, SEEK_SET);
    clockfunc(fp);
    fseek(fp, 0L, SEEK_SET);
    lrufunc(fp);
    fclose(fp);
    return 0;
}

int fifofunc(FILE* fp) {
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
    // page frames.
    pages = (int*)malloc(PAGE_LENGTH * sizeof(int));
    fscanf(fp, "%d", &tmp);
    while(!feof(fp)) {
        flag = 1;
        // search from existed pages.
        for (int i=0; i<currpage; i++) {
            if (tmp == pages[i]) {
                flag = 0;
                break;
            }
        }
        // load activity page.
        if (flag) {
            // page fault.
            if (currpage == PAGE_LENGTH) {
                pages[oldest] = tmp;
                oldest = oldest==PAGE_LENGTH-1 ? 0 : oldest+1;
                pagefault++;
            }
            // add activity page to current free page frame.
            else {
                pages[currpage] = tmp;
                currpage++;
            }
        }
        pagecount++;
        if (!(pagecount%FRAGEMENT)) {
            printf("%d,%f\n", pagecount, pagefault*1.0/pagecount);
        }
        fscanf(fp, "%d", &tmp);
    }
    printf("\n\n");
    free(pages);
    return pagefault;
}

int clockfunc(FILE* fp) {
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
    int* flagbits = (int*)malloc(PAGE_LENGTH * sizeof(int));
    // page frames.
    pages = (int*)malloc(PAGE_LENGTH * sizeof(int));
    for (int k=0; k<PAGE_LENGTH; k++) {
        flagbits[k] = 0;
    }
    fscanf(fp, "%d", &tmp);
    while(!feof(fp)) {
        flag = 1;
        // search from existed pages.
        for (int i=0; i<currpage; i++) {
            if (tmp == pages[i]) {
                flagbits[i] = 1;
                flag = 0;
                break;
            }
        }
        // load activity page.
        if (flag) {
            // page fault.
            if (currpage == PAGE_LENGTH) {
                flag = 1;
                for (int i=oldest; i<PAGE_LENGTH; i++) {
                    if (flagbits[i] == 1) {
                        flagbits[i] = 0;
                    }
                    else {
                        pages[i] = tmp;
                        flagbits[i] = 1;
                        oldest = i;
                        flag = 0;
                        break;
                    }
                }
                if (flag) {
                    for (int i=0; i<PAGE_LENGTH; i++) {
                        if (flagbits[i] == 1) {
                            flagbits[i] = 0;
                        }
                        else {
                            pages[i] = tmp;
                            flagbits[i] = 1;
                            oldest = i;
                            break;
                        }
                        
                    }
                }
                pagefault++;
            }
            // add activity page to current free page frame.
            else {
                pages[currpage] = tmp;
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
    free(flagbits);
    printf("\n\n");
    return pagefault;
}

int lrufunc(FILE* fp) {
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
    int* timers = (int*)malloc(PAGE_LENGTH * sizeof(int));
    // page frames.
    pages = (int*)malloc(PAGE_LENGTH * sizeof(int));
    for (int k=0; k<PAGE_LENGTH; k++) {
        timers[k] = 0;
    }
    fscanf(fp, "%d", &tmp);
    while(!feof(fp)) {
        flag = 1;
        // search from existed pages.
        for (int i=0; i<currpage; i++) {
            if (tmp == pages[i]) {
                for (int k=0; k<PAGE_LENGTH; k++) {
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
            if (currpage == PAGE_LENGTH) {
                int cnt = 0;
                int pit = 0;
                for (int k=0; k<PAGE_LENGTH; k++) {
                    if (timers[k] > cnt) {
                        cnt = timers[k];
                        pit = k;
                    }
                }
                pages[pit] = tmp;
                for (int k=0; k<PAGE_LENGTH; k++) {
                    timers[k] += 1;
                }
                timers[pit] = 0;
                pagefault++;
            }
            // add activity page to current free page frame.
            else {
                pages[currpage] = tmp;
                for (int k=0; k<PAGE_LENGTH; k++) {
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
