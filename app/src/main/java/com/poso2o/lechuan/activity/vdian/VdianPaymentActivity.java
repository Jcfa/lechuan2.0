package com.poso2o.lechuan.activity.vdian;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.realshop.RShopMainActivity;
import com.poso2o.lechuan.activity.wshop.WShopActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.PayEvent;
import com.poso2o.lechuan.broadcast.wopenbroad.WeiXinFuWuReceived;
import com.poso2o.lechuan.broadcast.wopenbroad.WeiXinKaiReceived;
import com.poso2o.lechuan.configs.AppConfig;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.wopenaccountmanager.EmpowermentManager;
import com.poso2o.lechuan.tool.print.Print;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 服务订购支付
 * <p>
 * Created by Administrator on 2018/3/14 0014.
 */
public class VdianPaymentActivity extends BaseActivity {
    /**
     * 支付类型、支付金额
     */
    private TextView vdian_payment_type, vdian_payment_money;
    private IWXAPI api;
    /**
     * 微信支付
     */
    private TextView vdian_payment_wechat;
    private WeiXinKaiReceived received;
    private WeiXinFuWuReceived fuWuReceived;
    private int service_type;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_vdian_payment;
    }

    @Override
    protected void initView() {
        vdian_payment_money = (TextView) findViewById(R.id.vdian_payment_money);
        vdian_payment_type = (TextView) findViewById(R.id.vdian_payment_type);
        vdian_payment_wechat = (TextView) findViewById(R.id.vdian_payment_wechat);
    }

    @Override
    protected void initData() {
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(AppConfig.WEIXIN_APPID);
        setTitle("服务订购");
        // 获取传过来的信息
        final String service_id = getIntent().getStringExtra("service_id");
        String service_name = getIntent().getStringExtra("service_name");
        String amount = getIntent().getStringExtra("amount");
        vdian_payment_type.setText(service_name);
        vdian_payment_money.setText(amount);
        vdian_payment_wechat.setOnClickListener(new View.OnClickListener() {

        // 发起微信支付
        EmpowermentManager.getInstance().trialTranslateDate(this, service_id, new IRequestCallBack() {

            @Override
            public void onResult(int tag, final Object result) {
                vdian_payment_wechat.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            JSONObject json = new JSONObject(result.toString());

                            PayReq req = new PayReq();
                            req.appId = json.getString("appid");
                            req.partnerId = json.getString("partnerid");
                            req.prepayId = json.getString("prepayid");
                            req.nonceStr = json.getString("noncestr");
                            req.timeStamp = json.getString("timestamp");
                            req.packageValue = json.getString("package");
                            req.sign = json.getString("sign");

                            api.sendReq(req);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            public void onClick(View v) {
                weixinPayment(service_id);
            }
        });
        // 注册广播
        service_type = Integer.valueOf(getIntent().getStringExtra("service_type"));
    }

    @Override
    protected void initListener() {
    }

    @Override
    public void onPayResult(PayEvent event) {
        if (event.code == PayEvent.SUCCESS) {
            if (service_type == 4) {
                Intent i = new Intent();
                i.setClass(activity, EmpowermentActivity.class);
                startActivity(i);
            } else if (service_type == 3) {
                Intent intent = new Intent();
                intent.putExtra(RShopMainActivity.DATA_SHOP, "");
                intent.setClass(activity, WShopActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void weixinPayment(String service_id) {
        showLoading();
        // 发起微信支付
        EmpowermentManager.getInstance().trialTranslateDate(this, service_id, new IRequestCallBack<String>() {
            @Override
            public void onResult(int tag, final String result) {
                dismissLoading();
                try {
                    JSONObject json = new JSONObject(result);
                    Print.println("trialTranslateDate_json:" + result);
                    PayReq req = new PayReq();
                    req.appId = json.getString("appid");
                    req.partnerId = json.getString("partnerid");
                    req.prepayId = json.getString("prepayid");
                    req.nonceStr = json.getString("noncestr");
                    req.timeStamp = json.getString("timestamp");
                    req.packageValue = json.getString("package");
                    req.sign = json.getString("sign");

                    api.sendReq(req);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销动态广播
        if (service_type == 3) {
            unregisterReceiver(fuWuReceived);
        } else if (service_type == 4) {
            unregisterReceiver(received);
        }
    }
}
