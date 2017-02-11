#!/bin/python2.7
#coding=gbk

import os, sys
from chardet import detect

# tip: 读取文件的每行的字符串都有\r\n，以及中间的间隔符\t
# filename: 打开文件，按行读取文件，格式化为['count' 'password']
# return: 包含格式化单元的list
def readtolist_id_pw(filename):
    lists = []
    with open(filename, 'rt') as f:
        for i in f:
            tmp = i.strip('\n').strip('\r')
            tmp = tmp.split('\t')
            lists.append(tmp)
            print tmp
    return lists

# 重置字符表内容
# alphabet: 待操作的字典
# return null
def __reset_alphabet(alphabet):
    for i in alphabet:
        alphabet[i] = 0

# 合并两个字典（对应key的value相加）
# *objs: 待合并的多个字典
# return： 合并后的字典
def __merge_dict(*objs):
    # sof上找的这段代码，，it works well, but i can not understand.
    keys = set(sum([obj.keys() for obj in objs],[]))  
    total = {}  
    for key in keys:  
        total[key] = sum([obj.get(key,0) for obj in objs])
    return total

# 生成一个alphabet存放key=ascii打印字符，value=0的字典
# return: alphabet字典
def __alphagen():
    alphabet = {
        '!':0,'"':0,'#':0,'$':0,'%':0,'&':0,"'":0,'(':0,')':0,'*':0,'+':0,',':0,'-':0,'.':0,'/':0,
        '0':0,'1':0,'2':0,'3':0,'4':0,'5':0,'6':0,'7':0,'8':0,'9':0,
        ':':0,';':0,'<':0,'=':0,'>':0,'?':0,'@':0,
        'A':0,'B':0,'C':0,'D':0,'E':0,'F':0,'G':0,
        'H':0,'I':0,'J':0,'K':0,'L':0,'M':0,'N':0,
        'O':0,'P':0,'Q':0,'R':0,'S':0,'T':0,
        'U':0,'V':0,'W':0,'X':0,'Y':0,'Z':0,
        '[':0,'\\':0,']':0,'^':0,'_':0,'`':0,
        'a':0,'b':0,'c':0,'d':0,'e':0,'f':0,'g':0,
        'h':0,'i':0,'j':0,'k':0,'l':0,'m':0,'n':0,
        'o':0,'p':0,'q':0,'r':0,'s':0,'t':0,
        'u':0,'v':0,'w':0,'x':0,'y':0,'z':0,
        '{':0,'|':0,'}':0,'~':0}
    return alphabet
    
# 统计字符：
# 字符总数
# 密码总行数
# 每个字符出现的总数
# 每个字符出现的总行数
# lists: 仅包含password的list
def count_freq(lists):
    filename = 'count_freq.txt' # 用于保存字频统计的结果
    chars_count = 0             # 统计字符总数量
    lines_count = len(lists)    # 存放密码条数
    alpha_all = __alphagen()    # 存放所有密码中出现相应字符的次数
    alpha_line = __alphagen()   # 存放每条密码中出现相应字符的条数
    alpha_lines = __alphagen()  # 存放所有密码中出现相应字符的条数
    for item in lists:
        chars_count += len(item)
        for c in item:
            for a in alpha_all:
                if a == c:
                    alpha_all[a] += 1
            for a in alpha_line:
                if a == c and alpha_line[a] == 0:
                    alpha_line[a] = 1
        alpha_lines = __merge_dict(alpha_lines, alpha_line)
        __reset_alphabet(alpha_line)
    with open(filename, 'a') as f:
        f.write('chars_count='+str(chars_count)+'\n')
        f.write('lines_count='+str(lines_count)+'\n')
        f.write('alpha_all\n')
        for item in alpha_all:
            f.write(item+':'+str(alpha_all[item])+'\n')
        f.write('alpha_lines\n')
        for item in alpha_lines:
            f.write(item+':'+str(alpha_lines[item])+'\n')
            

def pswd_len_count








        
'''
dict2 = {'ae3r':0, 'br32r':2, 'cfwf':0}
for i in dict2:
    print i
    print dict2[i]
'''

'''
dict1 = {'ae3r':20, 'br32r':12, 'cfwf':0}
dict2 = {'ae3r':0, 'br32r':2, 'cfwf':0}
print dict1
print dict2
x = union_dict(dict1, dict2)
print dict1
print dict2
print x
'''
'''
a = 'a'
b  = 'b'
for i in dict:
    if i == a:
        dict[i] += 1
print dict
'''
#lists = readtolist_id_pw('龙芯.txt')
'''
for i in lists:
    i.decode(detect(i)['encoding']).encode('utf-8')
    print detect(i)
'''    
'''
    print data
    unicodeData = data.decode("gb2312")
    print unicodeData
    gbkData = unicodeData.encode("utf-8")
    print gbkData
'''

