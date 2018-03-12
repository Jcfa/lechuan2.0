package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.OrderAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.oldorder.QuerySaleOrderData;
import com.poso2o.lechuan.bean.oldorder.SaleOrderDate;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.dialog.MainDateDialog;
import com.poso2o.lechuan.dialog.OrderDetailDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.ROrderManager;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.DateTimeUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/1.
 * 订单页
 */

public class OldOrderActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,OrderAdapter.OnOrderClickListener,View.OnClickListener,TextView.OnEditorActionListener{

    private Context context;

    //返回
    private ImageView old_order_back;
    //搜索时间布局
    private RelativeLayout sale_order_time;
    //搜索关键词布局
    private RelativeLayout sale_order_search;
    //关键词输入框
    private EditText order_search_input;
    //清除关键词
    private ImageView clear_search_input;
    //切换到输入框
    private ImageView sale_order_to_search;
    //时间
    private TextView order_calender_type;
    //下拉刷新
    private SwipeRefreshLayout order_swipe;
    //订单列表
    private RecyclerView order_recycler;
    //下划线
    private View under_line_list;
    //总金额
    private TextView order_total_amount;
    //总数量
    private TextView order_total_num;

    //订单列表适配器
    private OrderAdapter orderAdapter;

    //时间类型,1为日，2为周，3为月，4为自定义
    private int mDateType ;
    //开始时间
    private String mStartTime;
    //结束时间
    private String mEndTime;
    //是否为开始时间
    private boolean isStartTime;
    //日历选择弹窗
//    private CalendarDialog calendarDialog;

    //当前页数
    private int mCurrentPage = 1;
    //总页数
    private int mTotalPage = 1;
    //关键词
    private String keywords = "";

    private ArrayList<SaleOrderDate> saleOrderDates = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_old_order;
    }

    @Override
    public void initView() {

        context = this;

        old_order_back = (ImageView) findViewById(R.id.old_order_back);

        sale_order_time = (RelativeLayout) findViewById(R.id.sale_order_time);

        sale_order_search = (RelativeLayout) findViewById(R.id.sale_order_search);

        order_search_input = (EditText) findViewById(R.id.order_search_input);

        clear_search_input = (ImageView) findViewById(R.id.clear_search_input);

        sale_order_to_search = (ImageView) findViewById(R.id.sale_order_to_search);

        order_calender_type = (TextView) findViewById(R.id.order_calender_type);

        order_swipe = (SwipeRefreshLayout) findViewById(R.id.order_swipe);

        order_recycler = (RecyclerView) findViewById(R.id.order_recycler);

//        under_line_list = findViewById(R.id.under_line_list);

        order_total_num = (TextView) findViewById(R.id.order_total_num);

        order_total_amount = (TextView) findViewById(R.id.order_total_amount);

        order_swipe.setColorSchemeColors(Color.RED);
    }

    @Override
    public void initData() {

        initTime();

        initOrderAdapter();

        querySaleOrderList();
    }

    @Override
    public void initListener() {

        old_order_back.setOnClickListener(this);

        order_swipe.setOnRefreshListener(this);

        order_search_input.setOnEditorActionListener(this);

        clear_search_input.setOnClickListener(this);

        sale_order_to_search.setOnClickListener(this);

        sale_order_time.setOnClickListener(this);

        orderAdapter.setOnOrderClickListener(this);

    }

    @Override
    public void onRefresh() {
        querySaleOrderList();
    }

    @Override
    public void onOrderClick(SaleOrderDate saleOrderDate) {
        saleOrderDetail(saleOrderDate.order_id + "");
    }

    @Override
    public void onLoadMore(TextView textView, ProgressBar progressBar) {
        if (mCurrentPage >= mTotalPage){
            textView.setText("订单加载完毕");
            progressBar.setVisibility(View.GONE);
        }else {
            loadMore();
            textView.setText("正在加载更多。。。");
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.old_order_back:
                finish();
                break;
            case R.id.sale_order_time:
                showDateDialog();
                break;
            /*case R.id.order_calender_start:
                onDateClick(true);
                break;
            case R.id.order_calender_stop:
                onDateClick(false);
                break;*/
            case R.id.clear_search_input:
                keywords = "";
                order_search_input.setText(null);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(order_search_input.getWindowToken(),0);
                sale_order_time.setVisibility(View.VISIBLE);
                sale_order_search.setVisibility(View.GONE);
                sale_order_to_search.setVisibility(View.VISIBLE);
                querySaleOrderList();
                break;
            case R.id.sale_order_to_search:
                sale_order_time.setVisibility(View.GONE);
                sale_order_search.setVisibility(View.VISIBLE);
                order_search_input.requestFocus();
                sale_order_to_search.setVisibility(View.GONE);
                toggleSoftBoard();
                break;
        }
    }

    private void initTime(){
//        order_calender_start.setText(CalendarUtil.getFirstDay());
//        order_calender_stop.setText(CalendarUtil.getTodayDate());
        mDateType = 2;
        mEndTime = CalendarUtil.timeStamp(CalendarUtil.getTodayDate() + " 23:59:59");
        mStartTime = (Long.parseLong(CalendarUtil.timeStamp(CalendarUtil.getTodayDate() + " 00:00:00")) - 86400000*6) + "";
    }

    private void initOrderAdapter(){
        orderAdapter = new OrderAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        order_recycler.setLayoutManager(linearLayoutManager);
        order_recycler.setAdapter(orderAdapter);
    }

    //刷新和第一次加载订单数据
    private void querySaleOrderList(){
        if (!order_swipe.isRefreshing())order_swipe.setRefreshing(true);
        mCurrentPage = 1;
        ROrderManager.getrOrderManager().queryOrder(this, mCurrentPage, DateTimeUtil.StringToData(mStartTime,"yyyy-MM-dd"), DateTimeUtil.StringToData(mEndTime,"yyyy-MM-dd"), keywords, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object object) {
                order_swipe.setRefreshing(false);
                QuerySaleOrderData saleOrderData = (QuerySaleOrderData) object;
                if (orderAdapter == null)return;
                if (saleOrderData == null){
                    mTotalPage = 0;
                    saleOrderDates.clear();
                    order_total_num.setText("0");
                    order_total_amount.setText("0.00");
                    orderAdapter.notifyAdapter(saleOrderDates);
                    return;
                }
                mTotalPage = saleOrderData.total.pages;
                order_total_num.setText(saleOrderData.total.total_num);
                order_total_amount.setText(saleOrderData.total.total_amount);
                saleOrderDates.clear();
                saleOrderDates.addAll(saleOrderData.list);
                orderAdapter.notifyAdapter(saleOrderDates);
            }

            @Override
            public void onFailed(int tag, String msg) {
                order_swipe.setRefreshing(false);
                Toast.show(context,msg);
            }
        });
    }

    //加载更多数据
    private void loadMore(){
        mCurrentPage++;
        ROrderManager.getrOrderManager().queryOrder(this, mCurrentPage, DateTimeUtil.StringToData(mStartTime,"yyyy-MM-dd"), DateTimeUtil.StringToData(mEndTime,"yyyy-MM-dd"), null, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object object) {
                QuerySaleOrderData saleOrderData = (QuerySaleOrderData) object;
                if (orderAdapter == null)return;
                if (saleOrderData == null)return;
                saleOrderDates.addAll(saleOrderData.list);
                orderAdapter.notifyAdapter(saleOrderDates);
            }

            @Override
            public void onFailed(int tag, String msg) {
                order_swipe.setRefreshing(false);
                mCurrentPage--;
            }
        });
    }

    //选择日历
    /*private void onDateClick(boolean b) {
        isStartTime = b;
        calendarDialog = new CalendarDialog(context);
        calendarDialog.show();
        calendarDialog.setOnDateSelectListener(this);
    }*/

    private void showDateDialog(){
        //时间弹窗
        MainDateDialog dateDialog = new MainDateDialog(context,mDateType,mStartTime,mEndTime);
        dateDialog.show();
        dateDialog.setOnDateListener(new MainDateDialog.OnDateListener() {
            @Override
            public void onDateClick(int type,String begin,String end) {
                mDateType = type;
                mStartTime = begin;
                mEndTime = end;
                if (type == 1){
                    order_calender_type.setText("今天");
                }else if (type == 2){
                    order_calender_type.setText("最近七天");
                }else if (type == 3){
                    order_calender_type.setText("本月");
                }else if (type == 4){
                    mStartTime = DateTimeUtil.dataOne(begin + "-00-00-00");
                    mEndTime = DateTimeUtil.dataOne(end + "-59-59-59");
                    order_calender_type.setText(begin + "至" +  end);
                }
                querySaleOrderList();
            }
        });
    }

    /*@Override
    public void onDateSelect(CustomDate date) {
        if (date == null) return;
        String dateT = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
        if (isStartTime) {
            if (CalendarUtil.TimeCompare(dateT, order_calender_stop.getText().toString())) {
                order_calender_start.setText(dateT);
                mStartTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                calendarDialog.dismiss();
                querySaleOrderList();
            } else {
                Toast.show(context,"选择的时间范围不正确");
            }
        } else {
            if (CalendarUtil.TimeCompare(order_calender_start.getText().toString(), dateT)) {
                order_calender_stop.setText(dateT);
                mEndTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                calendarDialog.dismiss();
                querySaleOrderList();
            } else {
                Toast.show(context,"选择的时间范围不正确");
            }
        }
    }*/

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH){
            keywords = order_search_input.getText().toString();
            querySaleOrderList();
        }
        return false;
    }

    //显示软键盘
    private void toggleSoftBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void saleOrderDetail(final String order_id){
        showLoading("正在加载数据...");
        ROrderManager.getrOrderManager().orderInfo(this, DateTimeUtil.StringToData(mStartTime,"yyyy-MM-dd"), DateTimeUtil.StringToData(mEndTime,"yyyy-MM-dd"), order_id, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                SaleOrderDate saleOrderDate = (SaleOrderDate) object;
                OrderDetailDialog detailDialog = new OrderDetailDialog((BaseActivity)context);
                detailDialog.show();
                detailDialog.setOrderData(saleOrderDate);
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }
        });
    }
}
