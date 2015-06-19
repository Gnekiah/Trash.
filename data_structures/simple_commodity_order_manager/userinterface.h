#ifndef USERINTERFACE_H
#define USERINTERFACE_H

#include "data_structure/inventory.h"
#include "data_structure/transport.h"
#include "data_structure/delivered.h"
#include "define_dialog/newfiledialog.h"

#include <QMainWindow>
#include <QTableWidget>
#include <QAction>
#include <QPushButton>
#include <QSpinBox>
#include <QLineEdit>
#include <QComboBox>

class UserInterface : public QMainWindow
{
    Q_OBJECT

public:
    UserInterface(QWidget *parent = 0);
    ~UserInterface();

private:
    Inventory invenA, invenB, invenC, invenD, invenE;
    Transport trans;
    Delivered deliv;

    /* 定义表格对象.*/
    QTableWidget *inventoryTable;
    QTableWidget *transportTable;
    QTableWidget *deliveredTable;

    /* 定义动作对象.*/
    QAction *newAction;             // 新建
    QAction *openAction;            // 打开
    QAction *saveAction;            // 保存
    QAction *closeAction;           // 关闭
    QAction *aboutAction;           // 关于
    QAction *quitAction;            // 退出

    QLineEdit *name;                // 商品名
    QComboBox *type;                // 商品类型
    QLineEdit *summary;             // 商品摘要
    QSpinBox  *stock;               // 库存

    QPushButton *addInventoryAction;// 添加条目
    QPushButton *addTransportAction;// 加入运输队列
    QPushButton *addDeliveredAction;// 交付

    NewFileDialog *dialog;

public:
    /* 声明操作的方法.*/
    void newFile();                 // 新建文件
    void getNewFileInfo();
    void openFile();                // 打开文件
    void saveFile();                // 保存文件
    void closeFile();               // 关闭文件
    void aboutEditor();             // 关于

    void addInventory();           // 添加条目
    void addTransport();           // 加入运输队列
    void addDelivered();           // 交付

};

#endif 
