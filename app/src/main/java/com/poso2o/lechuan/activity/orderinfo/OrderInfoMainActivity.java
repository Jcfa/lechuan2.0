package com.poso2o.lechuan.activity.orderinfo;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.wshop.VdianActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoSellCountBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoSellManager;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by ${cbf} on 2018/3/12 0012.
 * 订单信息主界面
 */

public class OrderInfoMainActivity extends BaseActivity {

    private LinearLayout llOrderEntityShop;  // 实体店
    private LinearLayout llOrderPaper;  // 畅销管理
    private LinearLayout llOrderSell;  // 库存管理
    private LinearLayout llOrderWeid;  // 微店

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_main;
    }

    @Override
    protected void initView() {

        llOrderEntityShop = (LinearLayout) findViewById(R.id.ll_order_entity_shop);

        llOrderSell = (LinearLayout) findViewById(R.id.ll_order_sell);

        llOrderPaper = (LinearLayout) findViewById(R.id.ll_order_paper);

        llOrderWeid = (LinearLayout) findViewById(R.id.ll_order_weid);

    }

    @Override
    protected void initData() {
        OrderInfoSellManager.getOrderInfo().orderInfoSell(activity, "2018-02-01", "2018-02-16", new IRequestCallBack<OrderInfoSellCountBean>() {
            @Override
            public void onResult(int tag, OrderInfoSellCountBean result) {
                dismissLoading();
                OrderInfoSellCountBean sellCountBean = result;
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }
        });
    }

    @Override
    protected void initListener() {
        // 实体店
        llOrderEntityShop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderInfoMainActivity.this, OrderEntityShopActivity.class));
            }
        });
        // 畅销商品
        llOrderSell.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderInfoMainActivity.this, OrderInfoSellActivity.class));
            }
        });
        // 微店
        llOrderWeid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderInfoMainActivity.this, VdianActivity.class));
            }
        });
    }
}
