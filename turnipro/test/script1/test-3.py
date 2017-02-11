import datetime

a = "2013/04/12 12:11:24"
b = "2013/04/12 12:19:23"
m = "2013/04/26 13:45:27"
s = "2000/01/01 00:30:00"
t = "2000/01/01 00:00:00"
c = datetime.datetime.strptime(a, "%Y/%m/%d %H:%M:%S")
d = datetime.datetime.strptime(b, "%Y/%m/%d %H:%M:%S")
e = datetime.datetime.strptime(m, "%Y/%m/%d %H:%M:%S")
y = datetime.datetime.strptime(s, "%Y/%m/%d %H:%M:%S")
z = datetime.datetime.strptime(t, "%Y/%m/%d %H:%M:%S")
print c
print d
print e
p = d-c
q = e-c
f = y-z
print p
print q
print f
if p > f:
    print str(p) + ">" + str(f)
else:
    print str(p) + "<" + str(f)
if q > f:
    print str(q) + ">" + str(f)
else:
    print str(q) + "<" + str(f)
