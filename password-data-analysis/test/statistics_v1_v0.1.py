#!/bin/python2.7
#coding=gbk

import os, sys
from chardet import detect

# tip: ��ȡ�ļ���ÿ�е��ַ�������\r\n���Լ��м�ļ����\t
# filename: ���ļ������ж�ȡ�ļ�����ʽ��Ϊ['count' 'password']
# return: ������ʽ����Ԫ��list
def readtolist_id_pw(filename):
    lists = []
    with open(filename, 'rt') as f:
        for i in f:
            tmp = i.strip('\n').strip('\r')
            tmp = tmp.split('\t')
            lists.append(tmp)
            print tmp
    return lists

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
    
# ͳ���ַ���
# �ַ�����
# ����������
# ÿ���ַ����ֵ�����
# ÿ���ַ����ֵ�������
# lists: ������password��list
def count_freq(lists):
    filename = 'count_freq.txt' # ���ڱ�����Ƶͳ�ƵĽ��
    chars_count = 0             # ͳ���ַ�������
    lines_count = len(lists)    # �����������
    alpha_all = __alphagen()    # ������������г�����Ӧ�ַ��Ĵ���
    alpha_line = __alphagen()   # ���ÿ�������г�����Ӧ�ַ�������
    alpha_lines = __alphagen()  # ������������г�����Ӧ�ַ�������
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
#lists = readtolist_id_pw('��о.txt')
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

