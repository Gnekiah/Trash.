#!/bin/python2.7
#coding=gbk

import os, sys, re, datetime
from chardet import detect
from collections import OrderedDict
import checklib

# �����ַ�������
# alphabet: ���������ֵ�
# return null
def __reset_alphabet(alphabet):
    for i in alphabet:
        alphabet[i] = 0

# �ϲ������ֵ䣨��Ӧkey��value��ӣ�
# *objs: ���ϲ��Ķ���ֵ�
# return�� �ϲ�����ֵ�
def __merge_dict(*objs):
    # sof���ҵ���δ��룬��it works well, but i can not understand.
    keys = set(sum([obj.keys() for obj in objs],[]))  
    total = {}  
    for key in keys:  
        total[key] = sum([obj.get(key,0) for obj in objs])
    return total

# �����д���ļ�
# dictname: д����ֵ�����
# length_dict: ��¼����ͳ�Ƶ��ֵ�
# lines_count: ����Ŀ��
def __write_dict_todisk(dictname, dict, length_dict, lines_count):
    with open(dictname + '.csv', 'a') as f:
        dictsort = OrderedDict(sorted(dict.items(), key=lambda t: t[1]))
        for i in dictsort:
            f.write(i+','+str(dictsort[i])+','+str(dictsort[i]/float(length_dict[i]))+','+str(dictsort[i]/float(lines_count))+'\n')

# ��ȡ��ά�������ֵ�Լ�λ�ã�ͬʱ�����ֵ��Ϊ0
# return�����ֵ�Լ�λ��
def __getmax_of_matrix(L):
    F = []
    x = 0
    y = 0
    p = 0
    for i in range(94):
        for j in range(94):
            if L[i][j] > p:
                x = i
                y = j
                p = L[i][j]
    F.append(x)
    F.append(y)
    return F

# ����ά��������ǰ100��д���ļ�
# matrixname: ��ά��������
# matrix����ά����
# lines_count������Ŀ��
def __write_maxmatrix_todisk(matrixname, matrix, lines_count):
    with open(matrixname + '.csv', 'a') as f:
        for i in range(100):
            pv = __getmax_of_matrix(matrix)
            f.write(chr(0x21+pv[0])+chr(0x21+pv[1])+','+str(matrix[pv[0]][pv[1]])+','+str(matrix[pv[0]][pv[1]]/float(lines_count))+'\n')
            matrix[pv[0]][pv[1]] = 0

# ����һ��alphabet���key=ascii��ӡ�ַ���value=0���ֵ�
# return: alphabet�ֵ�
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
    d1 = datetime.datetime.now()
    savename = 'count_freq.txt' # ���ڱ�����Ƶͳ�ƵĽ��
    chars_count = 0             # ͳ���ַ�������
    lines_count = 0             # �����������
    birth_count = 0             # ���������а������յ�����
    alpha_all = __alphagen()    # ������������г�����Ӧ�ַ��Ĵ���
    alpha_line = __alphagen()   # ���ÿ�������г�����Ӧ�ַ�������
    alpha_lines = __alphagen()  # ������������г�����Ӧ�ַ�������
    ##########
    length_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}   # ���ȷֲ�ͳ��
    digital_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # �����֣��ֱ��Ӧ<=6,7,8,9,10,11,>=12����Ŀ����
    capital_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # ����д��ĸ
    lowercase_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}# ��Сд��ĸ
    symbol_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}   # �������
    ##########
    dig_cap_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # ����+��д��ĸ
    dig_low_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # ����+Сд��ĸ
    dig_sym_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # ����+����
    cap_low_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # ��д+Сд��ĸ
    cap_sym_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # ��д+����
    low_sym_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # Сд+����
    ##########
    anti_dig_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # �������������ַ�������
    anti_cap_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # ���˴�д��ĸ������
    anti_low_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # ����Сд��ĸ������
    anti_sym_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # ���˷��Ŷ�����
    clude_all_count = {'6':0,'7':0,'8':0,'9':0,'10':0,'11':0,'12':0}  # �����������͵ķ���
    ##########
    head_tail_chartype_count = [[0]*94, [0]*94]     # ��ά���飬��0�б�ʾ���ַ����Ͷ�Ӧ��������1�б�ʾβ�ַ����Ͷ�Ӧ����
    head_two_chartype_count = []                    # ��ά94*94���飬�ֱ��Ӧ���ַ����ͺ͵ڶ����ַ�����
    tail_two_chartype_count = []                    # ��ά94*94���飬�ֱ��Ӧβ���ַ����ͺ�β�ַ�����
    for htl in range(94):
        head_two_chartype_count.append([0]*94)
        tail_two_chartype_count.append([0]*94)
    ##########
    passwd_freq = {}                # ���ڱ���ʹ������ߵ�ǰһ��������
    for pfi in range(100):
        passwd_freq[str(pfi)] = 0
    itemstr = ''                    # �����ַ�������
    itemqua = 0                     # �����ַ�������
    ####################
    with open(filename, 'rb') as f:        
        # ���ÿһ������
        for item in f:
            item = item.strip('\n').strip('\r')
            length = len(item)
            lines_count += 1
            chars_count += length
            if checklib.birthcheck(item):
                birth_count += 1
            # ���һ�������е�ÿһ���ַ�
            for c in item:
                for a in alpha_all:
                    if a == c:
                        alpha_all[a] += 1
                for a in alpha_line:
                    if a == c and alpha_line[a] == 0:
                        alpha_line[a] = 1
            # ͳ��alpha_lines������alpha_line
            alpha_lines = __merge_dict(alpha_lines, alpha_line)
            __reset_alphabet(alpha_line)
            ####################
            # ����ͳ��
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
            if lines_count % 100000 == 0:
                print lines_count
            ####################
    # �����ļ��޸ĵ�λ��
    with open(savename, 'a') as f:
        f.write('���ַ���: ' + str(chars_count)+'\n')
        f.write('����Ŀ��: ' + str(lines_count)+'\n')
        f.write('�������յ���Ŀ��: ' + str(birth_count)+'\n')
        f.write('ƽ��ÿ���ַ���: ' + str(chars_count / float(lines_count))+'\n')
        f.write('����������Ϣռ����Ŀ���İٷֱ�: ' + str(birth_count / float(lines_count))+'\n')
        f.write('��������ʱ�䣺' + str(datetime.datetime.now()-d1)+'\n')
    ##########
    with open('alpha_all.csv', 'a') as f:
        dictsort = OrderedDict(sorted(alpha_all.items(), key=lambda t: t[1]))
        for i in dictsort:
            f.write(i + ',' + str(dictsort[i]) + ',' + str(dictsort[i] / float(chars_count))+'\n')
    with open('alpha_lines.csv', 'a') as f:
        dictsort = OrderedDict(sorted(alpha_lines.items(), key=lambda t: t[1]))
        for i in dictsort:
            f.write(i + ',' + str(dictsort[i]) + ',' + str(dictsort[i] / float(lines_count))+'\n')
    ##########
    with open('length_count.csv', 'a') as f:
        dictsort = OrderedDict(sorted(length_count.items(), key=lambda t: t[1]))
        for i in dictsort:
            f.write(i + ',' + str(dictsort[i]) + ',' + str(dictsort[i] / float(lines_count))+'\n')
    ##########
    __write_dict_todisk('digital_count', digital_count, length_count, lines_count)
    __write_dict_todisk('capital_count', capital_count, length_count, lines_count)
    __write_dict_todisk('lowercase_count', lowercase_count, length_count, lines_count)
    __write_dict_todisk('symbol_count', symbol_count, length_count, lines_count)
    __write_dict_todisk('dig_cap_count', dig_cap_count, length_count, lines_count)
    __write_dict_todisk('dig_low_count', dig_low_count, length_count, lines_count)
    __write_dict_todisk('dig_sym_count', dig_sym_count, length_count, lines_count)
    __write_dict_todisk('cap_low_count', cap_low_count, length_count, lines_count)
    __write_dict_todisk('cap_sym_count', cap_sym_count, length_count, lines_count)
    __write_dict_todisk('low_sym_count', low_sym_count, length_count, lines_count)
    __write_dict_todisk('anti_dig_count', anti_dig_count, length_count, lines_count)
    __write_dict_todisk('anti_cap_count', anti_cap_count, length_count, lines_count)
    __write_dict_todisk('anti_low_count', anti_low_count, length_count, lines_count)
    __write_dict_todisk('anti_sym_count', anti_sym_count, length_count, lines_count)
    __write_dict_todisk('clude_all_count', clude_all_count, length_count, lines_count)
    ##########
    with open('head_tail_chartype_count.csv', 'a') as f:
        listline1 = head_tail_chartype_count[0]
        listline2 = head_tail_chartype_count[1]
        for i in range(94):
            f.write(chr(0x21+i)+','+str(listline1[i])+','+str(listline1[i]/float(lines_count))+','+chr(0x21+i)+','+str(listline2[i])+','+str(listline2[i]/float(lines_count))+'\n')
    ##########
    __write_maxmatrix_todisk('head_two_chartype_count', head_two_chartype_count, lines_count)
    __write_maxmatrix_todisk('tail_two_chartype_count', tail_two_chartype_count, lines_count)
    ##########
    with open('passwd_freq.csv', 'a') as f:
        dictsort = OrderedDict(sorted(passwd_freq.items(), key=lambda t: t[1]))
        for i in dictsort:
            f.write(i + ',' + str(dictsort[i]) + ',' + str(dictsort[i] / float(lines_count))+'\n')     
#############################################

analysis('passwdtest.txt')

    
    
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

lists = readtolist_id_pw('��о.txt')

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

