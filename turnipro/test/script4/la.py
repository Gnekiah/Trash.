#! /bin/python2.7
# 输出单文件的数据表

import csv, itertools, os
import datetime, time
import operator


csvList = []
with open("count_info_with_score.csv", 'rb') as read_file:
    read_in = csv.reader(read_file)
    for row in itertools.islice(read_in, 1, None):
        csvList.append(row)

        
# 获取所有.csv文件名
all_csv_files = os.listdir("./consume_data_v2/")
i = 0
s = len(all_csv_files)
while i < s:
    all_csv_files[i] = "./consume_data_v2/" + all_csv_files[i]
    i += 1
row_list = []   # 用于保存每个ID的一行信息,学号,13.1消费beg,13.1消费end,13.9消费beg,13.9消费end,
                # 14.1消费beg,14.1消费end,14.9消费beg,14.9消费end
for filename in all_csv_files:
    ID = filename[-12:-4]         # 用于记录学生ID
    tmp_list = []
    date_sig = ["2013/03/01","2013/08/01","2013/09/01","2014/02/01","2014/03/01","2014/08/01","2014/09/01","2015/02/01"]
    date_poi = ["2013/08/01","2013/03/01","2014/02/01","2013/09/01","2014/08/01","2014/03/01","2015/02/01","2014/09/01"]
    with open(filename, 'rb') as read_file:
        read_in = csv.reader(read_file)
        for row in read_in:
            # 格式化时间，记录消费日期
            which_day = time.strftime("%Y/%m/%d", time.strptime(row[2], "%Y/%m/%d %H:%M:%S"))
            if which_day > date_sig[0] and which_day < date_poi[0]:
                date_poi[0] = which_day
            elif which_day > date_sig[2] and which_day < date_poi[2]:
                date_poi[2] = which_day
            elif which_day > date_sig[4] and which_day < date_poi[4]:
                date_poi[4] = which_day
            elif which_day > date_sig[6] and which_day < date_poi[6]:
                date_poi[6] = which_day
            else:
                pass
          
            if which_day < date_sig[1] and which_day > date_poi[1]:
                date_poi[1] = which_day
            elif which_day < date_sig[3] and which_day > date_poi[3]:
                date_poi[3] = which_day
            elif which_day < date_sig[5] and which_day > date_poi[5]:
                date_poi[5] = which_day
            elif which_day < date_sig[7] and which_day > date_poi[7]:
                date_poi[7] = which_day
            else:
                pass
    tmp_list.append(ID)
    for item in date_poi:
        tmp_list.append(item)
    row_list.append(tmp_list)

shit_rows = []
    
for info_row in csvList:
    for date_row in row_list:
        tmp_row = []
        if date_row[0] == info_row[0]:
            flag = False
            tmp_row.append(info_row[0])
            tmp_row.append(info_row[1])
            tmp_row.append(info_row[2])
            tmp_row.append(info_row[3])
            tmp_row.append(date_row[1])
            tmp_row.append(date_row[2])
            for i in range(0, 4):
                tmp_row.append(info_row[i*5+13])
                tmp_row.append(info_row[i*5+15])
                tmp_row.append(info_row[i*5+14])
                if info_row[14] == "0":
                    flag = True
                if info_row[i*5+14] == "0":
                    tmp_row.append(0)
                else:
                    tmp_row.append(float(info_row[i*5+15]) / float(info_row[i*5+14]))
                tmp_row.append(info_row[i*5+16])
                tmp_row.append(info_row[i*5+17])
                if i > 0:
                    if tmp_row[6] == "0":
                        tmp_row.append(0)
                    else:
                        tmp_row.append(float(info_row[i*5+13]) / float(tmp_row[6]))
                    if tmp_row[7] == "0":
                        tmp_row.append(0)
                    else:
                        tmp_row.append(float(info_row[i*5+15]) / float(tmp_row[7]))
                    if tmp_row[8] == "0":
                        tmp_row.append(0)
                    else:
                        tmp_row.append(float(info_row[i*5+14]) / float(tmp_row[8]))   
            if flag == False:
                shit_rows.append(tmp_row)
            flag = False
            tmp_row = []
            
            tmp_row.append(info_row[0])
            tmp_row.append(info_row[4])
            tmp_row.append(info_row[5])
            tmp_row.append(info_row[6])
            tmp_row.append(date_row[3])
            tmp_row.append(date_row[4])
            for i in range(0, 4):
                tmp_row.append(info_row[i*5+33])
                tmp_row.append(info_row[i*5+35])
                tmp_row.append(info_row[i*5+34])
                if info_row[34] == "0":
                    flag = True
                if info_row[i*5+34] == "0":
                    tmp_row.append(0)
                else:
                    tmp_row.append(float(info_row[i*5+35]) / float(info_row[i*5+34]))
                tmp_row.append(info_row[i*5+36])
                tmp_row.append(info_row[i*5+37])
                if i > 0:
                    if tmp_row[6] == "0":
                        tmp_row.append(0)
                    else:
                        tmp_row.append(float(info_row[i*5+33]) / float(tmp_row[6]))
                    if tmp_row[7] == "0":
                        tmp_row.append(0)
                    else:
                        tmp_row.append(float(info_row[i*5+35]) / float(tmp_row[7]))
                    if tmp_row[8] == "0":
                        tmp_row.append(0)
                    else:
                        tmp_row.append(float(info_row[i*5+34]) / float(tmp_row[8]))   
            if flag == False:
                shit_rows.append(tmp_row)
            flag = False
            tmp_row = []
            
            tmp_row.append(info_row[0])
            tmp_row.append(info_row[7])
            tmp_row.append(info_row[8])
            tmp_row.append(info_row[9])
            tmp_row.append(date_row[5])
            tmp_row.append(date_row[6])
            for i in range(0, 4):
                tmp_row.append(info_row[i*5+53])
                tmp_row.append(info_row[i*5+55])
                tmp_row.append(info_row[i*5+54])
                if info_row[54] == "0":
                    flag = True
                if info_row[i*5+54] == "0":
                    tmp_row.append(0)
                else:
                    tmp_row.append(float(info_row[i*5+55]) / float(info_row[i*5+54]))
                tmp_row.append(info_row[i*5+56])
                tmp_row.append(info_row[i*5+57])
                if i > 0:
                    if tmp_row[6] == "0":
                        tmp_row.append(0)
                    else:
                        tmp_row.append(float(info_row[i*5+53]) / float(tmp_row[6]))
                    if tmp_row[7] == "0":
                        tmp_row.append(0)
                    else:
                        tmp_row.append(float(info_row[i*5+55]) / float(tmp_row[7]))
                    if tmp_row[8] == "0":
                        tmp_row.append(0)
                    else:
                        tmp_row.append(float(info_row[i*5+54]) / float(tmp_row[8]))   
            if flag == False:
                shit_rows.append(tmp_row)
            flag = False
            tmp_row = []
            
            tmp_row.append(info_row[0])
            tmp_row.append(info_row[10])
            tmp_row.append(info_row[11])
            tmp_row.append(info_row[12])
            tmp_row.append(date_row[7])
            tmp_row.append(date_row[8])
            for i in range(0, 4):
                tmp_row.append(info_row[i*5+73])
                tmp_row.append(info_row[i*5+75])
                tmp_row.append(info_row[i*5+74])
                if info_row[74] == "0":
                    flag = True
                if info_row[i*5+74] == "0":
                    tmp_row.append(0)
                else:
                    tmp_row.append(float(info_row[i*5+75]) / float(info_row[i*5+74]))
                tmp_row.append(info_row[i*5+76])
                tmp_row.append(info_row[i*5+77])
                if i > 0:
                    if tmp_row[6] == "0":
                        tmp_row.append(0)
                    else:
                        tmp_row.append(float(info_row[i*5+73]) / float(tmp_row[6]))
                    if tmp_row[7] == "0":
                        tmp_row.append(0)
                    else:
                        tmp_row.append(float(info_row[i*5+75]) / float(tmp_row[7]))
                    if tmp_row[8] == "0":
                        tmp_row.append(0)
                    else:
                        tmp_row.append(float(info_row[i*5+74]) / float(tmp_row[8]))   
            if flag == False:
                shit_rows.append(tmp_row)
    
# 创建一个文件保存结果数据    
csv_file = file('count_info_with_score_v3_v1.csv', 'wb')
file_object = csv.writer(csv_file, dialect='excel')
file_object.writerow(["学号","挂科数","科目数","挂科率","消费起始时间","消费终止时间","学期消费总金额","总次数","总天数","日均消费次数","日均消费金额","次均消费金额","早消费总金额","早消费总次数","早消费总天数","早日均消费次数","早日均消费金额","早次均消费金额","早金额比重","早次数比重","早天数比重","中消费总金额","中消费总次数","中消费总天数","中日均消费次数","中日均消费金额","中次均消费金额","中金额比重","中次数比重","中天数比重","晚消费总金额","晚消费总次数","晚消费总天数","晚日均消费次数","晚日均消费金额","晚次均消费金额","晚金额比重","晚次数比重","晚天数比重"])
file_object.writerows(shit_rows)        
csv_file.close()