/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.tool.edit.TextUtils;

/**
 * 广播管理类
 */
public class BroadcastManager {

    private static LocalBroadcastManager localBroadcastManager;

    private static BroadcastReceiver phoneOverBroadcast;

    private BroadcastManager() {}

    /**
     * 初始化广播对象
     */
    public static void init(Context context) {
        if (localBroadcastManager == null) {
            localBroadcastManager = LocalBroadcastManager.getInstance(context);
        }
    }

    /**
     * 注册广播
     */
    public static void registerReceiver(BroadcastReceiver broadcastReceiver, String... actions) {
        if (localBroadcastManager != null) {
            // 同时只允许注册一个接收推送广播
            if (broadcastReceiver instanceof BaseActivity.PhoneOverBroadcast) {
                if (phoneOverBroadcast != null) {
                    unregisterReceiver(phoneOverBroadcast);
                }
                phoneOverBroadcast = broadcastReceiver;
            }
            IntentFilter intentFilter = new IntentFilter();
            for (int i = 0; i < actions.length; i++) {
                if (!TextUtils.EMPTY.equals(actions[i])) {
                    intentFilter.addAction(actions[i]);
                }
            }
            localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    /**
     * 注销广播
     */
    public static void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        if (localBroadcastManager != null) {
            localBroadcastManager.unregisterReceiver(broadcastReceiver);
        }
    }

    /**
     * 发送广播
     */
    public static void sendBroadcast(String action) {
        if (localBroadcastManager != null) {
            Intent intent = new Intent(action);
            localBroadcastManager.sendBroadcast(intent);
        }
    }

    /**
     * 发送广播
     */
    public static void sendBroadcast(Intent intent) {
        if (localBroadcastManager != null) {
            localBroadcastManager.sendBroadcast(intent);
        }
    }

    /**
     * 发送广播
     */
    public static void sendBroadcast(Intent intent, String action) {
        if (localBroadcastManager != null) {
            if (intent == null) {
                intent = new Intent();
            }
            intent.setAction(action);
            localBroadcastManager.sendBroadcast(intent);
        }
    }

}