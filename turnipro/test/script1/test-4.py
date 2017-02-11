import csv

with open("consume_type.csv", 'rb') as read_file:
    read_in = csv.reader(read_file)
    flag = 0
    for row in read_in:
        print row[0]
        break
    for row in read_in:
        print row[0]
        break
        