package com.mpush.android;

import com.mpush.api.Logger;

/**
 * Created by Joey on 2018/10/9.
 * 配置Mpush的一些初始化值
 */

public interface MPushConfig {


    String getAllotServer();

    String getUserId();

    String getDeviceId();

    String getClientVersion();

    void setAllotServer(String allotServer);

    void setUserId(String userId);

    void setDeviceId(String deviceId);

    String getPrivateKey();

    Integer getSmallIcon();

    Integer getLargeIcon();

    void setLogger(Logger logger);

    Logger getLogger();


}
