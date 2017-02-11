#!/bin/python2.7
#coding=gbk

import os, sys, re, datetime
from chardet import detect

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

# 判断是否全部为大写字符
# st: 待判断的字符串
# return: True or False
def __allupper(st):
    return re.search("^[A-Z]+$", st)

# 判断是否全部为小写字符
# st: 待判断的字符串
# return: True or False
def __alllower(st):
    return re.search("^[a-z]+$", st)

# 判断是否全为符号
# st: 待判断的字符串
# return: True or False
def __allsymbol(st):
    return re.search("[^A-Za-z0-9]", st)

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
    

def analysis(filename):
    savename = 'count_freq.txt' # 用于保存字频统计的结果
    chars_count = 0             # 统计字符总数量
    lines_count = 0             # 存放密码条数
    alpha_all = __alphagen()    # 存放所有密码中出现相应字符的次数
    alpha_line = __alphagen()   # 存放每条密码中出现相应字符的条数
    alpha_lines = __alphagen()  # 存放所有密码中出现相应字符的条数
    length_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}   # 长度分布统计
    digital_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 纯数字，分别对应<=6,7,8,9,10,11,>=12的条目数量
    capital_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 纯大写字母
    lowercase_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}# 纯小写字母
    symbol_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}   # 特殊符号
    ######################
    
    with open(filename, 'rt') as f:
        # 针对每一条数据
        for item in f:
            item = item.strip('\n').strip('\r')
            length = len(item)
            lines_count += 1
            chars_count += length
            # 针对一条数据中的每一个字符
            for c in item:
                for a in alpha_all:
                    if a == c:
                        alpha_all[a] += 1
                for a in alpha_line:
                    if a == c and alpha_line[a] == 0:
                        alpha_line[a] = 1
            # 统计alpha_lines并重置alpha_line
            alpha_lines = __merge_dict(alpha_lines, alpha_line)
            __reset_alphabet(alpha_line)
            # 长度统计
            if length <= 6:
                length_count['6'] += 1
                if item.isdigit():
                    digital_count['6'] += 1
                elif __allupper(item):
                    capital_count['6'] += 1
                elif __alllower(item):
                    lowercase_count['6'] += 1
                elif __allsymbol(item):
                    symbol_count['6'] += 1
                else:
                    pass  
            elif length >= 12:
                length_count['12'] += 1
                if item.isdigit():
                    digital_count['12'] += 1
                elif __allupper(item):
                    capital_count['12'] += 1
                elif __alllower(item):
                    lowercase_count['12'] += 1
                elif __allsymbol(item):
                    symbol_count['12'] += 1
                else: 
                    pass
            else:
                length_count[str(length)] += 1
                if item.isdigit():
                    digital_count[str(length)] += 1
                elif __allupper(item):
                    capital_count[str(length)] += 1
                elif __alllower(item):
                    lowercase_count[str(length)] += 1
                elif __allsymbol(item):
                    symbol_count[str(length)] += 1
                else: 
                    pass
#############################################
    print chars_count
    print lines_count
    print alpha_all
    print alpha_lines
    print length_count
    print digital_count
    print capital_count
    print lowercase_count
    print symbol_count


d1 = datetime.datetime.now()
analysis('passwdtest.txt')
d2 = datetime.datetime.now()
print d1
print d2 - d1
    
    
'''
    with open(filename, 'a') as f:
        f.write('chars_count='+str(chars_count)+'\n')
        f.write('lines_count='+str(lines_count)+'\n')
        f.write('alpha_all\n')
        for item in alpha_all:
            f.write(item+':'+str(alpha_all[item])+'\n')
        f.write('alpha_lines\n')
        for item in alpha_lines:
            f.write(item+':'+str(alpha_lines[item])+'\n') 
'''
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

lists = readtolist_id_pw('龙芯.txt')

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

