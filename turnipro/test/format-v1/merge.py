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
orgs1 = load_csv('all_data_fmt.csv')
orgs2 = load_csv('info_encoded_fmt.csv')
orgs3 = load_csv('meal_data_fmt.csv')
head.extend(orgs1[0])
head.extend(orgs2[0][1:])
head.extend(orgs3[0][1:])

for i in orgs1[1]:
    tmp = []
    flag = False
    for j in orgs2[1]:
        if i[0] == j[0]:
            tmp.extend(i)
            tmp.extend(j[1:])
            orgs2[1].remove(j)
            flag = True
            break
    if not flag:
        continue
    for j in orgs3[1]:
        if i[0] == j[0]:
            tmp.extend(j[1:])
            orgs3[1].remove(j)
            flag = False
            break
    if not flag:
        body.append(tmp)
with open("merge.csv", "wb") as f:
    for i in head:
        f.write(i)
        f.write(',')
    f.write('\n')
    for i in body:
        for j in i:
            f.write(j)
            f.write(',')
        f.write('\n')