list = []
csv = []
list.append("234567890")
list.append("78987654")
csv.append(list)

list = []
list.append("12.23")
list.append("thid")
csv.append(list)

for row in csv:
    a = row[0]
    print a
    print float(a)
