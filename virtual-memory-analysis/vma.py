#!/bin/env python2.7

import os

def mainfunc():
    if os.path.exists("./result-da") == False:
        os.makedirs("./result-da")
    if os.path.exists("./result-dp") == False:
        os.makedirs("./result-dp")
    allfiles = os.listdir("./workload")
    filepath = []
    for i in allfiles:
        filepath.append("./workload/" + i)
    for filename in filepath:
        cmd = "./da " + filename
        lists = os.popen(cmd).readlines()
        result = ""
        for i in lists:
            result += i
        with open("./result-da/"+filename[11:], "w") as f:
            f.write(result)
        print "./da completed with " + filename

    for filename in filepath:
        cmd = "./dp " + filename
        lists = os.popen(cmd).readlines()
        result = ""
        for i in lists:
            result += i
        with open("./result-dp/"+filename[11:], "w") as f:
            f.write(result)
        print "./dp completed with " + filename


if __name__ == "__main__":
    mainfunc()

