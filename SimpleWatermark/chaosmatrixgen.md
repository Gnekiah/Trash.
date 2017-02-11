乱序矩阵生成算法
===============

## 对传入的password做哈希运算
    hashval = sha1(passwd)
    
## 定义两个空序列串，用hashval进行扩展
    seq1 = hashval + hashval +...+ hashval (扩展12次)
    seq2 = hashval +...+ hashval (扩展4次)
    40×4长度允许水印上限为160×160
    
## 生成一组有序数对
    seqarr = [0, 1, 2, 3, 4,..., size[x]*size[y]]
    
## 对matrix赋值
    seqlen = len(seqarr)
    matrix[i][j] = seqarr[seq2[i]+seq[3*j:3*(j+1)] % seqlen]
    seqlen -= 1