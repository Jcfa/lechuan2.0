package com.poso2o.lechuan.activity.orderinfo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoPoplStaffBean;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoPoplStaffManager;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoPoplStaffAdapter;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/14 0014.
 * 人员业绩
 */

public class OrderPoplstaffActivity extends BaseActivity implements View.OnClickListener {
    private boolean isBeginTime;
    private String beginTime, endTime;
    private TextView tvBeginTime;
    private TextView tvEndTime;
    private RecyclerView rlv;
    private TextView tvSellTotal, tv_title;
    private TextView tvSellTotalTask;
    //显示网络异常和为空的情况
    private TextView iv_default_null;
    private LinearLayout ll_default_null;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_staff;
    }

    @Override
    protected void initView() {
        tvBeginTime = (TextView) findViewById(R.id.tv_order_info_bgin_time);
        tvEndTime = (TextView) findViewById(R.id.tv_order_end_time);
        rlv = (RecyclerView) findViewById(R.id.rlv_order_sell_list);
        tvSellTotal = (TextView) findViewById(R.id.tv_sell_total);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tvSellTotalTask = (TextView) findViewById(R.id.tv_sell_total_task);
        iv_default_null = (TextView) findViewById(R.id.iv_default_null);
        ll_default_null = (LinearLayout) findViewById(R.id.ll_default_null);

    }

    @Override
    protected void initData() {
        //默认为当天时间
        String nowDay = CalendarUtil.getTodayDate();
        //选择时间  根据选择的时间来进行数据的选择
        //本月第一天
        String begin = CalendarUtil.getFirstDay();
        chooseTime(begin, nowDay);
        tvBeginTime.setText(begin);
        tvEndTime.setText(nowDay);
        tv_title.setText("人员业绩");

        rlv.setLayoutManager(new LinearLayoutManager(activity));
        initNetApi();
    }

    private void initNetApi() {
        OrderInfoPoplStaffManager.getsInstance().poplStaffApi(activity, new IRequestCallBack<OrderInfoPoplStaffBean>() {
            @Override
            public void onResult(int tag, OrderInfoPoplStaffBean poplStaffBean) {
                dismissLoading();
                List<OrderInfoPoplStaffBean.ListBean> list = poplStaffBean.getList();
                OrderInfoPoplStaffAdapter adapter = new OrderInfoPoplStaffAdapter(activity, list);
                rlv.setAdapter(adapter);
                tvSellTotal.setText(poplStaffBean.getTotal().getTotal_order_amounts());
                tvSellTotalTask.setText(poplStaffBean.getTotal().getTotal_assignments());

            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
                iv_default_null.setVisibility(View.VISIBLE);
                rlv.setVisibility(View.GONE);
                ll_default_null.setVisibility(View.GONE);

            }
        });
    }

    //根据开始时间和结束时间对数据更新  默认有个当前时间段
    private void chooseTime(String beginTime, String endTime) {
        //这里要对时间进行处理  小的在前面  大的再后面 不然会有一大在前或者在后情况
        boolean isEqual = CalendarUtil.TimeCompare(beginTime, endTime);
        if (isEqual == true) {
//            initNet(endTime,beginTime);
        } else {
//            initNet(beginTime,endTime);
        }


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
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        calendarDialog.dismiss();
                    } else if (CalendarUtil.TimeCompare(dateT, str)) {
                        tvBeginTime.setText(dateT);
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        calendarDialog.dismiss();
                        //进行时间的转换的 将时间戳 转换为年月日形式
                        String end = CalendarUtil.stampToDate(beginTime);
                        chooseTime(str, end);
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
                        //进行时间的转换的 将时间戳 转换为年月日形式
                        String end = CalendarUtil.stampToDate(endTime);
                        chooseTime(str, end);
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
            rlv.setVisibility(View.VISIBLE);
            iv_default_null.setVisibility(View.GONE);
            ll_default_null.setVisibility(View.GONE);
        } else if (action.equals("网络未连接")) {
            rlv.setVisibility(View.GONE);
            iv_default_null.setVisibility(View.VISIBLE);
            ll_default_null.setVisibility(View.GONE);
        } else if (action.equals("网络已连接")) {
            rlv.setVisibility(View.VISIBLE);
            ll_default_null.setVisibility(View.VISIBLE);
            iv_default_null.setVisibility(View.GONE);
            initNetApi();
        } else if (action.equals("网络请求失败")) {
            ll_default_null.setVisibility(View.GONE);
            rlv.setVisibility(View.GONE);
            iv_default_null.setVisibility(View.VISIBLE);
        }
    }
}
