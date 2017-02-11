import datetime, time, os

a = "2013-05-03"
gap = "2013-04-01"
b = "2013-01-01"

time_gap = datetime.datetime.strptime("2000-01-01", "%Y-%m-%d") - datetime.datetime.strptime("2000-01-01", "%Y-%m-%d")
bb = datetime.datetime.strptime(b, "%Y-%m-%d")
aa = datetime.datetime.strptime(a, "%Y-%m-%d")
if bb > aa