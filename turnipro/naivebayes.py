
class NaiveBayes(object):
    """算法描述:
    x = {a1,a2,...} 为一个样本向量，a为一个特征属性
    p = {d1=[e11,e12],d2=[e21,e22],...} 为特征属性的一个划分，
    其中，d为特征属性，e为一个特征属性d的一个划分
    y = {t1,t2,...}样本所属的类别

    (1) 通过样本集中类别的分布，对每个类别计算先验概率P(t[i])
    (2) 计算每个类别下每个特征属性划分的频率P(a[j] in d[k] | t[i])
    (3) 计算每个样本的P(x | t[i])
        P(x | t[i]) = P(a[1] in d[1] | t[i]) * P(a[2] in d[2] | t[i]) * ...
    (4) 由贝叶斯定理得：
        P(t[i] | x) = (P(x | t[i]) * P(t[i])) / P(x)
    P(t[i] | x)是观测样本属于分类t[i]的概率，找出最大概率对应的分类作为分类结果。
    """
    def __init__(self):
        # 属性划分依据
        # 其中第0行为label=0的样本对应属性的平均值
        # 第1行为label=1的样本对应属性的平均值
        self.propcut = [[0.0 for i in range(2)] for i in range(12)]

    def loadData(self, filename):
        """定义一个接口用于加载数据，需要加载的数据列定义如下
        SID,FailRate,EthGroup,
        W3MessRate,WMessCnt,WBLawRate,WEarlyCnt,WEarlyRate,
        CBAllSum,CBPdayTimes,CLPdayTimes,CBSumRate,CDSumRate,
        HFailCnt,HGPA
        """
        pass

    def calcInfo(self, item):
        """
        对单条数据的各个属性进行分类
        """
        p = list()
        p.extend(self.__calcBase(item))
        p.extend(self.__calcWorkrest(item))
        p.extend(self.__calcComsume(item))
        p.extend(self.__calcHistory(item))
        return p

    def calcType(self, item):
        """
        样本分类
        """
        return 0 if item[1] < 0.1 else 1

    def __calcBase(self, item):
        """
        基本信息的属性分类
        SID,FailRate,EthGroup,
        0   1        2
        """
        p = list()
        p.append(0 if item[2] == 0 else 1)
        return p

    def __calcWorkrest(self, item):
        """
        作息规律的属性分类
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
        三餐消费的属性分类
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
        历史信息的属性分类
        HFailCnt,HGPA
        13       14
        """
        p = list()
        p.append(self.__cut(item[13], self.propcut[10]))
        p.append(self.__cut(item[14], self.propcut[11]))
        return p

    def __cut(self, i, cut):
        return 0 if abs(cut[0] - i) < abs(cut[1] - i) else 1

