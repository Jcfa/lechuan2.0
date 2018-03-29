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
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderentity_shop;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

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
