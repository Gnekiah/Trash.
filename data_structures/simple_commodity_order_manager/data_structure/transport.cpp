#include <QString>
#include "product.h"
#include "transport.h" 
#include <QDebug>

/* 清空队列.*/
void Transport::clearInfo(void)
{
    while (front->next != NULL)
    {
        rear = front;
        front = front->next;
        delete rear;
    }
    rear = front;
    cnt = 0;
}

/* 入队.*/
void Transport::enqueue(const QString& name, const QString& type, const QString& summary, const int& stock)
{
    rear = rear->next = new Product(name, type, summary, stock, NULL);
    ++cnt;
}

/* 出队.*/
void Transport::dequeue(void)
{
    if (cnt != 0)
    {
        Product *tmp = front->next;
        front->next = front->next->next;
        if (rear == tmp)
            rear = front;
        delete tmp;
        --cnt;
    }
}

/* 获得队首的元素.*/
const Product* Transport::frontValue() const
{
    if (cnt != 0)
    {
        Product *tmp = front->next;
        return tmp;
    }
    return NULL;
}

/* 计算队列长度.*/
int Transport::length(void) const { return cnt; }
