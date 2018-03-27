package com.poso2o.lechuan.activity.orderinfo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoSellBean;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.dialog.OrderInfoSellDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoGoodsManager;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoPaperAdapter;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoSellAdapter;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/13 0013.
 * 畅销管理
 */

public class OrderInfoSellActivity extends BaseActivity implements View.OnClickListener {
    //点击时间选择器
    private LinearLayout llOrderClick;
    private TextView tvBeginTime, tvEndTime;//时间显示2018-03-11至2018-03-12
    private TextView tvSellMany;//销售最多和最少
    private RecyclerView rlvSellList;
    private boolean isBeginTime;
    private String beginTime, endTime;
    private List<OrderInfoSellBean.DataBean> list = new ArrayList<>();
    private OrderInfoSellAdapter adapter;
    private boolean isClick = false;
    private SmartRefreshLayout refreshLayout;
    private int cuurapge = 1;
    private int apg;
    private TextView tvTitle;
    private List<OrderInfoSellBean.DataBean> data = new ArrayList<>();
    private TextView iv_default_null;
    private OrderInfoSellBean sellBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_sell;
    }

    @Override
    protected void initView() {
        llOrderClick = (LinearLayout) findViewById(R.id.ll_ordr_click);
        tvSellMany = (TextView) findViewById(R.id.tv_order_sell_many);
        rlvSellList = (RecyclerView) findViewById(R.id.rlv_order_sell_list);
        //开始时间  结束时间
        tvBeginTime = (TextView) findViewById(R.id.tv_order_info_bgin_time);
        tvEndTime = (TextView) findViewById(R.id.tv_order_end_time);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.swip_refreshlayout);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        iv_default_null = (TextView) findViewById(R.id.iv_default_null);


    }

    @Override
    protected void initData() {
        tvTitle.setText("畅销商品");
        //默认为当天时间
        String nowDay = CalendarUtil.getTodayDate();
        //本月第一天
        String begin = CalendarUtil.getFirstDay();
        tvBeginTime.setText(begin);
        tvEndTime.setText(nowDay);
        if (isClick == false) {
            refreshLayout.autoRefresh(200);
        }
        initNet(begin, nowDay, cuurapge);
        rlvSellList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderInfoSellAdapter(activity, beginTs, endTs);
        rlvSellList.setAdapter(adapter);
    }

    private void initNet(String beginTime, String endTime, int cuurapge) {
        boolean isdd = CalendarUtil.TimeCompare(beginTime, endTime);
        if (isdd == false) {
            initRequestApi(endTime, beginTime, cuurapge);
        } else {
            initRequestApi(beginTime, endTime, cuurapge);
        }

    }

    private String beginTs;
    private String endTs;

    private void initRequestApi(final String beginTime, final String endTime, final int cuurapge) {
        refreshLayout.autoRefresh(200);
        this.beginTs = beginTime;
        this.endTs = endTime;
        OrderInfoGoodsManager.getOrderInfo().orderInfoGoodsApi(activity, beginTime, endTime, cuurapge + "", new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                sellBean = (OrderInfoSellBean) result;
                data = sellBean.getData();
                if (cuurapge == 1) {
                    adapter.setData(data);
                } else {
                    adapter.addData(data);
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                iv_default_null.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);

            }
        });
    }

    @Override
    protected void initListener() {
        tvBeginTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvSellMany.setOnClickListener(this);

        llOrderClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                cuurapge = 1;
                initRequestApi(beginTs, endTs, cuurapge);
             /*   OrderInfoGoodsManager.getOrderInfo().orderInfoGoodsApi(activity, beginTs, endTs, cuurapge + "", new IRequestCallBack() {
                    @Override
                    public void onResult(int tag, Object result) {
                        dismissLoading();
                        sellBean = (OrderInfoSellBean) result;
                        data = sellBean.getData();
                     *//*   if (data == null || data.size() < 0) {
                            Toast.show(activity, "数据为空");
                        } else {
                            if (data == null) {
                                iv_default_null.setVisibility(View.VISIBLE);
                                refreshLayout.setVisibility(View.GONE);
                            } else if (data != null)
                                adapter.setData(data);
                        }*//*
                    }

                    @Override
                    public void onFailed(int tag, String msg) {
                        dismissLoading();
                        iv_default_null.setVisibility(View.VISIBLE);
                        refreshLayout.setVisibility(View.GONE);

                    }
                });*/
                refreshlayout.finishRefresh(200);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                cuurapge++;
                initRequestApi(beginTs, endTs, cuurapge);
              /*  OrderInfoGoodsManager.getOrderInfo().orderInfoGoodsApi(activity, beginTs, endTs, cuurapge + "", new IRequestCallBack() {
                    @Override
                    public void onResult(int tag, Object result) {
                        dismissLoading();
                        sellBean = (OrderInfoSellBean) result;
                        data = sellBean.getData();
                     *//*   if (data == null || data.size() < 0) {
                            Toast.show(activity, "数据为空");
                        } else {
                            adapter.addData(data);
                        }*//*
                    }

                    @Override
                    public void onFailed(int tag, String msg) {
                        dismissLoading();
                        iv_default_null.setVisibility(View.VISIBLE);
                        refreshLayout.setVisibility(View.GONE);
                    }
                });*/
                refreshLayout.finishLoadmore(200);

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order_info_bgin_time:
                isBeginTime = true;
                showCalender();
                break;
            case R.id.tv_order_end_time:
                isBeginTime = false;
                showCalender();
                break;
            case R.id.tv_order_sell_many:
//                Toast.show(activity, "点击了");
                break;
        }

    }

    private void showCalender() {
        final CalendarDialog calendarDialog = new CalendarDialog(this);
        calendarDialog.show();
        calendarDialog.setOnDateSelectListener(new CalendarDialog.OnDateSelectListener() {
            @Override
            public void onDateSelect(CustomDate date) {
                if (date == null) return;
                String dateT = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                if (isBeginTime) {
                    String str = tvEndTime.getText().toString();
                    if (str == null || str.equals("")) {
                        tvBeginTime.setText(dateT);
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        calendarDialog.dismiss();
                    } else if (CalendarUtil.TimeCompare(dateT, str)) {
                        tvBeginTime.setText(dateT);
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        calendarDialog.dismiss();
                        String end = CalendarUtil.stampToDate(beginTime);
                        initNet(str, end, cuurapge);
                    } else {
                        Toast.show(activity, "选择的时间范围不正确");
                    }
                } else {
                    String str = tvBeginTime.getText().toString();
                    if (str == null || str.equals("")) {
                        tvEndTime.setText(dateT);
                        endTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                        calendarDialog.dismiss();
                    } else if (CalendarUtil.TimeCompare(str, dateT)) {
                        tvEndTime.setText(dateT);
                        endTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                        calendarDialog.dismiss();
                        String end = CalendarUtil.stampToDate(endTime);
                        initNet(str, end, cuurapge);
                    } else {
                        Toast.show(activity, "选择的时间范围不正确");
                    }
                }
            }
        });
    }

    @Override
    public void onEvent(String action) {
        super.onEvent(action);
        if (action.equals("网络请求成功")) {
            if (cuurapge == 1) {
                refreshLayout.finishRefresh(true);
            } else {
                refreshLayout.finishLoadmore(true);
            }
            rlvSellList.setVisibility(View.VISIBLE);
            iv_default_null.setVisibility(View.GONE);
        } else if (action.equals("网络未连接")) {
            rlvSellList.setVisibility(View.GONE);
            iv_default_null.setVisibility(View.VISIBLE);
        } else if (action.equals("网络已连接")) {
            rlvSellList.setVisibility(View.VISIBLE);
            iv_default_null.setVisibility(View.GONE);
            initRequestApi(beginTs, endTs, cuurapge);
        } else if (action.equals("网络请求失败")) {
            rlvSellList.setVisibility(View.GONE);
            iv_default_null.setVisibility(View.VISIBLE);
        }
    }
}
