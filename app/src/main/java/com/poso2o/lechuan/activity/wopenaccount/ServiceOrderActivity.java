package com.poso2o.lechuan.activity.wopenaccount;

import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class ServiceOrderActivity extends BaseActivity {
    private TextView tv_title;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wopen_service_order;
    }

    @Override
    protected void initView() {
        tv_title=(TextView)findViewById(R.id.tv_title);
    }

    @Override
    protected void initData() {
        tv_title.setText(getResources().getString(R.string.service_order));
        tv_title.setTextColor(getResources().getColor(R.color.text_type));
    }

    @Override
    protected void initListener() {

    }
}
