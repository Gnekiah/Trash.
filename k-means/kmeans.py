#-------------------------------------------------------------------------------
# Name:        kmeans
# Purpose:
#
# Author:      Ekira
#
# Created:     06/07/2016
# Copyright:   (c) Ekira 2016
# Licence:     <your licence>
#-------------------------------------------------------------------------------

# -*- coding: utf-8 -*- #

import math

def standard(originSet):
    standardSet = []
    N = len(originSet)
    D = 6
    miu = [0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
    seta = [0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
    for item in originSet:
        for i in range(0, D):
            miu[i] += item[i]
    for i in range(0, D):
        miu[i] /= N;
    for item in originSet:
        for i in range(0, D):
            seta[i] += math.pow((miu[i]-item[i]), 2)
    for i in range(0, D):
        seta[i] = math.sqrt(seta[i] / N)
    for item in originSet:
        tmpitem = []
        for i in range(0, D):
            x = (item[i]-miu[i]) / seta[i]
            tmpitem.append(x)
        tmpitem.append(item[6])
        standardSet.append(tmpitem)
    return standardSet


def kmeans(dataSet, centerSet):
    K = len(centerSet)
    for i in range(0, K):
        centerSet[i][6] = 0
    changePosCnt = 0
    for dataItem in dataSet:
        pos = dataItem[7]
        length = 999999.0
        for k in range(0, K):
            tmplength = 0.0
            for i in range(0, 6):
                tmplength += math.pow((dataItem[i]-centerSet[k][i]), 2)
            if length > tmplength:
                pos = centerSet[k][7]
                length = tmplength
        if pos != dataItem[7]:
            dataItem[7] = pos
            changePosCnt += 1
        centerSet[pos][6] += 1
    __calCenter(dataSet, centerSet)
    return changePosCnt


def checkKmeans(dataSet, centerSet):
    K = len(centerSet)
    testamount = len(dataSet)
    for i in range(0, K):
        centerSet[i][6] = 0
    for dataItem in dataSet:
        pos = dataItem[7]
        length = 999999.0
        for k in range(0, K):
            tmplength = 0.0
            for i in range(0, 6):
                tmplength += math.pow((dataItem[i]-centerSet[k][i]), 2)
            if length > tmplength:
                pos = centerSet[k][7]
                length = tmplength
        if pos != dataItem[7]:
            dataItem[7] = pos
        centerSet[pos][6] += 1

    analysis = [[0 for col in range(5)] for row in range(K)]
    for dataItem in dataSet:
        ## print dataItem[7], dataItem[6]
        analysis[dataItem[7]][dataItem[6]] += 1
    types = [0 for row in range(K)]
    for i in range(K):
        types[i] = analysis[i].index(max(analysis[i]))
    print types
    accurate = 0
    testcnt = 0
    errorcnt = 0
    faultcnt = 0
    truecnt = 0
    falsecnt = 0
    for dataItem in dataSet:
        if types[dataItem[7]] == 0:
            if dataItem[6] == 0:
                accurate += 1
                falsecnt += 1
            else:
                truecnt += 1
                faultcnt += 1

        else:
            if dataItem[6] == 0:
                errorcnt += 1
                falsecnt += 1
            else:
                truecnt += 1
                testcnt += 1
                accurate += 1
    ##print accurate, testcnt, errorcnt, faultcnt
    print "zhun que lv:", float(accurate)/testamount
    print "jian ce lv:", float(testcnt)/truecnt
    print "wu bao lv:", float(errorcnt)/falsecnt
    print "lou bao lv:", float(faultcnt)/truecnt



def __calCenter(dataSet, centerSet):
    for i in range(0, len(centerSet)):
        for j in range(0, 6):
            centerSet[i][j] = 0
    for dataItem in dataSet:
        for i in range(0, 6):
            centerSet[dataItem[7]][i] += dataItem[i]
    for i in range(0, len(centerSet)):
        for j in range(0, 6):
            if centerSet[i][6] == 0:
                centerSet[i][j] = 0
            else:
                centerSet[i][j] = centerSet[i][j] / centerSet[i][6]


def main():
    pass

if __name__ == '__main__':
    main()
