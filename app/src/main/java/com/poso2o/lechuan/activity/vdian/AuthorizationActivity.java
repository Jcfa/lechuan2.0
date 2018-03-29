package com.poso2o.lechuan.activity.vdian;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.oa.OAHelperActivity;
import com.poso2o.lechuan.activity.wshop.WCAuthorityActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SettingSP;
import com.poso2o.lechuan.util.SharedPreferencesUtils;

/**
 * 授权说明
 * <p>
 * Created by Administrator on 2018/3/14 0014.
 */
public class AuthorizationActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_SERVICE_PURCHASE = 11;
    private static final int REQUEST_WEIXIN_BIND = 12;
    public static final String KEY_SERVICE_ID = "service_id";//购买的服务
    public static final String KEY_MODULE_ID = "module_id";//1=微店、2=公众号助手
    /**
     * 提示文本
     */
    private TextView authorization_hint_01, authorization_hint_02;
    /**
     * 点击公众号授权
     */
    private Button authorization_to_empower;
    private int mServiceId = 0;
    private int mModuleId = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_authorization;
    }

    @Override
    protected void initView() {
//        mServiceId = getIntent().getIntExtra(KEY_SERVICE_ID, 0);
        mServiceId = SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SERVICE_ID_OA, 0);
        mModuleId = getIntent().getIntExtra(KEY_MODULE_ID, 0);
        authorization_hint_01 = (TextView) findViewById(R.id.authorization_hint_01);
        authorization_hint_02 = (TextView) findViewById(R.id.authorization_hint_02);
        authorization_to_empower = (Button) findViewById(R.id.authorization_to_empower);
    }

    @Override
    protected void initData() {
        setTitle("授权说明");
        String w = "-日进斗金老板管理app <font color='#FF0000'>不会</font> 将您的公众号登陆账号和密码上传到服务器。";
        String s = "-删除app后，日进斗金服务端<font color='#FF0000'>不会保留您的隐私信息。</font>";
        authorization_hint_01.setText(Html.fromHtml(w));
        authorization_hint_02.setText(Html.fromHtml(s));
    }

    @Override
    protected void initListener() {
        authorization_to_empower.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.authorization_to_empower:// 进入授权页面
                Intent intent = new Intent();
                intent.setClass(AuthorizationActivity.this, WCAuthorityActivity.class);
                intent.putExtra(WCAuthorityActivity.BIND_TYPE, mModuleId == Constant.BOSS_MODULE_OA ? 1 : 0);
                startActivityForResult(intent, REQUEST_WEIXIN_BIND);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_WEIXIN_BIND:// 微信绑定
                    if (mModuleId == Constant.BOSS_MODULE_OA) {//返回公众号助手
                        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SERVICE_DAYS_OA, 0) > 0 && SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SERVICE_DAYS_OA, 0) > 0) {//有公众号助手服务权限&&未到期
                            startActivity(OAHelperActivity.class);
                        } else {//服务订购升级
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(ServiceOrderingActivity.IS_TRY, true);
//                            bundle.putInt(KEY_SERVICE_ID, mServiceId);
                            bundle.putInt(KEY_MODULE_ID, mModuleId);
                            startActivityForResult(ServiceOrderingActivity.class, bundle, REQUEST_SERVICE_PURCHASE);
                        }
                    } else if (mModuleId == Constant.BOSS_MODULE_WX) {//返回微店
                        if (mServiceId > 0 && SharedPreferencesUtils.getOACompetence()) {//已订购服务的授权成功直接进入微店&&未到期
                            startActivity(VdianActivity.class);
                        } else {//未订购的授权成功进入服务订购页
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(ServiceOrderingActivity.IS_TRY, true);
//                            bundle.putInt(KEY_SERVICE_ID, mServiceId);
                            bundle.putInt(KEY_MODULE_ID, mModuleId);
                            startActivityForResult(ServiceOrderingActivity.class, bundle, REQUEST_SERVICE_PURCHASE);
                        }
                    }
                    finish();
                    break;
                case REQUEST_SERVICE_PURCHASE:// 服务订购
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }
}