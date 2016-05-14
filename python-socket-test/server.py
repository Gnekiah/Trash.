#!/usr/bin/env python2.7

import socket, os

HOST = "127.0.0.1"
PORT = 65533

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(10)
while True:
    conn,addr = s.accept()
    try:
        conn.settimeout(30)
        while True:
            buff = conn.recv(1024)
            if len(buff) == 0:
                conn.send("Input something please!!")
            elif buff == "close-connect":
                conn.send("Goodbye, master!")
                conn.close()
                break
            else:
                conn.send("I have got your command: "+buff)
    except socket.timeout:
        pass
    conn.close()

