import csv, datetime, time, operator


# 加载csv文件
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


# 整理出所有消费类型,并用ID替换,然后将映射关系写到文件
def format_sites(orgs):
    sites = []
    sites_list = []
    for i in orgs:
        if i[1] not in sites:
            sites.append(i[1])
    cnt = 0
    with open("tmp_sites.csv", "wb") as f:
        f.write('ID,SITES\n')
        for i in sites:
            tmp = []
            cnt += 1
            tmp.append(str(cnt))
            tmp.append(i)
            sites_list.append(tmp)
            f.write(str(cnt))
            f.write(',')
            f.write(i)
            f.write('\n')
    for i in orgs:
        for j in sites_list:
            if i[1] == j[1]:
                i[1] = j[0]


# 根据就餐时间按先后顺序排序，并按照学号写入文件
def sort_by_time(head, org):
    DIR = "2012CSAll_encoded_fmt"
    for item in org:
        s_time = time.strftime("%Y/%m/%d %H:%M:%S", time.strptime(item[2], "%Y/%m/%d  %H:%M:%S"))
        item[2] = s_time
    org.sort(key=operator.itemgetter(2))
    filepath = DIR + "/" + org[0][0] + '.csv'
    print len(org)
    with open(filepath, "wb") as f:
        line = ""
        for i in head:
            line += i + ","
        f.write(line[:-1])
        f.write('\n')
        for i in org:
            line = ""
            for j in i:
                line += j + ","
            f.write(line[:-1])
            f.write('\n')


# 分理处不同的学号，剔除消费金额为正的条目
def format_all_encoded(orgs):
    sid_list = []
    for item in orgs[1]:
        if item[-1][0] != "-":
            continue
        if sid_list == []:
            sid_list.append(item)
            continue
        if sid_list[0][0] == item[0]:
            sid_list.append(item)
            continue
        sort_by_time(orgs[0], sid_list)
        sid_list = []
        sid_list.append(item)
    sort_by_time(orgs[0], sid_list)


#ori_path = "2012CSAll_encoded.csv"
#orgs = load_csv(ori_path)
#format_sites(orgs[1])
#format_all_encoded(orgs)
#print orgs[1][100]
