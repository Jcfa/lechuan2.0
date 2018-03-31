package com.poso2o.lechuan.activity.vdian;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.oa.OAHelperActivity;
import com.poso2o.lechuan.activity.realshop.RShopMainActivity;
import com.poso2o.lechuan.activity.wshop.WShopActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.PayEvent;
import com.poso2o.lechuan.bean.mine.InvitationBean;
import com.poso2o.lechuan.bean.mine.UserInfoBean;
import com.poso2o.lechuan.broadcast.wopenbroad.WeiXinFuWuReceived;
import com.poso2o.lechuan.broadcast.wopenbroad.WeiXinKaiReceived;
import com.poso2o.lechuan.configs.AppConfig;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.SetupBindAccountsDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.vdian.EmpowermentManager;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
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
    private SetupBindAccountsDialog bindAccountsDialog;
    /**
     * 微信支付
     */
    private TextView vdian_payment_wechat;
    //    private WeiXinKaiReceived received;
//    private WeiXinFuWuReceived fuWuReceived;
//    private int service_type;
    private int mModuleId;//微店或公众号助手
    private boolean mPaymentSuccess = false;//是否支付成功

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_vdian_payment;
    }

    @Override
    protected void initView() {
        mModuleId = getIntent().getIntExtra(AuthorizationActivity.KEY_MODULE_ID, 0);
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
        final int service_id = getIntent().getIntExtra("service_id", 0);
        String service_name = getIntent().getStringExtra("service_name");
        double amount = getIntent().getDoubleExtra("amount", 0.00);
        vdian_payment_type.setText(service_name);
        vdian_payment_money.setText(NumberUtils.format2(amount));
        vdian_payment_wechat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                weixinPayment(service_id);
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onPayResult(PayEvent event) {
        if (event.code == PayEvent.SUCCESS) {
            mPaymentSuccess = true;
            //支付成功，是否绑定过收款帐号，未绑定提示绑定
            if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_BIND_WX_ACCOUNT) != 1) {
                showSetupAccountDialog();
            } else {
                goBack();
            }
//            if (service_type == 4) {
//                Intent i = new Intent();
//                i.setClass(activity, EmpowermentActivity.class);
//                startActivity(i);
//            } else if (service_type == 3) {
//                Intent intent = new Intent();
//                intent.putExtra(RShopMainActivity.DATA_SHOP, "");
//                intent.setClass(activity, WShopActivity.class);
//                startActivity(intent);
//            }
        } else {
            mPaymentSuccess = false;
        }
    }

    private void weixinPayment(int service_id) {
        showLoading();
        // 发起微信支付
        EmpowermentManager.getInstance().trialTranslateDate(activity, service_id + "", new IRequestCallBack<String>() {
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

    /**
     * 显示设置收款帐号
     */
    private void showSetupAccountDialog() {
        bindAccountsDialog = new SetupBindAccountsDialog(activity);
        bindAccountsDialog.show(new SetupBindAccountsDialog.Callback() {
            @Override
            public void onResult() {
            }

            @Override
            public void onCancel() {
                goBack();
            }
        });
    }

    private boolean isActiv = true;

    @Override
    public void onResume() {
        super.onResume();
        if (!isActiv) {//从后台返回前台，即微信扫码绑定收款帐号后检测是否绑定成功
            getAccountDetail();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isActiv = false;
    }

    /**
     * 帐户详情
     */
    private void getAccountDetail() {
        WShopManager.getrShopManager().getlcAccountDetailInfo(activity, new IRequestCallBack<UserInfoBean>() {
            @Override
            public void onResult(int tag, UserInfoBean result) {
                if (result.has_bank_binding == 1) {//已绑定收款帐号
                    if (bindAccountsDialog != null) {
                        bindAccountsDialog.dismiss();
                    }
                    goBack();
                }
            }

            @Override
            public void onFailed(int tag, String msg) {

            }
        });
    }

//    @Override
//    public void onPushMessageEvent(InvitationBean event) {
//        if (event.code.equals(InvitationBean.BIND_WX_ACCOUNT_CODE)) {//绑定成功
//            goBack();
//        }
//    }

    private void goBack() {
        if (mModuleId == Constant.BOSS_MODULE_WX) {
            startActivity(VdianActivity.class);
        } else if (mModuleId == Constant.BOSS_MODULE_OA) {
            startActivity(OAHelperActivity.class);
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mPaymentSuccess) {
            goBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
