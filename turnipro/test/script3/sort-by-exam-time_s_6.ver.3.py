#! /bin/python2.7

# 格式化考试时间，并筛选需要的信息，同时按照考试时间排序

import csv, itertools, os
import datetime, time
import operator

def countInfoToFile():
    ids = [] # 记录每个学生的信息
    gbkType = [] # 记录中文成绩类型
    gbkTypeExamFail = [] # 记录中文成绩不及格
    csvList = [] # 记录count-info文件
    
    # 读取中文成绩类型
    with open("exam_failed_type.csv", 'rb') as read_file:
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
    all_csv_files = os.listdir("./score_data_v2/")
    i = 0
    s = len(all_csv_files)
    while i < s:
        all_csv_files[i] = "./score_data_v2/" + all_csv_files[i]
        i += 1
    
    # 初始化参考时间
    term12down = "2012-10-30"
    term13up   = "2013-04-30"
    term13down = "2013-10-30"
    term14up   = "2014-04-30"
    term14down = "2014-10-30"
    term15up   = "2015-04-30"
    # 读取每个成绩文件信息
    for filename in all_csv_files:
        # 打开文件读取成绩与时间
        with open(filename, 'rb') as read_file:
            read_in = csv.reader(read_file)    
            # 初始化记录表
            termScore129 = 0.0
            termScore131 = 0.0
            termScore139 = 0.0
            termScore141 = 0.0
            termScore149 = 0.0
            termScore151 = 0.0
            ExamItemQuantity129 = 0.0
            ExamItemQuantity131 = 0.0
            ExamItemQuantity139 = 0.0
            ExamItemQuantity141 = 0.0
            ExamItemQuantity149 = 0.0
            ExamItemQuantity151 = 0.0
            
            for row in read_in: 
                which_day = time.strftime("%Y-%m-%d", time.strptime(row[1], "%Y-%m-%d"))
                
                if which_day > term12down and which_day < term13up:
                    # 判断是否挂科
                    if row[0] in gbkType:
                        if row[0] in gbkTypeExamFail:
                            termScore129 += 1
                    else:
                        if float(row[0]) < 60:
                            termScore129 += 1
                    ExamItemQuantity129 += 1
              
                if which_day > term13up and which_day < term13down:
                    # 判断是否挂科
                    if row[0] in gbkType:
                        if row[0] in gbkTypeExamFail:
                            termScore131 += 1
                    else:
                        if float(row[0]) < 60:
                            termScore131 += 1
                    ExamItemQuantity131 += 1
                    
                if which_day > term13down and which_day < term14up:
                    # 判断是否挂科
                    if row[0] in gbkType:
                        if row[0] in gbkTypeExamFail:
                            termScore139 += 1
                    else:
                        if float(row[0]) < 60:
                            termScore139 += 1
                    ExamItemQuantity139 += 1
                    
                if which_day > term14up and which_day < term14down:
                    # 判断是否挂科
                    if row[0] in gbkType:
                        if row[0] in gbkTypeExamFail:
                            termScore141 += 1
                    else:
                        if float(row[0]) < 60:
                            termScore141 += 1
                    ExamItemQuantity141 += 1
                
                if which_day > term14down and which_day < term15up:
                    # 判断是否挂科
                    if row[0] in gbkType:
                        if row[0] in gbkTypeExamFail:
                            termScore149 += 1
                    else:
                        if float(row[0]) < 60:
                            termScore149 += 1
                    ExamItemQuantity149 += 1
                    
                if which_day > term15up:
                    # 判断是否挂科
                    if row[0] in gbkType:
                        if row[0] in gbkTypeExamFail:
                            termScore151 += 1
                    else:
                        if float(row[0]) < 60:
                            termScore151 += 1
                    ExamItemQuantity151 += 1
                
            # 将学生挂科情况加入列表
            id = []
            id.append(filename[-12:-4])
            id.append(termScore129)
            id.append(ExamItemQuantity129) 
            if ExamItemQuantity129 > 0.0:
                id.append(float(termScore129) / float(ExamItemQuantity129))
            else:
                id.append(0)
            
            id.append(termScore131)
            id.append(ExamItemQuantity131) 
            if ExamItemQuantity131 > 0.0:
                id.append(float(termScore131) / float(ExamItemQuantity131))
            else:
                id.append(0)
            
            id.append(termScore139)
            id.append(ExamItemQuantity139) 
            if ExamItemQuantity139 > 0.0:
                id.append(float(termScore139) / float(ExamItemQuantity139))
            else:
                id.append(0)
            
            id.append(termScore141)
            id.append(ExamItemQuantity141) 
            if ExamItemQuantity141 > 0.0:
                id.append(float(termScore141) / float(ExamItemQuantity141))
            else:
                id.append(0)
            
            id.append(termScore149)
            id.append(ExamItemQuantity149) 
            if ExamItemQuantity149 > 0.0:
                id.append(float(termScore149) / float(ExamItemQuantity149))
            else:
                id.append(0)
            
            id.append(termScore151)
            id.append(ExamItemQuantity151) 
            if ExamItemQuantity151 > 0.0:
                id.append(float(termScore151) / float(ExamItemQuantity151))
            else:
                id.append(0)
                
            ids.append(id)
            
    # 根据学号匹配消费数据与成绩
    scoreConsumeData = []
    for rowID in ids:
        for rowCL in csvList:
            # 匹配成功
            if rowID[0] == rowCL[0]:
                flag = 0
                for middle in rowCL:
                    if flag == 0:
                        flag = 1
                        continue
                    rowID.append(middle)
                break
        scoreConsumeData.append(rowID)
        
    countInfoWithScore = file('count_info_with_score.csv', 'wb')
    fileObject = csv.writer(countInfoWithScore, dialect='excel')
    fileObject.writerows(scoreConsumeData)
    countInfoWithScore.close()
    print "competed!"           
         
       
# sortByExamTime()      
# mergeSameClass() 
countInfoToFile()