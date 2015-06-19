#-------------------------------------------------
#
# Project created by QtCreator 2015-06-10T09:54:56
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = shortest-path-management
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp

HEADERS  += mainwindow.h \
    graph/adjacency.h \
    graph/path.h
