#! /bin/python2.7

import os, csv, itertools, operator
from math import sqrt

def correlationCoefficent():
    scoreConsume = []
    failRateAverage = 0         # 挂科比
    consumeTimesDayAverage = 0  # 三餐日均消费次数
    consumeSumDayAverage = 0    # 日均消费金额
    consumeSumTimeAverage = 0   # 每次消费金额
    numerator = [0, 0, 0]       # 相关系数分子
    denominator = [0, 0, 0]     # 相关系数分母
    correlation = [0, 0, 0]     # 相关系数
    
    # 读取score-consume-info文件
    with open("count_info_with_score.csv", 'rb') as readFile:
        readIn = csv.reader(readFile)
        count = 0
        for row in itertools.islice(readIn, 1, None):
            scoreConsume.append(row)
            count = count + 1
    print "read in %s lines" %(str(count))
    
    # 计算所需数据的均值
    for row in scoreConsume:
        failRateAverage += float(row[3])
        consumeTimesDayAverage += float(row[9])
        consumeSumDayAverage += float(row[10])
        consumeSumTimeAverage += float(row[11])
    failRateAverage /= len(scoreConsume)
    consumeTimesDayAverage /= len(scoreConsume)
    consumeSumDayAverage /= len(scoreConsume)
    consumeSumTimeAverage /= len(scoreConsume)
    
    # 计算相关性
    tmpX = 0
    tmpY = [0, 0, 0]
    for row in scoreConsume:
        numerator[0] += (float(row[3]) - failRateAverage) * (float(row[9]) - consumeTimesDayAverage)
        numerator[1] += (float(row[3]) - failRateAverage) * (float(row[10]) - consumeSumDayAverage)
        numerator[2] += (float(row[3]) - failRateAverage) * (float(row[11]) - consumeSumTimeAverage)
        tmpX += (float(row[3]) - failRateAverage) ** 2
        tmpY[0] += (float(row[9]) - consumeTimesDayAverage) ** 2
        tmpY[1] += (float(row[10]) - consumeSumDayAverage) ** 2
        tmpY[2] += (float(row[11]) - consumeSumTimeAverage) ** 2

    denominator[0] = sqrt(tmpX) * sqrt(tmpY[0])
    denominator[1] = sqrt(tmpX) * sqrt(tmpY[1])
    denominator[2] = sqrt(tmpX) * sqrt(tmpY[2])
    
    correlation[0] = numerator[0] / denominator[0]
    correlation[1] = numerator[1] / denominator[1]
    correlation[2] = numerator[2] / denominator[2]
    
    print "r(XY)(3/9):" + str(correlation[0])
    print "r(XY)(3/10):" + str(correlation[1])
    print "r(XY)(3/11):" + str(correlation[2])
    
    
    
correlationCoefficent()    
    
    
    
    
    