#include <stdio.h>
#include <stdlib.h>

const int flag_off = 0;
const int flag_ing = -1;
const int succe = 1;

int linecounter(char filename[]);
int check(char line[], int flag);

int main(int argc, char *argv[]) {
	for (int i =1; i < argc; ++i) 
		printf ("<%s>= %d\n", argv[i],linecounter(argv[i]));
	return 0;
}

int linecounter(char filename[]) {
	FILE *fp;
	int  liner = 0;
	int  p = 0;
	char str[256];
	if (!(fp = fopen(filename, "r"))) {
		printf ("ERROR: No such file or directory!\n");
		exit(1);	
	}
	while (!feof(fp)) {
		fgets(str, 0xff, fp);
		p = check(str, p);
		if (p == 1) {
			++liner;
			p = 0;
		}
	}
	fclose(fp);
	return liner;
}

int check(char line[], int flag) {
	int addon =0;
	if (flag == -1) {
		for (int i =0; line[i]; ++i) {
			if (line[i] == '*')
				if (line[i+1] == '/')
					return flag_off;
		}
		return flag_ing;
	}
	for (int i = 0; line[i]; ++i) {
		if (line[i] !=9 && line[i] !=32 && line[i] != '\n')
			addon = 1;
		if (line[i] == '/') {
			if (line[i+1] == '/')
				return flag_off;
			if (line[i+1] == '*')
				return flag_ing;
		}
	}
	return (addon == 0)? flag_off: succe;
}

