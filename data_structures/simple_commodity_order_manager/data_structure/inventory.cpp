#include <QString>
#include "product.h"
#include "inventory.h"
#include <QDebug>

/* 清空链表.*/
void Inventory::clearInfo(void) { removeAll(); init(); }

/* 插入一个节点.*/
void Inventory::insertInfo(const QString& name, const QString& type, const QString& summary, const int& stock)
{
    curr->next = new Product(name, type, summary, stock, curr->next);
    if (tail == curr)
        tail = curr->next;
    ++cnt;
}

/* 在链表尾部扩展一个节点.*/
void Inventory::appendInfo(const QString& name, const QString& type, const QString& summary, const int& stock)
{
    tail = tail->next = new Product(name, type, summary, stock, NULL);
    ++cnt;
}

/* 删除当前指向的节点.*/
void Inventory::removeInfo(void)
{
    if (curr->next != NULL)
    {
        Product *tmp = curr->next;
        if (tail == curr->next)
            tail = curr;
        curr->next = curr->next->next;
        delete tmp;
        --cnt;
    }
}

/* 当前位置指向链表头.*/
void Inventory::toStart(void) { curr = head; }

/* 当前位置指向表尾.*/
void Inventory::toEnd(void) { curr = tail; }

/* 返回到上一个节点.*/
void Inventory::toPrev(void)
{
    if (curr == head) return;
    Product* tmp = head;
    while (tmp->next != curr) 
        tmp = tmp->next;
    curr = tmp;
}

/* 到下一个节点.*/
void Inventory::toNext(void)
{
    if (curr != tail)
        curr = curr->next;
}

/* 到某个指定位置的节点.*/
void Inventory::toPos(int& pos)
{
    if (pos >= 0 && pos <= cnt)
    {
        curr = head;
        for (int i = 0; i < pos; ++i) 
            curr = curr->next;
    }
}

/* 返回当前位置.*/
int Inventory::currPos(void) const
{
    Product* tmp = head;
    int i;
    for (i = 0; tmp != curr; ++i)
        tmp = tmp->next;
    return i;
}

/* 返回链表长度.*/
int Inventory::length() const { return cnt; }
        
/* 获取节点存储的商品信息.*/
const Product* Inventory::getInfo(void) const
{
    if (curr->next != NULL)
    {
        Product *tmp = curr->next;
        return tmp;
    }
    return NULL;
}
