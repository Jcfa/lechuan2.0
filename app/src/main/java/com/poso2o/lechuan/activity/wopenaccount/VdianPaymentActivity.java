package com.poso2o.lechuan.activity.wopenaccount;

import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.broadcast.wopenbroad.WeiXinFuWuReceived;
import com.poso2o.lechuan.broadcast.wopenbroad.WeiXinKaiReceived;
import com.poso2o.lechuan.configs.AppConfig;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.wopenaccountmanager.EmpowermentManager;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 服务订购支付
 *
 * Created by Administrator on 2018/3/14 0014.
 */
public class VdianPaymentActivity extends BaseActivity {

    /**
     * 支付类型、支付金额
     */
    private TextView vdian_payment_type, vdian_payment_money;

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
        final IWXAPI api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(AppConfig.WEIXIN_APPID);
        setTitle("服务订购");
        // 获取传过来的信息
        String service_id = getIntent().getStringExtra("service_id");
        String service_name = getIntent().getStringExtra("service_name");
        String amount = getIntent().getStringExtra("amount");
        vdian_payment_type.setText(service_name);
        vdian_payment_money.setText(amount);

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
            }

            @Override
            public void onFailed(int tag, String msg) {

            }
        });


        // 注册广播
        service_type = Integer.valueOf(getIntent().getStringExtra("service_type"));
        if (service_type == 4) {
            received = new WeiXinKaiReceived();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constant.BROADCAST_WEIXIN_TOP_UP);
            registerReceiver(received, intentFilter);
        } else if (service_type == 3) {
            fuWuReceived = new WeiXinFuWuReceived();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constant.BROADCAST_WEIXIN_TOP_UP);
            registerReceiver(fuWuReceived, intentFilter);
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
