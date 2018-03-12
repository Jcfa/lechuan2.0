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
import com.poso2o.lechuan.adapter.AchievementListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.staffreport.AchieveDetailData;
import com.poso2o.lechuan.bean.staffreport.AllStaffReportData;
import com.poso2o.lechuan.bean.staffreport.StaffReportData;
import com.poso2o.lechuan.dialog.AchieveDetailDialog;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RReportManager;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mr zhang on 2017/8/4.
 * 业绩页
 */

public class RAchievementActivity extends BaseActivity implements View.OnClickListener, AchievementListAdapter.OnAchieveListListener, SwipeRefreshLayout.OnRefreshListener, CalendarDialog.OnDateSelectListener {

    private Context context;

    //返回
    private ImageView r_achievement_back;

    //下拉刷新
    private SwipeRefreshLayout achievement_swipe;
    //开始时间
    private TextView order_calender_start;
    //结束时间
    private TextView order_calender_stop;
    //销售
    private RelativeLayout achievement_sale;
    //销售排序
    private ImageView achievement_sale_picture;
    //达标
    private RelativeLayout achievement_achieve;
    //达标排序
    private ImageView achievement_achieve_picture;
    //列表
    private RecyclerView achievement_recycler;
    //合计销售
    private TextView achievement_sale_total;
    //合计目标额
    private TextView target_sale_total;
    //设置按钮
    private ImageView achievement_setting;

    //列表适配器
    private AchievementListAdapter adapter;

    //开始时间
    private String mStartTime;
    //结束时间
    private String mEndTime;
    //是否为开始时间
    private boolean isStartTime;
    //弹窗日历
    private CalendarDialog calendarDialog;

    //销售额排序升降，true为升，false为降
    private boolean isSaleUp;
    //达标排序升降，true为升，false为降
    private boolean isRateUp;

    //跳转业绩设置请求码
    private static final int SET_STAFF_ASSIG_REQUEST_CODE = 10001;

    //业绩列表数据
    private ArrayList<StaffReportData> staffReportDatas;

    //是否微店
    private boolean is_online ;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_achievement;
    }

    @Override
    public void initView() {

        context = this;

        r_achievement_back = (ImageView) findViewById(R.id.r_achievement_back);

        achievement_swipe = (SwipeRefreshLayout) findViewById(R.id.achievement_swipe);

        order_calender_start = (TextView) findViewById(R.id.order_calender_start);

        order_calender_stop = (TextView) findViewById(R.id.order_calender_stop);

        achievement_sale = (RelativeLayout) findViewById(R.id.achievement_sale);

        achievement_sale_picture = (ImageView) findViewById(R.id.achievement_sale_picture);

        achievement_achieve = (RelativeLayout) findViewById(R.id.achievement_achieve);

        achievement_achieve_picture = (ImageView) findViewById(R.id.achievement_achieve_picture);

        achievement_recycler = (RecyclerView) findViewById(R.id.achievement_recycler);

        achievement_sale_total = (TextView) findViewById(R.id.achievement_sale_total);

        target_sale_total = (TextView) findViewById(R.id.target_sale_total);

        achievement_setting = (ImageView) findViewById(R.id.achievement_setting);

        achievement_swipe.setColorSchemeColors(Color.RED);
    }

    @Override
    public void initData() {

        is_online = SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE);

        initDate();

        initAdapter();

        requestStaffReportList();
    }

    @Override
    public void initListener() {
        r_achievement_back.setOnClickListener(this);
        achievement_setting.setOnClickListener(this);
        achievement_swipe.setOnRefreshListener(this);
        order_calender_start.setOnClickListener(this);
        order_calender_stop.setOnClickListener(this);
        achievement_sale.setOnClickListener(this);
        achievement_achieve.setOnClickListener(this);
        adapter.setOnAchieveListListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.r_achievement_back:
                finish();
                break;
            case R.id.achievement_setting:
                Intent intent = new Intent();
                intent.setClass(context, RAchievementSetActivity.class);
                startActivityForResult(intent,SET_STAFF_ASSIG_REQUEST_CODE);
                break;
            case R.id.order_calender_start:
                onDateClick(true);
                break;
            case R.id.order_calender_stop:
                onDateClick(false);
                break;
            case R.id.achievement_sale:
                saleSort();
                break;
            case R.id.achievement_achieve:
                rateSort();
                break;
        }
    }

    @Override
    public void onRefresh() {
        requestStaffReportList();
    }

    private void initAdapter() {
        adapter = new AchievementListAdapter(context,is_online);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        achievement_recycler.setLayoutManager(linearLayoutManager);
        achievement_recycler.setAdapter(adapter);
    }

    @Override
    public void onAchieveClick(final StaffReportData staffReportData) {
        showLoading();
        RReportManager.getRReportManger().achieveDetail(this, staffReportData.czy, order_calender_start.getText().toString(), order_calender_stop.getText().toString() , new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                AchieveDetailData reportData = (AchieveDetailData) result;
                AchieveDetailDialog detailDialog = new AchieveDetailDialog(context, staffReportData,reportData);
                detailDialog.show();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }
        });
    }

    @Override
    public void onDateSelect(CustomDate date) {
        dateSelect(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_STAFF_ASSIG_REQUEST_CODE && resultCode == RESULT_OK){
            requestStaffReportList();
        }
    }

    private void initDate() {
        order_calender_start.setText(CalendarUtil.getFirstDay());
        order_calender_stop.setText(CalendarUtil.getTodayDate());
        mStartTime = CalendarUtil.timeStamp(CalendarUtil.getFirstDay() + " 00:00:00");
        mEndTime = CalendarUtil.timeStamp(CalendarUtil.getTodayDate() + " 23:59:59");
    }

    private void onDateClick(boolean b) {
        isStartTime = b;
        if (b){
            calendarDialog = new CalendarDialog(context,order_calender_start.getText().toString());
        }else {
            calendarDialog = new CalendarDialog(context,order_calender_stop.getText().toString());
        }
        calendarDialog.show();
        calendarDialog.setOnDateSelectListener(this);
    }

    private void dateSelect(CustomDate date) {
        if (date == null) return;
        String dateT = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
        if (isStartTime) {
            if (CalendarUtil.TimeCompare(dateT, order_calender_stop.getText().toString())) {
                order_calender_start.setText(dateT);
                mStartTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                calendarDialog.dismiss();
                requestStaffReportList();
            } else {
                Toast.show(context, "选择的时间范围不正确");
            }
        } else {
            if (CalendarUtil.TimeCompare(order_calender_start.getText().toString(), dateT)) {
                order_calender_stop.setText(dateT);
                mEndTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                calendarDialog.dismiss();
                requestStaffReportList();
            } else {
                Toast.show(context, "选择的时间范围不正确");
            }
        }
    }

    private void requestStaffReportList() {
        if (!achievement_swipe.isRefreshing()) achievement_swipe.setRefreshing(true);
        if (is_online){

        }else {
            RReportManager.getRReportManger().achievemence(this,order_calender_start.getText().toString(),order_calender_stop.getText().toString(), new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    achievement_swipe.setRefreshing(false);
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    achievement_swipe.setRefreshing(false);
                    achievement_sale_picture.setImageResource(R.mipmap.icon_default_sort);
                    achievement_achieve_picture.setImageResource(R.mipmap.icon_default_sort);
                    isSaleUp = false;
                    isRateUp = false;
                    if (adapter == null) {
                        achievement_swipe.setRefreshing(false);
                        return;
                    }
                    AllStaffReportData allStaffReportData = (AllStaffReportData) object;
                    if (allStaffReportData == null) {
                        staffReportDatas = null;
                        adapter.notifyAdapter(null);
                        achievement_sale_total.setText("0.00");
                        target_sale_total.setText("0.00");
                        achievement_swipe.setRefreshing(false);
                        return;
                    }
                    staffReportDatas = allStaffReportData.list;
                    adapter.notifyAdapter(allStaffReportData.list);
                    achievement_sale_total.setText(NumberFormatUtils.format(allStaffReportData.total.total_order_amounts));
                    target_sale_total.setText(allStaffReportData.total.total_assignments);
                    achievement_swipe.setRefreshing(false);
                }
            });
        }
    }

    private void saleSort() {
        achievement_achieve_picture.setImageResource(R.mipmap.icon_default_sort);
        if (isSaleUp) {
            achievement_sale_picture.setImageResource(R.mipmap.down_sort);
            isSaleUp = false;
            saleDownSort();
        } else {
            achievement_sale_picture.setImageResource(R.mipmap.icon_up_sort);
            isSaleUp = true;
            saleUpSort();
        }
    }

    private void rateSort() {
        achievement_sale_picture.setImageResource(R.mipmap.icon_default_sort);
        if (isRateUp) {
            achievement_achieve_picture.setImageResource(R.mipmap.down_sort);
            isRateUp = false;
            rateDownSort();
        } else {
            achievement_achieve_picture.setImageResource(R.mipmap.icon_up_sort);
            isRateUp = true;
            rateUpSort();
        }
    }

    //销售降序
    private void saleDownSort(){
        Collections.sort(staffReportDatas, new Comparator<StaffReportData>() {
            @Override
            public int compare(StaffReportData staffReportData, StaffReportData t1) {
                if (Double.parseDouble(staffReportData.order_amounts) > Double.parseDouble(t1.order_amounts)){
                    return -1;
                }else if (Double.parseDouble(staffReportData.order_amounts) < Double.parseDouble(t1.order_amounts)){
                    return 1;
                }else {
                    return 0;
                }
            }
        });
        if (adapter != null)
            adapter.notifyAdapter(staffReportDatas);
    }

    //销售升序
    private void saleUpSort(){
        Collections.sort(staffReportDatas, new Comparator<StaffReportData>() {
            @Override
            public int compare(StaffReportData staffReportData, StaffReportData t1) {
                if (Double.parseDouble(staffReportData.order_amounts) > Double.parseDouble(t1.order_amounts)){
                    return 1;
                }else if (Double.parseDouble(staffReportData.order_amounts) < Double.parseDouble(t1.order_amounts)){
                    return -1;
                }else {
                    return 0;
                }
            }
        });
        if (adapter != null)
            adapter.notifyAdapter(staffReportDatas);
    }

    //达标降序
    private void rateDownSort(){
        Collections.sort(staffReportDatas, new Comparator<StaffReportData>() {
            @Override
            public int compare(StaffReportData staffReportData, StaffReportData t1) {
                if (Double.parseDouble(staffReportData.completion_rate) > Double.parseDouble(t1.completion_rate)){
                    return -1;
                }else if (Double.parseDouble(staffReportData.completion_rate) < Double.parseDouble(t1.completion_rate)){
                    return 1;
                }else {
                    return 0;
                }
            }
        });
        if (adapter != null)
            adapter.notifyAdapter(staffReportDatas);
    }

    //达标升序
    private void rateUpSort(){
        Collections.sort(staffReportDatas, new Comparator<StaffReportData>() {
            @Override
            public int compare(StaffReportData staffReportData, StaffReportData t1) {
                if (Double.parseDouble(staffReportData.completion_rate) > Double.parseDouble(t1.completion_rate)){
                    return 1;
                }else if (Double.parseDouble(staffReportData.completion_rate) < Double.parseDouble(t1.completion_rate)){
                    return -1;
                }else {
                    return 0;
                }
            }
        });
        if (adapter != null)
            adapter.notifyAdapter(staffReportDatas);
    }

}
