#!/bin/python2.7

import datetime, time, os, csv

row = "2014/5/28  1:56:22"
a = time.strftime("%Y/%m/%d", time.strptime(row, "%Y/%m/%d %H:%M:%S"))
b = time.strftime("%H:%M:%S", time.strptime(row, "%Y/%m/%d %H:%M:%S"))

print a
print b
if a < "2011/8/13":
    print "false"
if a > "2011/8/13":
    print "true"
