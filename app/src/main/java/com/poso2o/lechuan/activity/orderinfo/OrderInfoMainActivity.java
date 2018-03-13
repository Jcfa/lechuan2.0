package com.poso2o.lechuan.activity.orderinfo;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * Created by ${cbf} on 2018/3/12 0012.
 * 订单信息主界面
 */

public class OrderInfoMainActivity extends BaseActivity {

    private LinearLayout llOrderEntityShop;  //实体店
    private LinearLayout llOrderPaper;  //畅销管理
    private LinearLayout llOrderSell;  //库存管理

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_main;
    }

    @Override
    protected void initView() {

        llOrderEntityShop = (LinearLayout) findViewById(R.id.ll_order_entity_shop);

        llOrderSell = (LinearLayout) findViewById(R.id.ll_order_sell);

        llOrderPaper = (LinearLayout) findViewById(R.id.ll_order_paper);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        //实体店
        llOrderEntityShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderInfoMainActivity.this, OrderEntityShopActivity.class));
            }
        });
        //畅销商品
        llOrderSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderInfoMainActivity.this, OrderInfoSellActivity.class));
            }
        });
    }
}
