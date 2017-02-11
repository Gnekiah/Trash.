import random
from math import log10
from copy import deepcopy
from PIL import Image
import numpy as np
from scipy.fftpack import dct
from scipy.fftpack import idct


LENA = 'lena.bmp'
SAVELENA = 'savelena.bmp'


def loadimg(imgpath):
    img = Image.open(imgpath)
    return np.array(img, dtype=np.uint8)


def saveimg(arr, imgpath):
    img = Image.fromarray(arr, 'L')
    img.save(imgpath)


def matrixcut(arr, mtr, ntr):
    matrix = [[0 for i in range(mtr)] for j in range(mtr)]
    for i in range(mtr):
        for j in range(mtr):
            matrixtmp = [[0 for m in range(ntr)] for n in range(ntr)]
            for m in range(ntr):
                for n in range(ntr):
                    matrixtmp[m][n] = arr[ntr*i+m][ntr*j+n]
            matrix[i][j] = matrixtmp
    return np.array(matrix, dtype=np.uint8)


def matrixmerge(arr, mtr, ntr):
    size =  mtr * ntr
    matrix = [[0 for i in range(size)] for j in range(size)]
    for i in range(mtr):
        for j in range(mtr):
            for m in range(ntr):
                for n in range(ntr):
                    matrix[ntr*i+m][ntr*j+n] = arr[i][j][m][n]
    return np.array(matrix, dtype=np.uint8)


def psnr(img, imgbeta):
    mse = 0.0
    for i in range(512):
        for j in range(512):
            dvalue = int(img[i,j])-imgbeta[i,j]
            mse += int(dvalue) * int(dvalue)
    mse = mse / (512*512)
    if mse == 0.0:
        return -1
    return (10 * log10((255*255) / mse))


def dodct(arr, mtr, ntr):
    ntr = ntr << 1
    dctmatrix = [[0 for i in range(mtr)] for j in range(mtr)]
    for i in range(mtr):
        for j in range(mtr):
            dctmatrix[i][j] = dct(dct(arr[i][j].T).T) / ntr
    return dctmatrix


def doidct(arr, mtr, ntr):
    ntr = ntr << 1
    for i in range(mtr):
        for j in range(mtr):
            arr[i][j] = idct(idct(arr[i][j].T).T) / ntr


def main():
    img = loadimg(LENA)
    matrix = matrixcut(img, 16, 32)
    print matrix[7][7]
    matrix = dodct(matrix, 16, 32)
    print matrix[7][7]
    doidct(matrix, 16, 32)
    print matrix[7][7]
    # x = matrixmerge(matrix, 64, 8)
    # print psnr(img, x)
    # saveimg(x, "12345678.bmp")


def test():
    x = [[145 ,144, 137, 134 ,129 ,120, 108,  94],
            [150, 149, 132 ,136 ,125, 119 ,107,  94],
            [152 ,145, 134 ,135, 126, 113, 104  ,98],
            [150, 146, 144 ,133 ,127, 113, 106 , 98],
            [148 ,137, 142 ,133 ,129, 118, 105 , 95],
            [153 ,147 ,142 ,139, 127, 122 ,111 , 97],
            [149, 145, 137 ,132, 127, 120 ,111 ,100],
            [150 ,147, 139 ,134, 127 ,115 ,108  ,98]]
    print np.array(x, dtype=np.uint8)
    print dct(dct(np.array(x, dtype=np.uint8).T).T)/128



if __name__ == '__main__':
    arr = loadimg(SAVELENA)
    img = Image.fromarray(arr, 'L')
    img.save('50.jpg', 'jpeg', quality=50)

