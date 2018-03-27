package com.poso2o.lechuan.receiver;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.bean.event.PushMessageEvent;
import com.poso2o.lechuan.bean.mine.InvitationBean;
import com.poso2o.lechuan.manager.main.NotificacationManager;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-12-09.
 */
public class MiMessageReceiver extends PushMessageReceiver {
    private final int FOREGROUND = 1;
    private final int BACKGROUND = 2;
    private InvitationBean mEvent;
    private Context mContext;

    //{"code":"10","data":{"shop_nick":"草莓","shop_uid":13423678930,"commission_discount":95,"commission_rate":5,"shop_logo":"http://img01.poso2o.com/20171207/65af99ebc4b8a30b.jpg"}}
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        mContext = context;
        String mMessage = message.getContent();
        Print.println("onReceivePassThroughMessage_mMessage=" + mMessage);
        try {
            JSONObject object = new JSONObject(mMessage);
            String code = object.optString("code");
            mEvent = new InvitationBean();
            if (code.equals(InvitationBean.BIND_WX_ACCOUNT_CODE)) {
                mEvent.code = code;
                new ForegroundRunning(context).start();
            } else {
                Gson gson = new Gson();
                InvitationBean event = gson.fromJson(object.optString("data"), InvitationBean.class);
                if (event != null) {
                    event.code = code;
                    mEvent = event;
                    new ForegroundRunning(context).start();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示关于邀请的通知
     */
    private void showNotify() {
        String title = "";
        String content = "";
        if (mEvent != null && mEvent.code.equals(InvitationBean.INVITATION_BIND_CODE)) {//邀请分销的推送
            title = "邀请分销";
            content = mEvent.nick + "邀请您成为他的分销商。";
            NotificacationManager.getInstance().showNotification(mContext, title, content, mEvent, InnerMessageReceiver.ACTION_INVITE_BIND);
        } else if (mEvent != null && (mEvent.code.equals(InvitationBean.INVITATION_UNBIND_CODE) || mEvent.code.equals(InvitationBean.INVITATION_UNBIND_AGREE_CODE))) {//申请解除分销关系的推送
            title = "解除分销";
            content = mEvent.nick + "解除了与您的分销关系。";
            NotificacationManager.getInstance().showNotification(mContext, title, content, mEvent, InnerMessageReceiver.ACTION_INVITE_UNBIND);
        } else if (mEvent != null && mEvent.code.equals(InvitationBean.BIND_WX_ACCOUNT_CODE)) {//绑定收款帐号
            title = "绑定收款账号";
            content = "微信收款账号绑定成功!";
            NotificacationManager.getInstance().showNotification(mContext, title, content, mEvent, InnerMessageReceiver.ACTION_BIND_WX_ACCOUNT);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FOREGROUND) {//如果App正在前台运行就直接弹窗显示
                EventBus.getDefault().post(mEvent);
            } else {//否则先启动mainactivity再显示弹窗
                showNotify();
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

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
//        mMessage = message.getContent();
//        if(!TextUtils.isEmpty(message.getTopic())) {
//            mTopic=message.getTopic();
//        } else if(!TextUtils.isEmpty(message.getAlias())) {
//            mAlias=message.getAlias();
//        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
//            mUserAccount=message.getUserAccount();
//        }
        Print.println("onNotificationMessageClicked_mMessage=" + message.getContent());
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
//        mMessage = message.getContent();
//        if(!TextUtils.isEmpty(message.getTopic())) {
//            mTopic=message.getTopic();
//        } else if(!TextUtils.isEmpty(message.getAlias())) {
//            mAlias=message.getAlias();
//        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
//            mUserAccount=message.getUserAccount();
//        }
        Print.println("onNotificationMessageArrived_mMessage=" + message.getContent());
    }

    private String mRegId = "";
    private String mAlias = "";
    private String mTopic = "";

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                Print.println("onCommandResult_mMessage=" + "mAlias:" + mAlias);
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                Print.println("onCommandResult_mMessage=" + "un_mAlias:" + mAlias);
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                mStartTime = cmdArg1;
//                mEndTime = cmdArg2;
            }
        }
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
//                String uid = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
//                if (!TextUtil.isEmpty(uid)) {
//                    MiPushClient.pausePush(context, uid);
//                }
            }
        }
//        Log.i("onReceiveRegisterResult","onReceiveRegisterResult...........mRegId:"+mRegId);
        Print.println("onReceiveRegisterResult_mMessage=" + "mRegId:" + mRegId);
    }
}
