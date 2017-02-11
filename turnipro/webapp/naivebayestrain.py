import csv
from itertools import islice
from naivebayes import NaiveBayes

class NaiveBayesTrain(NaiveBayes):
    def __init__(self, filename):
        self.pp0 = 0.0
        self.pp1 = 0.0
        self.originData = list()
        self.propcut = [[0.0 for i in range(2)] for i in range(12)]
        self.p0Property = [[0.0 for i in range(13)] for i in range(2)]
        self.p1Property = [[0.0 for i in range(13)] for i in range(2)]
        if filename is not None:
            self.loadData(filename)

    def loadData(self, filename):
        """
        SID,FailRate,EthGroup,
        0   3        7
        W3MessRate,WMessCnt,WBLawRate,WEarlyCnt,WEarlyRate,
        8          9        10        11        12
        CBAllSum,CBPdayTimes,CLPdayTimes,CBSumRate,CDSumRate,
        21       33          34          39        41
        HFailCnt,HGPA
        48       49
        """
        with open(filename, 'rb') as f:
            f_csv = csv.reader(f)
            for row in islice(f_csv, 1, None):
                tmp = list()
                tmp.extend([row[0], float(row[3]), int(row[7]), float(row[8])])
                tmp.extend([int(row[9]), float(row[10]), float(row[11])])
                tmp.extend([float(row[12]), float(row[21]), float(row[33])])
                tmp.extend([float(row[34]), float(row[39]), float(row[41])])
                tmp.extend([int(row[48]), float(row[49])])
                self.originData.append(tmp)
        self.__calcAverProp()

    def __calcAverProp(self):
        cnt0 = cnt1 = 0.0
        for item in self.originData:
            if self.calcType(item) == 0:
                cnt0 += 1
                for i in range(12):
                    self.propcut[i][0] += item[i+3]
            else:
                cnt1 += 1
                for i in range(12):
                    self.propcut[i][1] += item[i+3]
        for i in range(12):
            self.propcut[i][0] = self.propcut[i][0] / cnt0
            self.propcut[i][1] = self.propcut[i][1] / cnt1

    def train(self):
        p0 = p1 = 0
        pp = float(len(self.originData))
        p0cnt = [[0 for i in range(13)] for i in range(2)]
        p1cnt = [[0 for i in range(13)] for i in range(2)]
        for i in self.originData:
            if self.calcType(i) == 0:
                p0 += 1
                p = self.calcInfo(i)
                for j in range(13):
                    p0cnt[p[j]][j] += 1
            else:
                p1 += 1
                p = self.calcInfo(i)
                for j in range(13):
                    p1cnt[p[j]][j] += 1
        self.pp0 = p0 / pp
        self.pp1 = p1 / pp
        for i in range(2):
            for j in range(13):
                self.p0Property[i][j] = p0cnt[i][j] / pp
                self.p1Property[i][j] = p1cnt[i][j] / pp

    def getAttrProb(self):
        return self.pp0, self.pp1, self.propcut, self.p0Property, self.p1Property

    def showAttrProb(self):
        pass