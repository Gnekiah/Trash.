#-------------------------------------------------------------------------------
# Name:        Watermark GUI
# Purpose:     Package watermark to provide GUI.
#
# Author:      Gnekiah
#
# Created:     26/05/2016
# Copyright:   (c) Gnekiah 2016
# Licence:     <GNU General Public License>
#-------------------------------------------------------------------------------

# -*- coding: utf-8 -*- #

import os, wx
import wm

FRAME_SIZE = (640,480)
LABEL1_POS = (10,20)
LABEL2_POS = (10,330)
LABEL3_POS = (0,300)
LABEL3_SIZE = (640,5)
TEXTCTRL_SIZE = (300,30)
SRPATH_POS = (230,18)
WMTEXT_POS = (40,60)
WMTEXTCTRL_SIZE = (400,200)
CKPATH_POS = (230,328)
BUTTON_SIZE = (80,30)
BUTTON_SIZE_V2 = (100,50)
SR_BUTTON_POS = (535,18)
CK_BUTTON_POS = (535,328)
EMBED_BUTTON_POS = (515,180)
CHECK_BUTTON_POS = (515,380)

wildcard = "JPEG file(*.jpeg,*.jpg)|*.jpg;*.jpeg|BMP file(*.bmp)|*.bmp|All files(*.*)|*.*"


class MainWindow(wx.App):
    def __init__(self):
        wx.App.__init__(self)


    def OnInit(self):
        frame = wx.Frame(parent=None, title="Water Mark", size=FRAME_SIZE,
            style=wx.MINIMIZE_BOX|wx.SYSTEM_MENU|wx.CAPTION|wx.CLOSE_BOX|wx.CLIP_CHILDREN)
        frame.SetFont(wx.Font(18, wx.DEFAULT, wx.NORMAL, wx.NORMAL))

        # static text
        label1 = wx.StaticText(parent=frame, label="Origin Image Path:", pos=LABEL1_POS)
        label2 = wx.StaticText(parent=frame, label="Origin Image Path:", pos=LABEL2_POS)
        label3 = wx.StaticLine(parent=frame, pos=LABEL3_POS, size=LABEL3_SIZE)

        # file path, the target to add watermark
        srpath = wx.TextCtrl(parent=frame, pos=SRPATH_POS, size=TEXTCTRL_SIZE)
        # watermark text
        wmtext = wx.TextCtrl(parent=frame, pos=WMTEXT_POS, size=WMTEXTCTRL_SIZE, style=wx.TE_MULTILINE|wx.TE_PROCESS_ENTER)
        # file path, the target to check out watermark
        ckpath = wx.TextCtrl(parent=frame, pos=CKPATH_POS, size=TEXTCTRL_SIZE)

        # select source file path for embedding
        select_srpath = wx.Button(frame, label="Select", pos=SR_BUTTON_POS, size=BUTTON_SIZE)
        # select target file path for check out
        select_ckpath = wx.Button(frame, label="Select", pos=CK_BUTTON_POS, size=BUTTON_SIZE)
        # embed button
        embed = wx.Button(frame, label="Embed", pos=EMBED_BUTTON_POS, size=BUTTON_SIZE_V2)
        # check out button
        check = wx.Button(frame, label="Extract", pos=CHECK_BUTTON_POS, size=BUTTON_SIZE_V2)

        self.Bind(wx.EVT_BUTTON, self.OnButtonSelect1, select_srpath)
        self.Bind(wx.EVT_BUTTON, self.OnButtonSelect3, select_ckpath)
        self.Bind(wx.EVT_BUTTON, self.OnButtonEmbed, embed)
        self.Bind(wx.EVT_BUTTON, self.OnButtonCheck, check)
        self.srpath = srpath
        self.wmtext = wmtext
        self.ckpath = ckpath
        self.embed = embed
        self.check = check
        self.frame = frame
        frame.Show()
        return True


    def OnButtonSelect1(self, event):
        self.srpath.WriteText(self.SelectFileDialog(self))


    def OnButtonSelect3(self, event):
        self.ckpath.WriteText(self.SelectFileDialog(self))


    def OnButtonEmbed(self, event):
        srpath = self.srpath.GetValue()
        wmtext = self.wmtext.GetValue()
        self.srpath.Clear()
        self.wmtext.Clear()
        if srpath == '' or wmtext == '':
            self.ErrorMessageBox(self)
            return
        arr = wm.loadimg(srpath)
        arrbeta = wm.embed8(arr, wmtext)
        wm.saveimg(arrbeta, wm.SAVELENA)


    def OnButtonCheck(self, event):
        srpath = self.ckpath.GetValue()
        self.ckpath.Clear()
        if srpath == '':
            self.ErrorMessageBox(self)
            return
        text = wm.extract8(srpath)
        dlg = wx.MessageDialog(self.frame, text,
            'Xtract', wx.OK|wx.ICON_INFORMATION)
        dlg.ShowModal()
        dlg.Destroy()


    def SelectFileDialog(self, parent):
        path = ""
        dlg = wx.FileDialog(parent.frame, message="Choose a file", defaultDir=os.getcwd(),
            defaultFile="", wildcard=wildcard, style=wx.OPEN|wx.CHANGE_DIR)
        if dlg.ShowModal() == wx.ID_OK:
            path = dlg.GetPath()
        dlg.Destroy()
        return path


    def ErrorMessageBox(self, parent):
        dlg = wx.MessageDialog(parent.frame, 'No selected file or file not exist!',
            'Error', wx.OK|wx.ICON_INFORMATION)
        dlg.ShowModal()
        dlg.Destroy()


def main():
    app = MainWindow()
    app.MainLoop()


if __name__ == '__main__':
    main()
