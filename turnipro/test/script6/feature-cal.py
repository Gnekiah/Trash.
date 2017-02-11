#! /bin/python2.7

import os,csv,operator,itertools

origin = []
result = []

with open("_fifth___.csv", 'rb') as read_file:
    read_in = csv.reader(read_file)
    for row in itertools.islice(read_in, 1, None):
        origin.append(row)

# 通过求原特征的平方以使特征更加明显
def second_power_of_n():
    result = []
    for row in origin:
        result_row = []
        for j in range(0, 4):
            result_row.append(row[j])
        for i in range(4, 82):
            result_row.append(pow(float(row[i]), 2))
        result.append(result_row)    
    file1 = file('second_power_of_n.csv', 'wb')
    fileObject = csv.writer(file1, dialect='excel')
    fileObject.writerows(result)
    file1.close()
    print "complete"
    
# 通过放大挂科数以使特征明显
def enlarge_failure_feature():
    result = []
    for row in origin:
        result_row = []
        result_row.append(row[0])
        result_row.append(pow(float(row[1]), 2)) # 求挂科数的二次方，再计算放大后的挂科数与实际科目数的百分比
        result_row.append(row[2])
        result_row.append(float(result_row[1]) / float(result_row[2]))
        
        for i in range(4, 118):
            result_row.append(row[i])
        result.append(result_row)    
    file1 = file('enlarge_failure_feature.csv', 'wb')
    fileObject = csv.writer(file1, dialect='excel')
    fileObject.writerows(result)
    file1.close()
    print "complete"
    
# 交叉相乘
def remix():
    result = []
    for row in origin:
        result_row = []
        for j in range(0, 4):
            result_row.append(row[j])
        for i in range(4, 118):
            for k in range(4, 118):
                result_row.append(float(row[i]) * float(row[k]))
        result.append(result_row)    
    file1 = file('remix.csv', 'wb')
    fileObject = csv.writer(file1, dialect='excel')
    fileObject.writerows(result)
    file1.close()
    print "complete"    
    
# second_power_of_n()
# enlarge_failure_feature()
# remix()