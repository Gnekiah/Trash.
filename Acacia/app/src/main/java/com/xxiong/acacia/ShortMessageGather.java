package com.xxiong.acacia;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v4.content.ContextCompat;

/**
 * Created by Ekira on 2016/6/30.
 */
public class ShortMessageGather {
    private Context context = null;
    private final String SMS_URI_ALL = "content://sms/";
    private static final String[] SMSSTRUCT = new String[] {
            Telephony.Sms._ID,
            Telephony.Sms.ADDRESS,
            Telephony.Sms.PERSON,
            Telephony.Sms.BODY,
            Telephony.Sms.DATE,
            Telephony.Sms.TYPE };

    public ShortMessageGather(Context context1) {
        context = context1;
    }

    public String getAllSMS() {
        String contacts = null;
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse(SMS_URI_ALL);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            // 由于这里执行的是获取用户隐私的操作，所以不发出请求权限申请
            return "Access Short Message Permission deny.";
        }
        Cursor cursor = resolver.query(uri, SMSSTRUCT, null, null, "date desc");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                contacts += cursor.getString(0) + "\n" + cursor.getString(1) + "\n" +
                        cursor.getString(2) + "\n" + cursor.getString(3) + "\n" +
                        cursor.getString(4) + "\n" + cursor.getString(5) + "\n";
            }
        }
        cursor.close();
        return contacts;
    }
}
