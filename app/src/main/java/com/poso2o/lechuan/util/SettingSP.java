package com.poso2o.lechuan.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.poso2o.lechuan.base.BaseApplication;

/**
 * 退出登录不清除的数据
 * Created by Administrator on 2017-12-12.
 */

public class SettingSP {
    public static final String SP_NAME = "setting";
    private static SharedPreferences mSharedPreferences = BaseApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    private static SharedPreferences.Editor mEditor = mSharedPreferences.edit();

    public static final String KEY_USER_DAY = "day";//结算日是否当天提示过了

    public static void putSettleAccountsDayInt(int day) {
        if (mEditor == null) {
            return;
        }
        String uid = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        mEditor.putInt(uid + KEY_USER_DAY, day);
    }

    public static int getSettleAccountsDayInt() {
        String uid = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        return mSharedPreferences.getInt(uid + KEY_USER_DAY, 0);
    }

}
