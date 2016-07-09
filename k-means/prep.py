#-------------------------------------------------------------------------------
# Name:        data preprocessing
# Purpose:
#
# Author:      Ekira
#
# Created:     05/07/2016
# Copyright:   (c) Ekira 2016
# Licence:     <your licence>
#-------------------------------------------------------------------------------

# -*- coding: utf-8 -*- #

# 原始文件的550行有一条异常数据，字段长度为48

def main():
    table = []
    with open("kddcup_data_10_percent.txt", "rb") as f:
        for j in f:
            table.append(j.strip("\r\n").split(","))
    types = []
    for i in table:
        if i[41] not in types:
            types.append(i[41])
    print types     # result: ['normal.', 'smurf.', 'neptune.', 'ipsweep.', 'teardrop.']


    ## print len(table)
    ## a = table[0].split(",")
    ## print a

    '''
    cnt = 1
    for i in table:
        abc = i.strip("\r\n").split(",")
        if len(abc) != 42:
            print len(abc)  # 48
            print abc
            print cnt       # 550
        cnt += 1
        '''

if __name__ == '__main__':
    main()

