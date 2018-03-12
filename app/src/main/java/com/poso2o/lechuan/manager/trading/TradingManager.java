package com.poso2o.lechuan.manager.trading;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.util.LogUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * 交易管理
 * Created by Jaydon on 2017/12/6.
 */
public class TradingManager extends BaseManager {

    private static TradingManager tradingManager;

    public static TradingManager getInstance() {
        if (tradingManager == null) {
            synchronized (TradingManager.class) {
                if (tradingManager == null) {
                    tradingManager = new TradingManager();
                }
            }
        }
        return tradingManager;
    }

    /**
     * 微信充值支付
     */
    public void wxPay(final BaseActivity baseActivity, final String amount, final OnWXPayCallback callback) {
        Request<String> request = getStringRequest(HttpAPI.WEIXIN_PAY_API);
        request.add("amount", amount);
        defaultParam(request);
        baseActivity.request(111, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                LogUtils.i("response = " + response);
                if (TextUtil.isNotEmpty(response)) {
                    PayReq req = new PayReq();
                    try {
                        JSONObject data = new JSONObject(response);
                        req.appId = data.getString("appid");
                        req.nonceStr = data.getString("noncestr");
                        req.packageValue = data.getString("package");
                        req.partnerId = data.getString("partnerid");
                        req.prepayId = data.getString("prepayid");
                        req.sign = data.getString("sign");
                        req.timeStamp = data.getString("timestamp");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    callback.onSucceed(req);
                } else {
                    callback.onFail(baseActivity.getString(R.string.toast_wx_pay_fail));
                }
            }

            @Override
            public void onFailed(int what, String response) {
                callback.onFail(response);
            }
        }, true, false);
    }

    /**
     * 微信购买服务支付
     *
     * @param baseActivity
     * @param serviceId    购买服务ID
     * @param callback
     */
    public void wxServicePay(final BaseActivity baseActivity, final String serviceId, final OnWXPayCallback callback) {
        Request<String> request = getStringRequest(HttpAPI.WEIXIN_SERVICE_PAY_API);
        request.add("service_id", serviceId);
        request.add("shop_branch_id", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        defaultParam(request);
        baseActivity.request(111, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                LogUtils.i("response = " + response);
                if (TextUtil.isNotEmpty(response)) {
                    PayReq req = new PayReq();
                    try {
                        JSONObject data = new JSONObject(response);
                        req.appId = data.getString("appid");
                        req.nonceStr = data.getString("noncestr");
                        req.packageValue = data.getString("package");
                        req.partnerId = data.getString("partnerid");
                        req.prepayId = data.getString("prepayid");
                        req.sign = data.getString("sign");
                        req.timeStamp = data.getString("timestamp");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    callback.onSucceed(req);
                } else {
                    callback.onFail(baseActivity.getString(R.string.toast_wx_pay_fail));
                }
            }

            @Override
            public void onFailed(int what, String response) {
                callback.onFail(response);
            }
        }, true, false);
    }

    /**
     * 支付宝充值支付
     */
    public void aliPay(final BaseActivity baseActivity, final String amount, final OnAliPayCallback callback) {
        Request<String> request = getStringRequest(HttpAPI.ALIPAY_API);
        request.add("amount", amount);
        defaultParam(request);
        baseActivity.request(111, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                LogUtils.i("aliPay = " + response);
                if (response != null) {
                    callback.onSucceed(response);
                } else {
                    callback.onFail(baseActivity.getString(R.string.toast_wx_pay_fail));
                }
            }

            @Override
            public void onFailed(int what, String response) {
                callback.onFail(response);
            }
        }, true, false);
    }

    /**
     * 支付宝购买服务支付
     */
    public void aliPayService(final BaseActivity baseActivity, final String serviceId, final OnAliPayCallback callback) {
        Request<String> request = getStringRequest(HttpAPI.ALIPAY_SERVICE_API);
        request.add("service_id", serviceId);
        request.add("shop_branch_id", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        defaultParam(request);
        baseActivity.request(111, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                LogUtils.i("aliPay = " + response);
                if (response != null) {
                    callback.onSucceed(response);
                } else {
                    callback.onFail(baseActivity.getString(R.string.toast_wx_pay_fail));
                }
            }

            @Override
            public void onFailed(int what, String response) {
                callback.onFail(response);
            }
        }, true, false);
    }

    /**
     * 微信提现
     */
    public void weixinWithdrawal(final BaseActivity baseActivity, final String amount, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.WEIXIN_WITHDRAWAL_API);
        request.add("amount", amount);
        defaultParam(request);
        baseActivity.request(112, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Toast.show(baseActivity, "提现成功！");
                callBack.onResult(what, response);
            }

            @Override
            public void onFailed(int what, String response) {
                Toast.show(baseActivity, response);
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 微信支付回调
     */
    public interface OnWXPayCallback {

        void onSucceed(PayReq req);

        void onFail(String failMsg);
    }

    /**
     * 支付宝支付回调
     */
    public interface OnAliPayCallback {

        void onSucceed(String response);

        void onFail(String failMsg);
    }
}
