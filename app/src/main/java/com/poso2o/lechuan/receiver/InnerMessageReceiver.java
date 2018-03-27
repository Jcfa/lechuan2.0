package com.poso2o.lechuan.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.bean.mine.InvitationBean;
import com.poso2o.lechuan.dialog.BeInvitedDialog;
import com.poso2o.lechuan.manager.poster.MyFansDataManager;
import com.poso2o.lechuan.util.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2017-12-18.
 */

public class InnerMessageReceiver extends BroadcastReceiver {
    private final int FOREGROUND = 1;
    private final int BACKGROUND = 2;
    public static final String ACTION_INVITE_BIND = "com.poso2o.lechuan.INVITE_BIND";
    public static final String ACTION_INVITE_UNBIND = "com.poso2o.lechuan.INVITE_UNBIND";
    public static final String ACTION_BIND_WX_ACCOUNT = "com.poso2o.lechuan.BIND_WX_ACCOUNT";
    private InvitationBean mEvent;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (intent.getAction().equals("com.poso2o.lechuan.INVITE_BIND") || intent.getAction().equals("com.poso2o.lechuan.INVITE_UNBIND")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mEvent = (InvitationBean) bundle.getSerializable("info");
                new ForegroundRunning(context).start();
            }
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FOREGROUND) {//如果App正在前台运行就直接弹窗显示
//                showNotify();
                EventBus.getDefault().post(mEvent);
            } else {//否则先启动activity再显示弹窗
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(mEvent);
                    }
                }, 1000);
            }
        }
    };

    /**
     * 判断当前app是否在前台运行
     */
    private class ForegroundRunning extends Thread {
        private Context context;

        public ForegroundRunning(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runnings = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo running : runnings) {
                if (running.processName.equals(context.getPackageName())) {
                    if (running.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                            || running.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                        handler.sendEmptyMessage(FOREGROUND);
                    } else {
                        handler.sendEmptyMessage(BACKGROUND);
                    }
                    break;
                }
            }
        }
    }
}
