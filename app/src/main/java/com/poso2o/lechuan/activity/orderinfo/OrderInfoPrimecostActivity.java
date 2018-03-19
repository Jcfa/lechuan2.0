package com.poso2o.lechuan.activity.orderinfo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoPrimeCostBean;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.dialog.OrderPaperDetailDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoPrimeCostManager;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoPaperAdapter;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoPrimeAdapter;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/14 0014.
 * 月损益表
 */

public class OrderInfoPrimecostActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    //成本    收入          支出      利润
    private TextView tvcbPrice, tvinPrice, tvspPrice, tvpfPrice;
    //开始时间  结束时间
    private TextView tvBeginTime, tvEndTime;
    private boolean isBeginTime;
    private String beginTime;
    private String endTime;
    private RecyclerView rlv;
    private int type = 2;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_prcom;
    }

    @Override
    protected void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvcbPrice = (TextView) findViewById(R.id.tv_order_cb_price);
        tvinPrice = (TextView) findViewById(R.id.tv_order_income_price);
        tvspPrice = (TextView) findViewById(R.id.tv_order_spend_price);
        tvpfPrice = (TextView) findViewById(R.id.tv_order_profits_price);
        tvBeginTime = (TextView) findViewById(R.id.tv_order_info_bgin_time);
        tvEndTime = (TextView) findViewById(R.id.tv_order_end_time);
        rlv = (RecyclerView) findViewById(R.id.rlv_order_pc_list);

    }

    @Override
    protected void initData() {
        tvTitle.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK) + ">" + "月损益表");
        rlv.setLayoutManager(new LinearLayoutManager(activity));
        //默认当前时间
        String nowDay = CalendarUtil.getTodayDate();
        //本月第一天
        String begin = CalendarUtil.getFirstDay();
        tvBeginTime.setText(begin);
        tvEndTime.setText(nowDay);
        //网络访问
        initNet(nowDay);

    }

    /**
     * 网络访问
     */
    private void initNet(final String begin) {
        OrderInfoPrimeCostManager.getsInstance().oPrimeCostInfo(activity, begin, new IRequestCallBack<OrderInfoPrimeCostBean>() {
            @Override
            public void onResult(int tag, OrderInfoPrimeCostBean costBean) {
                dismissLoading();
                final List<OrderInfoPrimeCostBean.DataBean> data = costBean.getData();
                if (data == null || data.size() < 0) {
                    Toast.show(activity, "数据为空");
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        OrderInfoPrimeCostBean.DataBean dataBean = data.get(i);
                        tvcbPrice.setText(dataBean.getTotal_primecost());
                        tvinPrice.setText(dataBean.getAdd_amounts());
                        tvpfPrice.setText(dataBean.getTotal_profit());
                        tvspPrice.setText(dataBean.getDel_amount());

                    }
                    OrderInfoPrimeAdapter adapter = new OrderInfoPrimeAdapter(data);
                    rlv.setAdapter(adapter);
                    adapter.setOnItemClickListener(new OrderInfoPaperAdapter.RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(View view, int position) {
                            OrderPaperDetailDialog dialog = new OrderPaperDetailDialog(activity);
                            dialog.show();
                            dialog.setData(data.get(position).getShopid(),begin, type);
                        }
                    });
                }

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
        tvBeginTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order_info_bgin_time:
                isBeginTime = true;
                setCalender();
                break;
            case R.id.tv_order_end_time:
                isBeginTime = false;
                setCalender();
                break;
        }

    }

    /**
     * 获取时间  传参时只能如果小于10的好必须前面补个0
     * 不然会出现接口数据不一定 数据都为0
     * <p>
     * 注意:也不能参str  因为不是当前你所点击的当前时间
     */
    private void setCalender() {
        final CalendarDialog calendarDialog = new CalendarDialog(this);
        calendarDialog.show();
        calendarDialog.setOnDateSelectListener(new CalendarDialog.OnDateSelectListener() {
            @Override
            public void onDateSelect(CustomDate date) {
                if (date == null) return;
                String month;
                if (date.getMonth() < 10) {
                    month = "0" + date.getMonth();
                } else {
                    month = date.getMonth() + "";
                }
                String dateT = date.getYear() + "-" + month + "-" + date.getDay();
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
                        String begin = CalendarUtil.stampToDate(beginTime);
                        initNet(begin);
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
                        String begin = CalendarUtil.stampToDate(endTime);
                        initNet(begin);
                    } else {
                        Toast.show(activity, "选择的时间范围不正确");
                    }
                }
            }
        });

    }
}
