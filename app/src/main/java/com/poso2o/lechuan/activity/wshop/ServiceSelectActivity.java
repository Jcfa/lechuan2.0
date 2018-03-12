package com.poso2o.lechuan.activity.wshop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.ServiceAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.shopdata.AllServiceBean;
import com.poso2o.lechuan.bean.shopdata.ServiceBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.layoutmanager.MyGridLayoutManager;
import com.poso2o.lechuan.manager.wshopmanager.ShopServiceManager;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/12/8.
 */

public class ServiceSelectActivity extends BaseActivity {

    private Context context;

    //返回
    private ImageView service_select_back;
    //套餐列表
    private RecyclerView service_recycler;
    //前往支付
    private TextView go_to_pay;

    private ServiceAdapter serviceAdapter;

    //选择的套餐
    private ServiceBean selectService;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_service_select;
    }

    @Override
    protected void initView() {
        context = this;
        service_select_back = (ImageView) findViewById(R.id.service_select_back);
        service_recycler = (RecyclerView) findViewById(R.id.service_recycler);
        go_to_pay = (TextView) findViewById(R.id.go_to_pay);
    }

    @Override
    protected void initData() {
        initServiceList();
    }

    @Override
    protected void initListener() {
        service_select_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        go_to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPay();
            }
        });
    }

    private void initServiceList(){
        showLoading("正在加载套餐内容...");
        ShopServiceManager.getShopServiceManager().serviceList(this, "2", new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                AllServiceBean allServiceBean = (AllServiceBean) object;
                if (allServiceBean != null)
                    initAdapter(allServiceBean.list);
            }
        });
    }

    private void initAdapter(ArrayList<ServiceBean> serviceBeen){
        serviceAdapter = new ServiceAdapter(context,serviceBeen);
        MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(context,2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridLayoutManager.setScrollEnabled(false);
        service_recycler.setLayoutManager(gridLayoutManager);
        service_recycler.setAdapter(serviceAdapter);

        serviceAdapter.setOnServiceSelectListener(new ServiceAdapter.OnServiceSelectListener() {
            @Override
            public void onServiceSelect(ServiceBean bean) {
                selectService = bean;
            }
        });
    }

    private void goToPay(){
        if (selectService == null){
            Toast.show(context,"请选择购买套餐");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context,SelectPayActivity.class);
        intent.putExtra(SelectPayActivity.SERVICE_DATA,selectService);
        startActivity(intent);
    }
}
