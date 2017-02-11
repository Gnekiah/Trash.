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
    tT = 0  # t表示预测的高风险，f表示预测的非高风险
    tF = 0  # T表示实际的高风险，F表示实际的非高风险
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
    print "学期：", filename[:-4]
    print "预测     实际：高风险 非风险"
    print "高风险:\t\t", tT, "\t\t", tF
    print "非风险:\t\t", fT, "\t\t", fF
    print "accuracy:", float(tT + fF) / len(lists)
    if tT+tF == 0:
        print "precision 分母为0"
    else:
        print "precision:", tT / float(tT+tF)
    if tT+fT == 0:
        print "recall 分母为0"
    else:
        print "recall:", tT / float(tT+fT)
    if tT+tF+fT == 0:
        print "F1-measure 分母为0"
    else:
        print "F1-measure:", float(tT+tT) / (tT+tT+tF+fT)


nb = NaiveBayesTrain('format_v3.csv')
nb.train()
nb.showAttrProb()
for i in ['term2.csv', 'term3.csv', 'term4.csv', 'term5.csv']:
    createmodel(i, nb)