#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define MAX 100

char *bitcode(char *str, char n);

int main() {
	char n, srcstr[MAX];
	while (1) {
		fflush(stdin);
		printf ("?n=");
		n = getchar();
		getchar();
		printf ("?S=");
		scanf ("%s",srcstr);
		printf ("%s\n", bitcode(srcstr, n));
	}
	return 0;
}


char *bitcode(char *str, char n) {
	int len = strlen(str);
	char *wen;
	if ((wen = (char *)malloc(len+1)) == NULL) {
		printf ("Failed to get memory!\n");
		exit(1);
	}
	for (int i =0; i < len; ++i)
		wen[i] = str[i] ^ n;
	wen[len] = '\0';
	return wen;
}
