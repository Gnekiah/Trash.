#! /bin/python2.7
# -*- coding:utf-8 -*-

import csv
from itertools import islice
from time import strftime, strptime
from operator import itemgetter


"""
all data type:
int                 i
float               f
string              s
list                l
dictionary          d
tuple               t
file pointer        fp
set                 se
None                n
bool                b
void                v
"""


def load_csvlist(csv_path, ishead=True):
    """load csv file
    # Args:
        csv_path: file's path
        ishead: default True, True - csv file have head, False - without head
    # Returns:
        a csv list
    """
    csv_list = []
    with open(csv_path, 'rb') as f:
        f_csv = csv.reader(f)
        for row in islice(f_csv, 1, None):
            csv_list.append(row)
    return csv_list


def load_csvhead(csv_path):
    """load csv head
    # Args:
        csv_path: file's path
    # return:
        a head list
    """
    with open(csv_path, "rb") as f:
        f_csv = csv.reader(f)
        for row in f_csv:
            return row


def write_csvfile(csv_path, csv_list, csv_head=None):
    """write csv list to file
    # Args:
        csv_path: path to write
        csv_list: csv list
        csv_head: default=None
    """
    with open(csv_path, "wb") as f:
        if csv_head is not None:
            s = ""
            for i in csv_head:
                s += str(i) + ','
            f.write(s[:-1])
            f.write("\n")
        for i in csv_list:
            s = ""
            for j in i:
                s += str(j) + ','
            f.write(s[:-1])
            f.write("\n")


def round4(org1, org2):
    """divide org1 by org2 and round 4
    # Args:
        org1: val1
        org2: val2
    # Returns:
        round 4
    """
    return roundx(org1, org2, 4)


def round2(org1, org2):
    """divide org1 by org2 and round 2
    # Args:
        org1: val1
        org2: val2
    # Returns:
        round 2
    """
    return roundx(org1, org2, 2)


def roundx(org1, org2, x):
    """divide org1 by org2 and round x
    # Args:
        org1: val1
        org2: val2
    # Returns:
        round x
    """
    if org2 == 0 or org2 == 0.0:
        return 0.0
    return round(float(org1) / org2, x)


def gbk2utf(s, isfile=False):
    """transfer gbk to utf-8
    # Args:
        s: a string or a text file name
        isfile: check if s is a file name
    # Returns:
        if isfile is True, return None, else, return utf-8 coding string
    """
    if isfile:
        text = ""
        with open(s, 'rb') as f:
            text = f.read()
        text = text.decode('gbk').encode('utf-8')
        with open(s+".tsf", "wb") as f:
            f.write(text)
    else:
        return s.decode('gbk').encode('utf-8')


def utf2gbk(s, isfile=False):
    """transfer utf-8 to gbk
    # Args:
        s: a string or a text file name
        isfile: check if s is a file name
    # Returns:
        if isfile is True, return None, else, return gbk coding string
    """
    if isfile:
        text = ""
        with open(s, 'rb') as f:
            text = f.read()
        text = text.decode('utf-8').encode('gbk')
        with open(s+".tsf", "wb") as f:
            f.write(text)
    else:
        return s.decode('utf-8').encode('gbk')


def format_time(org, origin="%Y/%m/%d", target="%Y/%m/%d %H:%M:%S"):
    """format a time string
    # Args:
        org: a time string, eg: '2015/10/20 18:10:30'
        origin: the format of inputting org, eg: '%Y/%m/%d %H:%M:%S'
        target: the format of return, eg: '%Y/%m/%d %H:%M:%S'
    # Returns:
        a formatted time string, eg: '2015/10/20 18:10:30'
    """
    return strftime(target, strptime(org, origin))


def format_times(orgs, column, origin="%Y/%m/%d", target="%Y/%m/%d %H:%M:%S"):
    """format a list with time string
    # Args:
        orgs: a list with time column, eg: [[8,'2015/10/20'],[3,'2015/10/20']]
        column: the column of list that with time string
        origin: the format of inputting org, eg: '%Y/%m/%d %H:%M:%S'
        target: the format of return, eg: '%Y/%m/%d %H:%M:%S'
    # Returns:
        a time formatted list
    """
    for i in orgs:
        i[column] = strftime(target, strptime(i[column], origin))
    return orgs


def list_sort(orgs, column, reverse=False):
    """sort a list according to a pointted column
    # Args:
        orgs: inputted list
        column: according to the column to sorting
        reverse: False - sort in ascending order, True - in descending order
    # Returns:
        a sortted list
    """
    orgs.sort(key=itemgetter(column))
    if reverse:
        return orgs[::-1]
    return orgs






