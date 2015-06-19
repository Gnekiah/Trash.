/*
 * 20135134 –‹–‹ £®10£©
 * 20132945 ÃÔ≈Ù £®8.5£©
 * 20132963 Œƒø° £®7.5£©
 *
 */ 


#include <stdio.h>
#include <stdlib.h>
#include <string.h>

const int nameWeight = 10;

typedef struct {
	char  name[nameWeight];
	float score;
}STUDENT;

int cmp(const void* a, const void* b) {
	return ((STUDENT*)a)->score-((STUDENT*)b)->score;
}

int main() {
	int n = 0;
	printf ("n= ");
	scanf ("%d", &n);
	STUDENT* student = (STUDENT*)malloc(sizeof(STUDENT) * n);

	for (int i = 0; i < n; ++i) {
		printf ("Name: ");
		scanf ("%s", student[i].name);
		printf ("Score: ");
		scanf ("%f", &student[i].score);
	}
	
	qsort(student, n, sizeof(STUDENT), cmp);
	
	for (int i = (n/2)+1, j = n/2; ; ++i, --j) {
		if (student[i].score == student[n/2].score) {
			printf ("name: %10s   score: %3.1f\n",student[i].name, student[i].score);
		}
		else if (student[j].score == student[n/2].score) {
			printf ("name: %10s   score: %3.1f\n",student[j].name, student[j].score);
		}
		else break;
	}
	free(student);
	return EXIT_SUCCESS;
}
		
