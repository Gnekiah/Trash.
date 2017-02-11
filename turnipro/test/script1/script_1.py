#! /bin/python2.7

import csv, string, itertools, os
import datetime, time


# 检验是否有大于0的数据
def check_negative_number_in_csv(all_files_name):
    for filename in all_files_name:
        if filename[-4:] != ".csv":
            continue
        with open(filename, 'rt') as read_file:
            read_in = csv.reader(read_file)
            for row in itertools.islice(read_in, 1, None):
                if float(row[-1]) > 0:
                    return "some float bigger than 0!"
    return "all float smaller than 0!"
    
    
# 输出所有消费类型
def consume_type(all_files_name):
    file_object = open('consume_type.txt', 'wb') 
    file_object.write("consume_type:\n")
    consume_type_list = []

    for filename in all_files_name:
        if filename[-4:] != ".csv":
            continue
        with open(filename, 'rb') as read_file:
            read_in = csv.reader(read_file)
            for row in itertools.islice(read_in, 1, None):
                flag = 0 # 用于标记有无重复输出数据，1表示重复，0表示无重复
                for type_iter in consume_type_list:
                    if type_iter == row[1]:
                        flag = 1
                        break
                if flag == 0:
                    consume_type_list.append(row[1])
                    file_object.write(row[1] + '\n\r')
                    print row[1]
    file_object.close() 
    return "consume_type running competed!"
                
    
# 统计信息
def count_info(all_files_name):
    csv_file = file('count_info.csv', 'w')
    file_object = csv.writer(csv_file, dialect='excel')
    file_object.writerow(['STU_ID', 'Total-all', 'Time-begin', 'Time-end', ' Days',
                          'Average-all', 'Total-meals',
                          'Prop-meal', 'zaocan', 'wucan', 'wancan'])
    meals_type = [] # 读取三餐类型
    with open("meals_type.txt", 'rb') as read_meals_type:
        for line in read_meals_type:
            meals_type.append(line)

    for filename in all_files_name:
        # 判断文件类型
        if filename[-4:] != ".csv":
            continue
        row_list = [] # 用于保存每个ID的一行信息
        # 依次打开文件
        with open(filename, 'rt') as read_file:
            read_in = csv.reader(read_file)
            # 将ID写入文件
            for row in itertools.islice(read_in, 1, None):
                row_list.append(row[0])
                break
                
            # 统计每个ID消费总数
            sumup = 0
            for row in itertools.islice(read_in, 1, None):
                sumup += float(row[-1])
            row_list.append(-sumup)
        
        # 计算消费指标的起止时间
        with open(filename, 'rt') as read_file:
            read_in = csv.reader(read_file)

            time_beg = "2200-01-01 00:00:00"
            time_end = "2000-01-01 00:00:00"
            time_beg_array = datetime.datetime.strptime(time_beg, "%Y-%m-%d %H:%M:%S")
            time_end_array = datetime.datetime.strptime(time_end, "%Y-%m-%d %H:%M:%S")
            timeBegin = int(time.mktime(time_beg_array.timetuple()))
            timeEnd = int(time.mktime(time_end_array.timetuple()))
            
            for row in itertools.islice(read_in, 1, None):
                timeTmp = int(time.mktime(time.strptime(row[2], "%Y/%m/%d %H:%M:%S")))
                if timeBegin > timeTmp:
                    timeBegin = timeTmp
                if timeEnd < timeTmp:
                    timeEnd = timeTmp

            timeBegin = time.strftime("%Y-%m-%d", time.localtime(timeBegin))
            timeEnd = time.strftime("%Y-%m-%d", time.localtime(timeEnd))
            row_list.append(timeBegin)
            row_list.append(timeEnd)
            
        # 统计平均每天的消费
        with open(filename, 'rt') as read_file:
            read_in = csv.reader(read_file)
            days_count = []
            for row in itertools.islice(read_in, 1, None):
                flag = 0 # 用于标记有无重复数据，1表示重复，0表示无重复
                # 格式化时间
                which_day = time.strftime("%Y-%m-%d", time.strptime(row[2], "%Y/%m/%d  %H:%M:%S"))
                
                for type_iter in days_count:
                    if type_iter == which_day:
                        flag = 1
                        break
                if flag == 0:
                    days_count.append(which_day)
            # 消费天数
            consume_days = len(days_count)
            row_list.append(consume_days) 
            # 平均每天消费额
            row_list.append(row_list[1] / consume_days)
            
        # 统计三餐消费总额
        with open(filename, 'rt') as read_file:
            read_in = csv.reader(read_file)
            sumup = 0 # 三餐消费次数统计
            time6_8h = 0 # 6:00-8:30的消费次数
            time11h_13h = 0 # 11:30-13:30的消费次数
            time17_19h = 0 # 17:30-19:30的消费次数
            for row in itertools.islice(read_in, 1, None):
                if row[1] in meals_type:
                    sumup += float(row[-1])
                    
                # 格式化时间，提取time
                meal_time = time.strftime("%H:%M:%S", time.strptime(row[2], "%Y/%m/%d  %H:%M:%S"))
                if meal_time > "06:00:00" and meal_time < "10:30:00":
                    time6_8h += 1
                elif meal_time > "10:30:00" and meal_time < "14:00:00":
                    time11h_13h += 1
                elif meal_time > "14:00:00" and meal_time < "23:30:00":
                    time17_19h += 1
                else:
                    pass
            row_list.append(-sumup)
            row_list.append(-sumup / row_list[1])
            row_list.append(time6_8h)
            row_list.append(time11h_13h)
            row_list.append(time17_19h)
#            if row_list[6] == 0:
#                row_list[6] += 0.1
#            row_list.append(-time6_8h / row_list[6])
#            row_list.append(-time11h_13h / row_list[6])
#            row_list.append(-time17_19h / row_list[6])
        
        print row_list
        file_object.writerow(row_list)

    csv_file.close() 
    return "count_info running competed!"


# 获取所有.csv文件名
all_csv_files = os.listdir("./data/")
i = 0
s = len(all_csv_files)
while i < s:
    all_csv_files[i] = "./data/" + all_csv_files[i]
    i += 1
    
print consume_type(all_csv_files)
#print check_negative_number_in_csv(all_csv_files)
#print count_info(all_csv_files)

