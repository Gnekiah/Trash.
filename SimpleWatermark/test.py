#-------------------------------------------------------------------------------
# Name:        妯″潡1
# Purpose:
#
# Author:      DouBear
#
# Created:     31/05/2016
# Copyright:   (c) DouBear 2016
# Licence:     <your licence>
#-------------------------------------------------------------------------------

from PIL import Image
import ioformat
from chaosmatrixgen import matrixgen
import numpy as np
from wmgencrypt import wmgencrypt, decrypt
from wmembed import wmembed
from psnr import psnr
from extract import extract
import const

wmsize = const.WMSIZE

def main():
    matrix = [[0 for i in range(64)] for j in range(64)]
    for i in range(0, 64):
        for j in range(0, 64):
            matrix[i][j] = (4*i+j) % 255
    # arr = ioformat.loadimg('lena256.jpg')
    # print arr
    #    arr = array.array('i', [[255, 255, 255], [255, 255, 255]])
    #print arr
    asas =np.array(matrix, dtype=np.uint8)
    ioformat.saveimg(asas, 1, "qwqw.jpg")
    print asas


def main2():
    asd = [0 for i in range(64)]
    for i in range(64):
        asd[i] = i
    del asd[12]
    del asd[12]
    del asd[12]
    del asd[12]
    del asd[12]
    print asd

def main3():
    m = matrixgen(raw_input())
    for i in range(28):
        for j in range(128):
            m[i][j] = m[i][j] % 255
    ioformat.saveimg(np.array(m, dtype=np.uint8), 1, "123zxc.jpg")


def main4():
    a = [1, 2, 3, 4, 5]
    print a
    a[4] = tttt(a[4])
    print a

def tttt(x):
    x = 12
    return x


def main5():
    arr, arrbeta, sizexy, dms = ioformat.loadimg("9105080.jpg")
    wmarr, wmdms = wmgencrypt(wmtext="", passwd="12345678")
    wmarrimg = wmembed(arr, sizexy, dms, wmarr, wmdms)
    psnrval = psnr(arrbeta, wmarrimg, sizexy, dms)
    ioformat.saveimg(wmarrimg, dms, "wmed.jpg")
    print psnrval

def main6():
    matrix = [[[0 for k in range(2)] for i in range(4)] for j in range(4)]
    arr = np.array(matrix, dtype=np.uint8).reshape(1, 4, 8)[0]
    print arr.reshape(8, 4)


def main7():
    arr, arrbeta, sizexy, dms = ioformat.loadimg("lena256.jpg")
    wmarr, wmdms = wmgencrypt(wmtext="234567", passwd="12345678")
    arr = wmembed(arr, sizexy, dms, wmarr, wmdms)
    # print psnr(arr, arrbeta, sizexy, dms)
    wm1, wm2 = extract(arr, sizexy, dms)
    wm3 = decrypt(wm1 ,matrixgen("12345678"))
    s = Image.fromarray(wmarr, "L")
    s1 = Image.fromarray(wm3, "L")
    s.show()
    s1.show()

    # print arr
    # print wmarr
    # print len(wm1)
    #if wm2 != None:
    #    print wm2
    # print psnr(wm1, wmarr, wmsize, 1)



if __name__ == '__main__':
    main7()
