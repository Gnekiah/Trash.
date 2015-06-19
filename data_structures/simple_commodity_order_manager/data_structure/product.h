/*
 * 商品信息类
 * 作用：作为链表、队列等数据结构的节点
 * 创建时间：2015-04-21 16:53
 * 作者：李馨 
 *       
 */

#ifndef PRODUCT_H
#define PRODUCT_H

#include <QString>

class Product
{
public:
    QString   name;              // 商品名
    QString   type;              // 商品类型
    QString   summary;           // 商品摘要
    int       stock;             // 库存
    Product   *next;              // 指向下一个节点的指针

    /* 构造函数.*/
    Product(const QString &name, const QString &type, const QString &summary, const int& stock, Product *next = NULL)
    {
        this->name = name;
        this->type = type;
        this->summary = summary;
        this->stock = stock;
        this->next  = next;
    }
    Product(Product* next = NULL) { this->next = next; }
};

#endif
