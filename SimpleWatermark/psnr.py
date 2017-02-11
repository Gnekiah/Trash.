#-------------------------------------------------------------------------------
# Name:        PSNR
# Purpose:     Calculate psnr by origin image and embeded image.
#
# Author:      Gnekiah
#
# Created:     31/05/2016
# Copyright:   (c) Gnekiah 2016
# Licence:     <GNU General Public License>
#-------------------------------------------------------------------------------

# -*- coding: utf-8 -*- #

# psnr(img, imgbeta, sizexy, dms)
#   calculate psnr by img and imgbeta
#       img - array
#       imgbeta - array
#       sizexy - size of image
#       dms - dimension of each pixel
#   return
#       psnr
#   return 0 if error


import math


# calculate psnr by img and imgbeta
#       img - array
#       imgbeta - array
#       sizexy - size of image
#       dms - dimension of each pixel
#   return
#       psnr
def psnr(img, imgbeta, sizexy, dms):
    maxI = (1 << 8*dms) - 1
    mse = __mse(img, imgbeta, sizexy, dms)
    if mse == 0.0:
        return -1
    return (10 * math.log10(maxI * maxI / mse))


# calculate mse
#       img - array
#       imgbeta - array
#       sizexy - size of image
#       dms - dimension of each pixel
#   return
#       mse
#   return 0 if error
def __mse(img, imgbeta, sizexy, dms):
    mse = 0.0
    for i in range(0, sizexy[0]):
        for j in range(0, sizexy[1]):
            for k in range(0, dms):
                dvalue = int(img[i,j][k])-imgbeta[i,j][k]
                mse += int(dvalue) * int(dvalue)
    return mse / (sizexy[0]*sizexy[1]*dms)


