import csv
from itertools import islice
from naivebayes import NaiveBayes

class NaiveBayesClassify(NaiveBayes):
    def __init__(self, filename):
        self.pp0 = 0.0              # label列为0的先验概率
        self.pp1 = 0.0              # label列为1的先验概率
        self.originData = list()    # 原始数据
        # 属性划分依据
        # 其中第0行为label=0的样本对应属性的平均值
        # 第1行为label=1的样本对应属性的平均值
        self.propcut = list()
        self.p0Property = list()    # label为0的属性
        self.p1Property = list()    # label为1的属性
        self.result = list()        # 预测结果
        if filename is not None:
            self.loadData(filename)

    def loadAttrProb(self, attr):
        self.pp0 = attr[0]
        self.pp1 = attr[1]
        self.propcut = attr[2]
        self.p0Property = attr[3]
        self.p1Property = attr[4]

    def loadData(self, filename):
        """
        SID,FailRate,EthGroup,
        0   3        4
        W3MessRate,WMessCnt,WBLawRate,WEarlyCnt,WEarlyRate,
        5          6        7         8        9
        CBAllSum,CBPdayTimes,CLPdayTimes,CBSumRate,CDSumRate,
        10       11          12          13        14
        HFailCnt,HGPA
        15       16
        """
        with open(filename, 'rb') as f:
            f_csv = csv.reader(f)
            for row in islice(f_csv, 1, None):
                tmp = list()
                tmp.extend([row[0], float(row[3]), int(row[4]), float(row[5])])
                tmp.extend([int(row[6]), float(row[7]), float(row[8])])
                tmp.extend([float(row[9]), float(row[10]), float(row[11])])
                tmp.extend([float(row[12]), float(row[13]), float(row[14])])
                tmp.extend([int(row[15]), float(row[16])])
                self.originData.append(tmp)

    def classify(self):
        print self.propcut
        for i in self.originData:
            props = self.calcInfo(i)
            pxt0 = self.pp0
            pxt1 = self.pp1
            for j in range(13):
                pxt0 *= self.p0Property[props[j]][j]
                pxt1 *= self.p1Property[props[j]][j]
            self.result.append([i[0], 0 if pxt0 > pxt1 else 1])

    def getResult(self):
        return self.result