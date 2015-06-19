#include "newfiledialog.h"
#include "data_structure/inventory.h"
#include "data_structure/transport.h"
#include "data_structure/delivered.h"

#include <QDialog>
#include <QLineEdit>
#include <QGridLayout>
#include <QHBoxLayout>
#include <QPushButton>
#include <QString>

NewFileDialog::NewFileDialog(QWidget *parent, Qt::WindowFlags f) : QDialog(parent, f)
{
    setWindowTitle(tr("New File"));
    label1 = new QLabel(tr("商品 1:"));
    label2 = new QLabel(tr("商品 2:"));
    label3 = new QLabel(tr("商品 3:"));
    label4 = new QLabel(tr("商品 4:"));
    label5 = new QLabel(tr("商品 5:"));

    line1 = new QLineEdit;
    line2 = new QLineEdit;
    line3 = new QLineEdit;
    line4 = new QLineEdit;
    line5 = new QLineEdit;

    pushButtonOK = new QPushButton(tr("确定"));
    pushButtonCancel = new QPushButton(tr("取消"));

    QGridLayout *labelLayout = new QGridLayout();
    int labelCol = 0;
    int contentCol = 1;
    labelLayout->addWidget(label1, 0, labelCol);
    labelLayout->addWidget(line1, 0, contentCol);
    labelLayout->addWidget(label2, 1, labelCol);
    labelLayout->addWidget(line2, 1, contentCol);
    labelLayout->addWidget(label3, 2, labelCol);
    labelLayout->addWidget(line3, 2, contentCol);
    labelLayout->addWidget(label4, 3, labelCol);
    labelLayout->addWidget(line4, 3, contentCol);
    labelLayout->addWidget(label5, 4, labelCol);
    labelLayout->addWidget(line5, 4, contentCol);
    labelLayout->setSpacing(5);

    QHBoxLayout *buttonLayout = new QHBoxLayout();
    buttonLayout->addStretch();
    buttonLayout->addWidget(pushButtonOK);
    buttonLayout->addWidget(pushButtonCancel);

    QGridLayout *mainLayout = new QGridLayout(this);
    mainLayout->setMargin(5);
    mainLayout->setSpacing(5);
    mainLayout->addLayout(labelLayout, 0, 1);
    mainLayout->addLayout(buttonLayout, 1, 0, 1, 2);
    mainLayout->setSizeConstraint(QLayout::SetFixedSize);

}


NewFileDialog::~NewFileDialog()
{

}
