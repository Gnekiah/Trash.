#! /bin/python2.7

import csv, os, itertools

meals_type = []
with open("meals_type.txt", 'rb') as read_meals_type:
    for line in read_meals_type:
        meals_type.append(line)

all_csv_files = os.listdir("./data/")
i = 0
s = len(all_csv_files)
while i < s:
    all_csv_files[i] = "./data/" + all_csv_files[i]
    i += 1

for filename in all_csv_files:
        if filename[-4:] != ".csv":
            continue
        with open(filename, 'rb') as read_file:
            read_in = csv.reader(read_file)
            for row in itertools.islice(read_in, 1, None):
                for meal in meals_type:
                    if meal == row[1]:
                        print meal