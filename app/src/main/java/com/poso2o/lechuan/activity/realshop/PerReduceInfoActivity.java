package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.marketdata.PerReduceData;
import com.poso2o.lechuan.bean.marketdata.PromotionDetailData;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.DateTimeUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.PerReduceView;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

/**
 * Created by mr zhang on 2017/11/2.
 */

public class PerReduceInfoActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //返回
    private ImageView per_reduce_back;
    //确定
    private Button per_reduce_ok;

    //活动名称
    private EditText per_reduce_name;
    //开始时间
    private TextView per_reduce_begin;
    //结束时间
    private TextView per_reduce_end;

    //活动折扣
    private LinearLayout per_reduce_container;
    //添加下一级
    private Button per_reduce_add;

    //是否选择开始时间
    private boolean isBeginTime;
    //开始时间
    private String beginTime;
    //结束时间
    private String endTime;

    //满减活动对象
    private PerReduceData perReduceData;

    //偏好设置缓存
//    private SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_per_reduce;
    }

    @Override
    public void initView() {
        context = this;

        per_reduce_back = (ImageView) findViewById(R.id.per_reduce_back);
        per_reduce_ok = (Button) findViewById(R.id.per_reduce_ok);

        per_reduce_name = (EditText) findViewById(R.id.per_reduce_name);
        per_reduce_begin = (TextView) findViewById(R.id.per_reduce_begin);
        per_reduce_end = (TextView) findViewById(R.id.per_reduce_end);

        per_reduce_container = (LinearLayout) findViewById(R.id.per_reduce_container);
        per_reduce_add = (Button) findViewById(R.id.per_reduce_add);

        per_reduce_ok.setSelected(true);
        per_reduce_ok.setText("修改");
    }

    @Override
    public void initData() {
//        sharedPreferencesUtil = new SharedPreferencesUtil(context, LoginMemory.LOGIN_INFO);

        initInfo();
    }

    @Override
    public void initListener() {
        per_reduce_back.setOnClickListener(this);
        per_reduce_ok.setOnClickListener(this);

        per_reduce_begin.setOnClickListener(this);
        per_reduce_end.setOnClickListener(this);

        per_reduce_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.per_reduce_back:
                finish();
                break;
            case R.id.per_reduce_ok:
                editReduce();
                break;
            case R.id.per_reduce_begin:
                isBeginTime = true;
                showCalendar();
                break;
            case R.id.per_reduce_end:
                isBeginTime = false;
                showCalendar();
                break;
            case R.id.per_reduce_add:
                addPerReduce();
                break;
        }
    }

    private void initInfo(){
        perReduceData = (PerReduceData) getIntent().getExtras().get(RMarketActivity.REDUCE_INFO);
        if (perReduceData == null)return;
        per_reduce_name.setText(perReduceData.promotion_name);

        beginTime = perReduceData.begin_time;
        endTime = perReduceData.end_time;
        per_reduce_begin.setText(DateTimeUtil.StringToData(beginTime,"yyyy-MM-dd"));
        per_reduce_end.setText(DateTimeUtil.StringToData(endTime,"yyyy-MM-dd"));

        for (PromotionDetailData detailData : perReduceData.promotionDetails){
            final PerReduceView perReduceView = new PerReduceView(context,detailData);
            per_reduce_container.addView(perReduceView.getRootView());
            perReduceView.setAmount(detailData.amount_moq,detailData.delete_amount);
            perReduceView.setOnDelPerReduceListener(new PerReduceView.OnDelPerReduceListener() {
                @Override
                public void onDelPerReduce(View view, PromotionDetailData promotionDetailData) {
                    perReduceData.promotionDetails.remove(promotionDetailData);
                    per_reduce_container.removeView(view);
                }
            });
        }
    }

    //创建新满减
    private void addPerReduce() {
        PromotionDetailData detailData = perReduceData.promotionDetails.get(perReduceData.promotionDetails.size() - 1);
        if (detailData.delete_amount == null || detailData.amount_moq == null || detailData.amount_moq.equals("-0.01") || detailData.delete_amount.equals("-0.01")) {
            Toast.show(context, "请先完成上一级的满减输入");
            return;
        }
        if (perReduceData.promotionDetails.size() > 1) {
            PromotionDetailData pre = perReduceData.promotionDetails.get(perReduceData.promotionDetails.size() - 2);
            PromotionDetailData next = perReduceData.promotionDetails.get(perReduceData.promotionDetails.size() - 1);
            if (Double.parseDouble(pre.amount_moq) >= Double.parseDouble(next.amount_moq) || Double.parseDouble(pre.delete_amount) >= Double.parseDouble(next.delete_amount)) {
                Toast.show(context, "高一级的消费金额和立减金额应大于低一级。");
                return;
            }
        }
        PromotionDetailData promotionDetailData = new PromotionDetailData();
        promotionDetailData.no = System.currentTimeMillis() + "";
        perReduceData.promotionDetails.add(promotionDetailData);
        PerReduceView newReduce = new PerReduceView(context, promotionDetailData);
        per_reduce_container.addView(newReduce.getRootView());
        newReduce.setOnDelPerReduceListener(new PerReduceView.OnDelPerReduceListener() {
            @Override
            public void onDelPerReduce(View view, PromotionDetailData promotionDetailData) {
                per_reduce_container.removeView(view);
                perReduceData.promotionDetails.remove(promotionDetailData);
            }
        });
    }

    //选择时间
    private void showCalendar() {
        final CalendarDialog calendarDialog = new CalendarDialog(context);
        calendarDialog.show();
        calendarDialog.setOnDateSelectListener(new CalendarDialog.OnDateSelectListener() {
            @Override
            public void onDateSelect(CustomDate date) {
                if (date == null) return;
                String dateT = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                if (isBeginTime) {
                    String str = per_reduce_end.getText().toString();
                    if (str == null || str.equals("")) {
                        per_reduce_begin.setText(dateT);
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        perReduceData.begin_time = beginTime;
                        calendarDialog.dismiss();
                    } else if (CalendarUtil.TimeCompare(dateT, str)) {
                        per_reduce_begin.setText(dateT);
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        perReduceData.begin_time = beginTime;
                        calendarDialog.dismiss();
                    } else {
                        Toast.show(context, "选择的时间范围不正确");
                    }
                } else {
                    String str = per_reduce_begin.getText().toString();
                    if (str == null || str.equals("")) {
                        per_reduce_end.setText(dateT);
                        endTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                        perReduceData.end_time = endTime;
                        calendarDialog.dismiss();
                    } else if (CalendarUtil.TimeCompare(str, dateT)) {
                        per_reduce_end.setText(dateT);
                        endTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                        perReduceData.end_time = endTime;
                        calendarDialog.dismiss();
                    } else {
                        Toast.show(context, "选择的时间范围不正确");
                    }
                }
            }
        });
    }

    //修改满减
    private void editReduce(){
        perReduceData.promotion_name = per_reduce_name.getText().toString();
        if (perReduceData.promotion_name == null || perReduceData.promotion_name.equals("")) {
            Toast.show(context, "请输入活动名称");
            per_reduce_name.requestFocus();
            return;
        }
        if (perReduceData.begin_time == null || perReduceData.begin_time.equals("")) {
            Toast.show(context, "请选择开始时间");
            return;
        }
        if (perReduceData.end_time == null || perReduceData.end_time.equals("")) {
            Toast.show(context, "请选择结束时间");
            return;
        }
        PromotionDetailData detailData = perReduceData.promotionDetails.get(perReduceData.promotionDetails.size() - 1);
        if (detailData.delete_amount == null || detailData.amount_moq == null || detailData.amount_moq.equals("-0.01") || detailData.delete_amount.equals("-0.01")) {
            Toast.show(context, "请完整输入消费金额和减免金额");
            return;
        }
        if (perReduceData.promotionDetails.size() > 1) {
            PromotionDetailData pre = perReduceData.promotionDetails.get(perReduceData.promotionDetails.size() - 2);
            PromotionDetailData next = perReduceData.promotionDetails.get(perReduceData.promotionDetails.size() - 1);
            if (Double.parseDouble(pre.amount_moq) >= Double.parseDouble(next.amount_moq) || Double.parseDouble(pre.delete_amount) >= Double.parseDouble(next.delete_amount)) {
                Toast.show(context, "高一级的消费金额和立减金额应大于低一级。");
                return;
            }
        }

        /*showReadDialog();
        MarketPRManager.getMarketPRManager().editPerReduce(this, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), perReduceData, new MarketPRManager.OnEditPerReduceListener() {
            @Override
            public void onEdiPerReduceSuccess(BaseActivity baseActivity) {
                dismissReadDialog();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onEdiPerReduceFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(context,failStr).show();
            }
        });*/
    }
}
