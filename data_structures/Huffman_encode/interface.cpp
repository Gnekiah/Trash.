#include "interface.h"
#include "function/charscount.h"
#include "function/buildhuff.h"
#include <math.h>
#include <time.h>
#include <QMessageBox>
#include <QDebug>
#include <QFile>
#include <QFileDialog>
#include <QVector>

Interface::Interface(QWidget *parent)
    : QMainWindow(parent)
{
    setWindowTitle(tr("Encoder v1.0"));
    setFixedSize(440, 375);

    /*用于建立控件布局的绝对位置.*/
    QWidget *firstTabWidget = new QWidget(this);

    /*模式选择.*/
    selectEncode = new QRadioButton("Encode", firstTabWidget);
    selectEncode->setChecked(true);
    selectDecode = new QRadioButton("Decode", firstTabWidget);
    go = new QPushButton("Start", firstTabWidget);
    go->setStyleSheet("*{border-radius: 1px; color: gray; font-size: 40px;}"
                         "QPushButton:hover{border-radius: 25px; color: black;"
                         "border: 3px solid rgb(192, 192, 192);}");

    /*路径选择.*/
    selectOpenPath = new QPushButton("Open", firstTabWidget);
    showOpenPath = new QLabel(firstTabWidget);
    showOpenPath->setDisabled(true);
    selectSavePath = new QPushButton("Save", firstTabWidget);
    showSavePath = new QLabel(firstTabWidget);
    showSavePath->setDisabled(true);

    /*相关操作信息.*/
    sourceFileLength = new QLabel(firstTabWidget);
    codeQuantity = new QLabel(firstTabWidget);
    averageCodeLength = new QLabel(firstTabWidget);
    targetFileLength = new QLabel(firstTabWidget);
    compressionRate = new QLabel(firstTabWidget);
    usedTime = new QLabel(firstTabWidget);

    /*控件的绝对布局.*/
    /*模式选择布局.*/
    selectEncode->setGeometry(10, 30, 80, 20);
    selectDecode->setGeometry(130, 30, 80, 20);
    go->setGeometry(270, 10, 120, 70);

    /*路径选择布局.*/
    QLabel *sourceFile = new QLabel("Source File:", firstTabWidget);
    sourceFile->setDisabled(true);
    sourceFile->setGeometry(5, 80, 80, 20);
    selectOpenPath->setGeometry(85, 80, 40, 20);
    showOpenPath->setGeometry(125, 80, 320, 20);
    QLabel *targetFile = new QLabel("Target File:", firstTabWidget);
    targetFile->setDisabled(true);
    targetFile->setGeometry(5, 110, 80, 20);
    selectSavePath->setGeometry(85, 110, 40, 20);
    showSavePath->setGeometry(125, 110, 320, 20);

    /*原文件相关信息显示布局.*/
    QLabel *sourceFileInfo = new QLabel("Source File Information", firstTabWidget);
    sourceFileInfo->setDisabled(true);
    sourceFileInfo->setGeometry(10, 155, 160, 20);
    QLabel *sourceFileLengthLab = new QLabel("File Length:", firstTabWidget);
    sourceFileLengthLab->setDisabled(true);
    sourceFileLengthLab->setGeometry(10, 175, 80, 20);
    sourceFileLength->setGeometry(80, 195, 110, 20);
    QLabel *codeQuantityLab = new QLabel("Code Quantity:", firstTabWidget);
    codeQuantityLab->setGeometry(10, 215, 130, 20);
    codeQuantityLab->setDisabled(true);
    codeQuantity->setGeometry(80, 235, 110, 20);
    QLabel *averageCodeLengthLab = new QLabel("Average Code Length:", firstTabWidget);
    averageCodeLengthLab->setDisabled(true);
    averageCodeLengthLab->setGeometry(10, 255, 150, 20);
    averageCodeLength->setGeometry(80, 275, 110, 20);

    /*目标文件相关信息显示布局.*/
    QLabel *targetFileInfo = new QLabel("Target File Information", firstTabWidget);
    targetFileInfo->setDisabled(true);
    targetFileInfo->setGeometry(250, 155, 160, 20);
    QLabel *targetFileLengthLab = new QLabel("File Length:", firstTabWidget);
    targetFileLengthLab->setDisabled(true);
    targetFileLengthLab->setGeometry(250, 175, 160, 20);
    targetFileLength->setGeometry(320, 195, 110, 20);
    QLabel *compressionRateLab = new QLabel("Compression Rate:", firstTabWidget);
    compressionRateLab->setDisabled(true);
    compressionRateLab->setGeometry(250, 215, 160, 20);
    compressionRate->setGeometry(320, 235, 110, 20);
    QLabel *usingTimeLab = new QLabel("Using Time:", firstTabWidget);
    usingTimeLab->setDisabled(true);
    usingTimeLab->setGeometry(250, 255, 100, 20);
    usedTime->setGeometry(320, 275, 110, 20);

    /*编码表控件.*/
    codeTable = new QTableWidget(this);
    codeTable->setFixedSize(435, 300);
    codeTable->setColumnCount(2);
    codeTable->setEditTriggers(QAbstractItemView::NoEditTriggers);
    codeTable->setColumnWidth(0, 80);
    codeTable->setColumnWidth(1, 310);
    codeTable->setHorizontalHeaderItem(0, new QTableWidgetItem(tr("Ascii")));
    codeTable->setHorizontalHeaderItem(1, new QTableWidgetItem(tr("Code")));

    /* 显示作者信息.*/
    QLabel *aboutInfo = new QLabel(this);
    aboutInfo->setText(tr("                 20135134 熊熊\n"
                          "                 20135103 冉谨铭\n"
                          "                 20135145 李馨\n"
                          "                 20135138 梁婷芝\n"
                          "                 20135131 张博俊"));

    /*标签页控件.*/
    tab = new QTabWidget(this);
    tab->setGeometry(0, 0, 440, 335);
    tab->addTab(firstTabWidget, "Main Page");
    tab->addTab(codeTable, "Code Information");
    tab->addTab(aboutInfo, "About Editor");

    /*退出.*/
    exit = new QPushButton("Exit", this);
    exit->setGeometry(370, 340, 65, 30);

    connect(exit, &QPushButton::clicked, this, &Interface::close);
    connect(go, &QPushButton::clicked, this, &Interface::start);
    connect(selectOpenPath, &QPushButton::clicked, this, &Interface::openFile);
    connect(selectSavePath, &QPushButton::clicked, this, &Interface::saveFile);

}

Interface::~Interface()
{

}

void Interface::start()
{
    kaishi = clock();
    if (openPath.isEmpty())
    {
        QMessageBox::warning(this, tr("Read File"), tr("Select a source file please!"));
        return;
    }
    if (savePath.isEmpty())
    {
        QMessageBox::warning(this, tr("Save File"), tr("Select a path to save file please!"));
        return;
    }

    /*初始化.*/
    for (int i = 0; i < 256; ++i)
        charsStore[i] = 0;
    transInfo.sourceFileLength = 0;
    transInfo.targetFileLength = 0;
    transInfo.codeQuantity = 0;
    transInfo.averageCodeLength = 0.0;
    transInfo.usedTime = 0.0;
    transInfo.compressionRate = 0.0;

    if (selectEncode->isChecked())
        encode();
    else
        decode();

    QString str = QString::number(transInfo.sourceFileLength);
    sourceFileLength->setText(str);
    str = QString::number(transInfo.codeQuantity);
    codeQuantity->setText(str);
    str = QString::number(transInfo.averageCodeLength);
    averageCodeLength->setText(str);
    str = QString::number(transInfo.targetFileLength);
    targetFileLength->setText(str);
    str = QString::number(transInfo.compressionRate);
    compressionRate->setText(str);
    str = QString::number(transInfo.usedTime);
    usedTime->setText(str);
}

void Interface::openFile()
{
    openPath = QFileDialog::getOpenFileName(this, tr("Open File"), ".", tr("All File(*)"));
    showOpenPath->setText(openPath);
}

void Interface::saveFile()
{
    savePath = QFileDialog::getSaveFileName(this, tr("Save File"), "./untitled.hfm", tr("Code File(*.hfm)"));
    showSavePath->setText(savePath);
}

void Interface::encode()
{
    if (charsCount(charsStore, openPath, transInfo.sourceFileLength, transInfo.codeQuantity) == CANNOT_OPEN_FILE)
    { QMessageBox::warning(this, tr("Open File"), tr("Fail to open file\n%1").arg(openPath)); return; }

    long charsStoreBackUp[256];
    for (int i = 0; i < 256; ++i) {
        charsStoreBackUp[i] = charsStore[i];
    }

    HuffTree* huffTree = buildHuff(charsStore);
    QVector<QString> codeVector;
    QVector<quint8> charVector;

    while (huffTree->root()->getLeft() != 0 || huffTree->root()->getRight() != 0) {
        HuffNode* tmp = huffTree->root();
        QString stack;
        stack.clear();
        while(true) {
            HuffNode* tmpChild = tmp->getLeft();
            if (tmpChild != 0) {
                stack.append('0');
                if (tmpChild->getLeft() == 0 && tmpChild->getRight() == 0) {
                    if (tmpChild->getValue() != 0) {
                        charVector.append(tmpChild->getValue());
                        codeVector.append(stack);
                    }
                    delete tmpChild;
                    tmp->setLeft(0);
                    break;
                }
                else {
                    tmp = tmpChild;
                    continue;
                }
            }
            else {
                tmpChild = tmp->getRight();
                stack.append('1');
                if (tmpChild->getLeft() == 0 && tmpChild->getRight() == 0) {
                    if (tmpChild->getValue() != 0) {
                        charVector.append(tmpChild->getValue());
                        codeVector.append(stack);
                    }
                    delete tmpChild;
                    tmp->setRight(0);
                    break;
                }
                else {
                    tmp = tmpChild;
                    continue;
                }
            }
        }
    }

    QFile file(openPath);
    QFile save(savePath);
    if (file.open(QIODevice::ReadOnly) && save.open(QIODevice::WriteOnly)) {
        QDataStream in(&file);
        QDataStream out(&save);
        out << transInfo.codeQuantity;
        for (int i = 0; i < 256; ++i) {
            if (charsStoreBackUp[i] != 0)
                out << (quint32)charsStoreBackUp[i];
        }
        quint8 tmpin;
        QString tmpintmp;

        int flag = 0;
        while (!in.atEnd()) {
            in >> tmpin;
            for (int i = 0; i < charVector.length(); ++i) {
                if (tmpin == charVector[i]) {
                    tmpintmp.append(codeVector[i]);
                    ++flag;
                    break;
                }
            }
            if (flag == 20) {
                while (tmpintmp.length() >= 8)
                {
                    quint8 a = 0;
                    for (int k = 0; k < 8; ++k) {
                        if (tmpintmp[k] == '1')
                            a += pow(2, 7-k);
                    }
                    tmpintmp.remove(0, 8);
                    ++transInfo.targetFileLength;
                    out << a;
                }
                flag = 0;
            }
        }
        while (tmpintmp.length() > 0)
        {
            quint8 a = 0;
            for (int k = 0; k < 8; ++k) {
                if (tmpintmp[k] == '1')
                    a += pow(2, 7-k);
            }
            tmpintmp.remove(0, 8);
            out << a;
            ++transInfo.targetFileLength;
        }
    }

    codeTable->clear();
    int codelength = 0;
    for (int i = 0; i < charVector.length(); ++i) {
        codeTable->insertRow(codeTable->rowCount());
        codelength += codeVector[i].length();
        codeTable->setItem(i, 0, new QTableWidgetItem(QString::number(charVector[i])));
        codeTable->setItem(i, 1, new QTableWidgetItem(codeVector[i]));
    }
    transInfo.averageCodeLength = (double)codelength / transInfo.codeQuantity;
    transInfo.compressionRate = (double)transInfo.targetFileLength / transInfo.sourceFileLength;
    file.close();
    save.close();
    jieshu = clock();
    transInfo.usedTime = ((double)jieshu - kaishi) / 1000000;
}

void Interface::decode()
{
    QFile file(openPath);
    QFile save(savePath);
    if (file.open(QIODevice::ReadOnly) && save.open(QIODevice::WriteOnly)) {
        QDataStream in(&file);
        QDataStream out(&save);
        in >> transInfo.codeQuantity;
        quint8  tmpa;
        quint32 tmpb;
        for (int i = 0; i < transInfo.codeQuantity; ++i)
        {
            in >> tmpa;
            in >> tmpb;
            charsStore[tmpa] = tmpb;
        }

        HuffTree* huffTree = buildHuff(charsStore);
        HuffNode* pos = huffTree->root();

        QString readtmp;
        QString c;
        int flag = 0;
        while (!in.atEnd()) {
            in >> tmpa;
            c.clear();
            while (tmpa/2==0) {
                c.append(QString::number(tmpa%2));
                tmpa /= 2;
            }
            while (c.length() != 8)
                c.append('0');
            readtmp.append(c);

            ++flag;
            if (flag == 20) {
                for (int i =0; i < readtmp.length(); ++i) {
                    if (readtmp[0] == '0')
                        pos = pos->getLeft();
                    else
                        pos = pos->getRight();
                    if (pos->getLeft() == 0) {
                        out << pos->getValue();
                        pos = huffTree->root();
                    }
                }
                flag = 0;
            }
        }
        for (int i =0; i < readtmp.length(); ++i) {
            if (readtmp[0] == '0')
                pos = pos->getLeft();
            else
                pos = pos->getRight();
            if (pos->getLeft() == 0) {
                out << pos->getValue();
                pos = huffTree->root();
            }
        }
    }
    file.close();
    save.close();
    jieshu = clock();
    transInfo.usedTime = ((double)jieshu - kaishi) / 1000000;
}
