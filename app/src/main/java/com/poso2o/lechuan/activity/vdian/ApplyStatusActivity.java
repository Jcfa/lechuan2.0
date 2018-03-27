package com.poso2o.lechuan.activity.vdian;

import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.TimeUtil;

/**
 * 开通状态
 *
 * Created by Administrator on 2018/3/14 0014.
 */
public class ApplyStatusActivity extends BaseActivity {

    /**
     * 获取已缴费，开通状态信息
     */
    private TextView apply_status_service_name, apply_status_amount, apply_status_payment;
    private TextView apply_status_attn, apply_status_mobile;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_status;
    }

    @Override
    protected void initView() {
        apply_status_service_name = findView(R.id.apply_status_service_name);
        apply_status_amount = findView(R.id.apply_status_amount);
        apply_status_payment = findView(R.id.apply_status_payment);
        apply_status_attn = findView(R.id.apply_status_attn);
        apply_status_mobile = findView(R.id.apply_status_mobile);
    }

    @Override
    protected void initData() {
        setTitle("开通公众号");

        String amount = getIntent().getStringExtra("amount");
        String service_name = getIntent().getStringExtra("service_name");
        String payment_time = getIntent().getStringExtra("payment_time");
        String attn = getIntent().getStringExtra("attn");
        String mobile = getIntent().getStringExtra("mobile");

        apply_status_attn.setText(attn);
        apply_status_mobile.setText(mobile);
        apply_status_service_name.setText(service_name);
        apply_status_payment.setText(TimeUtil.longToDateString(NumberUtils.toLong(payment_time),"yyyy-MM-dd HH:mm:ss"));
        apply_status_amount.setText(amount);
    }

    @Override
    protected void initListener() {

    }
}
