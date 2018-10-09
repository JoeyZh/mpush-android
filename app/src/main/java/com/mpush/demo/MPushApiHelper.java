package com.mpush.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.mpush.android.BuildConfig;
import com.mpush.android.MPush;
import com.mpush.android.MPushLog;
import com.mpush.android.Notifications;
import com.mpush.api.Constants;
import com.mpush.api.http.HttpCallback;
import com.mpush.api.http.HttpMethod;
import com.mpush.api.http.HttpRequest;
import com.mpush.client.ClientConfig;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * 外部开发的API接口
 */

public class MPushApiHelper {

    private static MPushApiHelper apiHelper;

    private SharedPreferences sp;

    private Context context;

    public synchronized static MPushApiHelper getInstance() {
        if (apiHelper != null) {
            return apiHelper;
        }
        synchronized (MPushApiHelper.class) {
            if (apiHelper == null)
                apiHelper = new MPushApiHelper();
            return apiHelper;
        }
    }

    public MPushApiHelper initSDK(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("mpush.cfg", Context.MODE_PRIVATE);
        MPushConfig.ALLOC_SERVER = sp.getString("allotServer", MPushConfig.DEFAULT_ALLOC_SERVER);
        MPushConfig.DEVICE_ID = getDeviceId(context);
        MPushConfig.USER_ID = sp.getString("account", null);

        // 初始化通知内容
        Notifications.I.init(context);
        //注册Notification 图标
        registerIcon(MPushConfig.NOTICE_ICON_SMALL, MPushConfig.NOTICE_ICON_LARGE);
        return getInstance();
    }

    @SuppressLint("MissingPermission")
    private String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            String time = Long.toString((System.currentTimeMillis() / (1000 * 60 * 60)));
            deviceId = time + time;
        }
        return deviceId;
    }

    private void registerIcon(int smallIcon, int largeIcon) {
        Notifications.I.setSmallIcon(smallIcon);
        Notifications.I.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon));
    }

    private MPushApiHelper initPush(String allotServer) {
        MPushLog log = new MPushLog();
        log.enable(true);
        ClientConfig cc = ClientConfig.build()
                .setPublicKey(MPushConfig.publicKey)
                .setAllotServer(allotServer)
                .setDeviceId(MPushConfig.getDeviceId())
                .setClientVersion(BuildConfig.VERSION_NAME)
                .setLogger(log)
                .setLogEnabled(BuildConfig.DEBUG)
                .setEnableHttpProxy(true)
                .setUserId(MPushConfig.getUserId());
        MPushConfig.ALLOC_SERVER = allotServer;
        MPush.I.checkInit(context).setClientConfig(cc);
        return getInstance();
    }

    public MPushApiHelper bindUser(String userId, String tags) {
        if (!TextUtils.isEmpty(userId)) {
            MPush.I.bindAccount(userId, tags);
        }
        MPushConfig.USER_ID = userId;
        return getInstance();
    }

    /**
     * 启动服务
     *
     * @param
     */
    public MPushApiHelper startPush(String allocServer) {
        initPush(allocServer);
        MPush.I.checkInit(context).startPush();
        return getInstance();
    }

    public void sendMessageTo(String toUser, String msg, HttpCallback callback) throws Exception {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        JSONObject params = new JSONObject();
        params.put("userId", toUser);
        params.put("msg", MPushConfig.getUserId() + " say:" + msg);

        HttpRequest request = new HttpRequest(HttpMethod.POST, MPushConfig.getAllocServer() + "/push");
        byte[] body = params.toString().getBytes(Constants.UTF_8);
        request.setBody(body, "application/json; charset=utf-8");
        request.setTimeout((int) TimeUnit.SECONDS.toMillis(10));
        request.setCallback(callback);
        MPush.I.sendHttpProxy(request);
    }
}
