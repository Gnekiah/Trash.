#! /bin/python2.7

# 检测成绩表学号与统计信息学号相同数

import csv, itertools, os
import datetime, time
import operator

csvList = []
id = []

all_csv_files = os.listdir("./stu_data/")
for i in all_csv_files:
    id.append(i[:-4])

with open("count_info.csv", 'rb') as read_file:
    read_in = csv.reader(read_file)
    for row in itertools.islice(read_in, 1, None):
        csvList.append(row[0])

flag = 0
        
for i in csvList:
    if i in id:
        flag += 1
    else:
        print i

print "ID_number = " + str(len(id))
print "csv_list = " + str(len(csvList))
print "The number of students' ID pairing is: " + str(flag)