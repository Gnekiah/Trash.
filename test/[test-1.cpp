#include <stdio.h>

long fact(int n);

int main() {
	int n;
	printf ("?n=");
	scanf ("%ld", &n);
	printf ("n!=%ld\n",fact(n));
	return 0;
}

long fact(int n) {
	return (n < 1) ? 1: n * fact(--n);
}
