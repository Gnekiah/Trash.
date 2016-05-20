/**
 * clone ????
 * 
 */


#include <unistd.h>
#include <stdio.h>
#include <signal.h>
#include <linux/sched.h>

#define FIBER_STACK 8192

int a;
void* stack;

int doing() {
    printf("this kid, pid=%d, a=%d\n", getpid(), ++a);
    free(stack);
    return 0;
}

int main() {
    void* stack;
    a = 1;
    stack = malloc(FIBER_STACK);
    if (!stack) {
        printf("failed.\n");
        return 0;
    }
    printf("create kid.\n");
    clone(&doing, (char*)stack+FIBER_STACK, CLONE_VM | CLONE_VFORK, 0);
    printf("father, pid=%d, a=%d\n", getpid(), a);
    return 0;
}   
