#-------------------------------------------------------------------------------
# Name:        Watermark
# Purpose:     Create watermark and check out it.
#
# Author:      Gnekiah
#
# Created:     26/05/2016
# Copyright:   (c) Gnekiah 2016
# Licence:     <GNU General Public License>
#-------------------------------------------------------------------------------

# -*- coding: utf-8 -*- #

import os
from PIL import Image, ImageDraw, ImageFont

SIZE32 = (32, 32)
SIZE64 = (64, 64)


def wmgen(wmtext):
    if not isinstance(wmtext, unicode):
        wmtext = unicode(wmtext, 'UTF-8')
    img = Image.new("1", SIZE32, 1)
    dr = ImageDraw.Draw(img)
    font = ImageFont.truetype("C:/Windows/Fonts/msyh.ttf", 32)
    dr.text((0, 0), wmtext , 0, font)


    del dr
    # img.save("123456.png")
    img.show()



def main():
    img = Image.open('C:/Users/DouBear/Desktop/watermark/lena256.png')
    # img.save('lena256.png')
    x = img.size[0]
    y = img.size[1]
    print img.getbands()


    # img.putpixel((i,j), (242,156 ,177))
    # img.show()

if __name__ == '__main__':
    #wmtext = raw_input()
    #wmgen(wmtext)
    main()




'''
def main():
    img = Image.open('C:/Users/DouBear/Desktop/watermark/9105080.jpg')
    arr = np.array(img)
    zxc = copy.deepcopy(arr)
    for i in range(0, 20):
        for j in range(0, 20):
            for k in range(0, 3):
                zxc[i,j][k] = 0
    print psnr(arr, zxc, img.size, len(img.getbands()))
    for i in range(0, 20):
        for j in range(0, 20):
            for k in range(0, 3):
                arr[i,j] = 0
    zxc = Image.fromarray(arr, 'RGB')
    zxc.save('2345.png')


if __name__ == '__main__':
    main()
'''