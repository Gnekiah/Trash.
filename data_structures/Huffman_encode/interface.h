#ifndef INTERFACE_H
#define INTERFACE_H

#include "datastore/transinfo.h"
#include "huffcode/hufftree.h"

#include <QMainWindow>
#include <QTabWidget>
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <QTableWidget>
#include <QRadioButton>
#include <QCheckBox>
#include <QString>
#include <QList>
#include <QVector>
#include <time.h>

class Interface : public QMainWindow
{
    Q_OBJECT

public:
    Interface(QWidget *parent = 0);
    ~Interface();

public:
    long            charsStore[256];    //
    QString         openPath;           // 保存打开文件路径
    QString         savePath;           // 保存目标文件路径
    TransInfo       transInfo;
    clock_t  kaishi, jieshu;

private:
    QTabWidget      *tab;               // 标签页

    QPushButton     *go;                // 开始按钮
    QPushButton     *exit;              // 退出按钮
    QRadioButton    *selectEncode;      // 选择编码
    QRadioButton    *selectDecode;      // 选择解码

    QPushButton     *selectOpenPath;    // 选择原文件路径
    QPushButton     *selectSavePath;    // 选择目标文件路径

    QLabel          *showOpenPath;      // 显示打开文件的路径
    QLabel          *showSavePath;      // 显示保存文件的路径
    QLabel          *sourceFileLength;  // 原文件长度
    QLabel          *codeQuantity;      // 编码长度
    QLabel          *averageCodeLength; // 平均编码长度
    QLabel          *targetFileLength;  // 目标文件长度
    QLabel          *compressionRate;   // 压缩比率
    QLabel          *usedTime;          // 用时

    QTableWidget    *codeTable;         // 编码表

public:
    void start(void);           // 开始
    void openFile(void);        // 选择打开文件路径
    void saveFile(void);        // 选择保存文件路径
    void encode(void);          // 编码
    void decode(void);          // 解码

};

#endif // INTERFACE_H
