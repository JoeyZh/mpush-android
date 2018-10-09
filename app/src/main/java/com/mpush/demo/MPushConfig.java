package com.mpush.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.mpush.android.BuildConfig;
import com.mpush.android.R;

/**
 * Created by Joey on 2018/9/18.
 * 配置推送框架默认的属性
 */

public class MPushConfig {

    // 默认推送服务器Ip地址
    protected static final String DEFAULT_ALLOC_SERVER = "http://192.168.1.112:9999";
    // APP的推送smallIcon
    protected static final int NOTICE_ICON_SMALL = R.mipmap.ic_notification;
    // APP的推送largeIcon
    protected static final int NOTICE_ICON_LARGE = R.mipmap.ic_launcher;
    //公钥有服务端提供和私钥对应
    protected static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCghPCWCobG8nTD24juwSVataW7iViRxcTkey/B792VZEhuHjQvA3cAJgx2Lv8GnX8NIoShZtoCg3Cx6ecs+VEPD2fBcg2L4JK7xldGpOJ3ONEAyVsLOttXZtNXvyDZRijiErQALMTorcgi79M5uVX9/jMv2Ggb2XAeZhlLD28fHwIDAQAB";
    // 设备id
    protected static String DEVICE_ID;
    protected static String USER_ID;

    protected static final String APPLICATION_ID = BuildConfig.APPLICATION_ID;

    protected static String ALLOC_SERVER;

    public static String getAllocServer() {
        return ALLOC_SERVER;
    }

    public static String getUserId() {
        return USER_ID;
    }

    public static String getDeviceId() {
        return DEVICE_ID;
    }
}
