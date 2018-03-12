package com.poso2o.lechuan.activity.official;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.alipay.AlipayManager;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.PayEvent;
import com.poso2o.lechuan.bean.oa.OaService;
import com.poso2o.lechuan.configs.AppConfig;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.oa.OaServiceManager;
import com.poso2o.lechuan.util.Toast;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by mr zhang on 2018/2/2.
 *
 * 服务订购页面
 */

public class OaServiceActivity extends BaseActivity implements View.OnClickListener{

    public static final String OA_RUNNING_TYPE = "running_type";
    public static final String OA_SERVICE_DATA = "oa_service";
    public static final String OA_SERVICE_PRICE = "oa_service_price";

    //篇数
    private TextView oa_packages_num;
    //运营方式
    private TextView running_type;
    //购买时长
    private TextView packages_time;
    //应付金额
    private TextView oa_packages_total_amount;
    //同意协议
    private CheckBox agree_oa_pay;

    //运行模式:0是自运营，1是代运营
    private int oa_run_type = 1;
    //服务内容
    private OaService.Item oaService;
    //服务费用
    private String oaPrice = "0.00";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_oa_packages_detail;
    }

    @Override
    protected void initView() {
        oa_packages_num = (TextView) findViewById(R.id.oa_packages_num);
        running_type = (TextView) findViewById(R.id.running_type);
        packages_time = (TextView) findViewById(R.id.packages_time);
        oa_packages_total_amount = (TextView) findViewById(R.id.oa_packages_total_amount);
        agree_oa_pay = (CheckBox) findViewById(R.id.agree_oa_pay);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)return;
        oa_run_type = (int) bundle.get(OA_RUNNING_TYPE);
        oaService = (OaService.Item) bundle.get(OA_SERVICE_DATA);
        oaPrice = bundle.getString(OA_SERVICE_PRICE);
        oa_packages_num.setText(oaService.num + "篇");
        running_type.setText(oa_run_type == 0 ? "自主运营" : "代运营");
        packages_time.setText(oaService.unit);
        oa_packages_total_amount.setText(oaPrice);
    }

    @Override
    protected void initListener() {
        findViewById(R.id.oa_packages_back).setOnClickListener(this);
        findViewById(R.id.oa_pay_for_wechat).setOnClickListener(this);
        findViewById(R.id.oa_pay_for_ali).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.oa_packages_back:
                finish();
                break;
            case R.id.oa_pay_for_wechat:
                //微信支付
                payByWechat();
                break;
            case R.id.oa_pay_for_ali:
                //支付宝支付
                payByAli();
                break;
        }
    }

    //微信支付
    private void payByWechat(){
        if (!agree_oa_pay.isChecked()){
            Toast.show(this,"请阅读爱乐传协议并同意后再进行操作");
            return;
        }
        showLoading();
        OaServiceManager.getOaServiceManager().wechatService(this, oaService.service_id, oa_run_type, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                Toast.show(OaServiceActivity.this,"正在跳转微信支付...");
                IWXAPI api = WXAPIFactory.createWXAPI(activity, AppConfig.WEIXIN_APPID);
                // 微信支付只能通过广播回调
                api.sendReq((PayReq)result);
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(OaServiceActivity.this,msg);
            }
        });
    }

    //支付宝支付
    private void payByAli(){
        if (!agree_oa_pay.isChecked()){
            Toast.show(this,"请阅读爱乐传协议并同意后再进行操作");
            return;
        }
        showLoading();
        OaServiceManager.getOaServiceManager().aliService(this, oaService.service_id, oa_run_type, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                AlipayManager alipay = AlipayManager.getAlipayManager();
                alipay.pay(activity, (String)result);
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(OaServiceActivity.this,msg);
            }
        });
    }

    @Override
    public void onPayResult(PayEvent event) {
        if (event.code == PayEvent.SUCCESS) {//支付成功
            if (event.payType == PayEvent.WEIXIN_TYPE) {//微信

            } else if (event.payType == PayEvent.ALIPAY_TYPE) {//支付宝

            }
            setResult(RESULT_OK);
            finish();
        }else {
            Toast.show(this,"支付失败");
        }
    }
}
