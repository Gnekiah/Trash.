import csv, os, operator

# ����csv�ļ�
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


# ����ÿ��ѧ�ŵ�GPA���ҿ������ܿ�Ŀ�����ҿ���
def format_data(orgs):
    for i in orgs:
        try:  # ��ʽ�����������ּ����÷����滻��
              # ����5���ƣ��ֱ��Ӧ��95,85,75,65,0�����������ƣ���Ӧ��85,0
            i[3] = float(i[3])
        except:
            if i[3][0:2] == '\xba\xcf': # �ϸ�
                i[3] = 85
            elif i[3][0:2] == '\xb2\xbb': # ���ϸ� / ��
                i[3] = 0
            elif i[3] == '\xbc\xb0': # ��
                i[3] = 65
            elif i[3] == "\xd6\xd0": # ��
                i[3] = 75
            elif i[3] == '\xc1\xbc': # ��
                i[3] = 85
            elif i[3] == '\xd3\xc5': # ��
                i[3] = 95
            else:
                print "ERROR, can not distinguish score:", i[3]
        if i[5] == 'None': # NoneΪˢ�ֿ���
            i[5] = None
        elif i[5][0] == '\xd5' or i[5][0] == '\xb6': # �������ת
            i[5] = 0
        elif i[5][0] == '\xd6': # ����
            i[5] = 1
        elif i[5][0] == '\xca': # �Զ�
            i[5] = 2
        else:
            print "ERROR, can not distinguish class type:", i[5]
        if i[8][0] == '\xd5': # ����
            i[8] = 0
        elif i[8][0] == '\xb2': # ����
            i[8] = 1
        elif i[8][0] == '\xbb': # ����
            i[8] = 2
        else:
            print "ERROR, can not distinguish exam type:", i[8]
    orgs.sort(key=operator.itemgetter(1))
    # �޳�������
    tmp_list = []
    for i in orgs:
        if i[1] == '5200080' or i[1] == '5200081':
            continue
        else:
            tmp_list.append(i)
    orgs = tmp_list
    # �ϲ�����������
    ori_list = []
    ext_list = []
    for i in orgs:
        if i[1] not in ori_list:
            ori_list.append(i[1])
            continue
        ext_list.append(i)
    tmp_orgs = []
    tmp_id = []
    for i in orgs:
        if i[1] not in tmp_id:
            tmp_id.append(i[1])
            tmp_orgs.append(i)
    orgs = tmp_orgs
    for i in ext_list:
        for j in orgs:
            if j[1] == i[1]:
                j[8] = 1
                if j[3] > 59:
                    j[3] = 60
                    break
                if i[3] > 59:
                    j[3] = 60
                else:
                    j[3] = 0
                break
    # ��ֵ
    gpa = 0.0
    qua = len(orgs)
    fal = 0
    for i in orgs:
        if i[8] == 1 or i[3] < 59.9:
            fal += 1
        if i[3] > 90:
            gpa += 4.0
        elif i[3] < 59.9:
            pass
        else:
            gpa += (i[3] - 50) / 10
    gpa = gpa / qua
    return gpa, fal, qua, float(fal) / qua


#��������ѧ����GPA���ҿ������ܿ�Ŀ�����ҿ���
def format_all_data():
    csv_files = os.listdir('./all_data/')
    grade_list = []
    grade_list.append(['SID', 'GPA', 'FAILED', 'AMOUNT', 'FAILEDRATE'])
    for csv_name in csv_files:
        csv_file = './all_data/' + csv_name
        orgs = load_csv(csv_file)
        gpa, fal, qua ,rate = format_data(orgs[1])
        tmp = []
        tmp.append(csv_name[:-4])
        tmp.append(gpa)
        tmp.append(fal)
        tmp.append(qua)
        tmp.append(rate)
        grade_list.append(tmp)
    return grade_list


grade_list = format_all_data()
with open('all_data_fmt.csv', 'wb') as f:
    for i in grade_list:
        s = ""
        for j in i:
            s += str(j) + ','
        f.write(s[:-1])
        f.write('\n')
