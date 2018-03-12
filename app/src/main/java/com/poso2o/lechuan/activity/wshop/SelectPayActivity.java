package com.poso2o.lechuan.activity.wshop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.alipay.AlipayManager;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.PayEvent;
import com.poso2o.lechuan.bean.shopdata.ServiceBean;
import com.poso2o.lechuan.configs.AppConfig;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.manager.trading.TradingManager;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by mr zhang on 2017/12/8.
 * 选择支付方式
 */

public class SelectPayActivity extends BaseActivity implements View.OnClickListener {

    public static final String SERVICE_DATA = "service_deta";

    private Context context;

    //返回
    private ImageView service_back;

    //套餐图片
    private ImageView service_img;
    //套餐名称
    private TextView pay_service_name;
    //套餐费用
    private TextView pay_service_fee;

    //支付宝支付
    private LinearLayout pay_for_ali;
    //微信支付
    private LinearLayout pay_for_wechat;
    private ServiceBean mBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pay_for_service;
    }

    @Override
    protected void initView() {
        context = this;

        service_back = (ImageView) findViewById(R.id.service_back);
        service_img = (ImageView) findViewById(R.id.service_img);
        pay_service_name = (TextView) findViewById(R.id.pay_service_name);
        pay_service_fee = (TextView) findViewById(R.id.pay_service_fee);

        pay_for_ali = (LinearLayout) findViewById(R.id.pay_for_ali);
        pay_for_wechat = (LinearLayout) findViewById(R.id.pay_for_wechat);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        mBean = (ServiceBean) bundle.get(SERVICE_DATA);
        if (mBean == null) return;

        pay_service_name.setText(mBean.service_name + "--" + mBean.num + mBean.unit);
        pay_service_fee.setText("¥ " + mBean.amount);
    }

    @Override
    protected void initListener() {
        service_back.setOnClickListener(this);

        pay_for_ali.setOnClickListener(this);
        pay_for_wechat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.service_back:
                finish();
                break;
            case R.id.pay_for_ali:
                aliPayService();
                break;
            case R.id.pay_for_wechat:
                wxPayService();
                break;
        }
    }
    private boolean isPay=false;//是否发起了支付
    //跳转微信支付
    private void wxPayService() {
        WaitDialog.showLoaddingDialog(activity);
        TradingManager.getInstance().wxServicePay(this, mBean.service_id, new TradingManager.OnWXPayCallback() {

            @Override
            public void onSucceed(PayReq req) {
                isPay=true;
                Toast.show(activity, R.string.toast_open_wx_pay);
                IWXAPI api = WXAPIFactory.createWXAPI(activity, AppConfig.WEIXIN_APPID);
                // 微信支付只能通过广播回调
                api.sendReq(req);
            }

            @Override
            public void onFail(String failMsg) {
                Toast.show(activity, failMsg);
            }
        });
    }

    //跳转支付宝支付
    private void aliPayService() {
        WaitDialog.showLoaddingDialog(activity);
        TradingManager.getInstance().aliPayService(this, mBean.service_id, new TradingManager.OnAliPayCallback() {
            @Override
            public void onSucceed(String response) {
                isPay=true;
                AlipayManager alipay = AlipayManager.getAlipayManager();
                alipay.pay(activity, response);
            }

            @Override
            public void onFail(String failMsg) {
                Toast.show(activity, failMsg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 微信和支付宝支付成功的回调通知
     *
     * @param event
     */
    @Override
    public void onPayResult(PayEvent event) {
        if (event.code == PayEvent.SUCCESS) {//支付成功
            if (event.payType == PayEvent.WEIXIN_TYPE) {//微信

            } else if (event.payType == PayEvent.ALIPAY_TYPE) {//支付宝

            }
            if(isPay) {//
                if(mBean!=null){//购买服务成功后保存天数，否则需要重新登录才能生效
                    SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_SERVICE_DAYS, NumberUtils.toInt(mBean.num));
                }
                startActivity(MainActivity.class);//购买乐传服务支付成功，直接跳转到乐传首页
                finish();
            }
        }
    }
}
