import csv, os, operator, time

# ¼ÓÔØcsvÎÄ¼þ
def load_csv(csv_path):
    csv_head = []
    csv_list = []
    with open(csv_path, 'rb') as f:
        f_csv = csv.reader(f)
        for row in f_csv:
            csv_head.append(row)
            break
        for row in f_csv:
            csv_list.append(row)
    return csv_head[0], csv_list


head = []
body = []
orgs1 = load_csv('merge.csv')
orgs2 = load_csv('_fifth___.csv')
head.extend(orgs1[0])
head.extend(orgs2[0][4:])

for i in orgs1[1]:
    tmp = []
    for j in orgs2[1]:
        if i[0] == j[0]:
            tmp.extend(i)
            tmp.extend(j[4:])
            orgs2[1].remove(j)
            body.append(tmp)
            break

with open("_fifth__merge.csv", "wb") as f:
    for i in head:
        f.write(i)
        f.write(',')
    f.write('\n')
    for i in body:
        for j in i:
            f.write(j)
            f.write(',')
        f.write('\n')