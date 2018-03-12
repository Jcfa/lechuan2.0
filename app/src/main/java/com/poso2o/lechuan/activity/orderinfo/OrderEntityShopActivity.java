package com.poso2o.lechuan.activity.orderinfo;

import android.widget.FrameLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * Created by ${cbf} on 2018/3/12 0012.
 * 实体店订单信息
 */

public class OrderEntityShopActivity extends BaseActivity {

    private FrameLayout florderContent;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderentity_shop;
    }

    @Override
    protected void initView() {
        florderContent = (FrameLayout) findViewById(R.id.fl_order_entity_content);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_order_entity_content, new OrderInfoEntityFragment()).commit();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
