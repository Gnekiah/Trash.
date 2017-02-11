import os, wx
import random
from math import log10
from copy import deepcopy
from PIL import Image
import numpy as np
from scipy.fftpack import dct
from scipy.fftpack import idct


LENA = 'lena.bmp'
SAVELENA = 'savelena.bmp'
DCT_ZERO = 0
DCT_ONES = 5
DCT_THSH = 15
DCT_XTRC = 10

alphadict ={'a':'000000','b':'000001','c':'000010','d':'000011','e':'000100','f':'000101','g':'000110','h':'000111',
            'i':'001000','j':'001001','k':'001010','l':'001011','m':'001100','n':'001101','o':'001110','p':'001111',
            'q':'010000','r':'010001','s':'010010','t':'010011','u':'010100','v':'010101','w':'010110','x':'010111',
            'y':'011000','z':'011001','0':'011010','1':'011011','2':'011100','3':'011101','4':'011110',' ':'011111',
            'A':'100000','B':'100001','C':'100010','D':'100011','E':'100100','F':'100101','G':'100110','H':'100111',
            'I':'101000','J':'101001','K':'101010','L':'101011','M':'101100','N':'101101','O':'101110','P':'101111',
            'Q':'110000','R':'110001','S':'110010','T':'110011','U':'110100','V':'110101','W':'110110','X':'110111',
            'Y':'111000','Z':'111001','5':'111010','6':'111011','7':'111100','8':'111101','9':'111110','*':'111111'}


def loadimg(imgpath):
    img = Image.open(imgpath)
    return np.array(img, dtype=np.uint8)


def saveimg(arr, imgpath):
    img = Image.fromarray(np.array(arr, dtype=np.uint8), 'L')
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


def src_encoding(text, length):
    textlength = len(text)
    for i in range(textlength, length):
        text += '*'
    seq = ""
    __textlist = list(text)
    try:
        for i in __textlist:
            seq += alphadict[i]
    except Exception:
        return ""
    return seq


def src_decoding(textseq):
    seq = []
    length = len(textseq) / 6
    for i in range(length):
        seq.append(textseq[i*6:(i+1)*6])
    text = ""
    for i in seq:
        val = __getvalue(i)
        if val == None:
            val = '*'
        text += val
    return text


def __getvalue(value):
    for name in alphadict:
        if alphadict[name] == value:
            return name
    return None




##################################

##################################
def __embedtext(arr, seqlist, MATRIX_SIZE, UNIT_SIZE):
    textlen = len(seqlist)
    offset = 0
    for i in range(MATRIX_SIZE):
        for j in range(MATRIX_SIZE):
            pos = i * MATRIX_SIZE + j - offset
            if textlen == 0:
                return
            asc = 0
            if seqlist[pos] == '0':
                asc = DCT_ZERO
            else:
                asc = DCT_ONES
            flag = False
            for m in range(UNIT_SIZE):
                for n in range(UNIT_SIZE):
                    atom = arr[i][j][m][n]
                    if atom < DCT_THSH and atom >= 0:
                        flag = True
                        arr[i][j][m][n] = asc
                    if atom > (0-DCT_THSH) and atom < 0:
                        flag = True
                        arr[i][j][m][n] = -asc
            if flag == True:
                textlen -= 1
            else:
                offset += 1


def __embed(arr, text, MATRIX_SIZE, UNIT_SIZE):
    seq = src_encoding(text, MATRIX_SIZE * MATRIX_SIZE)
    matrix = matrixcut(arr, MATRIX_SIZE, UNIT_SIZE)
    matrix = dodct(matrix, MATRIX_SIZE, UNIT_SIZE)
    __embedtext(matrix, list(seq), MATRIX_SIZE, UNIT_SIZE)
    doidct(matrix, MATRIX_SIZE, UNIT_SIZE)
    matrix = matrixmerge(matrix, MATRIX_SIZE, UNIT_SIZE)
    print psnr(arr, matrix)
    return matrix


def embed8(arr, text):
    MATRIX_SIZE = 64
    UNIT_SIZE = 8
    return __embed(arr, text, MATRIX_SIZE, UNIT_SIZE)


def embed32(arr, text):
    MATRIX_SIZE = 16
    UNIT_SIZE = 32
    return __embed(arr, text, MATRIX_SIZE, UNIT_SIZE)




############################################

############################################
def __extracttext(arr, MATRIX_SIZE, UNIT_SIZE):
    strseq = ""
    for i in range(MATRIX_SIZE):
        for j in range(MATRIX_SIZE):
            flag = False
            ones = 0
            zero = 0
            for m in range(UNIT_SIZE):
                for n in range(UNIT_SIZE):
                    atom = abs(arr[i][j][m][n])
                    if atom < DCT_XTRC:
                        flag = True
                        if atom > DCT_ONES:
                            ones += 1
                        else:
                            if abs(atom - DCT_ONES) < abs(atom - DCT_ZERO):
                                ones += 1
                            else:
                                zero += 1
            if flag == True:
                if ones > zero:
                    strseq += '1'
                else:
                    strseq += '0'
    return strseq


def __extract(arr, MATRIX_SIZE, UNIT_SIZE):
    matrix = matrixcut(arr, MATRIX_SIZE, UNIT_SIZE)
    matrix = dodct(matrix, MATRIX_SIZE, UNIT_SIZE)
    strseq = __extracttext(matrix, MATRIX_SIZE, UNIT_SIZE)
    text = src_decoding(strseq)
    return text.strip('*')


def extract32(imgpath):
    img = Image.open(imgpath)
    imgbeta = img.resize((128, 128), Image.ANTIALIAS)
    arr = np.array(imgbeta, dtype=np.uint8)
    MATRIX_SIZE = 16
    UNIT_SIZE = 8
    return __extract(arr, MATRIX_SIZE, UNIT_SIZE)


def extract8(imgpath):
    arr = loadimg(imgpath)
    MATRIX_SIZE = 64
    UNIT_SIZE = 8
    return __extract(arr, MATRIX_SIZE, UNIT_SIZE)


def embed8_test():
    imgpath = LENA
    text = 'As Sergey and I wrote in the original founders letter 11 years ago'+\
            'Google is not a conventional company We do not intend to become one'+\
            'As part of that we also said that you could expect us to make'+\
            'smaller bets in areas that might seem very speculative or even'+\
            'strange when compared to our current businesses From the start weve'+\
            'always strived to do more and to do important and meaningful things'+\
            'with the resources we have We did a lot of things that seemed crazy'+\
            'at the time Many of those crazy things now have over a billion'+\
            'users like Google Maps YouTube Chrome and Android And we havent'+\
            'stopped there We are still trying to do things other people think'+\
            'are crazy but we are super excited'
    arr = loadimg(imgpath)
    arrbeta = embed8(arr, text)
    saveimg(arrbeta, SAVELENA)


def extract8_test():
    imgpath = SAVELENA
    print extract8(imgpath)


#embed8_test()
extract8_test()
