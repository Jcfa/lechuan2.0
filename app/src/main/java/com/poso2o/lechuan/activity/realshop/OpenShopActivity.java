package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.login.LoginActivity;
import com.poso2o.lechuan.activity.wshop.WCAuthorityActivity;
import com.poso2o.lechuan.activity.wshop.WShopDesActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.main.UserDataManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by mr zhang on 2017/12/8.
 */

public class OpenShopActivity extends BaseActivity implements View.OnClickListener{

    //跳转微店描述界面
    public static final int CODE_TO_DES = 10001;

    private Context context;

    //返回
    private ImageView open_shop_back;

    //开实体店
    private TextView open_r_shop;
    //开微店
    private TextView open_w_shop;
    //资质审核
    private TextView open_w_shop_state;
    //进店
    private TextView in_shop;
    private int vertify = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_open_shop;
    }

    @Override
    protected void initView() {
        context = this;

        open_shop_back = (ImageView) findViewById(R.id.open_shop_back);
        open_r_shop = (TextView) findViewById(R.id.open_r_shop);
        open_w_shop = (TextView) findViewById(R.id.open_w_shop);
        open_w_shop_state = (TextView) findViewById(R.id.open_w_shop_state);
        in_shop = (TextView) findViewById(R.id.in_shop);
    }

    @Override
    protected void initData() {
        initState();
    }

    @Override
    protected void initListener() {
        open_shop_back.setOnClickListener(this);

        open_r_shop.setOnClickListener(this);
        open_w_shop.setOnClickListener(this);
        in_shop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.open_shop_back:
                finish();
                break;
            case R.id.open_r_shop:
                Intent intent0 = new Intent();
                intent0.setClass(context,RShopDesActivity.class);
                startActivity(intent0);
                break;
            case R.id.open_w_shop:
                Intent intent = new Intent();
                if (vertify == 1){
                    intent.setClass(context,WShopDesActivity.class);
                    startActivityForResult(intent,CODE_TO_DES);
                }else if(vertify == 2){
                    intent.setClass(context,CompanyDefineActivity.class);
                    startActivity(intent);
                }else if (vertify == 4){
                    intent.setClass(context,CompanyDefineActivity.class);
                    startActivity(intent);
                }else {
                    intent.setClass(context,WShopDesActivity.class);
                    startActivityForResult(intent,CODE_TO_DES);
                }
                break;
            case R.id.in_shop:
                Intent w = new Intent();
                w.setClass(context,RShopMainActivity.class);
                startActivity(w);
                finish();
                break;
        }
    }

    private void initState(){
        vertify = SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SHOP_VERIFY);
        if (vertify == 1){

        }else if (vertify == 2){
            open_w_shop_state.setVisibility(View.VISIBLE);
            open_w_shop_state.setText("资质申请中...");
        }else if (vertify == 3){
            in_shop.setVisibility(View.VISIBLE);
        }else if (vertify == 4){
            open_w_shop_state.setVisibility(View.VISIBLE);
            open_w_shop_state.setText("认证不通过");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)return;
        if (requestCode == CODE_TO_DES){
            reLogin();
        }
    }

    //提交认证成功，重新登录获取账号信息
    private void reLogin(){
        showLoading("正在重新登录，请稍后...");
        UserDataManager.getUserDataManager().doLogin(this, SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ACCOUNT), SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_PASSWORD), true, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                initState();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,"登录失败，请重新登录");
                Intent intent = new Intent();
                intent.setClass(context, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
