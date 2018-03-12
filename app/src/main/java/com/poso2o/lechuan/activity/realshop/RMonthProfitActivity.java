package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.MonthProfitAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.monthprofit.AllMonthProfitData;
import com.poso2o.lechuan.bean.monthprofit.MonthProfitData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RReportManager;
import com.poso2o.lechuan.manager.wshopmanager.WReportManager;
import com.poso2o.lechuan.popubwindow.MonthProfitYearPop;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.Calendar;

/**
 * Created by mr zhang on 2017/8/5.
 * 损益表页
 */

public class RMonthProfitActivity extends BaseActivity implements MonthProfitAdapter.OnMonthProfitListener,View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    private Context context;
    
    //返回
    private ImageView r_profit_back;

    //下拉刷新
    private SwipeRefreshLayout month_profit_swipe;
    //损益列表
    private RecyclerView month_profit_recycler;
    //列表适配器
    private MonthProfitAdapter adapter;
    //年份框
    private RelativeLayout month_profit_year;
    //年份
    private TextView month_profit_year_select;

    //展示年份，默认为当年
    private String mYear;

    //是否微店
    private boolean is_online ;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_month_profit;
    }

    @Override
    public void initView() {
        context = this;

        r_profit_back = (ImageView) findViewById(R.id.r_profit_back);

        month_profit_swipe = (SwipeRefreshLayout) findViewById(R.id.month_profit_swipe);

        month_profit_recycler = (RecyclerView) findViewById(R.id.month_profit_recycler);

        month_profit_year = (RelativeLayout) findViewById(R.id.month_profit_year);

        month_profit_year_select = (TextView) findViewById(R.id.month_profit_year_select);

        month_profit_swipe.setColorSchemeColors(Color.RED);
    }

    @Override
    public void initData() {

        is_online = SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE);

        mYear = Calendar.getInstance().get(Calendar.YEAR) + "";
        month_profit_year_select.setText(mYear);

        initAdapter();
        requestMonthProfit();
    }

    @Override
    public void initListener() {
        r_profit_back.setOnClickListener(this);
        month_profit_swipe.setOnRefreshListener(this);
        adapter.setOnMonthProfitListener(this);
        month_profit_year.setOnClickListener(this);
    }

    private void initAdapter(){
        adapter = new MonthProfitAdapter(context,is_online);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        month_profit_recycler.setLayoutManager(linearLayoutManager);
        month_profit_recycler.setAdapter(adapter);
    }

    @Override
    public void onMonthProfitClick(MonthProfitData profitData) {
        if (profitData == null)return;
        Intent intent = new Intent();
        intent.setClass(context,RMonthProfitSetActivity.class);
        intent.putExtra(RMonthProfitSetActivity.MONTH_PROFIT_DATA,profitData);
        startActivityForResult(intent, RMonthProfitSetActivity.MONTH_PROFIT_REQUEST_CODE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.r_profit_back:
                finish();
                break;
            case R.id.month_profit_year:
                showYearPop();
                break;
        }
    }

    @Override
    public void onRefresh() {
        requestMonthProfit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RMonthProfitSetActivity.MONTH_PROFIT_REQUEST_CODE && resultCode == RESULT_OK){
            requestMonthProfit();
        }
    }

    //請求月損益列表數據
    private void requestMonthProfit(){
        if (!month_profit_swipe.isRefreshing())month_profit_swipe.setRefreshing(true);
        if (is_online){
            WReportManager.getwReportManager().wMonthProfitList(this, mYear, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    month_profit_swipe.setRefreshing(false);
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    month_profit_swipe.setRefreshing(false);
                    AllMonthProfitData allMonthProfitData = (AllMonthProfitData) object;
                    if (adapter != null)
                        adapter.notifyAdapter(allMonthProfitData.list);
                }
            });
        }else {
            RReportManager.getRReportManger().monthProfit(this,mYear,new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    month_profit_swipe.setRefreshing(false);
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    month_profit_swipe.setRefreshing(false);
                    AllMonthProfitData allMonthProfitData = (AllMonthProfitData) object;
                    if (adapter != null)
                        adapter.notifyAdapter(allMonthProfitData.list);
                }
            });
        }
    }

    private void showYearPop(){
        MonthProfitYearPop yearPop = new MonthProfitYearPop(context,mYear);
        yearPop.showPop(month_profit_year,-50,1);
        yearPop.setOnYearClickListener(new MonthProfitYearPop.OnYearClickListener() {
            @Override
            public void onYearClick(String year) {
                mYear = year;
                month_profit_year_select.setText(mYear);
                requestMonthProfit();
            }
        });
    }

}
