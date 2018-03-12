package com.poso2o.lechuan.activity.official;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.PayEvent;
import com.poso2o.lechuan.bean.oa.OaService;
import com.poso2o.lechuan.bean.oa.Template;
import com.poso2o.lechuan.bean.oa.TemplateGroup;
import com.poso2o.lechuan.configs.AppConfig;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.oa.OaServiceManager;
import com.poso2o.lechuan.util.ArithmeticUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.OaPackagesGroupView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by mr zhang on 2018/2/7.
 *
 * 购买模板服务
 */

public class ModelServiceActivity extends BaseActivity {

    //返回
    private ImageView model_service_back;

    //模板名称
    private TextView model_name;
    //模板套餐
    private LinearLayout model_packages;
    //套餐费用
    private TextView model_fee;
    //单位
    private TextView model_package_unit;

    //同意协议
    private CheckBox agree_model_pay;
    //协议
    private TextView model_protocol_love_spread;

    //微信支付
    private LinearLayout model_pay_for_wechat;
    //支付宝支付
    private LinearLayout model_pay_for_ali;

    private TemplateGroup templateGroup;

    private OaPackagesGroupView groupView;
    private OaService oaService;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_model_service_buy;
    }

    @Override
    protected void initView() {
        model_service_back = (ImageView) findViewById(R.id.model_service_back);

        model_name = (TextView) findViewById(R.id.model_name);
        model_packages = (LinearLayout) findViewById(R.id.model_packages);
        model_fee = (TextView) findViewById(R.id.model_fee);
        model_package_unit = (TextView) findViewById(R.id.model_package_unit);

        agree_model_pay = (CheckBox) findViewById(R.id.agree_model_pay);
        model_protocol_love_spread = (TextView) findViewById(R.id.model_protocol_love_spread);

        model_pay_for_wechat = (LinearLayout) findViewById(R.id.model_pay_for_wechat);
        model_pay_for_ali = (LinearLayout) findViewById(R.id.model_pay_for_ali);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)return;
        templateGroup = (TemplateGroup) bundle.get(Constant.DATA);
        if (templateGroup == null)return;
        model_name.setText(templateGroup.group_name);

        groupView = new OaPackagesGroupView(this,onPackageChangeListener);
        model_packages.addView(groupView.getRootView());

        initPackages();
    }

    @Override
    protected void initListener() {
        //返回
        model_service_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //微信支付
        model_pay_for_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wechatPay();
            }
        });
        //支付宝支付
        model_pay_for_ali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aliPay();
            }
        });
    }

    //时长点击回调
    private OaPackagesGroupView.OnPackageChangeListener onPackageChangeListener = new OaPackagesGroupView.OnPackageChangeListener() {
        @Override
        public void onPackageChange(OaService.Item item) {
            model_fee.setText("¥" + NumberFormatUtils.format(item.amount));
            model_package_unit.setText("元/" + item.unit);
        }
    };

    private void initPackages(){
        showLoading();
        OaServiceManager.getOaServiceManager().oaService(this, templateGroup.group_id, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                oaService = (OaService) result;
                if (oaService == null)return;
                groupView.setDatas(oaService.list);
                model_fee.setText("¥" + NumberFormatUtils.format(groupView.getSelectItem().amount));
                model_package_unit.setText("元/" + groupView.getSelectItem().unit);
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(ModelServiceActivity.this,msg);
            }
        });
    }

    //微信支付
    private void wechatPay(){
        if (!agree_model_pay.isChecked()){
            Toast.show(this,"请仔细阅读爱乐传协议并同意后再进行操作");
            return;
        }
        showLoading();
        OaServiceManager.getOaServiceManager().wechatService(this, groupView.getSelectItem().service_id, -1, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                Toast.show(ModelServiceActivity.this,"正在跳转微信支付...");
                IWXAPI api = WXAPIFactory.createWXAPI(activity, AppConfig.WEIXIN_APPID);
                // 微信支付只能通过广播回调
                api.sendReq((PayReq)result);
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(ModelServiceActivity.this,msg);
            }
        });
    }

    //支付宝支付
    private void aliPay(){
        if (!agree_model_pay.isChecked()){
            Toast.show(this,"请仔细阅读爱乐传协议并同意后再进行操作");
            return;
        }
        showLoading();
        OaServiceManager.getOaServiceManager().wechatService(this, groupView.getSelectItem().service_id, -1, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                Toast.show(ModelServiceActivity.this,"正在跳转微信支付...");
                IWXAPI api = WXAPIFactory.createWXAPI(activity, AppConfig.WEIXIN_APPID);
                // 微信支付只能通过广播回调
                api.sendReq((PayReq)result);
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(ModelServiceActivity.this,msg);
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
