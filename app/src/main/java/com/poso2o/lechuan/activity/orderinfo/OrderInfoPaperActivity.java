package com.poso2o.lechuan.activity.orderinfo;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * Created by ${cbf} on 2018/3/14 0014.
 * 库存管理
 */

public class OrderInfoPaperActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_paper;
    }

    @Override
    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);

    }

    @Override
    protected void initData() {
        ivBack.setImageResource(R.drawable.actionbar_back);

    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                startActivity(new Intent(OrderInfoPaperActivity.this, OrderInfoMainActivity.class));
                finish();
                break;
        }
    }
}
