#-------------------------------------------------
#
# Project created by QtCreator 2015-05-14 T 22:01:10
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = scoms
TEMPLATE = app


SOURCES += main.cpp\
        userinterface.cpp \
    define_dialog/newfiledialog.cpp \
    data_structure/delivered.cpp \
    data_structure/inventory.cpp \
    data_structure/transport.cpp

HEADERS  += userinterface.h \
    define_dialog/newfiledialog.h \
    data_structure/delivered.h \
    data_structure/inventory.h \
    data_structure/product.h \
    data_structure/transport.h

RESOURCES += \
    icons.qrc
