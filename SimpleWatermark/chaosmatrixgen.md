������������㷨
===============

## �Դ����password����ϣ����
    hashval = sha1(passwd)
    
## �������������д�����hashval������չ
    seq1 = hashval + hashval +...+ hashval (��չ12��)
    seq2 = hashval +...+ hashval (��չ4��)
    40��4��������ˮӡ����Ϊ160��160
    
## ����һ����������
    seqarr = [0, 1, 2, 3, 4,..., size[x]*size[y]]
    
## ��matrix��ֵ
    seqlen = len(seqarr)
    matrix[i][j] = seqarr[seq2[i]+seq[3*j:3*(j+1)] % seqlen]
    seqlen -= 1