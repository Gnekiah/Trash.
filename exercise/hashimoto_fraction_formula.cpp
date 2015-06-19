/* Using digits from '1' to '9' to fill in the formula:
** a/bc + d/ef = g/hi
** To make the formula ture.
** And figures can't be repeated.
*/


#include <stdio.h> 

int func(int n);
int num[10], div[3], ctr = 0;

int main() {
	func(1);
	return 0;
}

int func(int n) {
	if ( n >9) return 0;
	for (int i =1; i <10; ++i) {
		num[n] = i;
		for (int j =1; j <n; ++j) 
			if (num[n] == num[j]) goto next;
		if (n == 9 && num[1] < num[4] && (num[1]*(div[1] = num[5]*10 + num[6])*(div[2] = num[8]*10 + num[9])+num[4]*(div[0] = num[2]*10 + num[3])*div[2]==num[7]*div[0]*div[1]))
		printf ("%2d:%d/%d+%d/%d=%d/%d\n",++ctr,num[1],div[0],num[4],div[1],num[7],div[2]);
		else func(n+1);
	next: ;
	}
	return 0;
}
