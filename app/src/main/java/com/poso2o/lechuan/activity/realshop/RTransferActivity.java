package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.CashierListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.transfer.Transfer;
import com.poso2o.lechuan.bean.transfer.TransferAllData;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RReportManager;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.DateTimeUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;
import com.poso2o.lechuan.view.customcalendar.DateUtil;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/7.
 * 收银交接页
 */

public class RTransferActivity extends BaseActivity implements CashierListAdapter.OnCashierClickListener,CalendarDialog.OnDateSelectListener,View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    private Context context;

    //返回
    private ImageView translate_cashier_back;

    //下拉刷新
    private SwipeRefreshLayout cashier_swipe_refresh;
    //开始时间
    private TextView cashier_calender_start;
    //结束时间
    private TextView cashier_calender_stop;
    //收银交接列表
    private RecyclerView cashier_recycler;
    //收银交接列表适配器
    private CashierListAdapter listAdapter ;

    //开始时间
    private String mStartTime;
    //结束时间
    private String mEndTime;
    //是否为开始时间
    private boolean isStartTime;
    //日历选择弹窗
    private CalendarDialog calendarDialog;
    //无数据提示
    private TextView tips_no_order;

    //当前页
    private int mPage = 1;
    //总页数
    private int mPages = 1;
    //数据集合
    private ArrayList<Transfer> transfers = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_menu_cashier;
    }

    @Override
    public void initView() {

        context = this;

        translate_cashier_back = (ImageView) findViewById(R.id.translate_cashier_back);

        cashier_swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.cashier_swipe_refresh);

        cashier_calender_start = (TextView) findViewById(R.id.cashier_calender_start);

        cashier_calender_stop = (TextView) findViewById(R.id.cashier_calender_stop);

        cashier_recycler = (RecyclerView) findViewById(R.id.cashier_recycler);

        tips_no_order = (TextView) findViewById(R.id.tips_no_order);

        cashier_swipe_refresh.setColorSchemeColors(Color.RED);
    }

    @Override
    public void initData() {
        initTime();
        initAdapter();
        initTransferData();
    }

    @Override
    public void initListener() {
        translate_cashier_back.setOnClickListener(this);

        cashier_swipe_refresh.setOnRefreshListener(this);
        listAdapter.setOnCashierClickListener(this);
        cashier_calender_start.setOnClickListener(this);
        cashier_calender_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.translate_cashier_back:
                finish();
                break;
            case R.id.cashier_calender_start:
                onDateClick(true);
                break;
            case R.id.cashier_calender_stop:
                onDateClick(false);
                break;
        }
    }

    @Override
    public void onRefresh() {
        initTransferData();
    }

    @Override
    public void onCashierClick(Transfer transfer) {
        Intent intent = new Intent();
        intent.setClass(context,TranslateDetailActivity.class);
        intent.putExtra(TranslateDetailActivity.TRANSFER_DATA,transfer);
        startActivity(intent);
    }

    @Override
    public void onLoadMore(TextView textView, ProgressBar progressBar) {
        if (mPage == mPages){
            textView.setText("数据已加载完毕");
            progressBar.setVisibility(View.GONE);
        }else {
            textView.setText("数据正在加载。。。");
            progressBar.setVisibility(View.VISIBLE);
            loadMore();
        }
    }

    private void initTime(){
        cashier_calender_start.setText(CalendarUtil.getFirstDay());
        cashier_calender_stop.setText(CalendarUtil.getTodayDate());
        mStartTime = CalendarUtil.timeStamp(CalendarUtil.getFirstDay() + " 00:00:00");
        mEndTime = CalendarUtil.timeStamp(CalendarUtil.getTodayDate() + " 23:59:59");
    }

    //选择日历
    private void onDateClick(boolean b) {
        isStartTime = b;
        if (b){
            calendarDialog = new CalendarDialog(context,cashier_calender_start.getText().toString());
        }else {
            calendarDialog = new CalendarDialog(context,cashier_calender_stop.getText().toString());
        }
        calendarDialog.show();
        calendarDialog.setOnDateSelectListener(this);
    }

    @Override
    public void onDateSelect(CustomDate date) {
        if (date == null) return;
        String dateT = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
        if (isStartTime) {
            if (CalendarUtil.TimeCompare(dateT, cashier_calender_stop.getText().toString())) {
                cashier_calender_start.setText(dateT);
                mStartTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                calendarDialog.dismiss();
            } else {
                Toast.show(context, "选择的时间范围不正确");
                return;
            }
        } else {
            if (CalendarUtil.TimeCompare(cashier_calender_start.getText().toString(), dateT)) {
                cashier_calender_stop.setText(dateT);
                mEndTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                calendarDialog.dismiss();
            } else {
                Toast.show(context, "选择的时间范围不正确");
                return;
            }
        }
        initTransferData();
    }

    private void initAdapter(){
        listAdapter = new CashierListAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cashier_recycler.setLayoutManager(linearLayoutManager);
        cashier_recycler.setAdapter(listAdapter);
    }

    private void initTransferData(){
        if (!cashier_swipe_refresh.isRefreshing())cashier_swipe_refresh.setRefreshing(true);
        mPage = 1;
        RReportManager.getRReportManger().rTransfer(this, mPage + "", DateTimeUtil.StringToData(mStartTime, "yyyy-MM-dd"), DateTimeUtil.StringToData(mEndTime, "yyyy-MM-dd"), "", new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                cashier_swipe_refresh.setRefreshing(false);
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                cashier_swipe_refresh.setRefreshing(false);
                TransferAllData transferAllData = (TransferAllData) object;
                if (transferAllData == null || transferAllData.list.size() == 0){
                    tips_no_order.setVisibility(View.VISIBLE);
                }else {
                    tips_no_order.setVisibility(View.GONE);
                }
                if (transferAllData == null || listAdapter == null)return;
//                mPages = transferAllData.total.pages;
                transfers.clear();
                transfers.addAll(transferAllData.list);
                listAdapter.notifyAdapter(transfers);
            }
        });
    }

    private void loadMore(){
        mPage++;
        RReportManager.getRReportManger().rTransfer(this, mPage + "", DateTimeUtil.StringToData(mStartTime, "yyyy-MM-dd"), DateTimeUtil.StringToData(mEndTime, "yyyy-MM-dd"), "", new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                mPage--;
                cashier_swipe_refresh.setRefreshing(false);
                Toast.show(context, msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                cashier_swipe_refresh.setRefreshing(false);
                TransferAllData transferAllData = (TransferAllData) object;
                if (transferAllData == null || listAdapter == null) return;
//                mPages = transferAllData.total.pages;
                transfers.addAll(transferAllData.list);
                listAdapter.notifyAdapter(transfers);
            }
        });
    }

}
