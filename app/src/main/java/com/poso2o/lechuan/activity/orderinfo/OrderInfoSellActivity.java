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
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoSellManager;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

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
    private List<OrderInfoSellBean.DataBean> dataBeen;
    private boolean isBeginTime;
    private String beginTime, endTime;

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


    }

    @Override
    protected void initData() {
        //默认为当天时间
        String nowDay = CalendarUtil.getTodayDate();
        tvBeginTime.setText(nowDay);
        tvEndTime.setText(nowDay);
        rlvSellList.setLayoutManager(new LinearLayoutManager(this));
        OrderInfoSellAdapter adapter = new OrderInfoSellAdapter();
        rlvSellList.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        tvBeginTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        llOrderClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    } else {
                        Toast.show(activity, "选择的时间范围不正确");
                    }
                }
            }
        });
    }
}
