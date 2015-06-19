#include <stdio.h>
#include <time.h>
#include <stdlib.h>

#define SIZE 10

void shellsort(int *a, int len);

int main() {
	int data[SIZE];
	srand(time(NULL));
	for (int i =0; i < SIZE; ++i) { 
		data[i] = rand()%1000;
		printf ("%d ", data[i]);
	}
	printf ("\n");
	shellsort(data, SIZE);
	for (int i =0; i < SIZE; ++i)
		printf ("%d ", data[i]);
	printf ("\n");
}

void shellsort(int *a, int len) {
	int k, tmp;
	for (int i = len/2; i >0; i /= 2) 
		for (int j = i; j <len; ++j) {
			tmp = a[j];
			k = j - i;
			while (k >=0 && tmp < a[k]) {
				a[k+i] = a[k];
				k -= i;
			}
			a[k+i] = tmp;
		}	
}

