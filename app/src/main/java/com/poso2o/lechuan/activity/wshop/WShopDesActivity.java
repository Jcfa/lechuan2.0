package com.poso2o.lechuan.activity.wshop;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.realshop.CompanyDefineActivity;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * Created by mr zhang on 2017/12/5.
 */

public class WShopDesActivity extends BaseActivity {

    //企业认证跳转码
    private static final int COMPANY_DEFINE_CODE = 0001;

    private Context context;

    //返回
    private ImageView wc_des_back;
    //下一步
    private TextView wc_des_next;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wc_shop_des;
    }

    @Override
    protected void initView() {
        context = this;

        wc_des_back = (ImageView) findViewById(R.id.wc_des_back);
        wc_des_next = (TextView) findViewById(R.id.wc_des_next);

        wc_des_next.setSelected(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        wc_des_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        wc_des_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context,CompanyDefineActivity.class);
//                startActivityForResult(intent,COMPANY_DEFINE_CODE);
                startActivity(intent);
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == COMPANY_DEFINE_CODE){
            setResult(RESULT_OK);
            finish();
        }
    }*/
}
