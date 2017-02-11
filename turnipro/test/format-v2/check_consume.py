import csv, os, operator, time, math

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


lists = os.listdir("consumption./")
time_list = ""
time_max = []
for i in lists:
    orgs = load_csv("./consumption/"+i)
    t_min = "2018/04/29 08:57:48"
    t_max = "2011/04/29 08:57:48"
    for i in orgs[1]:
        t_min = min(t_min, i[2])
        t_max = max(t_max, i[2])
    time_max.append(t_max)
    time_list += t_min + "    " + t_max + "\n"
print min(time_max)
with open("consume_timecheck.txt", "ab") as f:
    f.write(time_list)
