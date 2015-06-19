/*
 * Calculation of the two large integer subtraction
 *
 * Character coding: UTF-8
 * Compile with gcc 4.9.2
 *
 * 20135134  熊熊 (10)
 * 20132963  文俊 (8.5)
 * 20132945  田鹏 (7.5)
 * 
 */


#include <stdio.h>
#include <ctype.h>
#include <stdlib.h>
#include <string.h>

#define LENGTH_MAX 301

/* Check if all inputs are digits or not.*/
void check_digit(char x[LENGTH_MAX]); 
/* Delete all zero behind the first not-zero digit.*/
int  delete_zero(char x[LENGTH_MAX]);
/* Compare if subtrahend is smaller than minuend.*/
int compare(char a[LENGTH_MAX], char b[LENGTH_MAX]);
/* Substraction calculating.*/
void substraction(char a[LENGTH_MAX], char b[LENGTH_MAX]);

int main() {
    char minuend[LENGTH_MAX];
    char subtrahend[LENGTH_MAX]; 

    printf("Minuend= ");
    scanf ("%s", minuend);
    printf("Subtrahend= ");
    scanf ("%s", subtrahend);
    if (strlen(minuend) > LENGTH_MAX || strlen(subtrahend) > LENGTH_MAX) {
        printf ("ERROR, your input is bigger than the length of max.\n");
        exit(EXIT_FAILURE);
    }
 
/* Check the validity of inputs.*/   
    check_digit(minuend);
    check_digit(subtrahend);
    delete_zero(minuend);
    delete_zero(subtrahend);
    compare(minuend, subtrahend);

/* Substraction calculating.*/
    substraction(minuend, subtrahend);
    delete_zero(minuend);

    if (!strlen(minuend))
        printf ("Result= 0\n");
    else
        printf ("Result= %s\n", minuend);
    return EXIT_SUCCESS;
}

/* Check if all inputs are digits or not.*/
void check_digit(char x[LENGTH_MAX]) {
    for (int i = 0; x[i]; ++i) 
        if (!isdigit(x[i])) {
            printf ("ERROR, your inputs should be all of digits.\n");
            exit(EXIT_FAILURE);
        }
}

/* Delete all zero behind the first not-zero digit.*/
int delete_zero(char x[LENGTH_MAX]) {
    int flag = 0;
    for (; x[flag] == 0x30; ++flag) {}
    if (!flag)
        return true;
    for (int i = flag; x[i-1]; ++i) 
        x[i-flag] = x[i];
    return true;
}

/* Compare if subtrahend is smaller than minuend.*/
int compare(char a[LENGTH_MAX], char b[LENGTH_MAX]) {
    int len = strlen(a) - strlen(b);
    if (len > 0)
        return true;
    if (len < 0 || strcmp(a, b) < 0) {
        printf ("ERROR, minuend should bigger than subtrahend.\n");
        exit(EXIT_FAILURE);
    }
    return true;
}

/* Substraction calculating.*/
void substraction(char a[LENGTH_MAX], char b[LENGTH_MAX]) {
    int len_a = strlen(a) - 1;
    int len_b = strlen(b) - 1;
    for (; len_b > -1; --len_b, --len_a) {
        if (a[len_a] >= b[len_b])
            a[len_a] = (a[len_a] - b[len_b]) + 0x30;
        else {
            a[len_a] = (b[len_b] - a[len_a]) + 0x30;
            int i = 1;
            for (; a[len_a-i] == 0x30; ++i) 
                a[len_a-i] = 9;
            --a[len_a-i];
        }
    }
}
