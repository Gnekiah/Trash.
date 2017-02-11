#! /bin/python2.7

# 输出所有消费类型

import csv, string, os

all_csv_files = os.listdir("./data/")
i = 0
s = len(all_csv_files)
while i < s:
    all_csv_files[i] = "./data/" + all_csv_files[i]
    i += 1
    
csv_file = file('consume_type.csv', 'wb')
file_object = csv.writer(csv_file, dialect='excel')
consume_type_list = []

for filename in all_csv_files:
    if filename[-4:] != ".csv":
        continue
    with open(filename, 'rb') as read_file:
        read_in = csv.reader(read_file)
        for row in read_in:
            flag = 0 # 用于标记有无重复输出数据，1表示重复，0表示无重复
            for type_iter in consume_type_list:
                if type_iter == row[1]:
                    flag = 1
                    break
            if flag == 0:
                consume_type_list.append(row[1])
                tmp = []
                # 重复3行，匹配步骤时取中间一行 || 用于解决字符编码导致的匹配问题
                tmp.append(row[1])
                tmp.append(row[1])
                tmp.append(row[1])
                file_object.writerow(tmp)
                print tmp
                
csv_file.close() 
print "competed!"