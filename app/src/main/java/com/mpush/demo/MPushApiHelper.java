package com.mpush.demo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.annotation.MainThread;
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
import com.mpush.api.http.HttpResponse;
import com.mpush.client.ClientConfig;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class MPushApiHelper {

    private static MPushApiHelper apiHelper;
    private SharedPreferences sp;
    // 应用注册Id
    private String userId;
    // 分发服务器地址
    private String allocServer;

    private Context context;

    private Handler mainHandler;

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
        Notifications.I.init(context);
        sp = context.getSharedPreferences("mpush.cfg", Context.MODE_PRIVATE);
        registerIcon(MPushConfig.NOTICE_ICON_SMALL, MPushConfig.NOTICE_ICON_LARGE);
        return getInstance();
    }

    private void registerIcon(int smallIcon, int largeIcon) {
        Notifications.I.setSmallIcon(smallIcon);
        Notifications.I.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon));
    }

    private MPushApiHelper initPush(String allocServer, String userId) {
        ClientConfig cc = ClientConfig.build()
                .setPublicKey(MPushConfig.publicKey)
                .setAllotServer(allocServer)
                .setDeviceId(MPushConfig.DeviceId)
                .setClientVersion(BuildConfig.VERSION_NAME)
                .setLogger(new MPushLog())
                .setLogEnabled(BuildConfig.DEBUG)
                .setEnableHttpProxy(true)
                .setUserId(userId);
        MPush.I.checkInit(context).setClientConfig(cc);
        return getInstance();
    }

    public MPushApiHelper bindUser(String userId) {
        if (!TextUtils.isEmpty(userId)) {
            sp.edit().putString("userId", userId);
            sp.edit().commit();
            this.userId = userId;
            MPush.I.bindAccount(userId, "mpush:" + (int) (Math.random() * 10));
        }
        return getInstance();
    }

    /**
     * 启动服务
     *
     * @param
     */
    public boolean startPush() {
        allocServer = "http://" + MPushConfig.ALLOC_SERVER;
        initPush(allocServer, userId);
        sp.edit().putString("allocServer", allocServer);
        sp.edit().commit();
        MPush.I.checkInit(context).startPush();
        return true;
    }

    public void sendMessageTo(String toUser, String msg) throws Exception {
        if (TextUtils.isEmpty(allocServer)) {
            return;
        }

        if (!allocServer.startsWith("http://")) {
            allocServer = "http://" + allocServer;
        }

        if (TextUtils.isEmpty(msg)) {
            return;
        }

        JSONObject params = new JSONObject();
        params.put("userId", toUser);
        params.put("msg", userId + " say:" + msg);

        final Context context = this.context;
        HttpRequest request = new HttpRequest(HttpMethod.POST, allocServer + "/push");
        byte[] body = params.toString().getBytes(Constants.UTF_8);
        request.setBody(body, "application/json; charset=utf-8");
        request.setTimeout((int) TimeUnit.SECONDS.toMillis(10));
        request.setCallback(new HttpCallback() {
            @Override
            public void onResponse(final HttpResponse httpResponse) {
                String response;
                if (httpResponse.statusCode == 200) {
                    response = new String(httpResponse.body, Constants.UTF_8);
                } else {
                    response = httpResponse.reasonPhrase;
                }
            }

            @Override
            public void onCancelled() {

            }
        });
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

    public void unbindUser() {
        MPush.I.unbindAccount();
    }
}
