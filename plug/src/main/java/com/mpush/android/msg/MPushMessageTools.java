package com.mpush.android.msg;

import android.content.Context;
import android.content.SharedPreferences;

import com.mpush.android.MPushMessage;

/**
 * Created by Joey on 2018/10/10.
 * 消息管理的工具类用来处理消息未读数的管理
 */

public class MPushMessageTools implements MessageTools {

    SharedPreferences sp;
    private final String UN_READ_COUNT = "unreadCount";
    private final static String SP_FILE_NAME = "mpush.cfg";
    int unreadcount = 0;
    public final static MPushMessageTools I = new MPushMessageTools();

    public void init (Context context) {
        sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public synchronized void add() {
        unreadcount++;
        update();
    }

    @Override
    public synchronized void read() {
        unreadcount--;
        update();
    }

    @Override
    public synchronized void clean() {
        unreadcount = 0;
        update();
    }

    @Override
    public Integer getUnread() {
        unreadcount = sp.getInt(UN_READ_COUNT, 0);
        return unreadcount;
    }

    private void update() {
        sp.edit().putInt(UN_READ_COUNT, unreadcount).apply();
    }
}
