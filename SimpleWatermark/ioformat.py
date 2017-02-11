#-------------------------------------------------------------------------------
# Name:        Input and output formatted
# Purpose:     Load image and return array; save array as image.
#
# Author:      Gnekiah
#
# Created:     31/05/2016
# Copyright:   (c) Gnekiah 2016
# Licence:     <GNU General Public License>
#-------------------------------------------------------------------------------

# -*- coding: utf-8 -*- #

# loadimg(imgpath)
#   load image return pixel array and some property
#       imgpath - path of image
#   return
#       arr - pixel array of source image
#       arrbeta - a deep copy of arr
#       sizexy - tuple of image size eg:(1024, 768)
#       dms - dimension of each pixel
#   return None if error

# saveimg(arrbeta, dms, imgpath)
#   save image
#       arrbeta - array that embeded watermark
#       dms - dimension of each pixel
#       imgpath - path of origin image
#   return
#       True - save image successful
#       False - save image failed


import copy
from time import localtime, time, strftime
from PIL import Image
import numpy as np


# load image return pixel array and some property
#       imgpath - path of image
#   return
#       arr - pixel array of source image
#       arrbeta - a deep copy of arr
#       sizexy - tuple of image size eg:(1024, 768)
#       dms - dimension of each pixel
#   return None if error
def loadimg(imgpath):
    arr = None
    arrbeta = None
    sizexy = None
    dms = None
    try:
        img = Image.open(imgpath)
    except:
        return arr, arrbeta, sizexy, dms
    arr = np.array(img)
    arrbeta = copy.deepcopy(arr)
    sizexy = img.size
    dms = len(img.getbands())
    return arr, arrbeta, sizexy, dms


# save image
#       arrbeta - array that embeded watermark
#       dms - dimension of each pixel
#       imgpath - path of origin image
#   return
#       True - save image successful
#       False - save image failed
def saveimg(arrbeta, dms, imgpath):
    mode = ""
    if dms == 1:
        mode = "L"
    elif dms == 3:
        mode = "RGB"
    else:
        return False
    img = Image.fromarray(arrbeta, mode)
    img.save(imgpath[:-4]+'-'+strftime('%Y%m%d-%H%M%S',localtime(time()))+".png")
    return True

