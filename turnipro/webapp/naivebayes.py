
class NaiveBayes(object):
    def __init__(self):
        self.propcut = [[0.0 for i in range(2)] for i in range(12)]

    def loadData(self, filename):
        """
        SID,FailRate,EthGroup,
        W3MessRate,WMessCnt,WBLawRate,WEarlyCnt,WEarlyRate,
        CBAllSum,CBPdayTimes,CLPdayTimes,CBSumRate,CDSumRate,
        HFailCnt,HGPA
        """
        pass

    def calcInfo(self, item):
        p = list()
        p.extend(self.__calcBase(item))
        p.extend(self.__calcWorkrest(item))
        p.extend(self.__calcComsume(item))
        p.extend(self.__calcHistory(item))
        return p

    def calcType(self, item):
        return 0 if item[1] < 0.1 else 1

    def __calcBase(self, item):
        """
        SID,FailRate,EthGroup,
        0   1        2
        """
        p = list()
        p.append(0 if item[2] == 0 else 1)
        return p

    def __calcWorkrest(self, item):
        """
        W3MessRate,WMessCnt,WBLawRate,WEarlyCnt,WEarlyRate,
        3          4        5         6         7
        """
        p = list()
        p.append(self.__cut(item[3], self.propcut[0]))
        p.append(self.__cut(item[4], self.propcut[1]))
        p.append(self.__cut(item[5], self.propcut[2]))
        p.append(self.__cut(item[6], self.propcut[3]))
        p.append(self.__cut(item[7], self.propcut[4]))
        return p

    def __calcComsume(self, item):
        """
        CBAllSum,CBPdayTimes,CLPdayTimes,CBSumRate,CDSumRate,
        8        9           10          11        12
        """
        p = list()
        p.append(self.__cut(item[8], self.propcut[5]))
        p.append(self.__cut(item[9], self.propcut[6]))
        p.append(self.__cut(item[10], self.propcut[7]))
        p.append(self.__cut(item[11], self.propcut[8]))
        p.append(self.__cut(item[12], self.propcut[9]))
        return p

    def __calcHistory(self, item):
        """
        HFailCnt,HGPA
        13       14
        """
        p = list()
        p.append(self.__cut(item[13], self.propcut[10]))
        p.append(self.__cut(item[14], self.propcut[11]))
        return p

    def __cut(self, i, cut):
        return 0 if abs(cut[0] - i) < abs(cut[1] - i) else 1

