#coding=gbk

import re, os
import math
import datetime
from datetime import time
import time
from collections import OrderedDict
from fuzzywuzzy import fuzz

def __check(st):
    return re.search("^[a-zA-Z0-9]+$", st)

def regex():
    x = str(raw_input())
    if __check(x):
        print "success"
    else:
        print "failed"

def planner():
    x = '!"~{}'
    # lists = [[0]*94, [0]*94,[0]*94]
    # lists[2][1] = 12
    ss = [[0]*3]
    ss[0][1] = 2
    ss[0][2] = 123
    print ss
    lists = []
    for i in range(10):
        lists.append([0]*10)
    lists[5][4] = 321342
    print lists
    print ord(x[0]) - 0x21
    print ord(x[2]) - 0x21
    print x[-2]

def splitss():
    x = '1234567890'
    print x[-1]
    y = x[-2:]
    print y[0]
    print y[1]

def cacShannonEnt(dataset):
    numEntries = len(dataset)
    labelCounts = {}
    for featVec in dataset:
        currentLabel = featVec[-1]
        if currentLabel not in labelCounts.keys():
            labelCounts[currentLabel] = 0
        labelCounts[currentLabel] +=1
        
    shannonEnt = 0.0
    for key in labelCounts:
        prob = float(labelCounts[key])/numEntries
        shannonEnt -= prob*math.log(prob, 2)
    return shannonEnt


def calcShannonEnt(dataSet):
    length,dataDict=float(len(dataSet)),{}
    for data in dataSet:
        try:dataDict[data]+=1
        except:dataDict[data]=1
    return sum([-d/length*math.log(d/length, 2) for d in list(dataDict.values())])


def dictfreq():
    dict = {}    
    for i in range(10):
        dict[str(i)] = 0
    print dict
    tmppp = min(dict.items(), key=lambda x: x[1])
    print tmppp
    del dict[min(dict.items(), key=lambda x: x[1])[0]]
    dict['']
    print dict


#####################################
# 字典排序输出
#####################################
def sort_directory():
    d = {'a':12, 'b':13, 'c':16, 'w':12, 'f':3}
    print d
    x =  OrderedDict(sorted(d.items(), key=lambda t: t[1]))
    for i in x:
        print x[i]
    print x
    print d


def birthday():
    while True:
        x = raw_input()
        print re.search("((19)?[5-9]{1}|(20[01]{1})[0-9]{1})((0{1}[0-9]{1})|(1{1}[0-2]{1})){1}([0-3]{1}[0-9]{1}){1}", x)


def datasc():
    d1 = datetime.datetime.now()
    print d1
    d2 = datetime.datetime.now()
    print d2
    d3 = d2 - d1
    print d3
    print str(d3)

def testlist():
    lists = [[0]*10, [0]*10,[0]*10]
    print lists
    i = lists[0]
    print i

def testlist2():
    L = [[21, 213, 45], [43, 65, 63], [32, 56, 231], [87, 342, 34], [4, 2341, 0], [2213, 8765, 123]]
    F = [(L.index(i), i.index(max(i))) for i in L if max([max(i) for i in L]) in i]
    print F[0]
    print L[F[0][0]][F[0][1]]

    a = chr(0x35)
    b = chr(0x56)
    print a+b

def timetest():
    t0 = time.time()
    t1 = time.time()
    print t1, t0
    print time.strftime('%H:%M:%S',time.localtime(t1-t0))

    now = datetime.datetime.now()
    print now
    a = datetime.datetime(2009,2,10,16,00)
    print a
    delta = now - a
    print str(delta)


def birthcheck(st):
    return re.search("((19)?[5-9]{1}|(20[01]{1})[0-9]{1})((0{1}[0-9]{1})|(1{1}[0-2]{1})){1}([0-3]{1}[0-9]{1}){1}", st)
  

# fuzz.ratio("a", "aa")






