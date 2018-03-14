package com.poso2o.lechuan.activity.orderinfo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.orderinfo.adapter.OrderInfoSellAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoSellBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoSellManager;
import com.poso2o.lechuan.util.Toast;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/13 0013.
 * 畅销管理
 */

public class OrderInfoSellActivity extends BaseActivity {
    //点击时间选择器
    private LinearLayout llOrderClick;
    private TextView tvSellTime;//时间显示2018-03-11至2018-03-12
    private TextView tvSellMany;//销售最多和最少
    private RecyclerView rlvSellList;
    private  List<OrderInfoSellBean.DataBean> dataBeen;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_sell;
    }

    @Override
    protected void initView() {
        llOrderClick = (LinearLayout) findViewById(R.id.ll_ordr_click);
        tvSellTime = (TextView) findViewById(R.id.tv_order_info_bgin_time);
        tvSellMany = (TextView) findViewById(R.id.tv_order_sell_many);
        rlvSellList = (RecyclerView) findViewById(R.id.rlv_order_sell_list);


    }

    @Override
    protected void initData() {
        rlvSellList.setLayoutManager(new LinearLayoutManager(this));
        OrderInfoSellAdapter adapter = new OrderInfoSellAdapter();
        rlvSellList.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        llOrderClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
