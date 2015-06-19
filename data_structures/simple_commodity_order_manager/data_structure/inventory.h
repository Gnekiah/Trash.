/*
 * 链表类
 * 作用：保存商品信息，每个节点采用Product类
 * 警告：每次创建一个新的商品链表或清空链表以后，必须初始化链表头
 * 初始化链表头函数的声明为：void initHead(const string& name, const string& type, const int& stock);
 * 创建时间：2015-04-21  17:32
 * 作者：梁婷芝
 *
 */

#ifndef INVENTORY_H
#define INVENTORY_H

#include <QString>
#include "product.h"

class Inventory
{
private:
    int      cnt;           // 计数器
    Product* head;          // 指向链表头
    Product* tail;          // 指向链表尾
    Product* curr;          // 指向当前位置

    /* 初始化函数.*/
    void init()
    {
        curr = tail = head = new Product;
        cnt = 0;
    }
    /* 删除链表.*/
    void removeAll()
    {
        while(head != NULL)
        {
            curr = head;
            head = head->next;
            delete curr;
        }
    }
        
public:
    Inventory() { init(); }
    ~Inventory() { removeAll(); }

    /* 商品类型.*/
    QString  inveType;
    /* 清空链表的节点.*/
    void clearInfo();
    /* 插入一个节点.*/
    void insertInfo(const QString& name, const QString& type, const QString& summary, const int& stock);
    /* 在链表尾部扩展一个节点.*/
    void appendInfo(const QString& name, const QString& type, const QString& summary, const int& stock);
    /* 删除当前指向的节点.*/
    void removeInfo();
    /* 当前位置指向链表头.*/
    void toStart();
    /* 当前位置指向表尾.*/
    void toEnd();
    /* 返回到上一个节点.*/
    void toPrev();
    /* 到下一个节点.*/
    void toNext();
    /* 到某个指定位置的节点.*/
    void toPos(int& pos);
    /* 返回当前位置.*/
    int currPos() const;
    /* 返回链表长度.*/
    int length() const;
    /* 获取节点存储的商品信息.*/
    const Product* getInfo() const;
};

#endif
