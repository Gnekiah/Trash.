#!/bin/python2.7
#coding=gbk

import os, sys, re, datetime
from chardet import detect
from collections import OrderedDict
import checklib

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
    

def analysis(filename):
    savename = 'count_freq.txt' # 用于保存字频统计的结果
    chars_count = 0             # 统计字符总数量
    lines_count = 0             # 存放密码条数
    alpha_all = __alphagen()    # 存放所有密码中出现相应字符的次数
    alpha_line = __alphagen()   # 存放每条密码中出现相应字符的条数
    alpha_lines = __alphagen()  # 存放所有密码中出现相应字符的条数
    ##########
    length_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}   # 长度分布统计
    digital_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 纯数字，分别对应<=6,7,8,9,10,11,>=12的条目数量
    capital_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 纯大写字母
    lowercase_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}# 纯小写字母
    symbol_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}   # 特殊符号
    ##########
    dig_cap_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 数字+大写字母
    dig_low_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 数字+小写字母
    dig_sym_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 数字+符号
    cap_low_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 大写+小写字母
    cap_sym_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 大写+符号
    low_sym_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 小写+符号
    ##########
    anti_dig_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 除了数字其他字符都包含
    anti_cap_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 除了大写字母都包含
    anti_low_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 除了小写字母都包含
    anti_sym_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 除了符号都包含
    clude_all_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # 包含所有类型的符号
    ##########
    head_tail_chartype_count = [[0]*94, [0]*94]     # 二维数组，第0行表示首字符类型对应数量，第1行表示尾字符类型对应数量
    head_two_chartype_count = []                    # 二维94*94数组，分别对应首字符类型和第二个字符类型
    tail_two_chartype_count = []                    # 二维94*94数组，分别对应尾二字符类型和尾字符类型
    for htl in range(94):
        head_two_chartype_count.append([0]*94)
        tail_two_chartype_count.append([0]*94)
    ##########
    passwd_freq = {}                # 用于保存使用率最高的前一百条密码
    for pfi in range(100):
        passwd_freq[str(pfi)] = 0
    itemstr = ''                    # 保存字符串内容
    itemqua = 0                     # 保存字符串数量
    ####################
    with open(filename, 'rb') as f:        
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
            ####################
            # 长度统计
            if length <= 6:
                length_count['6'] += 1
                if item.isdigit():
                    digital_count['6'] += 1
                elif checklib.__allupper(item):
                    capital_count['6'] += 1
                elif checklib.__alllower(item):
                    lowercase_count['6'] += 1
                elif checklib.__allsymbol(item):
                    symbol_count['6'] += 1
                elif checklib.dig_cap(item):
                    dig_cap_count['6'] += 1
                elif checklib.dig_low(item):
                    dig_low_count['6'] += 1
                elif checklib.dig_sym(item):
                    dig_sym_count['6'] += 1
                elif checklib.cap_low(item):
                    cap_low_count['6'] += 1
                elif checklib.cap_sym(item):
                    cap_sym_count['6'] += 1
                elif checklib.low_sym(item):
                    low_sym_count['6'] += 1
                elif checklib.anti_dig(item):
                    anti_dig_count['6'] += 1
                elif checklib.anti_cap(item):
                    anti_cap_count['6'] += 1
                elif checklib.anti_low(item):
                    anti_low_count['6'] += 1
                elif checklib.anti_sym(item):
                    anti_sym_count['6'] += 1
                else:
                    clude_all_count['6'] += 1  
            elif length >= 12:
                length_count['12'] += 1
                if item.isdigit():
                    digital_count['12'] += 1
                elif checklib.__allupper(item):
                    capital_count['12'] += 1
                elif checklib.__alllower(item):
                    lowercase_count['12'] += 1
                elif checklib.__allsymbol(item):
                    symbol_count['12'] += 1
                elif checklib.dig_cap(item):
                    dig_cap_count['12'] += 1
                elif checklib.dig_low(item):
                    dig_low_count['12'] += 1
                elif checklib.dig_sym(item):
                    dig_sym_count['12'] += 1
                elif checklib.cap_low(item):
                    cap_low_count['12'] += 1
                elif checklib.cap_sym(item):
                    cap_sym_count['12'] += 1
                elif checklib.low_sym(item):
                    low_sym_count['12'] += 1
                elif checklib.anti_dig(item):
                    anti_dig_count['12'] += 1
                elif checklib.anti_cap(item):
                    anti_cap_count['12'] += 1
                elif checklib.anti_low(item):
                    anti_low_count['12'] += 1
                elif checklib.anti_sym(item):
                    anti_sym_count['12'] += 1
                else:
                    clude_all_count['12'] += 1
            else:
                length_count[str(length)] += 1
                if item.isdigit():
                    digital_count[str(length)] += 1
                elif checklib.__allupper(item):
                    capital_count[str(length)] += 1
                elif checklib.__alllower(item):
                    lowercase_count[str(length)] += 1
                elif checklib.__allsymbol(item):
                    symbol_count[str(length)] += 1
                elif checklib.dig_cap(item):
                    dig_cap_count[str(length)] += 1
                elif checklib.dig_low(item):
                    dig_low_count[str(length)] += 1
                elif checklib.dig_sym(item):
                    dig_sym_count[str(length)] += 1
                elif checklib.cap_low(item):
                    cap_low_count[str(length)] += 1
                elif checklib.cap_sym(item):
                    cap_sym_count[str(length)] += 1
                elif checklib.low_sym(item):
                    low_sym_count[str(length)] += 1
                elif checklib.anti_dig(item):
                    anti_dig_count[str(length)] += 1
                elif checklib.anti_cap(item):
                    anti_cap_count[str(length)] += 1
                elif checklib.anti_low(item):
                    anti_low_count[str(length)] += 1
                elif checklib.anti_sym(item):
                    anti_sym_count[str(length)] += 1
                else:
                    clude_all_count[str(length)] += 1
            ####################
            # ascii head 1; ascii tail 1; ascii head 2; ascii tail 2
            asch1 = ord(item[0])
            if asch1 > 0x20 and asch1 < 0x7f:
                head_tail_chartype_count[0][asch1-0x21] += 1
            asct1 = ord(item[-1])
            if asct1 > 0x20 and asct1 < 0x7f:
                head_tail_chartype_count[1][asct1-0x21] += 1
            if length > 1:
                asch2 = ord(item[1])
                if asch1 > 0x20 and asch1 < 0x7f and asch2 > 0x20 and asch2 < 0x7f:
                    head_two_chartype_count[asch1-0x21][asch2-0x21] += 1
                asct2 = ord(item[-2])
                if asct1 > 0x20 and asct1 < 0x7f and asct2 > 0x20 and asct2 < 0x7f:
                    tail_two_chartype_count[asct2-0x21][asct1-0x21] += 1
            ####################
            if item == itemstr:
                itemqua += 1
            else:
                minitem = min(passwd_freq.items(), key=lambda x: x[1])
                if itemqua > minitem[1]:
                    del passwd_freq[minitem[0]]
                    passwd_freq[itemstr] = itemqua
                itemstr = item
                itemqua = 1
            ####################
                    
#############################################
    print chars_count
    print lines_count
    print birth_count
    print alpha_all
    print alpha_lines
    ########
    print length_count
    print digital_count
    print capital_count
    print lowercase_count
    print symbol_count
    ########
    print dig_cap_count
    print dig_low_count
    print dig_sym_count
    print cap_low_count
    print cap_sym_count
    print low_sym_count
    ########
    print OrderedDict(sorted(anti_dig_count.items(), key=lambda t: t[1]))
    print OrderedDict(sorted(anti_cap_count.items(), key=lambda t: t[1]))
    print OrderedDict(sorted(anti_low_count.items(), key=lambda t: t[1]))
    print OrderedDict(sorted(anti_sym_count.items(), key=lambda t: t[1]))
    print OrderedDict(sorted(clude_all_count.items(), key=lambda t: t[1]))
    ########
    print head_tail_chartype_count
    print head_two_chartype_count
    print tail_two_chartype_count
    print OrderedDict(sorted(passwd_freq.items(), key=lambda t: t[1]))
    
    
    


d1 = datetime.datetime.now()
analysis('passwdtest.txt')
d2 = datetime.datetime.now()
print d2-d1
    
    
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

