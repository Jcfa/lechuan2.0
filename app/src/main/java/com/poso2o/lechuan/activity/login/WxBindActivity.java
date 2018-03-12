package com.poso2o.lechuan.activity.login;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * 微信绑定
 * Created by Administrator on 2017-11-28.
 */

public class WxBindActivity extends BaseActivity {
    public static final String KEY_USER_INFO = "key_user_info";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_weixin_bind;
    }

    @Override
    protected void initView() {
        setTitle("微信绑定");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
