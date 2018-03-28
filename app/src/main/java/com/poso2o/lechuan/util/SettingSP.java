package com.poso2o.lechuan.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.poso2o.lechuan.base.BaseApplication;
import com.poso2o.lechuan.configs.Constant;

/**
 * 退出登录不清除的数据
 * Created by Administrator on 2017-12-12.
 */

public class SettingSP {
    public static final String SP_NAME = "setting";
    private static SharedPreferences mSharedPreferences = BaseApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    private static SharedPreferences.Editor mEditor = mSharedPreferences.edit();

    public static final String KEY_USER_DAY = "day";//结算日是否当天提示过了
    /**
     * 公众号是否已授权
     */
    public static final String KEY_USER_AUTHORIZATION_OA = "authorization_oa";

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
    public static void put(String key, Object object) {
        if (mEditor == null) {
            return;
        }
        if (object instanceof String) {
            mEditor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            mEditor.putInt(key, (Integer) object);
        } else if (object instanceof Long) {
            mEditor.putLong(key, (Long) object);
        } else if (object instanceof Boolean) {
            mEditor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            mEditor.putFloat(key, (Float) object);
        }
        mEditor.commit();
    }


    public static String getString(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public static int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public static int getInt(String key, int value) {
        return mSharedPreferences.getInt(key, value);
    }

    public static long getLong(String key) {
        return mSharedPreferences.getLong(key, 0);
    }

    public static float getFloat(String key) {
        return mSharedPreferences.getFloat(key, 0);
    }

    public static boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }
    /**
     * 根据帐号来获取授权状态，确保是当前帐号的授权状态
     *
     * @return
     */
    public static int getAuthorizationState() {
        String uid = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        return getInt(KEY_USER_AUTHORIZATION_OA + uid, Constant.AUTHORIZATION_OA_FALSE);
    }

    /**
     * 保存授权状态
     *
     * @param state
     */
    public static void setAuthorizationState(int state) {
        String uid = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        put(KEY_USER_AUTHORIZATION_OA + uid, state);
    }
}
