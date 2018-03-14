package com.poso2o.lechuan.activity.orderinfo;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

/**
 * Created by ${cbf} on 2018/3/14 0014.
 * 人员业绩
 */

public class OrderPoplstaffActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private boolean isBeginTime;
    private String beginTime, endTime;
    private TextView tvBeginTime;
    private TextView tvEndTime;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_staff;
    }

    @Override
    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvBeginTime = (TextView) findViewById(R.id.tv_order_info_bgin_time);
        tvEndTime = (TextView) findViewById(R.id.tv_order_end_time);
    }

    @Override
    protected void initData() {
        ivBack.setImageResource(R.drawable.actionbar_back);
        //默认为当天时间
        String nowDay = CalendarUtil.getTodayDate();
        tvBeginTime.setText(nowDay);
        tvEndTime.setText(nowDay);
    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);
        tvBeginTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                startActivity(new Intent(OrderPoplstaffActivity.this, OrderInfoMainActivity.class));
                finish();
                break;
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
