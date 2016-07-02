package com.xxiong.acacia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent MyIntent = new Intent(context, MyIntentService.class);
        context.startService(MyIntent);
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
