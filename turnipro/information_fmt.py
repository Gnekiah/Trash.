import os, csv, itertools

# 按规范整理info_encoded.csv
# 使用代号替换原始文件的中文字符

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


# 加载区域编码 - mapping文件
def load_mapping(path):
    lists = []
    with open(path, 'rb') as f:
        for i in f:
            tmp = i.strip("\r\n").split(' ')
            lists.append(tmp)
    return lists


# 按规范处理
def format_info_encoded():
    mapping = load_mapping('cid-area-code-map.txt')
    orgs = load_csv("info_encoded.csv")
    for i in orgs[1]:
        i[2] = "CS"
        if i[5] == '2':
            i[5] = '0'
        if i[6] == '\xe6\xb1\x89\xe6\x97\x8f':
            i[6] = '0'
        else:
            i[6] = '1'
        for j in mapping:
            if i[1][:2] == j[1][:2]:
                i[1] = j[0][0]
        if i[7][0:2] == '\xe7\xbd':
            i[7] = '1'
        elif i[7][0:2] == '\xe4\xbf':
            i[7] = '2'
        elif i[7][0:2] == '\xe7\x89':
            i[7] = '3'
        else:
            i[7] = '4'
    return orgs


# 写回
def write_back_formatted():
    orgs = format_info_encoded()
    with open("info_encoded_fmt.csv", "wb") as f:
        line = ""
        for i in orgs[0]:
            line += i + ","
        f.write(line[:-1])
        f.write('\n')
        for i in orgs[1]:
            line = ""
            for j in i:
                line += j + ','
            f.write(line[:-1])
            f.write('\n')


# write_back_formatted()