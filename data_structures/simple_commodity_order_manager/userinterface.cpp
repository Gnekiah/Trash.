#include "userinterface.h"
#include "data_structure/product.h"
#include "define_dialog/newfiledialog.h"

#include <QWidget>
#include <QTableWidget>
#include <QTableWidgetItem>
#include <QMenu>
#include <QMenuBar>
#include <QToolBar>
#include <QPushButton>
#include <QTabWidget>
#include <QLineEdit>
#include <QComboBox>
#include <QSpinBox>
#include <QFormLayout>
#include <QVBoxLayout>
#include <QGridLayout>
#include <QDebug>
#include <QMessageBox>
#include <QString>
#include <QFile>
#include <QFileDialog>
#include <QTextStream>

UserInterface::UserInterface(QWidget *parent)
    : QMainWindow(parent)
{
    /* 初始化.*/
    setWindowTitle(tr("简单商品订购管理"));
    setFixedSize(955,560);

    /* 定义动作对象.*/
    newAction = new QAction(QIcon(":file/new"), tr("&新建..."), this);
    newAction->setShortcuts(QKeySequence::New);
    newAction->setStatusTip(tr("新建文件..."));
    openAction = new QAction(QIcon(":file/open"), tr("&打开..."), this);
    openAction->setShortcuts(QKeySequence::Open);
    openAction->setStatusTip(tr("打开文件..."));
    saveAction = new QAction(QIcon(":file/save"), tr("&保存..."), this);
    saveAction->setShortcuts(QKeySequence::Save);
    saveAction->setStatusTip(tr("保存当前文件..."));
    closeAction = new QAction(QIcon(":/file/close"), tr("&关闭"), this);
    closeAction->setShortcuts(QKeySequence::Close);
    closeAction->setStatusTip(tr("关闭当前文件"));
    aboutAction = new QAction(tr("关于"), this);
    aboutAction->setStatusTip(tr("关于作者"));
    quitAction = new QAction(tr("&退出"), this);
    quitAction->setShortcuts(QKeySequence::Quit);
    quitAction->setStatusTip(tr("退出"));

    /* 创建菜单控件.*/
    QMenu *menu = menuBar()->addMenu(tr("&Menu"));
    menu->addAction(newAction);
    menu->addAction(openAction);
    menu->addAction(saveAction);
    menu->addAction(closeAction);
    menu->addAction(aboutAction);
    menu->addAction(quitAction);

    /* 创建工具栏控件.*/
    QToolBar *tool = addToolBar(tr("&Menu"));
    tool->addAction(newAction);
    tool->addAction(openAction);
    tool->addAction(saveAction);

    /* 创建按钮、输入对象.*/
    name = new QLineEdit(this);
    type = new QComboBox(this);
    summary = new QLineEdit(this);
    stock = new QSpinBox(this);
    stock->setMaximum(100000000);
    addInventoryAction = new QPushButton("产品入库", this);
    addTransportAction = new QPushButton("产品运输", this);
    addDeliveredAction = new QPushButton("产品抵达", this);
    addInventoryAction->setStatusTip(tr("产品入库..."));
    addTransportAction->setStatusTip(tr("产品运输..."));
    addDeliveredAction->setStatusTip(tr("产品抵达..."));

    /* 创建表格对象.*/
    inventoryTable = new QTableWidget(this);
    inventoryTable->setFixedSize(743, 425);
    inventoryTable->setColumnCount(4);
    inventoryTable->setEditTriggers(QAbstractItemView::NoEditTriggers);
    inventoryTable->setSelectionBehavior(QAbstractItemView::SelectRows);
    inventoryTable->setSelectionMode(QAbstractItemView::SingleSelection);
    inventoryTable->setColumnWidth(0, 100);
    inventoryTable->setColumnWidth(1, 150);
    inventoryTable->setColumnWidth(2, 100);
    inventoryTable->setColumnWidth(3, 350);
    inventoryTable->setHorizontalHeaderItem(0, new QTableWidgetItem(tr("商品类别")));
    inventoryTable->setHorizontalHeaderItem(1, new QTableWidgetItem(tr("商品名称")));
    inventoryTable->setHorizontalHeaderItem(2, new QTableWidgetItem(tr("商品数量")));
    inventoryTable->setHorizontalHeaderItem(3, new QTableWidgetItem(tr("商品摘要")));

    transportTable = new QTableWidget(this);
    transportTable->setFixedSize(743, 425);
    transportTable->setColumnCount(4);
    transportTable->setEditTriggers(QAbstractItemView::NoEditTriggers);
    transportTable->setSelectionBehavior(QAbstractItemView::SelectRows);
    transportTable->setSelectionMode(QAbstractItemView::SingleSelection);
    transportTable->setColumnWidth(0, 100);
    transportTable->setColumnWidth(1, 150);
    transportTable->setColumnWidth(2, 100);
    transportTable->setColumnWidth(3, 350);
    transportTable->setHorizontalHeaderItem(0, new QTableWidgetItem(tr("商品类别")));
    transportTable->setHorizontalHeaderItem(1, new QTableWidgetItem(tr("商品名称")));
    transportTable->setHorizontalHeaderItem(2, new QTableWidgetItem(tr("商品数量")));
    transportTable->setHorizontalHeaderItem(3, new QTableWidgetItem(tr("商品摘要")));

    deliveredTable = new QTableWidget(this);
    deliveredTable->setFixedSize(743, 425);
    deliveredTable->setColumnCount(4);
    deliveredTable->setEditTriggers(QAbstractItemView::NoEditTriggers);
    deliveredTable->setSelectionBehavior(QAbstractItemView::SelectRows);
    deliveredTable->setSelectionMode(QAbstractItemView::SingleSelection);
    deliveredTable->setColumnWidth(0, 100);
    deliveredTable->setColumnWidth(1, 150);
    deliveredTable->setColumnWidth(2, 100);
    deliveredTable->setColumnWidth(3, 350);
    deliveredTable->setHorizontalHeaderItem(0, new QTableWidgetItem(tr("商品类别")));
    deliveredTable->setHorizontalHeaderItem(1, new QTableWidgetItem(tr("商品名称")));
    deliveredTable->setHorizontalHeaderItem(2, new QTableWidgetItem(tr("商品数量")));
    deliveredTable->setHorizontalHeaderItem(3, new QTableWidgetItem(tr("商品摘要")));

    QTabWidget *tabWidget = new QTabWidget(this);
    tabWidget->setFixedSize(745, 461);
    tabWidget->addTab(inventoryTable, "产品库存信息");
    tabWidget->addTab(transportTable, "产品运输信息");
    tabWidget->addTab(deliveredTable, "产品交付信息");

    /* 布局管理.*/
    QWidget *centerWindow = new QWidget;
    this->setCentralWidget(centerWindow);

    QFormLayout *formLayout = new QFormLayout(this);
    formLayout->addRow("名称:", name);
    formLayout->addRow("类型:", type);
    formLayout->addRow("库存:", stock);
    formLayout->addRow("摘要:", summary);
    formLayout->addRow("入库:", addInventoryAction);
    formLayout->setSizeConstraint(QLayout::SetFixedSize);

    QVBoxLayout *vboxLayout = new QVBoxLayout(this);
    vboxLayout->addWidget(addTransportAction, 0, 0);
    vboxLayout->addWidget(addDeliveredAction, 1, 0);
    vboxLayout->setSizeConstraint(QLayout::SetFixedSize);

    QGridLayout *mainLayout = new QGridLayout(this);
    mainLayout->addWidget(tabWidget, 0, 0, 5, 1);
    mainLayout->addLayout(formLayout, 1, 1);
    mainLayout->addLayout(vboxLayout, 3, 1);
    mainLayout->setSizeConstraint(QLayout::SetFixedSize);
    centerWindow->setLayout(mainLayout);

    /* 建立动作信号与实现方法槽的连接.*/
    connect(newAction, &QAction::triggered, this, &UserInterface::newFile);
    connect(openAction, &QAction::triggered, this, &UserInterface::openFile);
    connect(saveAction, &QAction::triggered, this, &UserInterface::saveFile);
    connect(closeAction, &QAction::triggered, this, &UserInterface::closeFile);
    connect(aboutAction, &QAction::triggered, this, &UserInterface::aboutEditor);
    connect(quitAction, &QAction::triggered, this, &UserInterface::close);
    connect(addInventoryAction, &QPushButton::clicked, this, &UserInterface::addInventory);
    connect(addTransportAction, &QPushButton::clicked, this, &UserInterface::addTransport);
    connect(addDeliveredAction, &QPushButton::clicked, this, &UserInterface::addDelivered);

    statusBar();
}

UserInterface::~UserInterface()
{

}

void UserInterface::newFile()
{
    dialog = new NewFileDialog(this);
    dialog->setAttribute(Qt::WA_DeleteOnClose);
    dialog->setWindowTitle(tr("New File"));
    connect(dialog->pushButtonOK, &QPushButton::clicked, this, &UserInterface::getNewFileInfo);
    connect(dialog->pushButtonCancel, &QPushButton::clicked, dialog, &NewFileDialog::close);
    dialog->exec();
}

void UserInterface::getNewFileInfo()
{
    if (!dialog->line1->text().isEmpty() &&\
        !dialog->line2->text().isEmpty() &&\
        !dialog->line3->text().isEmpty() &&\
        !dialog->line4->text().isEmpty() &&\
        !dialog->line5->text().isEmpty())
    {
        invenA.inveType = dialog->line1->text();
        invenB.inveType = dialog->line2->text();
        invenC.inveType = dialog->line3->text();
        invenD.inveType = dialog->line4->text();
        invenE.inveType = dialog->line5->text();
        type->addItem(invenA.inveType);
        type->addItem(invenB.inveType);
        type->addItem(invenC.inveType);
        type->addItem(invenD.inveType);
        type->addItem(invenE.inveType);
        dialog->close();
    }
    else
    {
        QMessageBox warning(dialog);
        warning.setText(tr("警告：检测到无效输入！"));
        warning.setInformativeText(tr("请重新输入！"));
        warning.setStandardButtons(QMessageBox::Ok);
        warning.setDefaultButton(QMessageBox::Ok);
        warning.exec();

    }
}

void UserInterface::openFile()
{
    int readFlag = 0; // 用于标记读入的数据所属的数据结构
    QString path = QFileDialog::getOpenFileName(this, tr("Open File"), ".", tr("Scoms File(*.scm)"));
    if (!path.isEmpty())
    {
        QFile file(path);
        if (!file.open(QIODevice::ReadOnly))
        {
            QMessageBox::warning(this, tr("Read Only File"), tr("Cannot open file:\n%1").arg(path));
            return;
        }
        QTextStream readInfo(&file);

        QString readin = readInfo.readLine();
        invenA.inveType = readin;
        readin = readInfo.readLine();
        invenB.inveType = readin;
        readin = readInfo.readLine();
        invenC.inveType = readin;
        readin = readInfo.readLine();
        invenD.inveType = readin;
        readin = readInfo.readLine();
        invenE.inveType = readin;

        type->addItem(invenA.inveType);
        type->addItem(invenB.inveType);
        type->addItem(invenC.inveType);
        type->addItem(invenD.inveType);
        type->addItem(invenE.inveType);

        QString tmpname, tmptype, tmpsummary;
        int tmpstock;
        readin = readInfo.readLine();

        for (int i = 0, j = 0, k = 0; !readin.isNull();)
        {
            if (readin == "[Inventory]" || readin == "[Transport]" || readin == "[Delivered]")
            {
                ++readFlag;
                readin = readInfo.readLine();
            }
            tmpname = readin;
            readin = readInfo.readLine();
            tmptype = readin;
            readin = readInfo.readLine();
            tmpsummary = readin;
            readin = readInfo.readLine();
            tmpstock = readin.toInt();
            if (readFlag == 1)
            {
                if (invenA.inveType == tmptype)
                    invenA.appendInfo(tmpname, tmptype, tmpsummary, tmpstock);
                else if(invenB.inveType == tmptype)
                    invenB.appendInfo(tmpname, tmptype, tmpsummary, tmpstock);
                else if(invenC.inveType == tmptype)
                    invenC.appendInfo(tmpname, tmptype, tmpsummary, tmpstock);
                else if(invenD.inveType == tmptype)
                    invenD.appendInfo(tmpname, tmptype, tmpsummary, tmpstock);
                else
                    invenE.appendInfo(tmpname, tmptype, tmpsummary, tmpstock);
                inventoryTable->insertRow(inventoryTable->rowCount());
                inventoryTable->setItem(i, 0, new QTableWidgetItem(tmptype));
                inventoryTable->setItem(i, 1, new QTableWidgetItem(tmpname));
                inventoryTable->setItem(i, 2, new QTableWidgetItem(QString("%1").arg(tmpstock)));
                inventoryTable->setItem(i, 3, new QTableWidgetItem(tmpsummary));
                ++i;

            }
            else if (readFlag == 2)
            {
                trans.enqueue(tmpname, tmptype, tmpsummary, tmpstock);
                transportTable->insertRow(transportTable->rowCount());
                transportTable->setItem(j, 0, new QTableWidgetItem(tmptype));
                transportTable->setItem(j, 1, new QTableWidgetItem(tmpname));
                transportTable->setItem(j, 2, new QTableWidgetItem(QString("%1").arg(tmpstock)));
                transportTable->setItem(j, 3, new QTableWidgetItem(tmpsummary));
                ++j;
            }
            else
            {
                deliv.appendInfo(tmpname, tmptype, tmpsummary, tmpstock);
                deliveredTable->insertRow(deliveredTable->rowCount());
                deliveredTable->setItem(k, 0, new QTableWidgetItem(tmptype));
                deliveredTable->setItem(k, 1, new QTableWidgetItem(tmpname));
                deliveredTable->setItem(k, 2, new QTableWidgetItem(QString("%1").arg(tmpstock)));
                deliveredTable->setItem(k, 3, new QTableWidgetItem(tmpsummary));
                ++k;
            }
            readin = readInfo.readLine();
        }
        file.close();
    }
}

void UserInterface::saveFile()
{
    QString path = QFileDialog::getSaveFileName(this, tr("Save File"), "./untitled.scm", tr("Scoms File(*.scm)"));
    if (!path.isEmpty())
    {
        QFile file(path);
        file.open(QIODevice::WriteOnly);
        QTextStream writeout(&file);
        writeout << invenA.inveType;
        writeout << '\n' << invenB.inveType;
        writeout << '\n' << invenC.inveType;
        writeout << '\n' << invenD.inveType;
        writeout << '\n' << invenE.inveType;
        writeout << '\n' << "[Inventory]";
        invenA.toStart();
        for (int i = 0; i < invenA.length(); ++i)
        {
            writeout << '\n' << invenA.getInfo()->name;
            writeout << '\n' << invenA.getInfo()->type;
            writeout << '\n' << invenA.getInfo()->summary;
            writeout << '\n' << invenA.getInfo()->stock;
            invenA.toNext();
        }

        invenB.toStart();
        for (int i = 0; i < invenB.length(); ++i)
        {
            writeout << '\n' << invenB.getInfo()->name;
            writeout << '\n' << invenB.getInfo()->type;
            writeout << '\n' << invenB.getInfo()->summary;
            writeout << '\n' << invenB.getInfo()->stock;
            invenB.toNext();
        }

        invenC.toStart();
        for (int i = 0; i < invenC.length(); ++i)
        {
            writeout << '\n' << invenC.getInfo()->name;
            writeout << '\n' << invenC.getInfo()->type;
            writeout << '\n' << invenC.getInfo()->summary;
            writeout << '\n' << invenC.getInfo()->stock;
            invenC.toNext();
        }

        invenD.toStart();
        for (int i = 0; i < invenD.length(); ++i)
        {
            writeout << '\n' << invenD.getInfo()->name;
            writeout << '\n' << invenD.getInfo()->type;
            writeout << '\n' << invenD.getInfo()->summary;
            writeout << '\n' << invenD.getInfo()->stock;
            invenD.toNext();
        }

        invenE.toStart();
        for (int i = 0; i < invenE.length(); ++i)
        {
            writeout << '\n' << invenE.getInfo()->name;
            writeout << '\n' << invenE.getInfo()->type;
            writeout << '\n' << invenE.getInfo()->summary;
            writeout << '\n' << invenE.getInfo()->stock;
            invenE.toNext();
        }

        writeout << '\n' << "[Transport]";
        for (int i = trans.length(); i > 0; --i)
        {
            writeout << '\n' << trans.frontValue()->name;
            writeout << '\n' << trans.frontValue()->type;
            writeout << '\n' << trans.frontValue()->summary;
            writeout << '\n' << trans.frontValue()->stock;
            trans.dequeue();
        }

        writeout << '\n' << "[Delivered]";
        deliv.toStart();
        for (int i = 0; i < deliv.length(); ++i)
        {
            writeout << '\n' << deliv.getInfo()->name;
            writeout << '\n' << deliv.getInfo()->type;
            writeout << '\n' << deliv.getInfo()->summary;
            writeout << '\n' << deliv.getInfo()->stock;
            deliv.toNext();
        }
        file.close();
    }
}

void UserInterface::closeFile()
{

    invenA.clearInfo();
    invenB.clearInfo();
    invenC.clearInfo();
    invenD.clearInfo();
    invenE.clearInfo();
    trans.clearInfo();
    deliv.clearInfo();
    for (int i = inventoryTable->rowCount(); i >= 0; --i)
        inventoryTable->removeRow(i);
    for (int j = transportTable->rowCount(); j >= 0; --j)
        transportTable->removeRow(j);
    for (int k = deliveredTable->rowCount(); k >= 0; --k)
        deliveredTable->removeRow(k);
    name->setText("");
    summary->setText("");
    type->clear();
    stock->setValue(0);

}

void UserInterface::aboutEditor()
{
    QMessageBox editorInfo(this);
    editorInfo.setText(tr("简单商品订购管理系统 Version 1.0"));
    editorInfo.setInformativeText(tr("20135134 熊熊\n20135103 冉谨铭\n20135145 李馨\n20135138 梁婷芝\n20135131 张博俊"));
    editorInfo.setStandardButtons(QMessageBox::Close);
    editorInfo.setDefaultButton(QMessageBox::Close);
    editorInfo.exec();

}

void UserInterface::addInventory()
{
    if (!name->text().isEmpty() && \
        type->count() > 0)
    {
        inventoryTable->insertRow(inventoryTable->rowCount());
        inventoryTable->setItem(inventoryTable->rowCount()-1, 0, new QTableWidgetItem(type->currentText()));
        inventoryTable->setItem(inventoryTable->rowCount()-1, 1, new QTableWidgetItem(name->text()));
        inventoryTable->setItem(inventoryTable->rowCount()-1, 2, new QTableWidgetItem(QString("%1").arg(stock->value())));
        inventoryTable->setItem(inventoryTable->rowCount()-1, 3, new QTableWidgetItem(summary->text()));
        if (type->currentText() == invenA.inveType)
        {
            invenA.appendInfo(name->text(), type->currentText(), summary->text(), stock->value());
        }
        else if (type->currentText() == invenB.inveType)
        {
            invenB.appendInfo(name->text(), type->currentText(), summary->text(), stock->value());
        }
        else if (type->currentText() == invenC.inveType)
        {
            invenC.appendInfo(name->text(), type->currentText(), summary->text(), stock->value());
        }
        else if (type->currentText() == invenD.inveType)
        {
            invenD.appendInfo(name->text(), type->currentText(), summary->text(), stock->value());
        }
        else if (type->currentText() == invenE.inveType)
        {
            invenE.appendInfo(name->text(), type->currentText(), summary->text(), stock->value());
        }
        name->setText("");
        summary->setText("");
        stock->setValue(0);
    }
    else if (type->count() == 0)
    {
        QMessageBox warning(this);
        warning.setText(tr("警告：商品类型未初始化！"));
        warning.setInformativeText(tr("请新建商品类型！"));
        warning.setStandardButtons(QMessageBox::Ok);
        warning.setDefaultButton(QMessageBox::Ok);
        warning.exec();
        newFile();
    }
    else
    {
        QMessageBox warning(this);
        warning.setText(tr("警告：检测到无效输入！"));
        warning.setInformativeText(tr("请重新输入！"));
        warning.setStandardButtons(QMessageBox::Ok);
        warning.setDefaultButton(QMessageBox::Ok);
        warning.exec();
    }
}

void UserInterface::addTransport()
{
    int cnt = inventoryTable->currentRow();
    if (cnt >= 0)
    {
        if (inventoryTable->item(cnt, 0)->text() == invenA.inveType)
        {
            invenA.toStart();
            for (int i = 0; i < invenA.length(); ++i)
            {
                if (inventoryTable->item(cnt, 1)->text() == invenA.getInfo()->name)
                {
                    invenA.toPos(i);
                    invenA.removeInfo();
                    break;
                }
                invenA.toNext();
            }
        }
        else if (inventoryTable->item(cnt, 0)->text() == invenB.inveType)
        {
            invenB.toStart();
            for (int i = 0; i < invenB.length(); ++i)
            {
                if (inventoryTable->item(cnt, 1)->text() == invenB.getInfo()->name)
                {
                    invenB.toPos(i);
                    invenB.removeInfo();
                    break;
                }
                invenB.toNext();
            }

        }
        else if (inventoryTable->item(cnt, 0)->text() == invenC.inveType)
        {
            invenC.toStart();
            for (int i = 0; i < invenC.length(); ++i)
            {
                if (inventoryTable->item(cnt, 1)->text() == invenC.getInfo()->name)
                {
                    invenC.toPos(i);
                    invenC.removeInfo();
                    break;
                }
                invenC.toNext();
            }
        }
        else if (inventoryTable->item(cnt, 0)->text() == invenD.inveType)
        {
            invenD.toStart();
            for (int i = 0; i < invenD.length(); ++i)
            {
                if (inventoryTable->item(cnt, 1)->text() == invenD.getInfo()->name)
                {
                    invenD.toPos(i);
                    invenD.removeInfo();
                    break;
                }
                invenD.toNext();
            }
        }
        else if (inventoryTable->item(cnt, 0)->text() == invenE.inveType)
        {
            invenE.toStart();
            for (int i = 0; i < invenE.length(); ++i)
            {
                if (inventoryTable->item(cnt, 1)->text() == invenE.getInfo()->name)
                {
                    invenE.toPos(i);
                    invenE.removeInfo();
                    break;
                }
                invenE.toNext();
            }
        }
        transportTable->insertRow(transportTable->rowCount());
        transportTable->setItem(transportTable->rowCount()-1, 0, new QTableWidgetItem(inventoryTable->item(cnt,0)->text()));
        transportTable->setItem(transportTable->rowCount()-1, 1, new QTableWidgetItem(inventoryTable->item(cnt,1)->text()));
        transportTable->setItem(transportTable->rowCount()-1, 2, new QTableWidgetItem(inventoryTable->item(cnt,2)->text()));
        transportTable->setItem(transportTable->rowCount()-1, 3, new QTableWidgetItem(inventoryTable->item(cnt,3)->text()));
        trans.enqueue(inventoryTable->item(cnt,1)->text(), inventoryTable->item(cnt,0)->text(), inventoryTable->item(cnt,3)->text(), inventoryTable->item(cnt,2)->text().toInt());
        inventoryTable->removeRow(cnt);
    }
    else
    {
        QMessageBox warning(this);
        warning.setText(tr("警告：未选择数据行！"));
        warning.setInformativeText(tr("请选择待执行数据行后再操作!"));
        warning.setStandardButtons(QMessageBox::Ok);
        warning.setDefaultButton(QMessageBox::Ok);
        warning.exec();
    }

}

void UserInterface::addDelivered()
{
    if (transportTable->rowCount() != 0)
    {
        transportTable->removeRow(0);
        deliveredTable->insertRow(deliveredTable->rowCount());
        deliveredTable->setItem(deliveredTable->rowCount()-1, 0, new QTableWidgetItem(trans.frontValue()->type));
        deliveredTable->setItem(deliveredTable->rowCount()-1, 1, new QTableWidgetItem(trans.frontValue()->name));
        deliveredTable->setItem(deliveredTable->rowCount()-1, 2, new QTableWidgetItem(QString("%1").arg(trans.frontValue()->stock)));
        deliveredTable->setItem(deliveredTable->rowCount()-1, 3, new QTableWidgetItem(trans.frontValue()->summary));
        deliv.appendInfo(trans.frontValue()->name, trans.frontValue()->type, trans.frontValue()->summary, trans.frontValue()->stock);
        trans.dequeue();
    }
    else
    {
        QMessageBox warning(this);
        warning.setText(tr("警告：队列已全部出队！"));
        warning.setInformativeText(tr("请执行其他操作!"));
        warning.setStandardButtons(QMessageBox::Ok);
        warning.setDefaultButton(QMessageBox::Ok);
        warning.exec();
    }

}
