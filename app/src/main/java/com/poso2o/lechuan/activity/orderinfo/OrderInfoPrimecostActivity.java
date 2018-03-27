package com.poso2o.lechuan.activity.orderinfo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoPrimeCostBean;
import com.poso2o.lechuan.bean.orderInfo.OrderMothsDetailBean;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.dialog.OrderPaperDetailDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoPrimeCostManager;
import com.poso2o.lechuan.manager.orderInfomanager.OrderPaperDetailManager;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoPaperAdapter;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoPrimeAdapter;
import com.poso2o.lechuan.orderInfoAdapter.OrderMothsDetailAdapter;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/14 0014.
 * 月损益表
 */

public class OrderInfoPrimecostActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    //成本    收入          支出      利润
    private TextView tvcbPrice, tvinPrice, tvspPrice, tvpfPrice;
    private RecyclerView rlv;
    private int type = 2;
    private LinearLayout llCenter;
    private TextView tv_qchu, tv_shouc, tv_kc, tv_price, tv_select_time;
    private Spinner spinner;
    private List<String> timeList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private TextView iv_default_null;
    private LinearLayout ll1;
    private String nowDay;

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
        rlv = (RecyclerView) findViewById(R.id.rlv_order_pc_list);
        llCenter = (LinearLayout) findViewById(R.id.ll_order_center);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        tv_qchu = (TextView) findViewById(R.id.tv_qchu);
        tv_shouc = (TextView) findViewById(R.id.tv_shouc);
        tv_kc = (TextView) findViewById(R.id.tv_kc);
        tv_price = (TextView) findViewById(R.id.tv_price);
        iv_default_null = (TextView) findViewById(R.id.iv_default_null);
        tv_select_time = (TextView) findViewById(R.id.tv_select_time);
        spinner = (Spinner) findViewById(R.id.spinner);

    }

    @Override
    protected void initData() {
        tvTitle.setText("月损益表");
        rlv.setLayoutManager(new LinearLayoutManager(activity));
        //默认当前时间
        nowDay = CalendarUtil.getTodayDate();
        //网络访问
        initNet(nowDay);
        //隐藏图标
        llCenter.setVisibility(View.GONE);
        timeList.add("2015");
        timeList.add("2016");
        timeList.add("2017");
        timeList.add("2018");
        timeList.add("2019");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeList);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        spinner.setAdapter(adapter);
        spinner.setSelection(3, true);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String item = adapter.getItem(arg2);
                /* 将所选mySpinner 的值带入myTextView 中*/
                initNet(item + "-12-01");
                /* 将mySpinner 显示*/
                arg0.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });
        /*下拉菜单弹出的内容选项触屏事件处理*/
        spinner.setOnTouchListener(new Spinner.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        spinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });


    }

    /**
     * 网络访问
     */
    private void initNet(final String begin) {
        OrderPaperDetailManager.getOrderInfo().orderMothsDetailApi(activity, begin, new IRequestCallBack<OrderMothsDetailBean>() {
            @Override
            public void onResult(int tag, OrderMothsDetailBean detailBean) {
                activity.dismissLoading();
                List<OrderMothsDetailBean.ListBean> list = detailBean.getList();
                double profit = 0.00;
                double income = 0.00;
                double spend = 0.00;
                double fprice = 0.00;
                for (int i = 0; i < list.size(); i++) {
                    profit += Double.parseDouble(list.get(i).getClear_profit());
                    income += Double.parseDouble(list.get(i).getPrimecost_amount());
                    spend += Double.parseDouble(list.get(i).getSales_amount());
                    fprice += Double.parseDouble(list.get(i).getDel_amount());
                }
                BigDecimal bg = new BigDecimal(profit);
                BigDecimal bg2 = new BigDecimal(income);
                BigDecimal bg3 = new BigDecimal(spend);
                double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                double value2 = bg2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                double value3 = bg3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
                String fprices = df.format(fprice);
                if (fprice == 0.0) {
                    tv_shouc.setText("0.00");
                } else
                    tv_shouc.setText(fprices + "");
                tv_qchu.setText(value3 + "");
                tv_kc.setText(value2 + "");
                tv_price.setText(value + "");
                tv_qchu.setTextColor(getResources().getColor(R.color.color_FF6537));
                tv_shouc.setTextColor(getResources().getColor(R.color.color_FF6537));
                tv_kc.setTextColor(getResources().getColor(R.color.color_FF6537));
                tv_price.setTextColor(getResources().getColor(R.color.color_FF6537));
//                    tvPrice.setText("净利润" + value + "收入 " + value2 + " 支出 " + value3);
                OrderMothsDetailAdapter adapter = new OrderMothsDetailAdapter(detailBean.getList(), type);
                rlv.setAdapter(adapter);

            }

            @Override
            public void onFailed(int tag, String msg) {
                activity.dismissLoading();
                iv_default_null.setVisibility(View.VISIBLE);
                ll1.setVisibility(View.GONE);
                rlv.setVisibility(View.GONE);
            }
        });







       /* OrderInfoPrimeCostManager.getsInstance().oPrimeCostInfo(activity, begin, new IRequestCallBack<OrderInfoPrimeCostBean>() {
            @Override
            public void onResult(int tag, OrderInfoPrimeCostBean costBean) {
                dismissLoading();
                final List<OrderInfoPrimeCostBean.DataBean> data = costBean.getData();
                if (data == null || data.size() < 0) {
                    Toast.show(activity, "数据为空");
                } else {
                    //图标数据跟下面一样  所以隐藏掉 有需要再开启
                  *//*  for (int i = 0; i < data.size(); i++) {
                        OrderInfoPrimeCostBean.DataBean dataBean = data.get(i);
                        tvcbPrice.setText(dataBean.getTotal_primecost());
                        tvinPrice.setText(dataBean.getAdd_amounts());
                        tvpfPrice.setText(dataBean.getTotal_profit());
                        tvspPrice.setText(dataBean.getDel_amount());

                    }*//*
                    OrderInfoPrimeAdapter adapter = new OrderInfoPrimeAdapter(data);
                    rlv.setAdapter(adapter);
                    adapter.setOnItemClickListener(new OrderInfoPaperAdapter.RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(View view, int position) {
                            OrderPaperDetailDialog dialog = new OrderPaperDetailDialog(activity);
                            dialog.show();
                            dialog.setData(data.get(position).getShopid(), begin, type);
                        }
                    });
                }

            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);

            }
        });*/
    }


    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onEvent(String action) {
        super.onEvent(action);
        if (action.equals("网络请求成功")) {
            iv_default_null.setVisibility(View.GONE);
            ll1.setVisibility(View.VISIBLE);
            rlv.setVisibility(View.VISIBLE);
        } else if (action.equals("网络未连接")) {
            iv_default_null.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.GONE);
            rlv.setVisibility(View.GONE);
        } else if (action.equals("网络已连接")) {
            iv_default_null.setVisibility(View.GONE);
            ll1.setVisibility(View.VISIBLE);
            rlv.setVisibility(View.VISIBLE);
            initNet(nowDay);
        } else if (action.equals("网络请求失败")) {
            iv_default_null.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.GONE);
            rlv.setVisibility(View.GONE);

        }
    }
}
