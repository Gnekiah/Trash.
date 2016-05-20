#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <time.h>
#include <stdlib.h>

pthread_mutex_t tmutex[5] = {PTHREAD_MUTEX_INITIALIZER};
pthread_t pid[5];
int mutexcount = 0;

void* func(void* args) {
    int arc = (int*)args;
    srand((unsigned int)time(NULL));
    while(1) {
        sleep(5);
        if (lockfull()) {
            continue;
        }
        if (arc == 0) {
            pthread_mutex_lock(&tmutex[4]);
            mutexcount++;
            printf("I am zhexuejia: %d, I get kuaizi: %d, time is %ld.\n", arc, 4, (long)time(NULL));
            pthread_mutex_lock(&tmutex[0]);
            mutexcount++;
            printf("I am zhexuejia: %d, I get kuaizi: %d, time is %ld.\n", arc, 0, (long)time(NULL));
            pthread_mutex_unlock(&tmutex[4]);
            mutexcount--;
            pthread_mutex_unlock(&tmutex[0]);
            mutexcount--;
            continue;
        }
        if (arc == 4) {
            pthread_mutex_lock(&tmutex[3]);
            mutexcount++;
            printf("I am zhexuejia: %d, I get kuaizi: %d, time is %ld.\n", arc, 3, (long)time(NULL));
            pthread_mutex_lock(&tmutex[4]);
            mutexcount++;
            printf("I am zhexuejia: %d, I get kuaizi: %d, time is %ld.\n", arc, 4, (long)time(NULL));
            pthread_mutex_unlock(&tmutex[3]);
            mutexcount--;
            pthread_mutex_unlock(&tmutex[4]);
            mutexcount--;
            continue;
        }
        pthread_mutex_lock(&tmutex[arc-1]);
        mutexcount++;
        printf("I am zhexuejia: %d, I get kuaizi: %d, time is %ld.\n", arc, arc-1, (long)time(NULL));
        pthread_mutex_lock(&tmutex[arc]);
        mutexcount++;
        printf("I am zhexuejia: %d, I get kuaizi: %d, time is %ld.\n", arc, arc, (long)time(NULL));
        pthread_mutex_unlock(&tmutex[arc-1]);
        mutexcount--;
        pthread_mutex_unlock(&tmutex[arc]);
        mutexcount--;
    }
}

int lockfull() {
    return mutexcount>3 ? 1 : 0;
}

int main() {
    for (int i=0; i<5; i++) {
        if (pthread_create(&pid[i], NULL, func, (void*)i)) {
            return -1;
        }
    }
    while(1) {
        sleep(10);
    }
    return 0;
}
