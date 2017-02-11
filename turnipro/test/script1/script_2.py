#! /bin/python2.7

import csv, itertools, os
import datetime, time
import operator

# ����ʱ��˳���ÿ�����������,����ʽ��ʱ��  
# ��������ɾ��ÿ����ĵ�һ��
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
        

# ������ѵ���������        
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
                flag = 0 # ���ڱ�������ظ�������ݣ�1��ʾ�ظ���0��ʾ���ظ�
                for type_iter in consume_type_list:
                    if type_iter == row[1]:
                        flag = 1
                        break
                if flag == 0:
                    consume_type_list.append(row[1])
                    tmp = []
                    # �ظ�3�У�ƥ�䲽��ʱȡ�м�һ�� || ���ڽ���ַ����뵼�µ�ƥ������
                    tmp.append(row[1])
                    tmp.append(row[1])
                    tmp.append(row[1])
                    file_object.writerow(tmp)
                    print tmp
                
    csv_file.close() 
    print "competed!"    
    
    
# ɾȥ���������ѵ�����
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
        

# ��ʱ����С��30min���������ݽ��кϲ�
def mergeByTime():
    all_csv_files = os.listdir("./data/")
    i = 0
    s = len(all_csv_files)
    while i < s:
        all_csv_files[i] = "./data/" + all_csv_files[i]
        i += 1

    # ��ʼ����Сʱ��ʱ����
    time_gap = datetime.datetime.strptime("2000/01/01 00:30:00", "%Y/%m/%d %H:%M:%S") - datetime.datetime.strptime("2000/01/01 00:00:00", "%Y/%m/%d %H:%M:%S")

    for filename in all_csv_files:
        csv_list = []
        if filename[-4:] != ".csv":
            continue
            
        # ��ȡ����
        with open(filename, 'rb') as read_file:
            read_in = csv.reader(read_file)
            for row in read_in:
                csv_list.append(row)
                
        # �ϲ�ʱ����С��time_gap������
        j = len(csv_list) - 1
        while j > 0:
            t = datetime.datetime.strptime(csv_list[j][2], "%Y/%m/%d %H:%M:%S") - datetime.datetime.strptime(csv_list[j-1][2], "%Y/%m/%d %H:%M:%S")
            if t < time_gap:
                f = float(csv_list[j-1][3]) + float(csv_list[j][3]) # �����ֵ
                csv_list[j-1][3] = str(f)
                del csv_list[j]
            j -= 1
        
        # д������
        with open(filename, 'wb') as read_file:
            write_out = csv.writer(read_file)
            write_out.writerows(csv_list)
    print "completed!"
        

# ��ȡ������Ϣ��д���ļ�
def countInfo():
    # ��ȡ����.csv�ļ���
    all_csv_files = os.listdir("./data/")
    i = 0
    s = len(all_csv_files)
    while i < s:
        all_csv_files[i] = "./data/" + all_csv_files[i]
        i += 1

    # ����һ���ļ�����������    
    csv_file = file('count_info.csv', 'wb')
    file_object = csv.writer(csv_file, dialect='excel')
    file_object.writerow(['STU_ID', '����������', '��ʼʱ��', '��ֹʱ��', '��������',
                      '�������Ѵ���', '�����վ����Ѵ���', '�վ����ѽ��', 'ÿ�����ѽ��'])

    for filename in all_csv_files:
        if filename[-4:] != ".csv":
            continue
        row_list = []   # ���ڱ���ÿ��ID��һ����Ϣ
        ID = ""         # ���ڼ�¼ѧ��ID
        sumup = 0.0     # ��¼���������ܽ��
        time_beg = ""   # ��¼������ʼʱ��
        time_end = ""   # ��¼������ֹʱ��
        time_count = 0.0# ��¼���Ѵ���
        days_count = [] # ��¼��������
        mark = 0        # ���ڱ���Ƿ�Ϊ��һ�ζ�ȡ�ļ�
        with open(filename, 'rb') as read_file:
            read_in = csv.reader(read_file)
            for row in read_in:
                if mark == 0:  
                    # ��ȡID            
                    ID = row[0]
                    # ��ȡ��ʼʱ��
                    time_beg = row[2]
                    mark = 1

                # ͳ��ÿ��ID���͵���������
                sumup -= float(row[-1])
            
                # ��¼��ֹʱ��
                time_end = row[2]
            
                # ��¼���Ѵ���
                time_count += 1
            
                # ��ʽ��ʱ�䣬��¼��������
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


# ��ʽ������ʱ�䣬��ɸѡ��Ҫ����Ϣ��ͬʱ���տ���ʱ������
def sortByExamTime():
    # ��ȡ�ļ��������·��
    all_csv_files = os.listdir("./stu_data/")
    i = 0
    s = len(all_csv_files)
    while i < s:
        all_csv_files[i] = "./stu_data/" + all_csv_files[i]
        i += 1

    # ��һ���ļ���д��ɼ� || ���ڽ��GBK������Unicode��������������ַ�ƥ������
    csv_file = file('tmp.csv', 'wb')
    file_object = csv.writer(csv_file, dialect='excel')
    listEx = [] # ����ɸѡ�޳��ظ�����
    
    # ��ȡÿ���ɼ��ļ���Ϣ
    for filename in all_csv_files:
        if filename[-4:] != ".csv":
            continue
        # ���ļ���ȡ�ɼ���ʱ��
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
                # �ظ�3�У�ƥ�䲽��ʱȡ�м�һ�� || ���ڽ���ַ����뵼�µ�ƥ������
                tmp_type_list.append(row[3])
                tmp_type_list.append(row[3])
                tmp_type_list.append(row[3])
                if tmp_type_list in listEx:
                    pass
                else:
                    listEx.append(tmp_type_list)
        # ��ʱ������д���ļ�
        csvList.sort(key=operator.itemgetter(1))
        with open(filename, 'wb') as read_file:
            write_out = csv.writer(read_file)
            write_out.writerows(csvList)    
    file_object.writerows(listEx)
    csv_file.close()        
    print "completed!"
        

# ƥ��ɼ���Ϣ��������Ϣ����д���ļ�        
def countInfoToFile():
    ids = [] # ��¼ÿ��ѧ������Ϣ
    gbkType = [] # ��¼���ĳɼ�����
    gbkTypeExamFail = [] # ��¼���ĳɼ�������
    csvList = [] # ��¼count-info�ļ�
    
    # ��ȡ���ĳɼ�����
    with open("tmp.csv", 'rb') as read_file:
        read_in = csv.reader(read_file)
        for row in read_in:
            gbkType.append(row[1])
        gbkTypeExamFail = gbkType[1]
    
    # ��ȡcount-info�ļ�
    with open("count_info.csv", 'rb') as read_file:
        read_in = csv.reader(read_file)
        for row in itertools.islice(read_in, 1, None):
            csvList.append(row)
    
    # ��ȡ�ļ��������·��
    all_csv_files = os.listdir("./stu_data/")
    i = 0
    s = len(all_csv_files)
    while i < s:
        all_csv_files[i] = "./stu_data/" + all_csv_files[i]
        i += 1
    
    # ��ʼ���ο�ʱ��
    term12up   = datetime.datetime.strptime("2012-04-30", "%Y-%m-%d")
    term12down = datetime.datetime.strptime("2012-10-30", "%Y-%m-%d")
    term13up   = datetime.datetime.strptime("2013-04-30", "%Y-%m-%d")
    term13down = datetime.datetime.strptime("2013-10-30", "%Y-%m-%d")
    term14up   = datetime.datetime.strptime("2014-04-30", "%Y-%m-%d")
    term14down = datetime.datetime.strptime("2014-10-30", "%Y-%m-%d")
    term15up   = datetime.datetime.strptime("2015-04-30", "%Y-%m-%d")
    # ��ȡÿ���ɼ��ļ���Ϣ
    for filename in all_csv_files:
        if filename[-4:] != ".csv":
            continue
        # ���ļ���ȡ�ɼ���ʱ��
        with open(filename, 'rb') as read_file:
            read_in = csv.reader(read_file)    
            # ��ʼ����¼��
            termScore = [0, 0, 0, 0, 0, 0, 0, 0]
            ExamItemQuantity = [0, 0, 0, 0, 0, 0, 0, 0]
            for row in read_in:
                
                # �ж��Ƿ�ҿ�
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
                        
                # ����ʱ��ɸѡѧ��
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
                
            # ��ѧ���ҿ���������б�
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
            
    # ����ѧ��ƥ������������ɼ�
    scoreConsumeData = []
    for rowCL in csvList:
        for rowID in ids:
            # ƥ��ɹ�
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