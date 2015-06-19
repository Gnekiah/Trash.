#include <QString>
#include <QFile>
#include <QDataStream>
#include <QDebug>
#include "huffcode/hufftree.h"

#define SUCCESS             0
#define CANNOT_OPEN_FILE    1

int charsCount(long (&charsStore)[256], QString& openPath,
               long& sourceFileLength, int& codeQuantity)
{
    QFile file(openPath);
    if (!file.open(QIODevice::ReadOnly)) return CANNOT_OPEN_FILE;

    QDataStream in(&file);
    quint8 tmp;

    while(!in.atEnd()) { in >> tmp; ++charsStore[tmp]; ++sourceFileLength; }
    file.close();

    for (int i = 0; i < 256; ++i)
        if (charsStore[i])
            ++codeQuantity;

    return SUCCESS;
}
