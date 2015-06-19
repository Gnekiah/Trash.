#ifndef TRANSINFO_H
#define TRANSINFO_H

class TransInfo
{
public:
    long    sourceFileLength;
    long    targetFileLength;
    int     codeQuantity;
    double  averageCodeLength;
    double  compressionRate;
    double  usedTime;
};

#endif
