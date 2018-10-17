package com.mpush.demo;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.mpush.android.MPushConfig;

/**
 * Created by Joey on 2018/9/18.
 * 配置推送框架默认的属性
 */

public class MyConfig implements MPushConfig {

    // 默认推送服务器Ip地址
    private static final String DEFAULT_ALLOC_SERVER = "http://192.168.1.112:9999";
    // APP的推送smallIcon
    public final int NOTICE_ICON_SMALL = R.mipmap.ic_notification;
    // APP的推送largeIcon
    public final int NOTICE_ICON_LARGE = R.mipmap.ic_launcher;
    //公钥有服务端提供和私钥对应
    public final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCghPCWCobG8nTD24juwSVataW7iViRxcTkey/B792VZEhuHjQvA3cAJgx2Lv8GnX8NIoShZtoCg3Cx6ecs+VEPD2fBcg2L4JK7xldGpOJ3ONEAyVsLOttXZtNXvyDZRijiErQALMTorcgi79M5uVX9/jMv2Ggb2XAeZhlLD28fHwIDAQAB";
    // 设备id
    private String deviceId;
    // 绑定账号Id
    private String userId;

    private final String appId = BuildConfig.APPLICATION_ID;

    protected static String allotServer;

    public static MPushConfig build(Context context) {
        MPushConfig config = new MyConfig();
        SharedPreferences sp = context.getSharedPreferences("mpush.cfg", Context.MODE_PRIVATE);
        config.setAllotServer(sp.getString("allotServer", DEFAULT_ALLOC_SERVER));
        config.setDeviceId(getDeviceId(context));
        config.setUserId(sp.getString("account", null));
        return config;
    }

    @Override
    public String getAllotServer() {
        return allotServer;
    }

    @Override
    public void setAllotServer(String allotServer) {
        this.allotServer = allotServer;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String getPrivateKey() {
        return publicKey;
    }

    @Override
    public Integer getSmallIcon() {
        return NOTICE_ICON_SMALL;
    }

    @Override
    public Integer getLargeIcon() {
        return NOTICE_ICON_LARGE;
    }

    public String getUserId() {
        return userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public String getClientVersion() {
        return com.mpush.demo.BuildConfig.VERSION_NAME;
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            String time = Long.toString((System.currentTimeMillis() / (1000 * 60 * 60)));
            deviceId = time + time;
        }
        return deviceId;
    }
}
