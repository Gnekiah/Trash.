#! /bin/python2.7

# 格式调整

import csv, itertools, os
import datetime, time
import operator


csvList = []
lastCsvList = []
# 读取count-info_with_score文件
with open("count_info_with_score.csv", 'rb') as read_file:
    read_in = csv.reader(read_file)
    for row in read_in:
        csvList.append(row)
    
for row in csvList:
    i = 0
    while i < 6:
        i += 1
        solidList = []
        solidList.append(row[0])    # STU_ID,起始时间,终止时间,总消费金额,总消费天数,总消费次数,总日均消费金额,总每次消费金额,
        solidList.append(row[19])        
        solidList.append(row[20])
        solidList.append(row[21])
        solidList.append(row[22])
        solidList.append(row[23])
        solidList.append(row[24])
        solidList.append(row[25])
        
        solidList.append(row[3*i-2])    # 12.9挂科数,12.9总科数,12.9挂科率
        solidList.append(row[3*i-1])
        solidList.append(row[3*i])
        if i < 6:
            j = 0
            while j < 20:
                solidList.append(row[20*i+6+j])
                j += 1
        lastCsvList.append(solidList)

countInfoWithScore = file('count_info_with_score_v2.csv', 'wb')
fileObject = csv.writer(countInfoWithScore, dialect='excel')
fileObject.writerows(lastCsvList)
countInfoWithScore.close()
print "complete"