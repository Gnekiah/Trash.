#!/bin/python2.7
#coding=gbk

import os, sys
# from chardet import detect

# delete repeating and sort the file: filename
def liset_sort(filename):
    lists = []
    with open(filename, 'rb') as f:
        for i in f:
            lists.append(i)
    l2 = list(set(lists))
    l2.sort()
    with open(filename+'wangyi.cal.txt', 'a') as f:
        for i in l2:
            f.write(i.strip('\n').strip('\r') + '\n')
    print len(lists)
    print len(l2)

# cut origin data to {data1 2 3 4 5}
def cutu():
    filename = 'wangyi.txt'
    count = 0
    vet = 1
    lists = []
    with open(filename, 'rb') as f:
        for i in f:
            lists.append(i)
            count += 1
            if count == 20000000*vet:
                vet += 1
                with open(filename+str(vet)+'.txt', 'w') as f1:
                    for j in lists:
                        f1.write(j.strip('\n').strip('\r')+'\n')
                lists = []
    with open(filename+'_last.txt', 'w') as f2:
        for k in lists:
            f2.write(k.strip('\n').strip('\r')+'\n')

# merge filename1 and filename2, delete repeating and sort
def liset_sort_2(filename1, filename2):
    lists = []
    with open(filename1, 'rb') as f:
        for i in f:
            lists.append(i)
    with open(filename2, 'rb') as f:
        for i in f:
            lists.append(i)
    l2 = list(set(lists))
    l2.sort()
    with open(filename1+'wangyi.cal.txt', 'a') as f:
        for i in l2:
            f.write(i.strip('\n').strip('\r') + '\n')
    print len(lists)
    print len(l2)

# lists all .txt files from ./ , get password and sort, writting to passwd.txt
def getpasswd():
    allfiles = os.listdir("./")
    lists = []
    for filename in allfiles:
        if filename[-4:] != ".txt":
            continue
        with open(filename, 'rb') as f:
            for row in f:
                tmp = row.strip('\n').strip('\r').split('\t')
                lists.append(tmp[1])
    with open('passwd.txt', 'a') as f:
        lists.sort()
        for row in lists:
            f.write(row+'\n')



# run again
# liset_sort_2('wangyi.txt1.txt', 'wangyi.txt2.txt')
# 运行完上一个函数后，将文件移动到去重+排序中，并删掉wangyi.txt1.txt和wangyi.txt2.txt
# 之后移动cal.py到目录，删掉这几行中文，并运行以下函数
# getpasswd()




# delete repeating and sort files {1 2 3 4 5} 
'''
allfiles = os.listdir("./")
for filename in allfiles:
    if filename[-4:] != ".txt":
        continue
    liset_sort(filename)
'''



