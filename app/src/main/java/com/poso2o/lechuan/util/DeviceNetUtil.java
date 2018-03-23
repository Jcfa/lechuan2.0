package com.poso2o.lechuan.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import com.poso2o.lechuan.base.BaseApplication;
import com.poso2o.lechuan.configs.StateConstant;
import org.greenrobot.eventbus.EventBus;

public class DeviceNetUtil {
    public static boolean start(View view) {

        if (StateConstant.DRQState) {
            return false;
        }
        if (!isNetworkConnected(BaseApplication.getContext())) {
            Toast.show(BaseApplication.getContext(), "当前网络状态异常");
            return false;
        } else {
            StateConstant.DRQState = true;
        }
        return true;
    }

    public static void stop() {
        StateConstant.DRQState = false;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static void onError() {
        Toast.show(BaseApplication.getContext(), "主人您的网络异常，请稍后再试！么么哒！");
        EventBus.getDefault().post("网络请求失败");
    }

    public static void onSuccess() {
        EventBus.getDefault().post("网络请求成功");
    }
}  