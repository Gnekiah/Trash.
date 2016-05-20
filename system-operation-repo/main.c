/**
 * wtf???
 * I do not know whay I created this file ...
 *
 */


#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>

void thread1(void) {
    int n = 100;
   int h=0,leap=1,k,m,i;
   printf("the prime number between 1~%d is:\n",n);
   for(m=2;m<=n;m++)
  {
       // k=sqrt(m+1);
        for(i=2;i<=m/2;i++)
        {
            if(m%i==0)
            {
               leap=0;break;
            }  
        }   
      if(leap)
      {
         printf("%4d",m);
         h++;
        if(h%10==0)
           printf("\n");
      }
    leap=1;
  }
  printf("\nthread1 exit!\n");
  pthread_exit(0);
}

void thread2(void) {
    int fib0=0,fib1=1,fib2,i;
    int N = 10;
   scanf("%d",&N);
   printf("the fib sequence as following:\n");
   for(i=0;i<N;i++)
   {
      if(i==0)
      {
         printf("0 ");
       }
       else if(i==1)
       { 
          printf("1 ");
       }
       else
       {
          fib2=fib0+fib1;
          printf("%d ",fib2);
          fib0=fib1;
          fib1=fib2;
        }
   }
    printf("\nthread2 exit!\n");
    pthread_exit(0);
}

int main() {
    printf("I'm pid 1, my pid=%d, my father pid=%d\n", getpid(), getppid());
    pid_t   pid2 = fork();
    if (pid2 < 0) {
        perror("ERROR point 1\n");
        exit(EXIT_FAILURE);
    }

    // pid2 running.
    if (pid2 == 0) {
        printf("I'm pid 2, my pid=%d, my father pid=%d\n", getpid(), getppid());
        system("ls -l");
        system("ps -aux");
        system("cp a.out b.out");
    }
    // main pid.
    else {
        pid_t   pid3 = fork();
        if(pid3 < 0) {
            perror("ERROR point 2\n");
            exit(EXIT_FAILURE);
        }
        
        // pid3 running
        if(pid3 == 0) {
            printf("I'm pid 3, my pid=%d, my father pid=%d\n", getpid(), getppid());
            pid_t   pid4 = fork();
            if (pid4 < 0) {
                perror("ERROR point 3\n");
                exit(EXIT_FAILURE);
            }

            // pid4 running
            if (pid4 == 0) {
                printf("I'm pid 4, my pid=%d, my father pid=%d\n", getpid(), getppid());
                int ret1=0, ret2=0;
                pthread_t id1, id2;
                ret1 = pthread_create(&id1, NULL, (void*)thread1, NULL);
                ret2 = pthread_create(&id2, NULL, (void*)thread2, NULL);
                if(ret1 || ret2) {
                    return EXIT_FAILURE;
                }
                pthread_join(id1, NULL);
                pthread_join(id2, NULL);
            }
            // pid3 running
            else {
                pid_t pid5 = fork();
                if (pid5 < 0) {
                    perror("ERROR point 4\n");
                    exit(EXIT_FAILURE);
                }

                // pid5 running
                if (pid5 == 0) {
                    printf("I'm pid 5, my pid=%d, my father pid=%d\n", getpid(), getppid());
                    execl("./helloworld", NULL);
                }       
            }
        }
    }
    return EXIT_SUCCESS;
}

   
