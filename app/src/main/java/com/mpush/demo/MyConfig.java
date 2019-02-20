package com.mpush.demo;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.mpush.android.MPushConfig;
import com.mpush.android.MPushLog;
import com.mpush.api.Logger;

/**
 * Created by Joey on 2018/9/18.
 * 配置推送框架默认的属性
 */

public class MyConfig implements MPushConfig {

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
    protected Logger logger;

    public static MPushConfig build(Context context) {
        MPushConfig config = new MyConfig();
        SharedPreferences sp = context.getSharedPreferences("mpush.cfg", Context.MODE_PRIVATE);
        config.setAllotServer(sp.getString("allotServer", BuildConfig.APP_SERVER));
        config.setDeviceId(getDeviceId(context));
        config.setUserId(sp.getString("account", null));
        config.setLogger(new MyLog((Activity) context));
        return config;
    }

    @Override
    public String getAllotServer() {
        return allotServer;
    }

    @Override
    public void setAllotServer(String server) {
        allotServer = server;
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

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public String getClientVersion() {
        return com.mpush.demo.BuildConfig.VERSION_NAME;
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        String time = Long.toString((System.currentTimeMillis() / (1000 * 60 * 60)));
        String deviceId = time;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
            if (TextUtils.isEmpty(deviceId)) {

            }
        } catch (Exception e) {
        }
        return deviceId;
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
