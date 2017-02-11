import csv, os, operator, time

# ��ʽ������ֵ��ȥ������������
# ��ʽ���弶�ơ�������
# ��ʽ���������� ���γ�����
# ��ʽ��ʱ���ַ���
# ���γ̱������ȥ�����������Լ�¼


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


# ��ʽ��ֵ
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
        # ��ʽ��ʱ���ַ���
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
    orgs.sort(key=operator.itemgetter(1)) # ���ݿγ̱������
    f_orgs = []
    for i in orgs:
        if i[1] == '5200080' or i[1] == '5200081': # �޳�������
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