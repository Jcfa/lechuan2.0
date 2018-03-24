package com.poso2o.lechuan.activity.orderinfo;

import android.widget.FrameLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * Created by ${cbf} on 2018/3/12 0012.
 * 实体店订单信息
 */

public class OrderEntityShopActivity extends BaseActivity {
    private FrameLayout florderContent;
    //设置用户名
    private TextView tvNick;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderentity_shop;
    }

    @Override
    protected void initView() {
        //设置用户名
        tvNick = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    protected void initData() {
        tvNick.setText("我的订单");
        initTime();
    }

    private void initTime() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        OrderInfoEntityFragment infoEntityFragment = new OrderInfoEntityFragment();
        ft.add(R.id.fl_order_entity_content, infoEntityFragment).commit();
    }

    @Override
    protected void initListener() {


    }
}
