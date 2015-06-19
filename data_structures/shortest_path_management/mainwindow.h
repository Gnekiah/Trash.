#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include "graph/path.h"
#include "graph/adjacency.h"

#include <QMainWindow>
#include <QPushButton>
#include <QLineEdit>
#include <QTableWidget>
#include <QLinkedList>

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = 0);
    ~MainWindow();

private:
    QTableWidget    *nameTable;     // 显示城市名称表
    QTableWidget    *pathTable;     // 显示城市之间路径表
    QTableWidget    *resultTable;   //
    QTabWidget      *tabWidget;     //
    QPushButton     *addInfo;       //
    QPushButton     *removeSelected;// 删除选中
    QPushButton     *removeAll;     // 删除全部
    QPushButton     *sourcePath;    //
    QPushButton     *allPath;       //

    QLinkedList<QString>    nameList;
    QLinkedList<Path>       pathList;

public:
    void addInfoItem(void);
    void removeSelectedItem(void);
    void removeAllItem(void);

    void viewSourcePath(void);
    void viewAllPath(void);

    void open(void);
    void save(void);

    void buildGraph(Adjacency& adj);
};

#endif // MAINWINDOW_H
