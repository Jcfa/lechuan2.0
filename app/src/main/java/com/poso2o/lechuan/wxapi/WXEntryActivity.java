package com.poso2o.lechuan.wxapi;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.activity.login.RegisterActivity;
import com.poso2o.lechuan.activity.realshop.RShopMainActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseApplication;
import com.poso2o.lechuan.bean.login.LoginBean;
import com.poso2o.lechuan.bean.login.WXAccessTokenInfo;
import com.poso2o.lechuan.bean.login.WXUserInfo;
import com.poso2o.lechuan.configs.AppConfig;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.constant.BroadcastAction;
import com.poso2o.lechuan.dialog.UserMultipleDialog;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.main.UserDataManager;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static BaseActivity baseActivity;
    public static final int LOGIN_MODE = 101, BIND_MODE = 102;//登录、绑定
    private static int mMode = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.entry);
//
//        // 通过WXAPIFactory工厂，获取IWXAPI的实例
//        api = WXAPIFactory.createWXAPI(this, AppConfig.WEIXIN_APPID, false);
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            boolean bool = BaseApplication.getIWXAPI().handleIntent(getIntent(), this);
            if (!bool) {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        BaseApplication.getIWXAPI().handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:// 获取code
                Intent intent_bb = new Intent(BroadcastAction.WX_SUCCESS_BRODCAST);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent_bb);
                String code = ((SendAuth.Resp) resp).code;
                // 通过code获取授权口令access_token
                getAccessToken(code);


                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                finish();
                result = "用户取消授权";

                Intent intent = new Intent(BroadcastAction.WX_CANCEL_BRODCAST);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                finish();
                result = "授权被拒绝";

                Intent intent_b = new Intent(BroadcastAction.WX_REFUSE_BRODCAST);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent_b);

                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                finish();
                result = "微信版本不支持";

                Intent intent_c = new Intent(BroadcastAction.WX_NONSUPPORT_BRODCAST);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent_c);

                break;
            default:
                finish();
                result = "未知错误";

                Intent intent_d = new Intent(BroadcastAction.WX_UNKNOWN_ERREOR_BRODCAST);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent_d);

                break;
        }
        //Toast.show(this, result);
    }

    /**
     * 登录
     *
     * @param baseActivity
     */
    public static void loginWeiXin(BaseActivity baseActivity) {
        mMode = LOGIN_MODE;
        WXEntryActivity.baseActivity = baseActivity;
        // 判断是否安装了微信客户端
//        if (!api.isWXAppInstalled()) {
//            Toast.makeText(baseActivity, "您还未安装微信客户端！", Toast.LENGTH_SHORT).show();
//            return;
//        }
        // 发送授权登录信息，来获取code
        SendAuth.Req req = new SendAuth.Req();
        // 应用的作用域，获取个人信息
        req.scope = "snsapi_userinfo";
        /**
         * 用于保持请求和回调的状态，授权请求后原样带回给第三方
         * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来校验
         */
        req.state = "app_wechat";
        boolean bool = BaseApplication.getIWXAPI().sendReq(req);
        if (!bool) {
            WaitDialog.dismissLoaddingDialog();
        }
    }

    /**
     * 绑定
     *
     * @param baseActivity
     */
    public static void bindWeiXin(BaseActivity baseActivity) {
        mMode = BIND_MODE;
        WXEntryActivity.baseActivity = baseActivity;
        // 判断是否安装了微信客户端
//        if (!api.isWXAppInstalled()) {
//            Toast.makeText(baseActivity, "您还未安装微信客户端！", Toast.LENGTH_SHORT).show();
//            return;
//        }
        // 发送授权登录信息，来获取code
        SendAuth.Req req = new SendAuth.Req();
        // 应用的作用域，获取个人信息
        req.scope = "snsapi_userinfo";
        /**
         * 用于保持请求和回调的状态，授权请求后原样带回给第三方
         * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来校验
         */
        req.state = "app_wechat";
        BaseApplication.getIWXAPI().sendReq(req);
    }

    /**
     * 获取授权口令
     */
    private void getAccessToken(String code) {
//        WaitDialog.updateMessage("正在获取微信授权...");
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + AppConfig.WEIXIN_APPID +
                "&secret=" + AppConfig.WEIXIN_APPSECRET +
                "&code=" + code +
                "&grant_type=authorization_code";
        // 网络请求获取access_token
        Log.i("getAccessToken", "wxlogin_getAccessToken_url=" + url);
        Request<String> request = NoHttp.createStringRequest(url);
        baseActivity.request(0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Log.i("getAccessToken", "wxlogin_getAccessToken=" + response);
                Logger.e(response);
                // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                processGetAccessTokenResult(response);
            }

            @Override
            public void onFailed(int what, String response) {
            }
        }, true, false);
    }

    /**
     * 处理获取的授权信息结果
     *
     * @param response 授权信息结果
     */
    private void processGetAccessTokenResult(String response) {
        Gson gson = new Gson();
        // 验证获取授权口令返回的信息是否成功
        if (validateSuccess(response)) {
            // 使用Gson解析返回的授权口令信息
            WXAccessTokenInfo tokenInfo = gson.fromJson(response, WXAccessTokenInfo.class);
            Logger.e(tokenInfo.toString());
//            // 保存信息到手机本地
//            saveAccessInfotoLocation(tokenInfo);
            // 获取用户信息
            getWXUserInfo(tokenInfo.getAccess_token(), tokenInfo.getOpenid());
        } else {
//            // 授权口令获取失败，解析返回错误信息
//            WXErrorInfo wxErrorInfo = gson.fromJson(response, WXErrorInfo.class);
//            Logger.e(wxErrorInfo.toString());
//            // 提示错误信息
            Toast.show(this, "微信授权失败！");
        }
    }


    /**
     * 获取微信用户信息
     *
     * @param access_token
     * @param openid
     */
    private void getWXUserInfo(String access_token, String openid) {
//        WaitDialog.updateMessage("正在获取微信用户信息...");
        String url = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + access_token +
                "&openid=" + openid;
        Request<String> request = NoHttp.createStringRequest(url);
        baseActivity.request(0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                // 解析获取的用户信息
                Log.i("getUserInfo", "wxlogin_getUserInfo=" + response);
                Gson gson = new Gson();
                WXUserInfo userInfo = gson.fromJson(response, WXUserInfo.class);
                if (mMode == LOGIN_MODE) {//微信登录
                    weixinLogin(userInfo);
                } else if (mMode == BIND_MODE) {
                    UserDataManager.getUserDataManager().weixinBind(baseActivity, userInfo.getOpenid(), new IRequestCallBack() {
                        @Override
                        public void onResult(int tag, Object result) {
                            finish();
                        }

                        @Override
                        public void onFailed(int tag, String msg) {
                            finish();
                        }
                    });
                    finish();
                }
            }

            @Override
            public void onFailed(int what, String response) {
            }
        }, true, true);
    }

    /**
     * 微信登录
     */
    private void weixinLogin(final WXUserInfo userInfo) {
        WaitDialog.updateMessage("正在登录...");
        UserDataManager.getUserDataManager().weixinLogin(baseActivity, userInfo.getOpenid(), new IRequestCallBack<LoginBean>() {

            @Override
            public void onFailed(int tag, String object) {
                WaitDialog.dismissLoaddingDialog();
                Bundle bundle = new Bundle();
                bundle.putSerializable(RegisterActivity.KEY_OPENID, userInfo);
                baseActivity.startActivity(new Intent(baseActivity, RegisterActivity.class).putExtras(bundle));
                finish();
            }

            @Override
            public void onResult(int tag, LoginBean object) {
                WaitDialog.dismissLoaddingDialog();
//                baseActivity.startActivity(new Intent(baseActivity, MainActivity.class));
//                finish();
                switch (object.user_type) {
                    case Constant.MERCHANT_TYPE://商家
                        toRShopMainActivity();
                        break;
                    case Constant.DISTRIBUTION_TYPE://分销员
                        toMainActivity();
                        break;
                    case Constant.COMMON_TYPE://普通用户
                        /**
                         * 商家+分销员的帐户要选择登录商家还是分销
                         */
                        showMultipleDialog();
                        break;
//                    case Constant.MULTIPLE_TYPE://商家+分销员
//                        /**
//                         * 商家+分销员的帐户要选择登录商家还是分销
//                         */
//                        showMultipleDialog();
//                        break;
                }
            }
        });
    }


    /**
     * 乐传首页
     */
    private void toMainActivity() {
        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE, Constant.DISTRIBUTION_TYPE);//保存当前登录身份为分销员
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * 老板管理首页
     */
    private void toRShopMainActivity() {
        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE, Constant.MERCHANT_TYPE);//保存当前登录身份为商家
        startActivity(new Intent(this, RShopMainActivity.class));
        finish();
    }

    /**
     * 选择商家或分销员
     */
    private void showMultipleDialog() {
        UserMultipleDialog dialog = new UserMultipleDialog(this, new UserMultipleDialog.IChooseCallback() {
            @Override
            public void merchant() {
                toRShopMainActivity();
            }

            @Override
            public void distribution() {
                toMainActivity();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                finish();
            }
        });
        dialog.show();
    }


    /**
     * 验证是否成功
     *
     * @param response 返回消息
     * @return 是否成功
     */
    private boolean validateSuccess(String response) {
        String errFlag = "errmsg";
        return (errFlag.contains(response) && !"ok".equals(response))
                || (!"errcode".contains(response) && !errFlag.contains(response));
    }
}