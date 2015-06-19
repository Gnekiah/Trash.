#include "mainwindow.h"

#include <QString>
#include <QTabWidget>
#include <QFile>
#include <QFileDialog>
#include <QTextStream>
#include <QMessageBox>
#include <QTableWidgetItem>
#include <QDebug>

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
{
    setWindowTitle(tr("Shortest Path Management v0.1"));
    setFixedSize(1000, 600);
    tabWidget = new QTabWidget(this);

    nameTable = new QTableWidget(1, 1, tabWidget);
    nameTable->setSelectionBehavior(QAbstractItemView::SelectRows);
    nameTable->setSelectionMode(QAbstractItemView::SingleSelection);
    nameTable->setColumnWidth(0, 250);
    nameTable->setHorizontalHeaderItem(0, new QTableWidgetItem(tr("城市列表")));

    pathTable = new QTableWidget(1, 3, tabWidget);
    pathTable->setSelectionBehavior(QAbstractItemView::SelectRows);
    pathTable->setSelectionMode(QAbstractItemView::SingleSelection);
    pathTable->setColumnWidth(2, 50);
    pathTable->setHorizontalHeaderItem(0, new QTableWidgetItem(tr("出发城市")));
    pathTable->setHorizontalHeaderItem(1, new QTableWidgetItem(tr("到达城市")));
    pathTable->setHorizontalHeaderItem(2, new QTableWidgetItem(tr("间距")));

    resultTable = new QTableWidget(0, 4, this);
    resultTable->setEditTriggers(QAbstractItemView::NoEditTriggers);
    resultTable->setSelectionBehavior(QAbstractItemView::SelectRows);
    resultTable->setSelectionMode(QAbstractItemView::SingleSelection);
    resultTable->setColumnWidth(0, 80);
    resultTable->setColumnWidth(1, 80);
    resultTable->setColumnWidth(2, 50);
    resultTable->setColumnWidth(3, 430);
    resultTable->setHorizontalHeaderItem(0, new QTableWidgetItem(tr("出发城市")));
    resultTable->setHorizontalHeaderItem(1, new QTableWidgetItem(tr("到达城市")));
    resultTable->setHorizontalHeaderItem(2, new QTableWidgetItem(tr("间距")));
    resultTable->setHorizontalHeaderItem(3, new QTableWidgetItem(tr("路径")));

    addInfo = new QPushButton(tr("添加条目"), this);
    removeSelected = new QPushButton(tr("删除选中条目"), this);
    removeAll = new QPushButton(tr("删除全部"), this);
    sourcePath = new QPushButton(tr("源点:未指定源点"), this);
    allPath = new QPushButton(tr("显示所有最短路径"), this);

    tabWidget->addTab(nameTable, tr("城市名称"));
    tabWidget->addTab(pathTable, tr("城市间距"));

    resultTable->setGeometry(5, 5, 690, 590);
    tabWidget->setGeometry(695, 0, 300, 415);
    addInfo->setGeometry(700, 420, 95, 35);
    removeSelected->setGeometry(800, 420, 95, 35);
    removeAll->setGeometry(900, 420, 95, 35);
    sourcePath->setGeometry(700, 470, 150, 60);
    allPath->setGeometry(700, 535, 150, 60);
    QPushButton *open = new QPushButton(tr("打开"), this);
    open->setGeometry(935, 495, 60, 30);
    QPushButton *save = new QPushButton(tr("保存"), this);
    save->setGeometry(935, 530, 60, 30);
    QPushButton *exit = new QPushButton(tr("退出"), this);
    exit->setGeometry(935, 565, 60, 30);

    connect(addInfo, &QPushButton::clicked, this, &MainWindow::addInfoItem);
    connect(removeSelected, &QPushButton::clicked, this, &MainWindow::removeSelectedItem);
    connect(removeAll, &QPushButton::clicked, this, &MainWindow::removeAllItem);
    connect(sourcePath, &QPushButton::clicked, this, &MainWindow::viewSourcePath);
    connect(allPath, &QPushButton::clicked, this, &MainWindow::viewAllPath);
    connect(open, &QPushButton::clicked, this, &MainWindow::open);
    connect(save, &QPushButton::clicked, this, &MainWindow::save);
    connect(exit, &QPushButton::clicked, this, &MainWindow::close);
}

MainWindow::~MainWindow()
{

}

void MainWindow::addInfoItem()
{
    if (tabWidget->currentWidget() == nameTable) {
        QTableWidgetItem *tmp = nameTable->item(nameTable->rowCount()-1, 0);
        if (tmp != NULL &&\
            tmp->text() != tr("")) {
            nameList.append(tmp->text());
            nameTable->insertRow(nameTable->rowCount());
        }
    }
    else {
        QTableWidgetItem *tmpNameOne = pathTable->item(pathTable->rowCount()-1, 0);
        QTableWidgetItem *tmpNameTwo = pathTable->item(pathTable->rowCount()-1, 1);
        QTableWidgetItem *tmpDistance = pathTable->item(pathTable->rowCount()-1, 2);
        if (tmpNameOne != NULL &&\
            tmpNameTwo != NULL &&\
            tmpDistance != NULL &&\
            tmpNameOne->text() != tr("") &&\
            tmpNameTwo->text() != tr("") &&\
            tmpDistance->text() != tr("")) {
            Path tmpPath;
            tmpPath.nameOne = tmpNameOne->text();
            tmpPath.nameTwo = tmpNameTwo->text();
            tmpPath.distance = tmpDistance->text().toInt();
            pathList.append(tmpPath);
            pathTable->insertRow(pathTable->rowCount());
        }
    }
}

void MainWindow::removeSelectedItem()
{
    if (tabWidget->currentWidget() == nameTable) {
        int cnt = nameTable->currentRow();
        if (cnt < 0) return;
        nameTable->removeRow(cnt);
        QLinkedList<QString>::iterator iter = nameList.begin();
        for (int i = 0; iter != nameList.end(); ++i)
            { if (i == cnt) break; ++iter; }
        nameList.erase(iter);

    }
    else {
        int cnt = pathTable->currentRow();
        if (cnt < 0) return;
        pathTable->removeRow(cnt);
        QLinkedList<Path>::iterator iter = pathList.begin();
        for (int i = 0; iter != pathList.end(); ++i)
            { if (i == cnt) break; ++iter;}
        pathList.erase(iter);
    }
}

void MainWindow::removeAllItem()
{
    nameList.clear();
    pathList.clear();
    for (int i = nameTable->rowCount()-2; i >= 0; --i)
        nameTable->removeRow(i);
    for (int i = pathTable->rowCount()-2; i >= 0; --i)
        pathTable->removeRow(i);
}

void MainWindow::viewSourcePath()
{
    QTableWidgetItem *tmp = nameTable->currentItem();
    if (tabWidget->currentWidget() != nameTable ||\
        nameTable->currentRow() < 0 ||\
        tmp == NULL || \
        tmp->text() == tr("")) return;

    for (int i = resultTable->rowCount() - 1; i >=0; --i)
        resultTable->removeRow(i);
    QString strTmp(tr("源点: "));
    strTmp.append(tmp->text());
    sourcePath->setText(strTmp);

    Adjacency graph(nameList.size());
    buildGraph(graph);
    graph.dijkstra(nameTable->currentRow());
    for (int i = 0; i < graph.getVertexSize(); ++i) {
        QVector<int> path = graph.getPath(i);
        resultTable->insertRow(resultTable->rowCount());
        resultTable->setItem(i, 0, new QTableWidgetItem(tmp->text()));
        resultTable->setItem(i, 1, new QTableWidgetItem(graph.getName(i)));
        resultTable->setItem(i, 2, new QTableWidgetItem(QString::number(graph.getLength(i))));
        QString pathTmp;
        pathTmp.append(tmp->text());
        for (int j = 1; j < path.length(); ++j) {
            pathTmp.append(tr("->"));
            pathTmp.append(graph.getName(path.first()));
            path.removeFirst();
        }
        resultTable->setItem(i, 3, new QTableWidgetItem(pathTmp));
    }
}

void MainWindow::viewAllPath()
{
    sourcePath->setText(tr("源点:未指定源点"));
    for (int i = resultTable->rowCount() - 1; i >=0; --i)
        resultTable->removeRow(i);
    Adjacency graph(nameList.size());
    buildGraph(graph);

    graph.floyd();
    for (int j = 0; j < graph.getVertexSize(); ++j) {
    for (int i = 0; i < graph.getVertexSize(); ++i) {
        resultTable->insertRow(resultTable->rowCount());
        resultTable->setItem(i, 0, new QTableWidgetItem(graph.getName(j)));
        resultTable->setItem(i, 1, new QTableWidgetItem(graph.getName(i)));
        resultTable->setItem(i, 2, new QTableWidgetItem(QString::number(graph.getLength(i))));
    }
    }
}

void MainWindow::open()
{
    QString openPath = QFileDialog::getOpenFileName(this, tr("Open File"), ".", tr("SPM File(*.spm)"));
    QFile file(openPath);
    if (!file.open(QIODevice::ReadOnly))
        return;
    QTextStream in(&file);
    int flag = 0;
    QString tmp;
    Path tmpPath;
    tmp = in.readLine();
    tmp = in.readLine();
    int i = 0;

    while(!in.atEnd()) {
        if (tmp == "[pathlist]") { ++flag; i = 0; }
        if (flag == 0) {
            nameList.append(tmp);
            nameTable->insertRow(nameTable->rowCount());
            nameTable->setItem(i, 0, new QTableWidgetItem(tmp));
            tmp = in.readLine();
        }
        else {
            in >> tmpPath.nameOne >> tmpPath.nameTwo >> tmp;
            tmpPath.distance = tmp.toInt();
            pathList.append(tmpPath);
            pathTable->insertRow(pathTable->rowCount());
            pathTable->setItem(i, 0, new QTableWidgetItem(tmpPath.nameOne));
            pathTable->setItem(i, 1, new QTableWidgetItem(tmpPath.nameTwo));
            pathTable->setItem(i, 2, new QTableWidgetItem(tmp));
        }
        ++i;
    }
    pathList.removeLast();
    pathTable->removeRow(pathTable->rowCount()-1);
    file.close();
}

void MainWindow::save()
{
    QString savePath = QFileDialog::getSaveFileName(this, tr("Save File"), "./untitled.spm", tr("SPM File(*.spm)"));
    QFile file(savePath);
    if (!file.open(QIODevice::WriteOnly))
        return;
    QTextStream out(&file);

    out << "[namelist]";
    for (QLinkedList<QString>::iterator iter = nameList.begin(); iter != nameList.end(); ++iter)
        out << '\n' << *iter;

    out << '\n' << "[pathlist]";
    Path tmp;
    for (QLinkedList<Path>::iterator iter = pathList.begin(); iter != pathList.end(); ++iter) {
        tmp = *iter;
        out << '\n' << tmp.nameOne << ' ' << tmp.nameTwo << ' ' << tmp.distance;
    }
    file.close();
}

void MainWindow::buildGraph(Adjacency &adj)
{
    QLinkedList<QString>::iterator iter = nameList.begin();
    for (int i = 0; iter != nameList.end(); ++iter) {
        adj.setName(*iter, i);
        ++i;
    }
    QLinkedList<Path>::iterator iter2 = pathList.begin();
    Path tmp;
    int v1, v2;
    for (int i = 0; iter2 != pathList.end(); ++iter2) {
        tmp = *iter2;
        iter = nameList.begin();
        v1 = 0;
        for (; tmp.nameOne != *iter; ++iter) ++v1;
        iter = nameList.begin();
        v2 = 0;
        for (; tmp.nameTwo != *iter; ++iter) ++v2;
        adj.setEdge(v1, v2, tmp.distance);
        ++i;
    }
}
