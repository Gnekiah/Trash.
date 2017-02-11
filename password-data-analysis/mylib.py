#!/bin/python2.7
#coding=gbk

import os, sys
import chardet

class File_operation:
    # Get some lines form file
    # filename: the one you wanna read
    # start_line: read out begin with start_line from file
    # end_line: read out end with end_line
    def get_lines_from_file(self, filename, start_line=0, end_line=sys.maxint):
        with open(filename, 'rb') as f:
            rows = []
            i = 0
            while i < start_line:
                i += 1
                if not f.readline():
                    return None
            while i < end_line:
                i += 1
                # tip: strip() Return a copy of the string with leading and trailing characters removed.
                # tmp = f.readline().strip('\n').strip('\r')
                # Remove all of the \r\n and replace \t with space
                tmp = f.readline().replace('\r','').replace('\n','').replace('\t',' ')
                if not tmp:
                    break
                rows.append(tmp)
        return rows

    # Writing error logs into file.
    # Some errors like incompleted, unrecognized, unhandled, etc.
    # row: an item that could not dealed.
    # originfile: record where the item from.
    # position: location of the error line.
    # filename: optional. describe where to save error logs.
    def writing_errorlog(self, row, originfile, position, filename='__tmp_errorlog_'):
        if filename == '__tmp_errorlog_':
            filename = filename + originfile
        with open(filename, 'a') as f:
            f.write(row + '\t' + originfile + ' lines: ' + str(position) + '\n')
     
    # Writing error logs into file.
    # row: an item that could not dealed.
    # filename: optional. describe where to save error logs.
    def writing_errorlines(self, row, filename='__tmp_errorlog_'):
        with open(filename, 'a') as f:
            f.write(row + '\n')
            
    # Writing id and password to file.
    # rows: a formatted list with id column and password column.
    # filename: to save list
    # id_col: the id column
    # passwd_col: the password column
    def put_idpasswd_to_file(self, rows, filename, id_col=0, passwd_col=1):
        with open(filename, 'a') as f:
            for row in rows:
                f.write(row[id_col] + '\t' + row[passwd_col] + '\n')


class Format_operation:                
    def is_ascii(self, s):
        return all(ord(c) < 128 for c in s)
        
    def is_ascii2(self, s):
        try:
            s.decode('ascii')
        except UnicodeDecodeError:
            return False
        else:
            return True
 
    def check_charset(self, filename):
        f = open(filename, 'r')
        data = f.read()
        check = chardet.detect(data)
        f.close()
        return check

                
                