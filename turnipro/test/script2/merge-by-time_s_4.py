#! /bin/python2.7

# 将时间间隔小于30min的消费数据进行合并


import csv, itertools, os
import datetime, time
import operator

all_csv_files = os.listdir("./data/")
i = 0
s = len(all_csv_files)
while i < s:
    all_csv_files[i] = "./data/" + all_csv_files[i]
    i += 1

# 初始化半小时的时间间隔
time_gap = datetime.datetime.strptime("2000/01/01 00:30:00", "%Y/%m/%d %H:%M:%S") - datetime.datetime.strptime("2000/01/01 00:00:00", "%Y/%m/%d %H:%M:%S")

for filename in all_csv_files:
    csv_list = []
    if filename[-4:] != ".csv":
        continue
        
    # 读取数据
    with open(filename, 'rb') as read_file:
        read_in = csv.reader(read_file)
        for row in read_in:
            csv_list.append(row)
            
    # 合并时间间隔小于time_gap的数据
    j = len(csv_list) - 1
    while j > 0:
        t = datetime.datetime.strptime(csv_list[j][2], "%Y/%m/%d %H:%M:%S") - datetime.datetime.strptime(csv_list[j-1][2], "%Y/%m/%d %H:%M:%S")
        if t < time_gap:
            f = float(csv_list[j-1][3]) + float(csv_list[j][3]) # 计算差值
            csv_list[j-1][3] = str(f)
            del csv_list[j]
        j -= 1
        
    # 写入数据
    with open(filename, 'wb') as read_file:
        write_out = csv.writer(read_file)
        write_out.writerows(csv_list)
    print "complete to %s" %(filename)
print "completed!"
        

    
    