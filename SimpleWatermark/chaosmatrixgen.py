#-------------------------------------------------------------------------------
# Name:        Chaos matrix generator
# Purpose:     Genarate an out-of-order and non-repeated matrix via password;
#              Using for encrypting watermark.
#
# Author:      Gnekiah
#
# Created:     31/05/2016
# Copyright:   (c) Gnekiah 2016
# Licence:     <GNU General Public License>
#-------------------------------------------------------------------------------

# -*- coding: utf-8 -*- #

# matrixgen(passwd)
#   Genarate an out-of-order and non-repeated matrix via password
#       passwd - generate matrix by passwd
#   return
#       matrix - out-of-order and non-repeated matrix


from hashlib import sha1
import const


wmsize = const.WMSIZE


# Genarate an out-of-order and non-repeated matrix via password
#       passwd - generate matrix by passwd
#   return
#       matrix - out-of-order and non-repeated matrix
def matrixgen(passwd):
    hashval = sha1(passwd).hexdigest()
    seq2 = seq1 = ""
    for i in range(0, 4):
        seq2 += hashval
        for j in range(0, 3):
            seq1 += hashval
    seqlen = wmsize[0] * wmsize[1]
    seqarr = [0 for i in range(seqlen)]
    for i in range(seqlen):
        seqarr[i] = i
    matrix = [[0 for i in range(wmsize[0])] for j in range(wmsize[1])]
    for i in range(wmsize[0]):
        for j in range(wmsize[1]):
            pos = int((seq2[i]+seq1[3*j:3*(j+1)]), 16) % seqlen
            matrix[i][j] = seqarr[pos]
            del seqarr[pos]
            seqlen -= 1
    return matrix

