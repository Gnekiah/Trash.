#! /bin/python2.7

# 删去非三餐消费的数据


import csv, itertools, os
import datetime, time
import operator

all_csv_files = os.listdir("./data/")
i = 0
s = len(all_csv_files)
while i < s:
    all_csv_files[i] = "./data/" + all_csv_files[i]
    i += 1

meal_type = []
with open("meal_type.csv", 'rb') as read_file:
    read_in = csv.reader(read_file)
    for row in read_in:
        meal_type.append(row[1])

for filename in all_csv_files:
    csv_list = []
    if filename[-4:] != ".csv":
        continue
    with open(filename, 'rb') as read_file:
        read_in = csv.reader(read_file)
        for row in read_in:
            if row[1] in meal_type:
                csv_list.append(row)
    with open(filename, 'wb') as read_file:
        write_out = csv.writer(read_file)
        write_out.writerows(csv_list)
    print "complete to %s" %(filename)
print "completed!"
        

    
    