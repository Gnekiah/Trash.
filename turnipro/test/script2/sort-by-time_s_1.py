#! /bin/python2.7

# 按照时间顺序对每个表进行排序,并格式化时间  
# 排序结果将删除每个表的第一行


import csv, itertools, os
import datetime, time
import operator

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
        for row in read_in:
            meal_time = time.strftime("%Y/%m/%d %H:%M:%S", time.strptime(row[2], "%Y/%m/%d  %H:%M:%S"))
            row[2] = meal_time
            csv_list.append(row)
    csv_list.sort(key=operator.itemgetter(2))
    print "complete to %s" %(filename)
    with open(filename, 'wb') as read_file:
        write_out = csv.writer(read_file)
        write_out.writerows(csv_list)
print "completed!"
        

    
    