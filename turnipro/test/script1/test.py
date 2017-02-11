import time, datetime

""""
a = "2013/8/10 23:40:00"
timeArray = time.strptime(a, "%Y/%m/%d %H:%M:%S")
print timeArray
otherStyleTime = time.strftime("%Y-%m-%d %H:%M:%S", timeArray)
print otherStyleTime
timeStamp = int(time.mktime(timeArray))
print timeStamp

print "\n"+"\n"

timeStamp2 = 1381419600
timeArray2 = time.localtime(timeStamp2)
print timeArray2
otherStyleTime2 = time.strftime("%Y-%m-%d %H:%M:%S", timeArray2)
print otherStyleTime2
"""

"""
b = datetime.datetime.min
tim = time.strptime(b, "%Y-%m-%d %H:%M:%S")
print int(time.mktime(tim))
"""
""""
a = str(datetime.datetime.min)
timeArray = time.strptime(a, "%Y-%m-%d %H:%M:%S")
print timeArray
timeStamp = int(time.mktime(timeArray))
print timeStamp
"""
"""
time_beg = str(datetime.datetime.max)
print time_beg
time_beg_array = datetime.datetime.strptime(time_beg, "%Y-%m-%d %H:%M:%S.%f")
timeBegin = long(time.mktime(time_beg_array.timetuple()))
print timeBegin


mytime = "2015-02-16 10:36:41.387000"
myt = "2015-02-16 10:36:41"
myTime = datetime.datetime.strptime(mytime, "%Y-%m-%d %H:%M:%S.%f")
myT = datetime.datetime.strptime(myt, "%Y-%m-%d %H:%M:%S")
tmp = int(time.mktime(myTime.timetuple()))
tmp2 = int(time.mktime(myT.timetuple()))
myFormat = "%Y-%m-%d %H:%M:%S"
print tmp
print tmp2
print "Original", myTime
print "New", myTime.strftime(myFormat)


a = "2013/8/10 12:01:23"
A = time.strptime(a, "%Y/%m/%d  %H:%M:%S")
tmp = int(time.mktime(A))
otherStyleTime2 = time.strftime("%Y-%m-%d", A)
print otherStyleTime2
print A 
print tmp
"""


row = "2013/8/10 21:12:32"

which_day = time.strftime("%H:%M:%S", time.strptime(row, "%Y/%m/%d  %H:%M:%S"))
if "21:12:33" < which_day:
    print which_day
if "10:00:00" > which_day:
    print which_day + "<"
else:
    print which_day + ">" 




