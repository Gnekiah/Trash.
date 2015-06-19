#ifndef NEWFILEDIALOG_H
#define NEWFILEDIALOG_H

#include <QDialog>
#include <QLabel>
#include <QLineEdit>
#include <QPushButton>

class NewFileDialog : public QDialog
{
    Q_OBJECT
public:
    NewFileDialog(QWidget *parent=0, Qt::WindowFlags f=0);
    ~NewFileDialog();

    QLabel *label1;
    QLabel *label2;
    QLabel *label3;
    QLabel *label4;
    QLabel *label5;

    QLineEdit *line1;
    QLineEdit *line2;
    QLineEdit *line3;
    QLineEdit *line4;
    QLineEdit *line5;

    QPushButton *pushButtonOK;
    QPushButton *pushButtonCancel;

};

#endif 

