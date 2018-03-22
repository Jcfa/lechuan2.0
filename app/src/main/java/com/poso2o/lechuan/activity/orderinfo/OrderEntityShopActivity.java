package com.poso2o.lechuan.activity.orderinfo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

/**
 * Created by ${cbf} on 2018/3/12 0012.
 * 实体店订单信息
 */

public class OrderEntityShopActivity extends BaseActivity implements View.OnClickListener {
    private FrameLayout florderContent;
    private TextView tvBeginTime;
    private TextView tvEndTime;
    private ImageView ivVisibility;
    private LinearLayout llOrderClick;
    private TextView today, tomad, trecently, trecentlys, tlastm, tv_visibility;
    private TextView tvOrderHao, tvOrderNum, tvOrderMoney, tvOrderSaleName, tvNick;
    private boolean isBeginTime;
    private String beginTime, endTime;
    private OrderInfoEntityFragment infoEntityFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderentity_shop;
    }

    @Override
    protected void initView() {
        tvBeginTime = (TextView) findViewById(R.id.tv_order_info_bgin_time);
        tvEndTime = (TextView) findViewById(R.id.tv_order_end_time);
        tv_visibility = (TextView) findViewById(R.id.tv_visibility);
        ivVisibility = (ImageView) findViewById(R.id.iv_order_time_visibility);
        llOrderClick = (LinearLayout) findViewById(R.id.ll_ordr_click);
        //设置用户名
        tvNick = (TextView) findViewById(R.id.tv_title);
        //我的订单统计
        tvOrderHao = (TextView) findViewById(R.id.tv_order_hao);
        tvOrderNum = (TextView) findViewById(R.id.tv_order_num);
        tvOrderMoney = (TextView) findViewById(R.id.tv_order_money);
        tvOrderSaleName = (TextView) findViewById(R.id.tv_order_salesname);
        florderContent = (FrameLayout) findViewById(R.id.fl_order_entity_content);

    }

    @Override
    protected void initData() {
        ivVisibility.setVisibility(View.GONE);
        //默认为当天时间
        String nowDay = CalendarUtil.getTodayDate();
        //本月第一天
        String begin = CalendarUtil.getFirstDay();
        tvBeginTime.setText(begin);
        tvEndTime.setText(nowDay);
        tvNick.setText("我的订单");
        initTime(begin,nowDay);
    }

    private void initTime(String begin,String endTime) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        infoEntityFragment = new OrderInfoEntityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("beginTime", begin);
        bundle.putString("endTime", endTime);
        infoEntityFragment.setArguments(bundle);
        ft.add(R.id.fl_order_entity_content, infoEntityFragment).commit();
    }

    @Override
    protected void initListener() {
        tvBeginTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);

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

    //显示日期
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
                        calendarDialog.dismiss();
                    } else if (CalendarUtil.TimeCompare(dateT, str)) {
                        tvBeginTime.setText(dateT);
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        calendarDialog.dismiss();
                        String end = CalendarUtil.stampToDate(beginTime);
                        initTimes(str, end);
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
                        initTimes(str, end);
                    } else {
                        Toast.show(activity, "选择的时间范围不正确");
                    }
                }
            }
        });

    }

    private void initTimes(String str, String end) {
        infoEntityFragment.onInstance(str,end);
    }

}
