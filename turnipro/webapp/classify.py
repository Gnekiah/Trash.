import csvdp
from naivebayesclassify import NaiveBayesClassify


def classify(filename, attrProb):
    nbc = NaiveBayesClassify(filename)
    nbc.loadAttrProb(attrProb)
    nbc.classify()
    return nbc.getResult()
