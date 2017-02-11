#!/bin/python2.7
#coding=gbk

import os, sys
from mylib import File_operation, Format_operation

def func1():
    fo = File_operation()
    filename = '��о�ʺ���������.txt'
    # �ļ���ǰ5������Ч����
    rows = fo.get_lines_from_file(filename, 5)
    with open('passwd/' + filename, 'w') as f:
        for i in range(0, len(rows)):
            # ��ÿһ�����ݽ��а��ո���Ƭ���������˵���Ԫ��
            tmp = rows[i].split(' ')
            while '' in tmp:
                tmp.remove('')
            # ��ÿһ�У�������ȱʧ�����Լ���Ч������,���������Լ���ԭʼ�ļ��е�λ�ü�¼д��error log
            if len(tmp) != 3 or '@' not in tmp[1] and '@' not in tmp[2]:
                print tmp
                fo.writing_errorlog(rows[i], filename, i+5)
                continue
            f.write(tmp[2] + '\t' + tmp[1] + '\n')

def func2():    
    fo = File_operation()
    filename = '���������ʺ�����.txt'
    rows = fo.get_lines_from_file(filename)
    with open('passwd/' + filename, 'w') as f:
        for i in range(0, len(rows)):
            # ��ÿһ��������Ƭ����
            tmp = rows[i].split('----')
            while '' in tmp:
                tmp.remove('')
            # ��ÿһ�У�������ȱʧ�����Լ���Ч������
            if len(tmp) != 2 or '@' not in tmp[0] and '@' not in tmp[1]:
                print tmp
                fo.writing_errorlog(rows[i], filename, i)
                continue
            f.write(tmp[0] + '\t' + tmp[1] + '\n')
            
def func3():
    f = File_operation()
    filename = '12306�ʺ����뼰������Ϣ.txt'
    rows = f.get_lines_from_file(filename)
    idpwlist = []
    for i in range(0, len(rows)):
        # ��ÿһ��������Ƭ����
        tmp = rows[i].split('----')
        while '' in tmp:
            tmp.remove('')
        # ��ÿһ�У�������ȱʧ�����Լ���Ч������
        if len(tmp) != 7:
            print tmp
            f.writing_errorlog(rows[i], filename, i)
            continue
        idpwlist.append(tmp)     
    f.put_idpasswd_to_file(idpwlist, 'passwd/'+filename, 0, 1)

def func4():
    f = File_operation()
    filename = '��Ϸ��̳49w��������.txt'
    rows = f.get_lines_from_file(filename)
    idpwlist = []
    for i in rows:
        tmp = i.split(' ')
        idpwlist.append(tmp)
    f.put_idpasswd_to_file(idpwlist, 'passwd/'+filename, 0, 1)
    
def func5():
    f = File_operation()
    filename = 'WOW-100w�˺�����.txt'
    rows = f.get_lines_from_file(filename)
    idpwlist = []
    count = 0
    for i in rows:
        count += 1
        tmp = i.split(' ')
        while '' in tmp:
            tmp.remove('')
        if len(tmp) != 2:
            f.writing_errorlines(i)
            continue
        idpwlist.append(tmp)
    f.put_idpasswd_to_file(idpwlist, 'passwd/'+filename, 0, 1)

def func6():
    f = File_operation()
    filename = '__tmp_errorlog_'
    rows = f.get_lines_from_file(filename)
    idpwlist = []
    count = 0
    for i in rows:
        count += 1
        tmp = i.split('----')
        while '' in tmp:
            tmp.remove('')
        if len(tmp) != 2 or '@' not in tmp[0]:
            f.writing_errorlog(i, filename, count, '___error_.txt')
            continue
        idpwlist.append(tmp)
    f.put_idpasswd_to_file(idpwlist, 'passwd/WOW-100w�˺�����.txt', 0, 1)
  
'''     
def func7():
    forma = Format_operation()
    filename = '7k7k��126��163��178��766��5000w��17173��csdn��tianya��duduniu��duowan��houdao��ispeakcn��kaixin��mop��qq13E��renren��rockyou��̨��2G��uuu9��zhenai���ʺ�����.txt'
    fl = 'plain1.txt'
    with open(filename, 'rb') as f:
        list = []
        count = 0
        for i in f:
            count += 1
            # if count < 40000000:
            #    continue
            if forma.is_ascii2(i):
                tmp = i.replace('\r','').replace('\n','').replace('\t', ' ').split(' ')
                while '' in tmp:
                    tmp.remove('')
                if len(tmp) != 1:
                    print str(count) + '---' + i
                    continue
                list.append(tmp)  
            if count == 10000000:
                print count
                break
    with open(fl, 'a') as f:
        for i in list:
            f.write(i + '\n')
'''
    
def func8():
    fo = File_operation()
    filename = 'gmail.com�ʺ�����2.txt'
    rows = fo.get_lines_from_file(filename)
    count = 0
    idpwlist = []
    for row in rows:
        count += 1
        tmp = row.split(' ')
        while '' in tmp:
            tmp.remove('')
        if len(tmp) != 2 or '@' not in tmp[0]:
            tmp = row.split(',')
            if len(tmp) != 2:
                fo.writing_errorlog(row, filename, count)
                print row
                continue        
        idpwlist.append(tmp)
    fo.put_idpasswd_to_file(idpwlist, 'passwd/' + filename, 0, 1)

def func9():
    fo = File_operation()
    filename = '163.com�ʺ�����.txt'
    rows = fo.get_lines_from_file(filename)
    count = 0
    idpwlist = []
    for row in rows:
        count += 1
        tmp = row.split('----')
        while '' in tmp:
            tmp.remove('')
        if len(tmp) != 2 or '@' not in tmp[0]:
            tmp = row.split(' ')
            if len(tmp) != 2:
                fo.writing_errorlog(row, filename, count)
                print row
                continue        
        idpwlist.append(tmp)
    fo.put_idpasswd_to_file(idpwlist, 'passwd/' + filename, 0, 1)
    
def func10():
    fo = File_operation()
    filename = 'gmail.com�ʺ�����.txt'
    rows = fo.get_lines_from_file(filename)
    count = 0
    idpwlist = []
    for row in rows:
        count += 1
        tmp = row.split(':')
        while '' in tmp:
            tmp.remove('')
        if len(tmp) != 2 or '@' not in tmp[0]:
            if len(tmp) > 2:
                flag = None
                for x in tmp:
                    if flag == None:
                        flag = x + '\t'
                        continue
                    flag += x
                tmp = flag.split('\t')
            else:
                fo.writing_errorlog(row, filename, count)
                print row
                continue
        idpwlist.append(tmp)
    fo.put_idpasswd_to_file(idpwlist, 'passwd/' + filename, 0, 1)

'''
def func11():
    filename = 'duowan_user800w.txt'
    count = 0
    with open(filename, 'r') as f:
        for i in f:
            count +=1
            if count < 9000:
                continue
            print i
            if count > 9300:
                return
    print count
''' 
    
def func12():
    filename = '�½� �ı��ĵ�.txt'
    fo = File_operation()
    lists = []
    count = 0
    with open(filename, 'r') as f:
        for i in f:
            count +=1
            i = i.replace('\n', '').replace('\r', '')
            tmp = i.split('\t')
            while '' in tmp:
                tmp.remove('')
            if len(tmp) != 2:
                print i
                fo.writing_errorlog(i, filename, count)
                continue
            lists.append(tmp)
    fo.put_idpasswd_to_file(lists, 'passwd' + filename, 0, 1)

def func13():
    filename = '1.txt'
    fo = File_operation()
    rows = fo.get_lines_from_file(filename)
    count = 0
    idpwlist = []
    for row in rows:
        count += 1
        tmp = row.split(',')
        while '' in tmp:
            tmp.remove('')
        if len(tmp) != 2 or '@' not in tmp[0]:
            if len(tmp) > 2:
                flag = None
                for x in tmp:
                    if flag == None:
                        flag = x + '\t'
                        continue
                    flag += x
                tmp = flag.split('\t')
            else:
                fo.writing_errorlog(row, filename, count)
                print row
                continue     
        idpwlist.append(tmp)
    fo.put_idpasswd_to_file(idpwlist, 'passwd' + filename, 0, 1)
    
def func14():
    filename = '2.txt'
    fo = File_operation()
    rows = fo.get_lines_from_file(filename)
    count = 0
    idpwlist = []
    for row in rows:
        count += 1
        tmp = row.split(' ')
        while '' in tmp:
            tmp.remove('')
        if len(tmp) != 2 or '@' not in tmp[0]:
            fo.writing_errorlog(row, filename, count)
            print row
            continue 
        idpwlist.append(tmp)
    fo.put_idpasswd_to_file(idpwlist, 'passwd' + filename, 0, 1)

def func15():
    filename = '3.txt'
    fo = File_operation()
    rows = fo.get_lines_from_file(filename)
    count = 0
    idpwlist = []
    for row in rows:
        count += 1
        tmp = row.split(' ')
        while '' in tmp:
            tmp.remove('')
        if len(tmp) != 2 or '@' not in tmp[0]:
            if len(tmp) > 2:
                flag = None
                for x in tmp:
                    if flag == None:
                        flag = x + '\t'
                        continue
                    flag += x
                tmp = flag.split('\t')
            else:
                fo.writing_errorlog(row, filename, count)
                print row
                continue     
        idpwlist.append(tmp)
    fo.put_idpasswd_to_file(idpwlist, 'passwd' + filename, 0, 1)
    
def func16():
    savefile = 'QQ50w.txt'
    errorfile = '__tmp_errorlog_QQ50w.txt'
    fo = File_operation()
    allfiles = os.listdir("./")
    for filename in allfiles:
        if filename[-4:] != ".txt":
            continue
        rows = fo.get_lines_from_file(filename)
        count = 0
        idpwlist = []
        for row in rows:
            count += 1
            tmp = row.split(' ')
            while '' in tmp:
                tmp.remove('')
            idpwlist.append(tmp)
        print filename
        fo.put_idpasswd_to_file(idpwlist, savefile, 1, 2)

def func17():
    savefile = 'sina.com.txt'
    errorfile = '__tmp_errorlog_sina.com.txt'
    fo = File_operation()
    allfiles = os.listdir("./")
    for filename in allfiles:
        if filename[-4:] != ".txt":
            continue
        rows = fo.get_lines_from_file(filename)
        count = 0
        idpwlist = []
        for row in rows:
            count += 1
            tmp = row.split('----')
            while '' in tmp:
                tmp.remove('')
            if len(tmp) != 2:
                fo.writing_errorlog(row, errorfile, count)
                print row
                continue     
            idpwlist.append(tmp)
        print filename
        fo.put_idpasswd_to_file(idpwlist, savefile, 0, 1)

def func18():
    savefile = '�ﵺ.txt'
    errorfile = '__tmp_errorlog_�ﵺ.txt'
    fo = File_operation()
    allfiles = os.listdir("./")
    print allfiles
    for filename in allfiles:
        if filename[-4:] != ".txt":
            continue
        rows = fo.get_lines_from_file(filename)
        print len(rows)
        count = 0
        idpwlist = []
        for row in rows:
            count += 1
            tmp = row.split(' ')
            while '' in tmp:
                tmp.remove('')
            if len(tmp) != 2:
                if len(tmp) > 2:
                    flag = None
                    for x in tmp:
                        if flag == None:
                            flag = x + '\t'
                            continue
                        flag += x
                    tmp = flag.split('\t')
                else:
                    fo.writing_errorlog(row, filename, count, errorfile)
                    print row
                    continue
            idpwlist.append(tmp)
            if count % 100000 == 0:
                print len(idpwlist)
        print len(idpwlist)
        fo.put_idpasswd_to_file(idpwlist, savefile, 0, 1)

'''
savefile = '�ﵺ.txt'
errorfile = '__tmp_errorlog_�ﵺ.txt'
fo = File_operation()
allfiles = os.listdir("./")
print allfiles
for filename in allfiles:
    if filename[-4:] != ".txt":
        continue
    rows = fo.get_lines_from_file(filename)
    with open('abc' + filename, 'a') as f1, open('bcd' + filename, 'a') as f2:
        count = 0
        for i in rows:
            count += 1
            if count < 4000000:
                f1.write(i + '\n')
            else:
                f2.write(i + '\n')
'''

def func19():
    savefile = '����.txt'
    errorfile = '__tmp_errorlog_����.txt'
    fo = File_operation()
    allfiles = os.listdir("./")
    for filename in allfiles:
        if filename[-4:] != ".txt":
            continue
        rows = fo.get_lines_from_file(filename)
        count = 0
        idpwlist = []
        for row in rows:
            count += 1
            tmp = row.split('----')
            while '' in tmp:
                tmp.remove('')
            if len(tmp) != 2:
                fo.writing_errorlog(row, filename, count, errorfile)
                print row
                continue     
            idpwlist.append(tmp)
        print filename
        fo.put_idpasswd_to_file(idpwlist, savefile, 0, 1)

def func20():
    savefile = '����V2.txt'
    errorfile = '__tmp_errorlog_����V2.txt'
    fo = File_operation()
    allfiles = os.listdir("./")
    for filename in allfiles:
        if filename[-4:] != ".txt":
            continue
        rows = fo.get_lines_from_file(filename)
        count = 0
        idpwlist = []
        for row in rows:
            count += 1
            tmp = row.split('----')
            while '' in tmp:
                tmp.remove('')
            if len(tmp) == 1:
                tmp = row.split(' ')
                if len(tmp) > 2:
                    flag = None
                    for x in tmp:
                        if flag == None:
                            flag = x + '\t'
                            continue
                        flag += x
                    tmp = flag.split('\t')
                elif len(tmp) == 1:
                    fo.writing_errorlog(row, filename, count, errorfile)
                    print row
                    continue     
            idpwlist.append(tmp)
        print filename
        fo.put_idpasswd_to_file(idpwlist, savefile, 0, 1)

def func21():
    savefile = '����V3.txt'
    errorfile = '__tmp_errorlog_����V3.txt'
    fo = File_operation()
    allfiles = os.listdir("./")
    for filename in allfiles:
        if filename[-4:] != ".txt":
            continue
        rows = fo.get_lines_from_file(filename)
        count = 0
        idpwlist = []
        for row in rows:
            count += 1
            tmp = row.split('----')
            while '' in tmp:
                tmp.remove('')
            if len(tmp) != 2:
                fo.writing_errorlog(row, filename, count, errorfile)
                print row
                continue     
            idpwlist.append(tmp)
        print filename
        fo.put_idpasswd_to_file(idpwlist, savefile, 0, 1)

'''
file1 = 'gmail.com�ʺ�����.txt'
file2 = 'gmail.com�ʺ�����2.txt'
fo = File_operation()
rows = []
with open(file2, 'r') as f2, open(file1, 'a') as f1:
    for i in f2:
        rows.append(i.strip('\n').strip('\r'))
    for j in rows:
        f1.write(j+'\n')
'''   
'''
file1 = '126.com.txt'
file2 = '163.com�ʺ�����.txt'
file3 = '����.txt'
file4 = '����V2.txt'
file5 = '����V3.txt'
file6 = '���������ʺ�����.txt'
fo = File_operation()
rows = []
count = 0
with open(file1, 'r') as f1, open(file2, 'r') as f2, open(file3, 'r') as f3, open(file4, 'a') as f4, open(file5, 'r') as f5, open(file6, 'r') as f6:
    for i in f1:
        f4.write(i.strip('\n').strip('\r') + '\n')
    for i in f2:
        f4.write(i.strip('\n').strip('\r') + '\n')
    for i in f3:
        f4.write(i.strip('\n').strip('\r') + '\n')
    for i in f5:
        f4.write(i.strip('\n').strip('\r') + '\n')
    for i in f6:
        f4.write(i.strip('\n').strip('\r') + '\n')
'''
'''
count = 0
with open('����.txt', 'r') as f:
    for i in f:
        count += 1
print count 
'''