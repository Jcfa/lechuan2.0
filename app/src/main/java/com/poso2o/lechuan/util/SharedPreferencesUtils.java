package com.poso2o.lechuan.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.poso2o.lechuan.base.BaseApplication;
import com.poso2o.lechuan.bean.login.LoginBean;
import com.poso2o.lechuan.bean.mine.UserInfoBean;

/**
 * Created by Administrator on 2017-11-25.
 */

public class SharedPreferencesUtils {
    public static final String SP_NAME = "UserInfo";
    private static SharedPreferences mSharedPreferences = BaseApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    private static SharedPreferences.Editor mEditor = mSharedPreferences.edit();

    /**
     * 用户ID
     */
    public static final String KEY_USER_ID = "uid";
    /**
     * 用户类型
     */
    public static final String KEY_USER_TYPE = "user_type";
    /**
     * 选择的用户类型，界面显示要区分用户类型时以这个标识为准
     */
    public static final String KEY_USER_SELECTED_TYPE = "selected_type";
    /**
     * 用户名
     */
    public static final String KEY_USER_ACCOUNT = "account";
    /**
     * 登录密码
     */
    public static final String KEY_USER_PASSWORD = "password";
    /**
     * 用户昵称
     */
    public static final String KEY_USER_NICK = "userNick";
    /**
     * 用户简介
     */
    public static final String KEY_USER_DESCRIPTION = "description";
    /**
     * 手户手机号
     */
    public static final String KEY_USER_MOBILE = "mobile";
    /**
     * 用户头像
     */
    public static final String KEY_USER_LOGO = "logo";
    /**
     * 相册封面图
     */
    public static final String KEY_USER_BACKBROUND_LOGO = "background_logo";
    /**
     * 乐传服务到期日期
     */
    public static final String KEY_USER_SERVICE_DATA = "service_date";
    /**
     * 乐传服务剩余天数
     */
    public static final String KEY_USER_SERVICE_DAYS = "service_days";
    /**
     * 是否开通过乐传服务7天试用
     */
    public static final String KEY_USER_LECHUAN_TRY = "has_lechuan_try";
    /**
     * token
     */
    public static final String KEY_USER_TOKEN = "token";
    /**
     * openId绑定微信的标识
     */
    public static final String KEY_USER_OPEN_ID = "openid";
    /**
     * 是否有实体店
     */
    public static final String KEY_USER_HAS_SHOP = "has_shop";
    /**
     * 是否有微店
     */
    public static final String KEY_USER_HAS_WEBSHOP = "has_webshop";
    /**
     * 企业认证状态
     */
    public static final String KEY_USER_SHOP_VERIFY = "has_shop_verify";
    /**
     * 是否设置佣金
     */
    public static final String KEY_USER_HAS_COMMISSION = "has_commission";
    /**
     * 专享折扣
     */
    public static final String KEY_USER_COMMISSION_DISCOUNT = "commission_discount";
    /**
     * 佣金比例
     */
    public static final String KEY_USER_COMMISSION_RATE = "commission_rate";
    /**
     * 红包余额
     */
    public static final String KEY_USER_RED_ENVELOPES_AMOUNT = "red_envelopes_amount";

    /**
     * 店铺类型：实体店，微店
     */
    public static final String KEY_SHOP_TYPE = "is_on_line";

    public static final String TAG_EXIT = "exit";

    /**
     * 微店店铺名称
     */
    public static final String KEY_V_SHOP_NAME = "v_shop_name";
    /**
     * 微店联系人名称
     */
    public static final String KEY_V_SHOP_CONTACTS = "v_shop_contacts";
    /**
     * 微店联系人手机号码
     */
    public static final String KEY_V_SHOP_PHONE = "v_shop_phone";
    /**
     * 微店电话
     */
    public static final String KEY_V_SHOP_TEL = "v_shop_tel";
    /**
     * 微店地址
     */
    public static final String KEY_V_SHOP_ADDRESS = "v_shop_address";

    /**
     * 公众号文章搜索历史
     */
    public static final String KEY_OA_ARTICLE_SEARCH_HISTORY = "oa_article_search_history";

    /**
     *  文章已选择的类型
     */
    public static final String KEY_OA_TYPES = "oa_types";

    /**
     *  文章已选择的标签
     */
    public static final String KEY_OA_LAYBELS = "oa_laybels";


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
     * 保存登录信息
     *
     * @param loginBean
     */
    public static void saveLoginInfo(LoginBean loginBean) {
        put(KEY_USER_NICK, loginBean.nick);//昵称
        put(KEY_USER_ID, loginBean.uid);
        put(KEY_USER_ACCOUNT, loginBean.account);//帐号
        put(KEY_USER_PASSWORD, loginBean.password);//密码
        put(KEY_USER_TYPE, loginBean.user_type);//int 用户类型，0=商家,1=分销商,2=商家+分销商
        put(KEY_USER_MOBILE, loginBean.mobile);//手机号
        put(KEY_USER_LOGO, loginBean.logo);//头像
        put(KEY_USER_BACKBROUND_LOGO, loginBean.background_logo);//相册封面图
        put(KEY_USER_TOKEN, loginBean.token);
        put(KEY_USER_OPEN_ID, loginBean.openid);
        put(KEY_USER_DESCRIPTION, loginBean.remark);//简介
        put(KEY_USER_SERVICE_DATA, loginBean.lechuan_service_date);//乐传服务到期日期
        put(KEY_USER_SERVICE_DAYS, loginBean.lechuan_service_days);//乐传服务剩余天数
        put(KEY_USER_LECHUAN_TRY, loginBean.has_lechuan_try);//是否开通乐传服务7天试用，0=未开通，1=已开通
        put(KEY_USER_TOKEN, loginBean.token);//token
        put(KEY_USER_SHOP_VERIFY, loginBean.has_shop_verify);//int 1=未认证，2=申请认证，3=认证通过，4=认证不通过
        put(KEY_USER_HAS_SHOP, loginBean.has_shop);//int 是否有实体店，0=无，1=有
        put(KEY_USER_HAS_WEBSHOP, loginBean.has_webshop);//int 是否有微店，0=无，1=有
    }

    /**
     * 保存用户详情信息
     */
    public static void saveUserInfo(UserInfoBean userInfo) {
        put(KEY_USER_NICK, userInfo.nick);//昵称
        put(KEY_USER_ID, userInfo.uid);
        put(KEY_USER_TYPE, userInfo.user_type);//用户类型
        put(KEY_USER_MOBILE, userInfo.mobile);//手机号
        put(KEY_USER_LOGO, userInfo.logo);//头像
        put(KEY_USER_BACKBROUND_LOGO, userInfo.background_logo);//相册封面图
        put(KEY_USER_DESCRIPTION, userInfo.remark);//简介
        put(KEY_USER_HAS_COMMISSION, userInfo.has_commission);//是否设置佣金
        put(KEY_USER_COMMISSION_DISCOUNT, userInfo.commission_discount);//专享折扣
        put(KEY_USER_COMMISSION_RATE, userInfo.commission_rate);//佣金比例
        put(KEY_USER_RED_ENVELOPES_AMOUNT, userInfo.red_envelopes_amount);//红包余额
        put(KEY_USER_TOKEN, userInfo.token);//token
        put(KEY_USER_OPEN_ID, userInfo.openid);
        put(KEY_USER_HAS_SHOP, userInfo.has_shop);//int 是否有实体店，0=无，1=有
        put(KEY_USER_HAS_WEBSHOP, userInfo.has_webshop);//int 是否有微店，0=无，1=有
    }

    /**
     * 退出登录，清除数据
     */
    public static void clear() {
        if (mEditor == null) {
            return;
        }
        mEditor.clear();
        mEditor.commit();
    }

    /**
     * 退出
     */
    public static void logout() {
        if (mEditor == null) {
            return;
        }
        /**
         * 清除所有数据，只保留手机帐号和密码
         */
        String phone = getString(KEY_USER_MOBILE);
        String pw = getString(KEY_USER_PASSWORD);
        clear();
        put(KEY_USER_MOBILE, phone);
        put(KEY_USER_PASSWORD, pw);
        mEditor.commit();
    }
}
