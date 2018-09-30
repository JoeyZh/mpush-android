package com.mpush.demo;

import com.mpush.android.MPushMessage;

/**
 * Created by Joey on 2018/9/20.
 * 消息管理接口
 */

public interface MessageTools {

    void add(MPushMessage msg);

    void read();

    void clean();

    void remove(MPushMessage msg);

}
