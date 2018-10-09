package com.mpush.android;

import android.content.Context;

/**
 * Created by Joey on 2018/10/9.
 */

public interface MPushConfig {


    String getAllotServer();

    String getUserId();

    String getDeviceId();

    void setAllotServer(String allotServer);

    void setUserId(String userId);

    void setDeviceId(String deviceId);

    String getPrivateKey();

    Integer getSmallIcon();

    Integer getLargeIcon();

}
