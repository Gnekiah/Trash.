package com.xxiong.acacia;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * get information.
 * get information.
 * Created by Ekira on 2016/6/28.
 */
public class InfoGather {

    TelephonyManager tm = null;
    WifiManager wm = null;

    public InfoGather(Context context) {
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 收集不太会改变的信息
     * @return 收集到的String
     */
    public String getInfo() {
        String info;
        info = "Phone Number:" + tm.getLine1Number() + "\n";
        info += "IMSI:" + tm.getSubscriberId() + "\n";
        info += "MIEI:" + tm.getDeviceId() + "\n";
        info += "SSN:" + tm.getSimSerialNumber() + "\n";
        info += "WIFI MAC:" + wm.getConnectionInfo().getMacAddress() + "\n";
        info += "Network Country ISO:" + tm.getNetworkCountryIso() + "\n";
        info += "Network Operator Name:" + tm.getNetworkOperatorName() + "\n";
        return info;
    }

}
