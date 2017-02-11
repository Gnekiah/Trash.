#! /bin/python2.7

# 格式化考试时间，并筛选需要的信息，同时按照考试时间排序

import csv, itertools, os
import datetime, time
import operator


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
                tmp.append(row[1])
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
        

def mergeSameClass():
    # 读取文件名并添加路径
    all_csv_files = os.listdir("./stu_data/")
    i = 0
    s = len(all_csv_files)
    while i < s:
        all_csv_files[i] = "./stu_data/" + all_csv_files[i]
        i += 1
    
    # 读取每个成绩文件信息
    for filename in all_csv_files:
        if filename[-4:] != ".csv":
            continue
        # 打开文件读取
        csvList = []
        flagList = []
        overList = []
        with open(filename, 'rb') as read_file:
            read_in = csv.reader(read_file)    
            for row in read_in:
                csvList.append(row)
            for row in csvList:
                i = 0
                flagm = 0
                while i < len(flagList):
                    if row[2] == flagList[i][0]:
                        flagList[i][1] += 1
                        flagm = 1
                        break
                    i += 1
                if flagm == 0:
                    asd = []
                    asd.append(row[2])
                    asd.append(0)
                    flagList.append(asd)
                    
        for row in flagList:
            if row[1] == 0:
                for qwe in csvList:
                    if qwe[2] == row[0]:
                        middle = []
                        middle.append(qwe[0])
                        middle.append(qwe[1])
                        overList.append(middle)
                        break
            else:
                for qwe in csvList:
                    if qwe[2] == row[0]:
                        middle = []
                        middle.append(0)
                        middle.append(qwe[1])
                        overList.append(middle)
                        break
                        
        with open(filename, 'wb') as read_file:
            write_out = csv.writer(read_file)
            write_out.writerows(overList)
    print "completed!"
      
        
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
        gbkTypeExamFail.append(gbkType[5])
        gbkTypeExamFail.append(gbkType[6])
    
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
            termScore = 0
            ExamItemQuantity = 0
            for row in read_in:          
                # 判断是否挂科
                if row[0] in gbkType:
                    if row[0] == gbkTypeExamFail[0] or row[0] == gbkTypeExamFail[1]:
                        termScore += 1
                    else:
                        pass
                else:
                    if float(row[0]) < 60:
                        termScore += 1
                    else:
                        pass
                ExamItemQuantity += 1
                
            # 将学生挂科情况加入列表
            id = []
            id.append(filename[11:-4])
            id.append(termScore)
            id.append(ExamItemQuantity) 
            id.append(float(termScore) / float(ExamItemQuantity))
            ids.append(id)
            
    # 根据学号匹配消费数据与成绩
    scoreConsumeData = []
    for rowID in ids:
        for rowCL in csvList:
            # 匹配成功
            if rowID[0] == rowCL[0]:
                rowID.append(rowCL)
        scoreConsumeData.append(rowID)
        
    countInfoWithScore = file('count_info_with_score.csv', 'wb')
    fileObject = csv.writer(countInfoWithScore, dialect='excel')
    fileObject.writerows(scoreConsumeData)
    countInfoWithScore.close()
    print "competed!"           
         
       
countInfoToFile()