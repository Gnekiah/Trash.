import csv, os, operator, time

# 格式化考试值，去除无用数据列
# 格式化五级制、二级制
# 格式化考试类型 、课程类型
# 格式化时间字符串
# 按课程编号排序，去除四六级考试记录


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


# 格式化值
def format_data(orgs):
    for i in orgs:
        try:  # 格式化分数（将分级制用分数替换）
              # 对于5级制，分别对应于95,85,75,65,0，对于两级制，对应于85,0
            i[3] = float(i[3])
        except:
            if i[3][0:2] == '\xba\xcf': # 合格
                i[3] = 85
            elif i[3][0:2] == '\xb2\xbb': # 不合格 / 不
                i[3] = 0
            elif i[3] == '\xbc\xb0': # 及
                i[3] = 65
            elif i[3] == "\xd6\xd0": # 中
                i[3] = 75
            elif i[3] == '\xc1\xbc': # 良
                i[3] = 85
            elif i[3] == '\xd3\xc5': # 优
                i[3] = 95
            else:
                print "ERROR, can not distinguish score:", i[3]
        if i[5] == 'None': # None为刷分考试
            i[5] = None
        elif i[5][0] == '\xd5' or i[5][0] == '\xb6': # 正常或二转
            i[5] = 0
        elif i[5][0] == '\xd6': # 重修
            i[5] = 1
        elif i[5][0] == '\xca': # 试读
            i[5] = 2
        else:
            print "ERROR, can not distinguish class type:", i[5]
        if i[8][0] == '\xd5': # 正考
            i[8] = 0
        elif i[8][0] == '\xb2': # 补考
            i[8] = 1
        elif i[8][0] == '\xbb': # 缓正
            i[8] = 2
        else:
            print "ERROR, can not distinguish exam type:", i[8]
        # 格式化时间字符串
        p_time = None
        try:
            p_time = time.strptime(i[-1], "%Y-%m-%d %H:%M:%S")
        except:
            try:
                p_time = time.strptime(i[-1], "%Y-%m-%d")
            except:
                p_time = None
        if p_time == None:
            print "ERROR, can not distinguish time string:", i[-1]
        else:
            i[-1] = time.strftime("%Y/%m/%d", p_time)
    orgs.sort(key=operator.itemgetter(1)) # 根据课程编号排序
    f_orgs = []
    for i in orgs:
        if i[1] == '5200080' or i[1] == '5200081': # 剔除四六级
            continue
        f_orgs.append([i[1], i[3], i[4], i[5], i[8], i[10]])
    return f_orgs


files = os.listdir("./examination_origin")
for j in files:
    orgs = load_csv("./examination_origin/" + j)
    f_orgs = format_data(orgs[1])
    with open("./examination/" + j, "wb") as f:
        f.write("CLASS_ID,SCORE,CREDIT,CLASS_TYPE,EXAM_TYPE,DATE")
        f.write("\n")
        for i in f_orgs:
            s = ""
            for k in i:
                s += str(k) + ','
            f.write(s[:-1])
            f.write("\n")