#-------------------------------------------------
#
# Project created by QtCreator 2015-05-20T16:04:01
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = Encoder
TEMPLATE = app


SOURCES += main.cpp\
        interface.cpp

HEADERS  += interface.h \
    huffcode/huffnode.h \
    huffcode/hufftree.h \
    function/charscount.h \
    datastore/transinfo.h \
    function/buildhuff.h \
    datastore/minheap.h

