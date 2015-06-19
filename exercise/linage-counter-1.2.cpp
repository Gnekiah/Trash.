/* Using for counting how many lines you C or C++ files.
   (exclude blanklines or comments)

USAGE:
	cc			Count all of the files in current DIR.
	cc [filename]		Count files appointed.
*/

#include <stdio.h>
#include <stdlib.h>

const int FILENAME_LENGTH = 50;  // define the max length of filename read
const int CHARS_MAX       = 256; // define the max length read from a line
const int SIGNAL_OK       = 0;   // current line is effective code
const int SIGNAL_ON       = 1;   // current line is in the comment of '/*'
const int SIGNAL_OFF      = 2;   // current line is the comment of '//','*/' or blankline

int file_count(char filename[]);
int line_count(char line[], int signal);

int main(int argc, char *argv[]) {
	int lines       = 0;
	int lines_total = 0;
	char filename[FILENAME_LENGTH];
	FILE *fp;
	if (argc == 1) {
		system("ls | grep .cpp > .tmp_cc");
		if (!(fp = fopen(".tmp_cc", "r"))) {
			printf ("ERROR: Failed to get files information!\n");
			exit(1);
		}
		while (!feof(fp)) {
			fgets(filename, CHARS_MAX, fp);
			for (int i = 0; filename[i]; ++i) 
				if (filename[i] == '\n')
					filename[i] = '\0';
			if ((lines = file_count(filename)))
				printf ("\033[32m%4d\033[37m  ->%s\n", lines, filename);
			lines_total += lines;
		}
		fclose(fp);
		system("rm .tmp_cc");
	}
	else 
		for (int i = 1; i < argc; ++i) {
			if ((lines = file_count(argv[i])))
				printf ("\033[32m%4d\033[37m  ->%s\n", lines, argv[i]);
			lines_total += lines;
		}
	printf ("\033[32m%4d\033[37m  ->TOTAL\n", lines_total);
	return 0;
}

// Count for each file inputted
int file_count(char filename[]) {
	FILE *fp;                 // file pointer
	char str[CHARS_MAX];      // maxium of a line
	int  lines  = 0;          // linage
	int  signal = SIGNAL_OFF; // signal that convert to line_count function
	if (!(fp = fopen(filename, "r"))) {
		printf ("ERROR <%s>: No such file!\n", filename);
		return 0;
	}
	while (!feof(fp)) {
		fgets(str, CHARS_MAX, fp);
		if ((signal = line_count(str, signal)) == SIGNAL_OK) {
			++lines;
			signal = SIGNAL_OFF;
		}
	}
	fclose(fp);
	return lines;
}

// Check out each line, key_chars are '/', '*', '\n', '{', '}', SPACE, TAB
int line_count(char line[], int signal) {
	if (signal == SIGNAL_ON) {
		for (int i = 0; line[i]; ++i) 
			if (line[i] == '*' && line[i+1] == '/')
				return SIGNAL_OFF;
		return SIGNAL_ON;
	}
	for (int i = 0; line[i]; ++i) {
		if (line[i] != 9 && line[i] != 32 && line[i] != '\n' && line[i] != '}' && line[i] != '{' && line[i] != '/' && line[i] != '*') 
			return SIGNAL_OK;
		if (line[i] == '/') {
			if (line[i+1] == '/')
				return SIGNAL_OFF;
			if (line[i+1] == '*')
				return SIGNAL_ON;
		}
	}
	return SIGNAL_OFF;
}

