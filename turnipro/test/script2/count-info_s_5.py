#! /bin/python2.7

import csv, string, itertools, os
import datetime, time
import operator


# 获取所有.csv文件名
all_csv_files = os.listdir("./data/")
i = 0
s = len(all_csv_files)
while i < s:
    all_csv_files[i] = "./data/" + all_csv_files[i]
    i += 1

# 创建一个文件保存结果数据    
csv_file = file('count_info.csv', 'wb')
file_object = csv.writer(csv_file, dialect='excel')
file_object.writerow(['STU_ID', '三餐总消费', '起始时间', '终止时间', '消费天数',
                      '三餐消费次数', '三餐日均消费次数', '日均消费金额', '每次消费金额'])

for filename in all_csv_files:
    if filename[-4:] != ".csv":
        continue
    row_list = []   # 用于保存每个ID的一行信息
    ID = ""         # 用于记录学生ID
    sumup = 0.0     # 记录三餐消费总金额
    time_beg = ""   # 记录消费起始时间
    time_end = ""   # 记录消费终止时间
    time_count = 0.0# 记录消费次数
    days_count = [] # 记录消费日期
    mark = 0        # 用于标记是否为第一次读取文件
    with open(filename, 'rb') as read_file:
        read_in = csv.reader(read_file)
        for row in read_in:
            if mark == 0:  
                # 获取ID            
                ID = row[0]
                # 获取起始时间
                time_beg = row[2]
                mark = 1

            # 统计每个ID三餐的消费总数
            sumup -= float(row[-1])
            
            # 记录终止时间
            time_end = row[2]
            
            # 记录消费次数
            time_count += 1
            
            # 格式化时间，记录消费日期
            which_day = time.strftime("%Y/%m/%d", time.strptime(row[2], "%Y/%m/%d %H:%M:%S"))
            if which_day in days_count:
                pass
            else:
                days_count.append(which_day)

    row_list.append(ID)
    row_list.append(sumup)
    row_list.append(time_beg)
    row_list.append(time_end)
    row_list.append(len(days_count))
    row_list.append(time_count)
    row_list.append(time_count / len(days_count))
    row_list.append(sumup / len(days_count))
    row_list.append(sumup / time_count)
    file_object.writerow(row_list)
        
csv_file.close()
print "competed!"




