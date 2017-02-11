import csv
import csvdp
from itertools import islice
from naivebayes import NaiveBayes
from naivebayestrain import NaiveBayesTrain
from naivebayesclassify import NaiveBayesClassify


def createmodel(filename, nb):
    nbc = NaiveBayesClassify(filename)
    nbc.loadAttrProb(nb.getAttrProb())
    nbc.classify()
    sids = nbc.getResult()
    lists = csvdp.load_csvlist(filename)
    tT = 0  # t��ʾԤ��ĸ߷��գ�f��ʾԤ��ķǸ߷���
    tF = 0  # T��ʾʵ�ʵĸ߷��գ�F��ʾʵ�ʵķǸ߷���
    fT = 0
    fF = 0
    for i in range(len(lists)):
        if float(lists[i][3]) < 0.1:
            if sids[i][1] == 0:
                fF += 1
            else:
                tF += 1
        else:
            if sids[i][1] == 0:
                fT += 1
            else:
                tT += 1
    print "ѧ�ڣ�", filename[:-4]
    print "Ԥ��     ʵ�ʣ��߷��� �Ƿ���"
    print "�߷���:\t\t", tT, "\t\t", tF
    print "�Ƿ���:\t\t", fT, "\t\t", fF
    print "accuracy:", float(tT + fF) / len(lists)
    if tT+tF == 0:
        print "precision ��ĸΪ0"
    else:
        print "precision:", tT / float(tT+tF)
    if tT+fT == 0:
        print "recall ��ĸΪ0"
    else:
        print "recall:", tT / float(tT+fT)
    if tT+tF+fT == 0:
        print "F1-measure ��ĸΪ0"
    else:
        print "F1-measure:", float(tT+tT) / (tT+tT+tF+fT)


nb = NaiveBayesTrain('format_v3.csv')
nb.train()
nb.showAttrProb()
for i in ['term2.csv', 'term3.csv', 'term4.csv', 'term5.csv']:
    createmodel(i, nb)