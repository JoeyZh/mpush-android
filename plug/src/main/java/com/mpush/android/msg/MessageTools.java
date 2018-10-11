package com.mpush.android.msg;

import android.content.Intent;

import com.mpush.android.MPushMessage;

/**
 * Created by Joey on 2018/9/20.
 * 消息管理接口
 */

public interface MessageTools {

    void add();

    void read();

    void clean();

    Integer getUnread();

}
