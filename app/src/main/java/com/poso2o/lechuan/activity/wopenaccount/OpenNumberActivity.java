package com.poso2o.lechuan.activity.wopenaccount;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class OpenNumberActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title;
    private Button bt_wopen_number_pay;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wopen_open_number;
    }

    @Override
    protected void initView() {
        tv_title=(TextView)findViewById(R.id.tv_title);
        bt_wopen_number_pay=(Button)findViewById(R.id.bt_wopen_number_pay);
    }

    @Override
    protected void initData() {
        tv_title.setText(getResources().getString(R.string.wopen_number));
        tv_title.setTextColor(getResources().getColor(R.color.text_type));
    }

    @Override
    protected void initListener() {
        bt_wopen_number_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //发起缴费
            case R.id.bt_wopen_number_pay:
                Intent i=new Intent();
                i.setClass(OpenNumberActivity.this,ServiceOrderActivity.class);
                startActivity(i);
                break;
        }
    }
}
