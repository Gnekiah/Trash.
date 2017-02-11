import csv, os, operator

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


def format_meal_data(orgs):
    meals = []
    huxi3_amount = 0.0
    days = []
    bre_days = []
    bre_amount = 0.0
    f_bre_amount = 0.0
    din_days = []
    din_amount = 0.0
    for i in orgs:
        if i[1] == '12':
            huxi3_amount += 1
        if i[1] not in meals:
            meals.append(i[1])
        if i[2][0:10] not in days:
            days.append(i[2][0:10])
        if i[2][11:16] <= "09:00":
            if i[2][0:10] not in bre_days:
                bre_days.append(i[2][0:10])
                bre_amount += 1
                if i[2][11:16] >= "07:00":
                    f_bre_amount += 1
        if i[2][11:16] >= "21:00":
            if i[2][0:10] not in din_days:
                din_days.append(i[2][0:10])
                din_amount += 1
    return len(orgs), huxi3_amount, len(meals), f_bre_amount/len(days), bre_amount, bre_amount / len(days), din_amount, din_amount / len(days)



def format_all_meal_data():
    csv_files = os.listdir('./2012CSAll_encoded_fmt/')
    cost_list = []
    for csv_name in csv_files:
        csv_file = './2012CSAll_encoded_fmt/' + csv_name
        orgs = load_csv(csv_file)
        cnt, huxi3_amount, meals_cnt, f_bre_rate, bre_amount, bre_rate, din_amount, din_rate = format_meal_data(orgs[1])
        tmp = [csv_name[:-4]]
        tmp.append(f_bre_rate)
        tmp.append(bre_amount)
        tmp.append(bre_rate)
        tmp.append( din_amount)
        tmp.append(din_rate)
        tmp.append(meals_cnt)
        tmp.append(huxi3_amount / cnt)
        cost_list.append(tmp)
    with open('meal_data_fmt.csv', "wb")as f:
        f.write("学号,早餐规律频率,早餐次数,早餐比例,夜宵数量,夜宵比例,吃饭地点数量,三食堂频率\n")
        for i in cost_list:
            s = ""
            for j in i:
                s += str(j) + ','
            f.write(s[:-1])
            f.write('\n')


format_all_meal_data()