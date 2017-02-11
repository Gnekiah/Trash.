#! /bin/python2.7

import csv, itertools, os
import datetime, time
import operator

# 按照时间顺序对每个表进行排序,并格式化时间  
# 排序结果将删除每个表的第一行
def sortByTime():
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
        

# 输出消费的所有类型        
def printConsumeType():
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
    
    
# 删去非三餐消费的数据
def filterOutNonmeal():
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
    print "completed!"
        

# 将时间间隔小于30min的消费数据进行合并
def mergeByTime():
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
    print "completed!"
        

# 提取消费信息并写入文件
def countInfo():
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


# 格式化考试时间，并筛选需要的信息，同时按照考试时间排序
def sortByExamTime():
    # 读取文件名并添加路径
    all_csv_files = os.listdir("./stu_data/")
    i = 0
    s = len(all_csv_files)
    while i < s:
        all_csv_files[i] = "./stu_data/" + all_csv_files[i]
        i += 1

    # 打开一个文件，写入成绩 || 用于解决GBK编码与Unicode编码产生的中文字符匹配问题
    csv_file = file('tmp.csv', 'wb')
    file_object = csv.writer(csv_file, dialect='excel')
    listEx = [] # 用于筛选剔除重复数据
    
    # 读取每个成绩文件信息
    for filename in all_csv_files:
        if filename[-4:] != ".csv":
            continue
        # 打开文件读取成绩与时间
        with open(filename, 'rb') as read_file:
            read_in = csv.reader(read_file)    
            csvList = []
            for row in itertools.islice(read_in, 1, None):
                tmp = []
                if len(row[-1]) < 12:
                    exam_time = time.strftime("%Y-%m-%d", time.strptime(row[-1], "%Y-%m-%d"))
                else:
                    exam_time = time.strftime("%Y-%m-%d", time.strptime(row[-1], "%Y-%m-%d  %H:%M:%S"))
                tmp.append(row[3])
                tmp.append(exam_time)
                csvList.append(tmp)
                tmp_type_list = []
                # 重复3行，匹配步骤时取中间一行 || 用于解决字符编码导致的匹配问题
                tmp_type_list.append(row[3])
                tmp_type_list.append(row[3])
                tmp_type_list.append(row[3])
                if tmp_type_list in listEx:
                    pass
                else:
                    listEx.append(tmp_type_list)
        # 按时间排序并写入文件
        csvList.sort(key=operator.itemgetter(1))
        with open(filename, 'wb') as read_file:
            write_out = csv.writer(read_file)
            write_out.writerows(csvList)    
    file_object.writerows(listEx)
    csv_file.close()        
    print "completed!"
        

# 匹配成绩信息与消费信息，并写入文件        
def countInfoToFile():
    ids = [] # 记录每个学生的信息
    gbkType = [] # 记录中文成绩类型
    gbkTypeExamFail = [] # 记录中文成绩不及格
    csvList = [] # 记录count-info文件
    
    # 读取中文成绩类型
    with open("tmp.csv", 'rb') as read_file:
        read_in = csv.reader(read_file)
        for row in read_in:
            gbkType.append(row[1])
        gbkTypeExamFail = gbkType[1]
    
    # 读取count-info文件
    with open("count_info.csv", 'rb') as read_file:
        read_in = csv.reader(read_file)
        for row in itertools.islice(read_in, 1, None):
            csvList.append(row)
    
    # 读取文件名并添加路径
    all_csv_files = os.listdir("./stu_data/")
    i = 0
    s = len(all_csv_files)
    while i < s:
        all_csv_files[i] = "./stu_data/" + all_csv_files[i]
        i += 1
    
    # 初始化参考时间
    term12up   = datetime.datetime.strptime("2012-04-30", "%Y-%m-%d")
    term12down = datetime.datetime.strptime("2012-10-30", "%Y-%m-%d")
    term13up   = datetime.datetime.strptime("2013-04-30", "%Y-%m-%d")
    term13down = datetime.datetime.strptime("2013-10-30", "%Y-%m-%d")
    term14up   = datetime.datetime.strptime("2014-04-30", "%Y-%m-%d")
    term14down = datetime.datetime.strptime("2014-10-30", "%Y-%m-%d")
    term15up   = datetime.datetime.strptime("2015-04-30", "%Y-%m-%d")
    # 读取每个成绩文件信息
    for filename in all_csv_files:
        if filename[-4:] != ".csv":
            continue
        # 打开文件读取成绩与时间
        with open(filename, 'rb') as read_file:
            read_in = csv.reader(read_file)    
            # 初始化记录表
            termScore = [0, 0, 0, 0, 0, 0, 0, 0]
            ExamItemQuantity = [0, 0, 0, 0, 0, 0, 0, 0]
            for row in read_in:
                
                # 判断是否挂科
                tmpTermScore = 0
                if row[0] in gbkType:
                    if row[0] == gbkTypeExamFail:
                        tmpTermScore = 1
                    else:
                        pass
                else:
                    if float(row[0]) < 60:
                        tmpTermScore = 1
                    else:
                        pass
                        
                # 根据时间筛选学期
                tmpTime = datetime.datetime.strptime(row[1], "%Y-%m-%d")
                if tmpTime < term12up:
                    termScore[0] += tmpTermScore
                    ExamItemQuantity[0] += 1
                elif tmpTime < term12down:
                    termScore[1] += tmpTermScore
                    ExamItemQuantity[1] += 1
                elif tmpTime < term13up:
                    termScore[2] += tmpTermScore
                    ExamItemQuantity[2] += 1
                elif tmpTime < term13down:
                    termScore[3] += tmpTermScore
                    ExamItemQuantity[3] += 1
                elif tmpTime < term14up:
                    termScore[4] += tmpTermScore
                    ExamItemQuantity[4] += 1
                elif tmpTime < term14down:
                    termScore[5] += tmpTermScore
                    ExamItemQuantity[5] += 1
                elif tmpTime < term15up:
                    termScore[6] += tmpTermScore
                    ExamItemQuantity[6] += 1
                else:
                    termScore[7] += tmpTermScore
                    ExamItemQuantity[7] += 1
                
            # 将学生挂科情况加入列表
            id = []
            id.append(filename[11:-4])
            i = 0
            while i < 8:
                id.append(termScore[i])
                id.append(ExamItemQuantity[i])
                if ExamItemQuantity[i] != 0:    
                    id.append(float(termScore[i]) / float(ExamItemQuantity[i]))
                else:
                    id.append(0)
                i += 1
            ids.append(id)
            
    # 根据学号匹配消费数据与成绩
    scoreConsumeData = []
    for rowCL in csvList:
        for rowID in ids:
            # 匹配成功
            if rowID[0] == rowCL[0]:
                flag = 0
                for middle in rowID:
                    if flag == 0:
                        flag = 1
                        continue
                    rowCL.append(middle)
                break
        scoreConsumeData.append(rowCL)
        
    countInfoWithScore = file('count_info_with_score.csv', 'wb')
    fileObject = csv.writer(countInfoWithScore, dialect='excel')
    fileObject.writerows(scoreConsumeData)
    countInfoWithScore.close()
    print "competed!"           
                
      
sortByTime()
printConsumeType()
filterOutNonmeal()
mergeByTime()
countInfo()
sortByExamTime()      
countInfoToFile()
print "All Competed!"