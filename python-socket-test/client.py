#!/usr/bin/env python2.7

import socket, os

HOST = '127.0.0.1'
PORT = 65533

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST, PORT))
while True:
    buff = raw_input()
    if buff == "close-connect":
        s.send(buff)
        print "It goes to close the connect\n"
        break
    s.send(buff)
    print s.recv(1024)
s.close()
