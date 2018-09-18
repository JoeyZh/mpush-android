package com.mpush.demo;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.mpush.android.R;

/**
 * Created by Joey on 2018/9/18.
 */

public class MPushConfig {

    // 推送服务器Ip地址
    protected static final String ALLOC_SERVER = "192.168.1.112";
    // APP的推送smallIcon
    protected static final int NOTICE_ICON_SMALL = R.mipmap.ic_notification;
    // APP的推送largeIcon
    protected static final int NOTICE_ICON_LARGE = R.mipmap.ic_launcher;
    //公钥有服务端提供和私钥对应
    protected static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCghPCWCobG8nTD24juwSVataW7iViRxcTkey/B792VZEhuHjQvA3cAJgx2Lv8GnX8NIoShZtoCg3Cx6ecs+VEPD2fBcg2L4JK7xldGpOJ3ONEAyVsLOttXZtNXvyDZRijiErQALMTorcgi79M5uVX9/jMv2Ggb2XAeZhlLD28fHwIDAQAB";
    // 设备id
    protected static String DeviceId;

    static {
        String time = Long.toString((System.currentTimeMillis() / (1000 * 60 * 60)));
        DeviceId = time + time;
    }

    public static void initConfig(String deviceId) {
        DeviceId = deviceId;
    }

}
