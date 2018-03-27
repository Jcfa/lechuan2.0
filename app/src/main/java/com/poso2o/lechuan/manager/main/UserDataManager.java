package com.poso2o.lechuan.manager.main;

import android.util.Log;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.login.LoginBean;
import com.poso2o.lechuan.bean.login.WXUserInfo;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.wxapi.WXEntryActivity;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by Administrator on 2017-11-25.
 */

public class UserDataManager<T> extends BaseManager {
    //    private BaseActivity activity;
    public static UserDataManager mUserDataManager;
    /**
     * 登录标识
     */
    private final int TAG_LOGIN_ID = 101;
    /**
     * 注册标识
     */
    private final int TAG_REGISTER_ID = 102;
    /**
     * 注册验证码
     */
    private final int TAG_VERIFICATION_ID = 103;
    /**
     * 微信登录
     */
    private final int TAG_WEIXIN_LOGIN_ID = 104;
    /**
     * 微信绑定
     */
    private final int TAG_WEIXIN_BIND_ID = 105;
    /**
     * 重置密码
     */
    private final int TAG_RESET_PASSWORD_ID = 106;
    /**
     * 修改密码
     */
    private final int TAG_UPDATE_PASSWORD_ID = 107;

    private UserDataManager() {
    }


    public static UserDataManager getUserDataManager() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager();
        }
        return mUserDataManager;
    }

    /**
     * 登录
     *
     * @param baseActivity
     * @param account      登录帐号
     * @param password     登录密码
     * @param remember     是否记住密码
     * @param callBack     登录回调
     */
    public void doLogin(final BaseActivity baseActivity, final String account, final String password, final boolean remember, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.FZ_LOGIN_API);
//        request.add("uid", account);
//        request.add("key", password);
        request.add("loginname", account);
        request.add("password", password);
        baseActivity.request(TAG_LOGIN_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
//                Log.i("doLogin", "doLogin_response:" + response);
                Gson gson = new Gson();
                LoginBean loginBean = gson.fromJson(response, LoginBean.class);
//                if (checkResult(baseActivity, loginBean)) {//登录成功
//                    loginBean.data.account = account;
//                loginBean.password = remember ? password : "";
                if (remember) {
                    SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_REMEMBER_PASSWORD, remember ? password : "");
                }
                saveUserInfo(loginBean);
                callBack.onResult(TAG_LOGIN_ID, loginBean);
                Toast.show(baseActivity, "登录成功！");
                doRegisterToPhysical(baseActivity, account, loginBean.password,loginBean.nick);
            }

            @Override
            public void onFailed(int what, String response) {
                Toast.show(baseActivity, response);
            }
        }, true, true);
    }

    /**
     * 登录成功后注册到实体店
     *
     * @param baseActivity
     * @param account
     * @param password
     */
    public void doRegisterToPhysical(final BaseActivity baseActivity, final String account, final String password,String nick) {
        Request<String> request = getStringRequest(HttpAPI.REGISTER_SHOP_API);
        request.add("uid", account);
        request.add("token", password);
        request.add("nick", nick);
        baseActivity.request(TAG_LOGIN_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {

            }

            @Override
            public void onFailed(int what, String response) {
                Toast.show(baseActivity, response);
            }
        }, true, true);
    }

    /**
     * 保存用户信息
     *
     * @param loginBean 登录信息
     */
    private void saveUserInfo(LoginBean loginBean) {
        SharedPreferencesUtils.saveLoginInfo(loginBean);//保存登录信息
//        if (!remember) {//不保存密码
//            SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_PASSWORD, "");
//        }
    }

    /**
     * 微信授权
     */
    public void weixinAuth(BaseActivity baseActivity) {
        WXEntryActivity.loginWeiXin(baseActivity);
    }

    /**
     * 微信绑定，微信提现需要先绑定
     */
    public void weixinBindAuth(BaseActivity baseActivity) {
        WXEntryActivity.bindWeiXin(baseActivity);
    }

    /**
     * 微信登录
     */
    public void weixinLogin(final BaseActivity baseActivity, String openId, final IRequestCallBack<LoginBean> callBack) {
        Request<String> request = getStringRequest(HttpAPI.WEIXIN_LOGIN_API);
        request.add("openid", openId);
//        Log.i("weixinLogin", "weixinLogin_url=" + HttpAPI.WEIXIN_LOGIN_API + "&openid=" + openId);
        baseActivity.request(TAG_WEIXIN_LOGIN_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                LoginBean loginBean = gson.fromJson(response, LoginBean.class);
                if (loginBean != null) {//登录成功
                    saveUserInfo(loginBean);
                    callBack.onResult(what, loginBean);
                    Toast.show(baseActivity, "登录成功！");
                } else {//未绑定帐号，去注册页面绑定注册
                    callBack.onFailed(what, response);
                }
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);

    }

    /**
     * 微信绑定
     *
     * @param baseActivity
     * @param openId
     * @param
     */
    public void weixinBind(final BaseActivity baseActivity, String openId, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.WEIXIN_BIND_API);
        request = defaultParam(request);
        request.add("openid", openId);
//        Log.i("weixinBind", "weixinBind_url=" + HttpAPI.WEIXIN_BIND_API + "&openid=" + openId);
        baseActivity.request(TAG_WEIXIN_BIND_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Toast.show(baseActivity, "微信绑定成功！");
                callBack.onResult(what, response);
            }

            @Override
            public void onFailed(int what, String response) {
                Toast.show(baseActivity, "绑定失败：" + response);
                callBack.onFailed(what, response);
            }
        }, true, true);

    }

    /**
     * 注册
     *
     * @param baseActivity
     * @param account
     * @param password
     * @param userInfo     微信用户信息
     * @param callBack
     */
    public void doRegister(final BaseActivity baseActivity, String account, String password, String verificationCode, WXUserInfo userInfo, final IRequestCallBack callBack) {
//        activity = baseActivity;
        Request<String> request = getStringRequest(HttpAPI.REGISTER_API);
//        request = defaultParam(request);
        request.add("uid", account);
        request.add("key", password);
        if (userInfo != null) {
            request.add("nick", userInfo.getNickname());
            request.add("logo", userInfo.getHeadimgurl());
            request.add("openid", userInfo.getOpenid());
        }
        request.add("verifyCode", verificationCode);

//        Log.i("doRegister", "doRegister_url=" + HttpAPI.REGISTER_API + "&uid=" + account + "&key=" + password + "&openid=" + openid + "&verifyCode=" + verificationCode);
        baseActivity.request(TAG_REGISTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
//                Log.i("doRegister", "doRegister_response:" + response);
                Gson gson = new Gson();
                LoginBean loginBean = gson.fromJson(response, LoginBean.class);
                SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE, loginBean.user_type);
                saveUserInfo(loginBean);
                callBack.onResult(TAG_REGISTER_ID, loginBean);
//                } else {
//                    Toast.show(baseActivity, loginBean.msg);
//                }
            }

            @Override
            public void onFailed(int what, String response) {
                Toast.show(baseActivity, response);
            }
        }, true, true);
    }

    /**
     * 重置密码
     *
     * @param baseActivity
     * @param account
     * @param password
     * @param verificationCode
     * @param callBack
     */
    public void resetPassowrd(final BaseActivity baseActivity, String account, String password, String verificationCode, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.RESET_PASSWORD_API);
        request.add("mobile", account);
        request.add("key", password);
        request.add("verifyCode", verificationCode);
        baseActivity.request(TAG_RESET_PASSWORD_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
//                Log.i("doRegister", "doRegister_response:" + response);
                Toast.show(baseActivity, "密码重置成功！");
                Gson gson = new Gson();
                LoginBean loginBean = gson.fromJson(response, LoginBean.class);
                saveUserInfo(loginBean);
                callBack.onResult(what, loginBean);
            }

            @Override
            public void onFailed(int what, String response) {
                Toast.show(baseActivity, response);
            }
        }, true, true);
    }

    /**
     * 修改密码
     *
     * @param baseActivity
     * @param account
     * @param passwordOld
     * @param passwordNew
     * @param callBack
     */
    public void updatePassowrd(final BaseActivity baseActivity, String account, String passwordOld, String passwordNew, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.UPDATE_PASSWORD_API);
        request = defaultParam(request);
        request.add("mobile", account);
        request.add("old_key", passwordOld);
        request.add("new_key", passwordNew);
        baseActivity.request(TAG_UPDATE_PASSWORD_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
//                Log.i("doRegister", "doRegister_response:" + response);
                Toast.show(baseActivity, "修改密码成功！");
//                Gson gson = new Gson();
//                LoginBean loginBean = gson.fromJson(response, LoginBean.class);
//                saveUserInfo(loginBean);
                callBack.onResult(what, response);
            }

            @Override
            public void onFailed(int what, String response) {
                Toast.show(baseActivity, response);
            }
        }, true, true);
    }

    /**
     * 获取注册验证码
     *
     * @param baseActivity
     * @param account
     * @param callBack
     */
    public void getVerificationCode(final BaseActivity baseActivity, String account, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.VERIFICATION_CODE_API);
        request.add("mobile", account);
        baseActivity.request(TAG_VERIFICATION_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                callBack.onResult(TAG_VERIFICATION_ID, response);
                Toast.show(baseActivity, "发送成功！");
            }

            @Override
            public void onFailed(int what, String response) {
                Toast.show(baseActivity, "发送失败:" + response);
            }
        }, true, true);
    }

}
