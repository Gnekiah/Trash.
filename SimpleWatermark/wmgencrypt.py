#-------------------------------------------------------------------------------
# Name:        Watermark generator and encryption
# Purpose:     Generate watermark via input text or load from file; and encrypt
#              with <out-of-order and non-repeated matrix>.
#
# Author:      Gnekiah
#
# Created:     31/05/2016
# Copyright:   (c) Gnekiah 2016
# Licence:     <GNU General Public License>
#-------------------------------------------------------------------------------

# -*- coding: utf-8 -*- #

# wmgencrypt(wmpath="", wmtext="", passwd="")
#   watermark generator and encryption
#       wmpath="" - watermark file path
#       wmtext="" - watermark text
#       passwd="" - password to encrypt
#   return
#       wmarren - watermark array encrypted
#       dms - dimension of each pixel
#   return None if error

# decrypt(wmarr, matrix)
#   decrypt watermark with <out-of-order and non-repeated matrix>
#       wmarren - watermark array encrypted
#       matrix - out-of-order and non-repeated matrix
#   return
#       wmarrde - watermark array decrypted


from ioformat import loadimg, saveimg
from chaosmatrixgen import matrixgen
from PIL import Image, ImageDraw, ImageFont
import numpy as np
from math import sqrt, ceil
from copy import deepcopy
import const

wmsize = const.WMSIZE
fonttype = const.FONTTYPE


# watermark generator and encryption
#       wmpath="" - watermark file path
#       wmtext="" - watermark text
#       passwd="" - password to encrypt
#   return
#       wmarren - watermark array encrypted
#       dms - dimension of each pixel
#   return None if error
def wmgencrypt(wmpath="", wmtext="", passwd=""):
    wmarr = None
    dms = None
    if wmpath != "":
        wmarr, dms = __loadwm(wmpath)
    elif wmtext != "":
        wmarr, dms = __genwm(wmtext)
    else:
        return None, None
    if wmarr == None:
        return None, None
    matrix = matrixgen(passwd)
    wmarren = __encrypt(wmarr, matrix)
    return wmarren, dms


# decrypt watermark with <out-of-order and non-repeated matrix>
#       wmarren - watermark array encrypted
#       matrix - out-of-order and non-repeated matrix
#   return
#       wmarrde - watermark array decrypted
def decrypt(wmarr, matrix):
    wmarrde = deepcopy(wmarr)
    for i in range(wmsize[0]):
        for j in range(wmsize[1]):
            wmarrde[matrix[i][j]/wmsize[0]][matrix[i][j]%wmsize[0]] = wmarr[i][j]
    return wmarrde


# load watermark from file
#       imgpath - input image path
#   return
#       arr - pixel array of source image
#       dms - dimension of each pixel, here is 3 or 1
#   return None if error
def __loadwm(imgpath):
    arr = None
    dms = None
    try:
        imgrs = Image.open(imgpath)
    except:
        return arr, dms
    img = imgrs.resize(wmsize)
    arr = np.array(img)
    dms = len(img.getbands())
    return arr, dms


# generate watermark by input text
#       wmtext - input text
#   return
#       arr - pixel array of source image
#       dms - dimension of each pixel, here is 1
#   return None if error
def __genwm(wmtext):
    if len(wmtext) == 0:
        return None, None
    if not isinstance(wmtext, unicode):
        wmtext = unicode(wmtext, 'UTF-8')
    side = int(ceil(sqrt(len(wmtext))))
    fontsize = wmsize[0] / side
    img = Image.new("L", wmsize, 255)
    dr = ImageDraw.Draw(img)
    font = ImageFont.truetype(fonttype, fontsize)
    for i in range(side):
        dr.text((0, i*fontsize-5), wmtext[side*i:side*(i+1)] , 0, font)
    del dr
    arr = np.array(img)
    return arr, 1


# encrypt watermark with <out-of-order and non-repeated matrix>
#       wmarr - watermark array
#       matrix - out-of-order and non-repeated matrix
#   return
#       wmarren - watermark array encrypted
def __encrypt(wmarr, matrix):
    wmarren = deepcopy(wmarr)
    for i in range(wmsize[0]):
        for j in range(wmsize[1]):
            wmarren[i][j] = wmarr[matrix[i][j]/wmsize[0]][matrix[i][j]%wmsize[0]]
    return wmarren

