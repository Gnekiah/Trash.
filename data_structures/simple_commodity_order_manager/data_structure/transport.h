#ifndef TRANSPORT_H
#define TRANSPORT_H

#include <QString>
#include "product.h"

class Transport
{
private:
    int      cnt;       // 计数器
    Product* front;     // 指向队头的指针
    Product* rear;      // 指向队尾的指针
    
public:
    Transport()
    {
        front = rear = new Product();
        cnt = 0;
    }
    ~Transport()
    {
        clearInfo();
        delete front;
    }
        
    /* 清空队列.*/
    void clearInfo();
    /* 入队.*/
    void enqueue(const QString& name, const QString& type, const QString& summary, const int& stock);
    /* 出队.*/
    void dequeue();
    /* 获得队首的元素.*/
    const Product* frontValue() const;
    /* 计算队列长度.*/
    int length() const;
};

#endif
