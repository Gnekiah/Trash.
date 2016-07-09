import matplotlib.pyplot as plt

# -*- coding: utf-8 -*- #


def showCluster(dataSet, centerSet, x, y):
    numSample = len(dataSet)
    numCenter = len(centerSet)
    '''
    plt.figure(1)
    x1 = plt.subplot(111)
    x2 = plt.subplot(112)
    x3 = plt.subplot(121)
    x4 = plt.subplot(122)
    x5 = plt.subplot(131)
    '''
    ##      red   blue  green  black
    mark = ['or', 'ob', 'og', 'ok', '^r', '+r', 'sr', 'dr', '<r', 'pr']
    for i in range(numSample):
        plt.plot(dataSet[i][x], dataSet[i][y], mark[dataSet[i][7]])
    mark = ['Dr', 'Db', 'Dg', 'Dk', '^b', '+b', 'sb', 'db', '<b', 'pb']
    for i in range(numCenter):
        plt.plot(centerSet[i][x], centerSet[i][y], mark[centerSet[i][7]], markersize = 12)
    plt.show()