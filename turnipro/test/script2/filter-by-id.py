#!/bin/python2.7
# -*- coding: utf-8 -*-

# 用于拆分一个文件下的多个学号条目
# 拆分结果将保存在原文件目录下

import csv, itertools, os
import datetime, time
import operator

csvlist = [] # 用于保存一个所有ID的所有条目
with open("./data/2012CSAll_encoded.csv", 'rb') as f:
    readin = csv.reader(f)
    for row in readin:
        csvlist.append(row)
print "reading files completed"
print "source file has %s items" %(len(csvlist))

count = 0      
      
# 读取文件后对数据进行拆分
idpoint = csvlist[0][0]
idlist = []
for row in csvlist:
    if idpoint == row[0]:
        idlist.append(row)
    else:
        # 构造文件名
        count = count + 1
        print count
        filename = "./data/" + str(idlist[0][0]) + ".csv"
        print "write out %s" %(str(idlist[0][0]))
        # 写入文件
        with open(filename, 'wb') as f:
            writeout = csv.writer(f)
            writeout.writerows(idlist)
        # 重新初始化一个列表
        idlist = []
        idlist.append(row)
        idpoint = row[0]
        
# 构造文件名
filename = "./data/" + str(idlist[0][0]) + ".csv"
count = count + 1
print count
print "write out %s" %(str(idlist[0][0]))
# 写入文件
with open(filename, 'wb') as f:
    writeout = csv.writer(f)
    writeout.writerows(idlist)
print "completed"
print count
    

"""
all_csv_files = os.listdir("./data/")
i = 0
s = len(all_csv_files)
while i < s:
    all_csv_files[i] = "./data/" + all_csv_files[i]
    i += 1

for filename in all_csv_files:
    csv_list = []
    if filename[-4:] != ".csv":
        continue
    with open(filename, 'rb') as read_file:
        read_in = csv.reader(read_file)
        for row in itertools.islice(read_in, 1, None):
            meal_time = time.strftime("%Y/%m/%d %H:%M:%S", time.strptime(row[2], "%Y/%m/%d  %H:%M:%S"))
            row[2] = meal_time
            csv_list.append(row)
    csv_list.sort(key=operator.itemgetter(2))
    with open(filename, 'wb') as read_file:
        write_out = csv.writer(read_file)
        write_out.writerows(csv_list)
print "completed!"
"""     