package com.poso2o.lechuan.activity.wopenaccount;

import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class StardOpenActivity extends BaseActivity {
    private TextView tv_title;
    //获取已缴费，开通状态信息
    private TextView tv_wopen_stard_service_name,tv_wopen_stard_amount,tv_wopen_stard_payment;
    private TextView tv_wopen_stard_attn,tv_wopen_stard_mobile;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wopen_stard_open;
    }

    @Override
    protected void initView() {
        tv_title=(TextView)findViewById(R.id.tv_title);
    }

    @Override
    protected void initData() {
//        tv_title.setText(getResources().getString(R.string.wopen_number));
        tv_title.setTextColor(getResources().getColor(R.color.text_type));

        String amount=getIntent().getStringExtra("amount");
        String service_name=getIntent().getStringExtra("service_name");
        String payment_time=getIntent().getStringExtra("payment_time");
        String attn=getIntent().getStringExtra("attn");
        String mobile=getIntent().getStringExtra("mobile");

        tv_wopen_stard_attn.setText(attn);
        tv_wopen_stard_mobile.setText(mobile);
        tv_wopen_stard_service_name.setText(service_name);
        tv_wopen_stard_payment.setText(payment_time);
        tv_wopen_stard_amount.setText(amount);
    }

    @Override
    protected void initListener() {

    }
}
