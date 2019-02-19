package com.mpush.android;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.mpush.android.msg.MPushMessageTools;
import com.mpush.api.Constants;
import com.mpush.api.http.HttpCallback;
import com.mpush.api.http.HttpMethod;
import com.mpush.api.http.HttpRequest;
import com.mpush.client.ClientConfig;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * 外部开发的API接口，二次封装的mpush功能的聚合，
 * 包括消息管理
 * push管理
 * 通知管理
 */

public class MPushApiHelper {

    private static MPushApiHelper apiHelper;

    private Context context;
    private MPushConfig config;

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

    public MPushApiHelper initSDK(Context context, MPushConfig config) {
        this.context = context;
        // 初始化通知内容
        Notifications.I.init(context);
        //注册Notification 图标
        registerIcon(config.getSmallIcon(), config.getLargeIcon());
        //初始化配置
        this.config = config;
        //初始化消息管理
        MPushMessageTools.I.init(context);
        return getInstance();
    }

    private void registerIcon(int smallIcon, int largeIcon) {
        Notifications.I.setSmallIcon(smallIcon);
        Notifications.I.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon));
    }

    private MPushApiHelper initPush(String allotServer) {
        MPushLog log = new MPushLog();
        log.enable(true);
        ClientConfig cc = ClientConfig.build()
                .setPublicKey(config.getPrivateKey())
                .setAllotServer(allotServer)
                .setDeviceId(config.getDeviceId())
                .setClientVersion(config.getClientVersion())
                .setLogger(log)
                .setLogEnabled(BuildConfig.DEBUG)
                .setEnableHttpProxy(true)
                .setUserId(config.getUserId());
        config.setAllotServer(allotServer);
        MPush.I.checkInit(context).setClientConfig(cc);
        return getInstance();
    }

    public MPushApiHelper bindAccount(String userId, String tags) {
        if (!TextUtils.isEmpty(userId)) {
            MPush.I.bindAccount(userId, tags);
        }
        config.setUserId(userId);
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
        params.put("msg", config.getUserId() + " say:" + msg);

        HttpRequest request = new HttpRequest(HttpMethod.POST, config.getAllotServer() + "/push");
        byte[] body = params.toString().getBytes(Constants.UTF_8);
        request.setBody(body, "application/json; charset=utf-8");
        request.setTimeout((int) TimeUnit.SECONDS.toMillis(10));
        request.setCallback(callback);
        MPush.I.sendHttpProxy(request);
    }

    public void stopPush() {
        MPush.I.stopPush();
    }

    public void pausePush() {
        MPush.I.pausePush();
    }

    public void resumePush() {
        MPush.I.resumePush();
    }

    public void unbindAccount() {
        MPush.I.unbindAccount();
    }
}
