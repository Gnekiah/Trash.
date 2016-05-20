#!/bin/python2.7

import os, threading, time, random

class thrd(threading.Thread):
    pid = 0
    tmr = 0
    def __init__(self, pcb):
        threading.Thread.__init__(self)
        pid = pcb['pid']
        tmr = pcb['timer']

    def run(self):
        while True:
            if LOCK[self.pid] == 1:
                for i in range(self.tmr):
                    print 'Thread'+str(self.pid)+': '+str(TIMER)
                    TIMER += 1
                    time.sleep(1)
                LOCK[self.pid] = 0
                RUNED = 0
            else:
                time.sleep(1)


def pcbgen(pid, tmr):
    return {'pid':pid, 'timer':tmr}


LOCK = [0] * 20
LOCK[0] = 1
TIMER = 0
RUNED = 0

def main():
    pcbmap = []
    threads = []
    for i in range(20):
        pcb = pcbgen(i, random.randint(2, 10))
        pcbmap.append(pcb)
        print pcb['pid']
        t = thrd(pcb)
        threads.append(t)
        t.start()
    while True:
        for i in range(20):
            if RUNED == 1:
                time.sleep(1)
            elif LOCK[i-1] == 0:
                LOCK[i] = 1
                time.sleep(1)
            else: pass
        

main()
