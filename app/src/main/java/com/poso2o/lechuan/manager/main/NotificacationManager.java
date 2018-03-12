package com.poso2o.lechuan.manager.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.mine.InvitationBean;
import com.poso2o.lechuan.receiver.InnerMessageReceiver;

/**
 * Created by Administrator on 2017-12-18.
 */

public class NotificacationManager {

    private static NotificacationManager manager;

    private NotificacationManager() {
    }

    /**
     * 通知管理实例
     *
     * @return
     */
    public static NotificacationManager getInstance() {
        synchronized (NotificacationManager.class) {
            if (manager == null) {
                manager = new NotificacationManager();
            }
            return manager;
        }
    }

    /**
     * 显示通知
     */
    public void showNotification(Context context, String title, String content, InvitationBean event, String action) {
        // 获取通知服务对象NotificationManager
        NotificationManager notiManager = (NotificationManager)
                context.getSystemService(context.NOTIFICATION_SERVICE);
        // 创建Notification对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setTicker("Ticker");            // 通知弹出时状态栏的提示文本
        builder.setContentInfo("");  //
        builder.setContentTitle(title)    // 通知标题
                .setContentText(content)  // 通知内容
                .setSmallIcon(R.mipmap.ic_launcher);    // 通知小图标
        builder.setDefaults(Notification.DEFAULT_SOUND);    // 设置声音/震动等
        // 设置通知的点击行为：自动取消/跳转等
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        Intent intent = new Intent(context, InnerMessageReceiver.class);
        intent.setAction(action);
        Bundle bundle=new Bundle();
        bundle.putSerializable("info",event);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = pendingIntent;
        // 通过NotificationManager发送通知
        notiManager.notify(1003, notification);
    }
}
