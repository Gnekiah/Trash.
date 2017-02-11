#! /bin/python2.7

import csv, string, itertools, os
import datetime, time
import operator

# 学期分界线
date_2012_9 = "2012/09/01"
date_2013_1 = "2013/02/01"
date_2013_9 = "2013/09/01"
date_2014_1 = "2014/02/01"
date_2014_9 = "2014/09/01"
date_2015_1 = "2015/02/01"

# 早中晚餐分界线
breakfast_lunch = "10:01:01"
lunch_dinner = "15:01:01"

# 获取所有.csv文件名
all_csv_files = os.listdir("./consume_data_v2/")
i = 0
s = len(all_csv_files)
while i < s:
    all_csv_files[i] = "./consume_data_v2/" + all_csv_files[i]
    i += 1

# 创建一个文件保存结果数据    
csv_file = file('count_info.csv', 'wb')
file_object = csv.writer(csv_file, dialect='excel')
file_object.writerow(['STU_ID', '起始时间', '终止时间', '总消费金额', '总消费天数',
                      '总消费次数', '总日均消费金额', '总每次消费金额',
                      '12.09消费金额', '12.09消费天数', '12.09消费次数', '12.09日均消费金额', '12.09每次消费金额',
                      '12.09早餐消费金额', '12.09早餐消费天数', '12.09早餐消费次数', '12.09早餐日均消费金额', '12.09早餐每次消费金额',
                      '12.09中餐消费金额', '12.09中餐消费天数', '12.09中餐消费次数', '12.09中餐日均消费金额', '12.09中餐每次消费金额',
                      '12.09晚餐消费金额', '12.09晚餐消费天数', '12.09晚餐消费次数', '12.09晚餐日均消费金额', '12.09晚餐每次消费金额',
                      '13.01消费金额', '13.01消费天数', '13.01消费次数', '13.01日均消费金额', '13.01每次消费金额',
                      '13.01早餐消费金额', '13.01早餐消费天数', '13.01早餐消费次数', '13.01早餐日均消费金额', '13.01早餐每次消费金额',
                      '13.01中餐消费金额', '13.01中餐消费天数', '13.01中餐消费次数', '13.01中餐日均消费金额', '13.01中餐每次消费金额',
                      '13.01晚餐消费金额', '13.01晚餐消费天数', '13.01晚餐消费次数', '13.01晚餐日均消费金额', '13.01晚餐每次消费金额',
                      '13.09消费金额', '13.09消费天数', '13.09消费次数', '13.09日均消费金额', '13.09每次消费金额',
                      '13.09早餐消费金额', '13.09早餐消费天数', '13.09早餐消费次数', '13.09早餐日均消费金额', '13.09早餐每次消费金额',
                      '13.09中餐消费金额', '13.09中餐消费天数', '13.09中餐消费次数', '13.09中餐日均消费金额', '13.09中餐每次消费金额',
                      '13.09晚餐消费金额', '13.09晚餐消费天数', '13.09晚餐消费次数', '13.09晚餐日均消费金额', '13.09晚餐每次消费金额',
                      '14.01消费金额', '14.01消费天数', '14.01消费次数', '14.01日均消费金额', '14.01每次消费金额',
                      '14.01早餐消费金额', '14.01早餐消费天数', '14.01早餐消费次数', '14.01早餐日均消费金额', '14.01早餐每次消费金额',
                      '14.01中餐消费金额', '14.01中餐消费天数', '14.01中餐消费次数', '14.01中餐日均消费金额', '14.01中餐每次消费金额',
                      '14.01晚餐消费金额', '14.01晚餐消费天数', '14.01晚餐消费次数', '14.01晚餐日均消费金额', '14.01晚餐每次消费金额',
                      '14.09消费金额', '14.09消费天数', '14.09消费次数', '14.09日均消费金额', '14.09每次消费金额',
                      '14.09早餐消费金额', '14.09早餐消费天数', '14.09早餐消费次数', '14.09早餐日均消费金额', '14.09早餐每次消费金额',
                      '14.09中餐消费金额', '14.09中餐消费天数', '14.09中餐消费次数', '14.09中餐日均消费金额', '14.09中餐每次消费金额',
                      '14.09晚餐消费金额', '14.09晚餐消费天数', '14.09晚餐消费次数', '14.09晚餐日均消费金额', '14.09晚餐每次消费金额',
                      '15.01消费金额', '15.01消费天数', '15.01消费次数', '15.01日均消费金额', '15.01每次消费金额',
                      '15.01早餐消费金额', '15.01早餐消费天数', '15.01早餐消费次数', '15.01早餐日均消费金额', '15.01早餐每次消费金额',
                      '15.01中餐消费金额', '15.01中餐消费天数', '15.01中餐消费次数', '15.01中餐日均消费金额', '15.01中餐每次消费金额',
                      '15.01晚餐消费金额', '15.01晚餐消费天数', '15.01晚餐消费次数', '15.01晚餐日均消费金额', '15.01晚餐每次消费金额'])

for filename in all_csv_files:
    # 数据类型定义
    id_row = []                 # 用于保存每个ID的一行信息
    
    id = ""                     # 用于记录学生ID
    consume_sumup_all = 0.0     # 记录三餐消费总金额
    time_beg = ""               # 记录消费起始时间
    time_end = ""               # 记录消费终止时间
    consume_days_count = 0.0    # 记录消费天数
    consume_times_count = 0.0   # 记录消费次数
    
    consume_sumup_12_09 = 0.0   # 2012年9月学期的消费金额
    consume_sumup_13_01 = 0.0   # 
    consume_sumup_13_09 = 0.0   # 
    consume_sumup_14_01 = 0.0   # 
    consume_sumup_14_09 = 0.0   # 
    consume_sumup_15_01 = 0.0   # 
    
    consume_days_count_12_09 = 0.0 # 记录2012年9月学期的消费天数
    consume_days_count_13_01 = 0.0 #
    consume_days_count_13_09 = 0.0 #
    consume_days_count_14_01 = 0.0 #
    consume_days_count_14_09 = 0.0 #
    consume_days_count_15_01 = 0.0 #
    
    consume_times_count_12_09 = 0.0 # 记录2012年9月学期的消费次数
    consume_times_count_13_01 = 0.0 #
    consume_times_count_13_09 = 0.0 #
    consume_times_count_14_01 = 0.0 #
    consume_times_count_14_09 = 0.0 #
    consume_times_count_15_01 = 0.0 #
    
    breakfast_sumup_12_09 = 0.0   # 2012年9月学期的早餐消费金额
    breakfast_sumup_13_01 = 0.0   # 
    breakfast_sumup_13_09 = 0.0   # 
    breakfast_sumup_14_01 = 0.0   # 
    breakfast_sumup_14_09 = 0.0   # 
    breakfast_sumup_15_01 = 0.0   # 
    
    lunch_sumup_12_09 = 0.0   # 2012年9月学期的中餐消费金额
    lunch_sumup_13_01 = 0.0   # 
    lunch_sumup_13_09 = 0.0   # 
    lunch_sumup_14_01 = 0.0   # 
    lunch_sumup_14_09 = 0.0   # 
    lunch_sumup_15_01 = 0.0   # 
    
    dinner_sumup_12_09 = 0.0   # 2012年9月学期的晚餐消费金额
    dinner_sumup_13_01 = 0.0   # 
    dinner_sumup_13_09 = 0.0   # 
    dinner_sumup_14_01 = 0.0   # 
    dinner_sumup_14_09 = 0.0   # 
    dinner_sumup_15_01 = 0.0   # 
    
    breakfast_days_count_12_09 = 0.0 # 记录2012年9月学期的早餐消费天数
    breakfast_days_count_13_01 = 0.0 #
    breakfast_days_count_13_09 = 0.0 #
    breakfast_days_count_14_01 = 0.0 #
    breakfast_days_count_14_09 = 0.0 #
    breakfast_days_count_15_01 = 0.0 #
    
    lunch_days_count_12_09 = 0.0 # 记录2012年9月学期的中餐消费天数
    lunch_days_count_13_01 = 0.0 #
    lunch_days_count_13_09 = 0.0 #
    lunch_days_count_14_01 = 0.0 #
    lunch_days_count_14_09 = 0.0 #
    lunch_days_count_15_01 = 0.0 #
    
    dinner_days_count_12_09 = 0.0 # 记录2012年9月学期的晚餐消费天数
    dinner_days_count_13_01 = 0.0 #
    dinner_days_count_13_09 = 0.0 #
    dinner_days_count_14_01 = 0.0 #
    dinner_days_count_14_09 = 0.0 #
    dinner_days_count_15_01 = 0.0 #
    
    breakfast_times_count_12_09 = 0.0 # 记录2012年9月学期的早餐消费次数
    breakfast_times_count_13_01 = 0.0 #
    breakfast_times_count_13_09 = 0.0 #
    breakfast_times_count_14_01 = 0.0 #
    breakfast_times_count_14_09 = 0.0 #
    breakfast_times_count_15_01 = 0.0 #
    
    lunch_times_count_12_09 = 0.0 # 记录2012年9月学期的中餐消费次数
    lunch_times_count_13_01 = 0.0 #
    lunch_times_count_13_09 = 0.0 #
    lunch_times_count_14_01 = 0.0 #
    lunch_times_count_14_09 = 0.0 #
    lunch_times_count_15_01 = 0.0 #
    
    dinner_times_count_12_09 = 0.0 # 记录2012年9月学期的晚餐消费次数
    dinner_times_count_13_01 = 0.0 #
    dinner_times_count_13_09 = 0.0 #
    dinner_times_count_14_01 = 0.0 #
    dinner_times_count_14_09 = 0.0 #
    dinner_times_count_15_01 = 0.0 #
    
    date_list = []              # 记录所有消费日期
    mark = 0                    # 用于标记是否为第一次读取文件
    
    
    
    
    with open(filename, 'rb') as read_file:
        read_in = csv.reader(read_file)
        for row in read_in:
            if mark == 0:  
                # 获取ID            
                id = row[0]
                # 获取起始时间
                time_beg = row[2]
                mark = 1

            # 统计每个ID三餐的消费总数
            consume_sumup_all -= float(row[-1])
            
            # 记录终止时间
            time_end = row[2]
            
            # 记录消费次数
            consume_times_count += 1
            
            # 格式化日期和时间，记录消费日期和时间
            which_day = time.strftime("%Y/%m/%d", time.strptime(row[2], "%Y/%m/%d %H:%M:%S"))
            which_time = time.strftime("%H:%M:%S", time.strptime(row[2], "%Y/%m/%d %H:%M:%S"))
            # 用于标记日期是否重复
            flag = False
            if which_day in date_list:
                flag = True
            else:
                date_list.append(which_day)
            # 若日期未重复，则将记录日期的计数器自加 1
            if flag == False:
                consume_days_count += 1
            # 判断学期
            if which_day > date_2012_9 and which_day < date_2013_1:
                consume_sumup_12_09 -= float(row[-1])
                if flag == False:
                    consume_days_count_12_09 += 1
                consume_times_count_12_09 += 1
                if which_time < breakfast_lunch:
                    breakfast_sumup_12_09 -= float(row[-1])
                    if flag == False:
                        breakfast_days_count_12_09 += 1
                    breakfast_times_count_12_09 += 1
                elif which_time >= breakfast_lunch and which_time < lunch_dinner:
                    lunch_sumup_12_09 -= float(row[-1])
                    if flag == False:
                        lunch_days_count_12_09 += 1
                    lunch_times_count_12_09 += 1
                else:
                    dinner_sumup_12_09 -= float(row[-1])
                    if flag == False:
                        dinner_days_count_12_09 += 1
                    dinner_times_count_12_09 += 1
                    
            elif which_day >= date_2013_1 and which_day < date_2013_9:
                consume_sumup_13_01 -= float(row[-1])
                if flag == False:
                    consume_days_count_13_01 += 1
                consume_times_count_13_01 += 1
                if which_time < breakfast_lunch:
                    breakfast_sumup_13_01 -= float(row[-1])
                    if flag == False:
                        breakfast_days_count_13_01 += 1
                    breakfast_times_count_13_01 += 1
                elif which_time >= breakfast_lunch and which_time < lunch_dinner:
                    lunch_sumup_13_01 -= float(row[-1])
                    if flag == False:
                        lunch_days_count_13_01 += 1
                    lunch_times_count_13_01 += 1
                else:
                    dinner_sumup_13_01 -= float(row[-1])
                    if flag == False:
                        dinner_days_count_13_01 += 1
                    dinner_times_count_13_01 += 1

            elif which_day >= date_2013_9 and which_day < date_2014_1:
                consume_sumup_13_09 -= float(row[-1])
                if flag == False:
                    consume_days_count_13_09 += 1
                consume_times_count_13_09 += 1
                if which_time < breakfast_lunch:
                    breakfast_sumup_13_09 -= float(row[-1])
                    if flag == False:
                        breakfast_days_count_13_09 += 1
                    breakfast_times_count_13_09 += 1
                elif which_time >= breakfast_lunch and which_time < lunch_dinner:
                    lunch_sumup_13_09 -= float(row[-1])
                    if flag == False:
                        lunch_days_count_13_09 += 1
                    lunch_times_count_13_09 += 1
                else:
                    dinner_sumup_13_09 -= float(row[-1])
                    if flag == False:
                        dinner_days_count_13_09 += 1
                    dinner_times_count_13_09 += 1
            
            elif which_day >= date_2014_1 and which_day < date_2014_9:
                consume_sumup_14_01 -= float(row[-1])
                if flag == False:
                    consume_days_count_14_01 += 1
                consume_times_count_14_01 += 1
                if which_time < breakfast_lunch:
                    breakfast_sumup_14_01 -= float(row[-1])
                    if flag == False:
                        breakfast_days_count_14_01 += 1
                    breakfast_times_count_14_01 += 1
                elif which_time >= breakfast_lunch and which_time < lunch_dinner:
                    lunch_sumup_14_01 -= float(row[-1])
                    if flag == False:
                        lunch_days_count_14_01 += 1
                    lunch_times_count_14_01 += 1
                else:
                    dinner_sumup_14_01 -= float(row[-1])
                    if flag == False:
                        dinner_days_count_14_01 += 1
                    dinner_times_count_14_01 += 1
                    
            elif which_day >= date_2014_9 and which_day < date_2015_1:
                consume_sumup_14_09 -= float(row[-1])
                if flag == False:
                    consume_days_count_14_09 += 1
                consume_times_count_14_09 += 1
                if which_time < breakfast_lunch:
                    breakfast_sumup_14_09 -= float(row[-1])
                    if flag == False:
                        breakfast_days_count_14_09 += 1
                    breakfast_times_count_14_09 += 1
                elif which_time >= breakfast_lunch and which_time < lunch_dinner:
                    lunch_sumup_14_09 -= float(row[-1])
                    if flag == False:
                        lunch_days_count_14_09 += 1
                    lunch_times_count_14_09 += 1
                else:
                    dinner_sumup_14_09 -= float(row[-1])
                    if flag == False:
                        dinner_days_count_14_09 += 1
                    dinner_times_count_14_09 += 1        
         
            else:
                consume_sumup_15_01 -= float(row[-1])
                if flag == False:
                    consume_days_count_15_01 += 1
                consume_times_count_15_01 += 1
                if which_time < breakfast_lunch:
                    breakfast_sumup_15_01 -= float(row[-1])
                    if flag == False:
                        breakfast_days_count_15_01 += 1
                    breakfast_times_count_15_01 += 1
                elif which_time >= breakfast_lunch and which_time < lunch_dinner:
                    lunch_sumup_15_01 -= float(row[-1])
                    if flag == False:
                        lunch_days_count_15_01 += 1
                    lunch_times_count_15_01 += 1
                else:
                    dinner_sumup_15_01 -= float(row[-1])
                    if flag == False:
                        dinner_days_count_15_01 += 1
                    dinner_times_count_15_01 += 1        
                    
    id_row.append(id)
    id_row.append(time_beg)
    id_row.append(time_end)
    # 所有记录的数据
    id_row.append(consume_sumup_all)
    id_row.append(consume_days_count)
    id_row.append(consume_times_count)
    if consume_days_count > 0.0:
        id_row.append(consume_sumup_all / consume_days_count)
    else:
        id_row.append(0)
    if consume_times_count > 0.0:
        id_row.append(consume_sumup_all / consume_times_count)
    else:
        id_row.append(0)
    # 整个学期数据
    id_row.append(consume_sumup_12_09)
    id_row.append(consume_days_count_12_09)
    id_row.append(consume_times_count_12_09)
    if consume_days_count_12_09 > 0.0:
        id_row.append(consume_sumup_12_09 / consume_days_count_12_09)
    else:
        id_row.append(0)
    if consume_times_count_12_09 > 0.0:
        id_row.append(consume_sumup_12_09 / consume_times_count_12_09)
    # 早餐
    id_row.append(breakfast_sumup_12_09)
    id_row.append(breakfast_days_count_12_09)
    id_row.append(breakfast_times_count_12_09)
    if breakfast_days_count_12_09 > 0.0:
        id_row.append(breakfast_sumup_12_09 / breakfast_days_count_12_09)
    else:
        id_row.append(0)
    if breakfast_times_count_12_09 > 0.0:
        id_row.append(breakfast_sumup_12_09 / breakfast_times_count_12_09)
    # 中餐
    id_row.append(lunch_sumup_12_09)
    id_row.append(lunch_days_count_12_09)
    id_row.append(lunch_times_count_12_09)
    if lunch_days_count_12_09 > 0.0:
        id_row.append(lunch_sumup_12_09 / lunch_days_count_12_09)
    else:
        id_row.append(0)
    if lunch_times_count_12_09 > 0.0:
        id_row.append(lunch_sumup_12_09 / lunch_times_count_12_09)
    # 晚餐
    id_row.append(dinner_sumup_12_09)
    id_row.append(dinner_days_count_12_09)
    id_row.append(dinner_times_count_12_09)
    if dinner_days_count_12_09 > 0.0:
        id_row.append(dinner_sumup_12_09 / dinner_days_count_12_09)
    else:
        id_row.append(0)
    if dinner_times_count_12_09 > 0.0:
        id_row.append(dinner_sumup_12_09 / dinner_times_count_12_09)
    
    
    # 整个学期数据
    id_row.append(consume_sumup_13_01)
    id_row.append(consume_days_count_13_01)
    id_row.append(consume_times_count_13_01)
    if consume_days_count_13_01 > 0.0:
        id_row.append(consume_sumup_13_01 / consume_days_count_13_01)
    else:
        id_row.append(0)
    if consume_times_count_13_01 > 0.0:
        id_row.append(consume_sumup_13_01 / consume_times_count_13_01)
    # 早餐
    id_row.append(breakfast_sumup_13_01)
    id_row.append(breakfast_days_count_13_01)
    id_row.append(breakfast_times_count_13_01)
    if breakfast_days_count_13_01 > 0.0:
        id_row.append(breakfast_sumup_13_01 / breakfast_days_count_13_01)
    else:
        id_row.append(0)
    if breakfast_times_count_13_01 > 0.0:
        id_row.append(breakfast_sumup_13_01 / breakfast_times_count_13_01)
    # 中餐
    id_row.append(lunch_sumup_13_01)
    id_row.append(lunch_days_count_13_01)
    id_row.append(lunch_times_count_13_01)
    if lunch_days_count_13_01 > 0.0:
        id_row.append(lunch_sumup_13_01 / lunch_days_count_13_01)
    else:
        id_row.append(0)
    if lunch_times_count_13_01 > 0.0:
        id_row.append(lunch_sumup_13_01 / lunch_times_count_13_01)
    # 晚餐
    id_row.append(dinner_sumup_13_01)
    id_row.append(dinner_days_count_13_01)
    id_row.append(dinner_times_count_13_01)
    if dinner_days_count_13_01 > 0.0:
        id_row.append(dinner_sumup_13_01 / dinner_days_count_13_01)
    else:
        id_row.append(0)
    if dinner_times_count_13_01 > 0.0:
        id_row.append(dinner_sumup_13_01 / dinner_times_count_13_01)
        
    # 整个学期数据
    id_row.append(consume_sumup_13_09)
    id_row.append(consume_days_count_13_09)
    id_row.append(consume_times_count_13_09)
    if consume_days_count_13_09 > 0.0:
        id_row.append(consume_sumup_13_09 / consume_days_count_13_09)
    else:
        id_row.append(0)
    if consume_times_count_13_09 > 0.0:
        id_row.append(consume_sumup_13_09 / consume_times_count_13_09)
    # 早餐
    id_row.append(breakfast_sumup_13_09)
    id_row.append(breakfast_days_count_13_09)
    id_row.append(breakfast_times_count_13_09)
    if breakfast_days_count_13_09 > 0.0:
        id_row.append(breakfast_sumup_13_09 / breakfast_days_count_13_09)
    else:
        id_row.append(0)
    if breakfast_times_count_13_09 > 0.0:
        id_row.append(breakfast_sumup_13_09 / breakfast_times_count_13_09)
    # 中餐
    id_row.append(lunch_sumup_13_09)
    id_row.append(lunch_days_count_13_09)
    id_row.append(lunch_times_count_13_09)
    if lunch_days_count_13_09 > 0.0:
        id_row.append(lunch_sumup_13_09 / lunch_days_count_13_09)
    else:
        id_row.append(0)
    if lunch_times_count_13_09 > 0.0:
        id_row.append(lunch_sumup_13_09 / lunch_times_count_13_09)
    # 晚餐
    id_row.append(dinner_sumup_13_09)
    id_row.append(dinner_days_count_13_09)
    id_row.append(dinner_times_count_13_09)
    if dinner_days_count_13_09 > 0.0:
        id_row.append(dinner_sumup_13_09 / dinner_days_count_13_09)
    else:
        id_row.append(0)
    if dinner_times_count_13_09 > 0.0:
        id_row.append(dinner_sumup_13_09 / dinner_times_count_13_09)

    # 整个学期数据
    id_row.append(consume_sumup_14_01)
    id_row.append(consume_days_count_14_01)
    id_row.append(consume_times_count_14_01)
    if consume_days_count_14_01 > 0.0:
        id_row.append(consume_sumup_14_01 / consume_days_count_14_01)
    else:
        id_row.append(0)
    if consume_times_count_14_01 > 0.0:
        id_row.append(consume_sumup_14_01 / consume_times_count_14_01)
    # 早餐
    id_row.append(breakfast_sumup_14_01)
    id_row.append(breakfast_days_count_14_01)
    id_row.append(breakfast_times_count_14_01)
    if breakfast_days_count_14_01 > 0.0:
        id_row.append(breakfast_sumup_14_01 / breakfast_days_count_14_01)
    else:
        id_row.append(0)
    if breakfast_times_count_14_01 > 0.0:
        id_row.append(breakfast_sumup_14_01 / breakfast_times_count_14_01)
    # 中餐
    id_row.append(lunch_sumup_14_01)
    id_row.append(lunch_days_count_14_01)
    id_row.append(lunch_times_count_14_01)
    if lunch_days_count_14_01 > 0.0:
        id_row.append(lunch_sumup_14_01 / lunch_days_count_14_01)
    else:
        id_row.append(0)
    if lunch_times_count_14_01 > 0.0:
        id_row.append(lunch_sumup_14_01 / lunch_times_count_14_01)
    # 晚餐
    id_row.append(dinner_sumup_14_01)
    id_row.append(dinner_days_count_14_01)
    id_row.append(dinner_times_count_14_01)
    if dinner_days_count_14_01 > 0.0:
        id_row.append(dinner_sumup_14_01 / dinner_days_count_14_01)
    else:
        id_row.append(0)
    if dinner_times_count_14_01 > 0.0:
        id_row.append(dinner_sumup_14_01 / dinner_times_count_14_01)
        
    # 整个学期数据
    id_row.append(consume_sumup_14_09)
    id_row.append(consume_days_count_14_09)
    id_row.append(consume_times_count_14_09)
    if consume_days_count_14_09 > 0.0:
        id_row.append(consume_sumup_14_09 / consume_days_count_14_09)
    else:
        id_row.append(0)
    if consume_times_count_14_09 > 0.0:
        id_row.append(consume_sumup_14_09 / consume_times_count_14_09)
    # 早餐
    id_row.append(breakfast_sumup_14_09)
    id_row.append(breakfast_days_count_14_09)
    id_row.append(breakfast_times_count_14_09)
    if breakfast_days_count_14_09 > 0.0:
        id_row.append(breakfast_sumup_14_09 / breakfast_days_count_14_09)
    else:
        id_row.append(0)
    if breakfast_times_count_14_09 > 0.0:
        id_row.append(breakfast_sumup_14_09 / breakfast_times_count_14_09)
    # 中餐
    id_row.append(lunch_sumup_14_09)
    id_row.append(lunch_days_count_14_09)
    id_row.append(lunch_times_count_14_09)
    if lunch_days_count_14_09 > 0.0:
        id_row.append(lunch_sumup_14_09 / lunch_days_count_14_09)
    else:
        id_row.append(0)
    if lunch_times_count_14_09 > 0.0:
        id_row.append(lunch_sumup_14_09 / lunch_times_count_14_09)
    # 晚餐
    id_row.append(dinner_sumup_14_09)
    id_row.append(dinner_days_count_14_09)
    id_row.append(dinner_times_count_14_09)
    if dinner_days_count_14_09 > 0.0:
        id_row.append(dinner_sumup_14_09 / dinner_days_count_14_09)
    else:
        id_row.append(0)
    if dinner_times_count_14_09 > 0.0:
        id_row.append(dinner_sumup_14_09 / dinner_times_count_14_09)

    # 整个学期数据
    id_row.append(consume_sumup_15_01)
    id_row.append(consume_days_count_15_01)
    id_row.append(consume_times_count_15_01)
    if consume_days_count_15_01 > 0.0:
        id_row.append(consume_sumup_15_01 / consume_days_count_15_01)
    else:
        id_row.append(0)
    if consume_times_count_15_01 > 0.0:
        id_row.append(consume_sumup_15_01 / consume_times_count_15_01)
    # 早餐
    id_row.append(breakfast_sumup_15_01)
    id_row.append(breakfast_days_count_15_01)
    id_row.append(breakfast_times_count_15_01)
    if breakfast_days_count_15_01 > 0.0:
        id_row.append(breakfast_sumup_15_01 / breakfast_days_count_15_01)
    else:
        id_row.append(0)
    if breakfast_times_count_15_01 > 0.0:
        id_row.append(breakfast_sumup_15_01 / breakfast_times_count_15_01)
    # 中餐
    id_row.append(lunch_sumup_15_01)
    id_row.append(lunch_days_count_15_01)
    id_row.append(lunch_times_count_15_01)
    if lunch_days_count_15_01 > 0.0:
        id_row.append(lunch_sumup_15_01 / lunch_days_count_15_01)
    else:
        id_row.append(0)
    if lunch_times_count_15_01 > 0.0:
        id_row.append(lunch_sumup_15_01 / lunch_times_count_15_01)
    # 晚餐
    id_row.append(dinner_sumup_15_01)
    id_row.append(dinner_days_count_15_01)
    id_row.append(dinner_times_count_15_01)
    if dinner_days_count_15_01 > 0.0:
        id_row.append(dinner_sumup_15_01 / dinner_days_count_15_01)
    else:
        id_row.append(0)
    if dinner_times_count_15_01 > 0.0:
        id_row.append(dinner_sumup_15_01 / dinner_times_count_15_01)
        
    file_object.writerow(id_row)
csv_file.close()
print "competed!"




