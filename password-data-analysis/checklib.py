#!/bin/python2.7
#coding=gbk

import os, sys, re

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

# 判断是否包含且仅包含大写+数字
# @warning: 该判断必须放在单类型判断的后面，以if.else结构
def dig_cap(st):
    return re.search("^[A-Z0-9]+$", st)

# 判断是否包含且仅包含小写+数字
# @warning: 该判断必须放在单类型判断的后面，以if.else结构
def dig_low(st):
    return re.search("^[a-z0-9]+$", st)

# 判断是否包含且仅包含数字+符号
# @warning: 该判断必须放在单类型判断的后面，以if.else结构
def dig_sym(st):
    return re.search("^[^a-zA-Z]+$", st)

# 判断是否包含且仅包含大写+小写
# @warning: 该判断必须放在单类型判断的后面，以if.else结构
def cap_low(st):
    return re.search("^[a-zA-Z]+$", st)

# 判断是否包含且仅包含大写+符号
# @warning: 该判断必须放在单类型判断的后面，以if.else结构
def cap_sym(st):
    return re.search("^[^a-z0-9]+$", st)

# 判断是否包含且仅包含符号+小写
# @warning: 该判断必须放在单类型判断的后面，以if.else结构
def low_sym(st):
    return re.search("^[^0-9A-Z]+$", st)

# 判断是否包含且仅包含除数字
# @warning: 该判断必须放在双类型判断的后面，以if.else结构
def anti_dig(st):
    return re.search("^[^0-9]+$", st)

# 判断是否包含且仅包含除大写
# @warning: 该判断必须放在双类型判断的后面，以if.else结构
def anti_cap(st):
    return re.search("^[^A-Z]+$", st)
    
# 判断是否包含且仅包含除小写
# @warning: 该判断必须放在双类型判断的后面，以if.else结构
def anti_low(st):
    return re.search("^[^a-z]+$", st)
    
# 判断是否包含且仅包含除符号
# @warning: 该判断必须放在双类型判断的后面，以if.else结构
def anti_sym(st):
    return re.search("^[0-9A-Za-z]+$", st)    
    
# 判断是否包含生日
# return：True包含；None不包含
def birthcheck(st):
    return re.search("((19)?[5-9]{1}|(20[01]{1})[0-9]{1})((0{1}[0-9]{1})|(1{1}[0-2]{1})){1}([0-3]{1}[0-9]{1}){1}", st)
    
    
    
    
    
    
    
    
    
    
    
    
    


