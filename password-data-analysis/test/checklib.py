#!/bin/python2.7
#coding=gbk

import os, sys, re

# �ж��Ƿ�ȫ��Ϊ��д�ַ�
# st: ���жϵ��ַ���
# return: True or False
def __allupper(st):
    return re.search("^[A-Z]+$", st)

# �ж��Ƿ�ȫ��ΪСд�ַ�
# st: ���жϵ��ַ���
# return: True or False
def __alllower(st):
    return re.search("^[a-z]+$", st)

# �ж��Ƿ�ȫΪ����
# st: ���жϵ��ַ���
# return: True or False
def __allsymbol(st):
    return re.search("[^A-Za-z0-9]", st)

# �ж��Ƿ�����ҽ�������д+����
# @warning: ���жϱ�����ڵ������жϵĺ��棬��if.else�ṹ
def dig_cap(st):
    return re.search("^[A-Z0-9]+$", st)

# �ж��Ƿ�����ҽ�����Сд+����
# @warning: ���жϱ�����ڵ������жϵĺ��棬��if.else�ṹ
def dig_low(st):
    return re.search("^[a-z0-9]+$", st)

# �ж��Ƿ�����ҽ���������+����
# @warning: ���жϱ�����ڵ������жϵĺ��棬��if.else�ṹ
def dig_sym(st):
    return re.search("^[^a-zA-Z]+$", st)

# �ж��Ƿ�����ҽ�������д+Сд
# @warning: ���жϱ�����ڵ������жϵĺ��棬��if.else�ṹ
def cap_low(st):
    return re.search("^[a-zA-Z]+$", st)

# �ж��Ƿ�����ҽ�������д+����
# @warning: ���жϱ�����ڵ������жϵĺ��棬��if.else�ṹ
def cap_sym(st):
    return re.search("^[^a-z0-9]+$", st)

# �ж��Ƿ�����ҽ���������+Сд
# @warning: ���жϱ�����ڵ������жϵĺ��棬��if.else�ṹ
def low_sym(st):
    return re.search("^[^0-9A-Z]+$", st)

# �ж��Ƿ�����ҽ�����������
# @warning: ���жϱ������˫�����жϵĺ��棬��if.else�ṹ
def anti_dig(st):
    return re.search("^[^0-9]+$", st)

# �ж��Ƿ�����ҽ���������д
# @warning: ���жϱ������˫�����жϵĺ��棬��if.else�ṹ
def anti_cap(st):
    return re.search("^[^A-Z]+$", st)
    
# �ж��Ƿ�����ҽ�������Сд
# @warning: ���жϱ������˫�����жϵĺ��棬��if.else�ṹ
def anti_low(st):
    return re.search("^[^a-z]+$", st)
    
# �ж��Ƿ�����ҽ�����������
# @warning: ���жϱ������˫�����жϵĺ��棬��if.else�ṹ
def anti_sym(st):
    return re.search("^[0-9A-Za-z]+$", st)    
    
# �ж��Ƿ��������
# return��True������None������
def birthcheck(st):
    return re.search("((19)?[5-9]{1}|(20[01]{1})[0-9]{1})((0{1}[0-9]{1})|(1{1}[0-2]{1})){1}([0-3]{1}[0-9]{1}){1}", st)
    
    
    
    
    
    
    
    
    
    
    
    
    


