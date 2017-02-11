#-------------------------------------------------------------------------------
# Name:        Watermark extracting
# Purpose:     extract watermark from image
#
# Author:      Gnekiah
#
# Created:     01/06/2016
# Copyright:   (c) Gnekiah 2016
# Licence:     <GNU General Public License>
#-------------------------------------------------------------------------------

# -*- coding: utf-8 -*- #

import const
import numpy as np


wmsize = const.WMSIZE
depth = const.DEPTH


def extract(arr, arrsize, arrdms):
    arrlen = arrsize[0]*arrsize[1]
    wmarrlen = wmsize[0]*wmsize[1]
    if arrlen < wmarrlen * 8:
        return None
    matrixL = [[0 for i in range(wmsize[0])] for j in range(wmsize[1])]
    matrixRGB = [[[0 for k in range(3)] for i in range(wmsize[0])] for j in range(wmsize[1])]
    wmarrL = np.array(matrixL, dtype=np.uint8).reshape(1, wmarrlen, 1)[0]
    wmarrRGB = np.array(matrixRGB, dtype=np.uint8).reshape(1, wmarrlen, 3)[0]
    arrbeta = arr.reshape(1, arrlen, arrdms)[0]
    gap = float(arrlen) / (wmarrlen * 8)
    if arrdms == 1:
        for i in range(wmarrlen):
            wmbyte = 0b00000000
            for j in range(8):
                zero = 0
                ones = 0
                for k in range(int(round(8*i*gap)), int(round(8*(i+1)*gap))):
                    if arrbeta[k]%depth > 1 and arrbeta[k]%depth < 5:
                        ones += 1
                    else:
                        zero += 1
                if zero > ones:
                    wmbyte = (wmbyte << j)
                else:
                    wmbyte = (wmbyte << j) + 0b00000001
            wmarrL[i] = wmbyte
        return wmarrL.reshape(wmsize[0], wmsize[1]), None
    if arrdms == 3:
        for i in range(wmarrlen):
            wmbyte = 0b00000000
            wmbyteRGB = [0b00000000, 0b00000000, 0b00000000]
            for j in range(8):
                zero = 0
                ones = 0
                zeroRGB = [0, 0, 0]
                onesRGB = [0, 0, 0]
                for k in range(int(round(8*i*gap)), int(round(8*(i+1)*gap))):
                    if arrbeta[k][0]%depth > 1 and arrbeta[k][0]%depth < 5:
                        ones += 1
                    else:
                        zero += 1
                    for m in range(3):
                        if arrbeta[k][m]%depth > 1 and arrbeta[k][m]%depth < 5:
                            onesRGB[m] += 1
                        else:
                            zeroRGB[m] += 1
                if zero > ones:
                    wmbyte = (wmbyte << j)
                else:
                    wmbyte = (wmbyte << j) + 0b00000001
                for n in range(3):
                    if zeroRGB[n] > onesRGB[n]:
                        wmbyteRGB[n] = (wmbyteRGB[n] << j)
                    else:
                        wmbyteRGB[n] = (wmbyteRGB[n] << j) + 0b00000001
            wmarrL[i] = wmbyte
            for n in range(3):
                wmarrRGB[i][n] = wmbyteRGB[n]
    return wmarrL.reshape(wmsize[0], wmsize[1]), wmarrRGB.reshape(wmsize[0], wmsize[1], 3)


