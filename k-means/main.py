#-------------------------------------------------------------------------------
# Name:        main
# Purpose:
#           1. types:['normal':0, 'smurf':1, 'neptune':2, 'ipsweep':3, 'teardrop':4]
#           2. type = ipsweep only has 3 items, so we select 4 types: 0,1,2,4
#
# Author:      Ekira
#
# Created:     06/07/2016
# Copyright:   (c) Ekira 2016
# Licence:     <your licence>
#-------------------------------------------------------------------------------

# -*- coding: utf-8 -*- #

from random import randint
from kmeans import standard
from kmeans import kmeans
from kmeans import checkKmeans
from show import showCluster

DATAPATH = "kddcup_data_10_percent.txt"
TYPEDICT = {'normal':0, 'smurf':1, 'neptune':2, 'ipsweep':3, 'teardrop':4}


# load data from 'path' and return a list with 6 fields each item.
def loadData():
    lists = []
    with open(DATAPATH, "rb") as f:
        for i in f:
            arr = i.strip(".\r\n").split(",")
            item = [int(arr[4]), int(arr[5]), int(arr[22]), int(arr[23]), int(arr[31]), int(arr[32])]
            if TYPEDICT.get(arr[41]) == 3:  ## filter the type ipsweep
                continue
            item.append(TYPEDICT.get(arr[41]))
            lists.append(item)
    return lists


# get train set by random position.
def getTrainSet(OriginSet, length):
    setSize = len(OriginSet)
    if setSize < length:
        return None
    trainSet = []
    for i in range(0, length):
        tmpitem = []
        setSize -= 1
        pos = randint(0, setSize-1)
        tmp = OriginSet[pos]
        for i in range(0, 7):
            tmpitem.append(tmp[i])
        tmpitem.append(0)
        trainSet.append(tmpitem)
        del OriginSet[pos]
    return trainSet


# get test set by random position.
def getTestSet(OriginSet, length):
    return getTrainSet(OriginSet, length)


def main():
    trainamount = 300
    testamount = 500
    '''
    centerSet = [[1,1,1,1,1,1,0,0],
                 [1,1,1,0,0,0,0,1],
                 [0,0,0,1,1,1,0,2],
                 [0,0,0,0,0,0,0,3]]
    '''
    centerSet = [[-0.40,-0.07,-1.35,-1.33,-1.96,-2.78,0,0],
                 [0.02,-0.07,0.77,0.77,0.51,0.37,1,1],
                 [-0.47,-0.07,-1.33,-1.32,0.51,-3.31,2,2],
                 [-0.44,-0.07,-1.19,-1.17,0.51,-3.25,4,3]]
    originSet = loadData()                      ## return features + flag
    originSet = standard(originSet)             ## return standard features + flag
    trainSet = getTrainSet(originSet, trainamount)      ## return standard features + flag + group
    testSet = getTestSet(originSet, testamount)        ## return standard features + flag + group
    cnt = 1
    while (cnt != 0):
        cnt = kmeans(trainSet, centerSet)
        ## showCluster(trainSet, centerSet, 4, 5)
        print cnt,
    print
    checkKmeans(testSet, centerSet)

    showCluster(testSet, centerSet, 4, 5)



if __name__ == '__main__':
    main()
