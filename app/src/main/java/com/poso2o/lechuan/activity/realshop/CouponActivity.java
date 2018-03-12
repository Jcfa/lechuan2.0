package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

/**
 * Created by mr zhang on 2017/11/2.
 * 优惠券
 */

public class CouponActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //返回
    private ImageView coupon_back;
    //确定
    private Button coupon_ok;

    //活动名称
    private EditText coupon_name;
    //卡券金额
    private EditText coupon_amount;
    //使用条件
    private EditText coupon_use_amount;
    //发券总数
    private EditText coupon_total_num;
    //每人限领
    private EditText coupon_num_person;

    //开始时间
    private TextView coupon_begin;
    //结束时间
    private TextView coupon_end;

    //店铺信息缓存
//    private SharedPreferencesUtil sharedPreferencesUtil ;
    //点击的是开始时间还是结束时间
    private boolean isBeginTime;
    //开始时间
    private String beginTime;
    //结束时间
    private String endTime;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_coupon;
    }

    @Override
    public void initView() {
        context = this;

        coupon_back = (ImageView) findViewById(R.id.coupon_back);
        coupon_ok = (Button) findViewById(R.id.coupon_ok);

        coupon_name = (EditText) findViewById(R.id.coupon_name);
        coupon_amount = (EditText) findViewById(R.id.coupon_amount);
        coupon_use_amount = (EditText) findViewById(R.id.coupon_use_amount);
        coupon_total_num = (EditText) findViewById(R.id.coupon_total_num);
        coupon_num_person = (EditText) findViewById(R.id.coupon_num_person);

        coupon_begin = (TextView) findViewById(R.id.coupon_begin);
        coupon_end = (TextView) findViewById(R.id.coupon_end);

        coupon_ok.setSelected(true);
    }

    @Override
    public void initData() {
//        sharedPreferencesUtil = new SharedPreferencesUtil(context, LoginMemory.LOGIN_INFO);
    }

    @Override
    public void initListener() {
        coupon_back.setOnClickListener(this);
        coupon_ok.setOnClickListener(this);

        coupon_begin.setOnClickListener(this);
        coupon_end.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.coupon_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.coupon_ok:
                addCoupon();
                break;
            case R.id.coupon_begin:
                isBeginTime = true;
                showCalendar();
                break;
            case R.id.coupon_end:
                isBeginTime = false;
                showCalendar();
                break;
        }
    }

    //选择时间
    private void showCalendar(){
        final CalendarDialog calendarDialog = new CalendarDialog(context);
        calendarDialog.show();
        calendarDialog.setOnDateSelectListener(new CalendarDialog.OnDateSelectListener() {
            @Override
            public void onDateSelect(CustomDate date) {
                if (date == null) return;
                String dateT = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                if (isBeginTime) {
                    String str = coupon_end.getText().toString();
                    if (str == null || str.equals("")) {
                        coupon_begin.setText(dateT);
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        calendarDialog.dismiss();
                    } else if (CalendarUtil.TimeCompare(dateT, str)) {
                        coupon_begin.setText(dateT);
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        calendarDialog.dismiss();
                    } else {
                        Toast.show(context, "选择的时间范围不正确");
                    }
                } else {
                    String str = coupon_begin.getText().toString();
                    if (str == null || str.equals("")) {
                        coupon_end.setText(dateT);
                        endTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                        calendarDialog.dismiss();
                    } else if (CalendarUtil.TimeCompare(str, dateT)) {
                        coupon_end.setText(dateT);
                        endTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                        calendarDialog.dismiss();
                    } else {
                        Toast.show(context, "选择的时间范围不正确");
                    }
                }
            }
        });
    }

    //发布卡券
    public void addCoupon(){
        String name = coupon_name.getText().toString();
        if (name == null || name.equals("")){
            Toast.show(context, "请输入活动名称");
            coupon_name.requestFocus();
            return;
        }
        String amount = coupon_amount.getText().toString();
        if (amount == null || amount.equals("")){
            Toast.show(context, "请输入优惠券金额");
            coupon_amount.requestFocus();
            return;
        }else if (amount.startsWith(".")){
            amount = "0" + amount;
        }else if (amount.endsWith(".")){
            amount = amount + "00";
        }
        String use = coupon_use_amount.getText().toString();
        if (use == null || use.equals("")){
            Toast.show(context, "请输入使用条件金额");
            coupon_use_amount.requestFocus();
            return;
        }else if (use.startsWith(".")){
            use = "0" + use;
        }else if (use.endsWith(".")){
            use = use + "00";
        }
        String total_num = coupon_total_num.getText().toString();
        if (total_num == null || total_num.equals("")){
            Toast.show(context, "请输入优惠券总数量");
            coupon_total_num.requestFocus();
            return;
        }
        String per_num = coupon_num_person.getText().toString();
        if (per_num == null || per_num.equals("")){
            Toast.show(context, "请输入限领张数");
            coupon_num_person.requestFocus();
            return;
        }
        if (beginTime == null || beginTime.equals("")){
            Toast.show(context, "请选择开始时间");
            return;
        }
        if (endTime == null || endTime.equals("")){
            Toast.show(context, "请选择结束时间");
            return;
        }

        /*showReadDialog();
        CouponManager.getCouponManager().addCoupon(this, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), name, use, total_num, amount, per_num, beginTime, endTime, new CouponManager.OnAddCouponListener() {
            @Override
            public void onAddCouponSuccess(BaseActivity baseActivity) {
                dismissReadDialog();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onAddCouponFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(baseActivity,failStr).show();
            }
        });*/
    }
}
