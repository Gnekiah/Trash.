#-------------------------------------------------------------------------------
# Name:        Watermark embedding
# Purpose:     Embed watermark to image
#
# Author:      Gnekiah
#
# Created:     01/06/2016
# Copyright:   (c) Gnekiah 2016
# Licence:     <GNU General Public License>
#-------------------------------------------------------------------------------

# -*- coding: utf-8 -*- #

import const


wmsize = const.WMSIZE
depth = const.DEPTH


def wmembed(arr, arrsize, arrdms, wmarr, wmdms):
    arrlen = arrsize[0]*arrsize[1]
    wmarrlen = wmsize[0]*wmsize[1]
    if wmdms > arrdms:
        return None
    if arrlen < wmarrlen * 8:
        return None
    arrbeta = arr.reshape(1, arrlen, arrdms)[0]
    wmarrbeta = wmarr.reshape(1, wmarrlen, wmdms)[0]
    gap = float(arrlen) / (wmarrlen * 8)
    if wmdms == 1 and arrdms == 1:
        for i in range(wmarrlen):
            wmbyte = wmarrbeta[i]
            for j in range(8):
                wmbit = (wmbyte >> (7-j)) & 0b00000001
                for k in range(int(round(8*i*gap)), int(round(8*(i+1)*gap))):
                    arrbeta[k] = (arrbeta[k]/depth)*6 + 3*wmbit
        return arrbeta.reshape(arrsize[0], arrsize[1])
    if wmdms == 1 and arrdms == 3:
        for i in range(wmarrlen):
            wmbyte = wmarrbeta[i]
            for j in range(8):
                wmbit = (wmbyte >> (7-j)) & 0b00000001
                for k in range(int(round(8*i*gap)), int(round(8*(i+1)*gap))):
                    arrbeta[k][0] = (arrbeta[k][0]/depth)*6 + 3*wmbit
        return arrbeta.reshape(arrsize[0], arrsize[1], arrdms)
    if wmdms == 3 and arrdms == 3:
        for i in range(wmarrlen):
            for m in range(3):
                wmbyte = wmarrbeta[i][m]
                for j in range(8):
                    wmbit = (wmbyte >> (7-j)) & 0b00000001
                    for k in range(int(round(8*i*gap)), int(round(8*(i+1)*gap))):
                        arrbeta[k][m] = (arrbeta[k][m]/depth)*6 + 3*wmbit
        return arrbeta.reshape(arrsize[0], arrsize[1], arrdms)

