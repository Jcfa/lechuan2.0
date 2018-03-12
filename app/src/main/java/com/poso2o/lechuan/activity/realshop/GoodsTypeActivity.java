package com.poso2o.lechuan.activity.realshop;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.configs.Constant;

/**
 * 选择销售类型
 * Created by Jaydon on 2017/8/18.
 */
public class GoodsTypeActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 服饰类
     */
    private View type_clothing_type;
    private TextView type_clothing_type_name;

    /**
     * 百货类
     */
    private View type_shop_type;
    private TextView type_shop_type_name;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_sale_type;
    }

    @Override
    public void initView() {
        type_clothing_type = findViewById(R.id.type_clothing_type);
        type_clothing_type_name = (TextView) findViewById(R.id.type_clothing_type_name);
        type_shop_type = findViewById(R.id.type_shop_type);
        type_shop_type_name = (TextView) findViewById(R.id.type_shop_type_name);
    }

    @Override
    public void initData() {
        int sale_type = getIntent().getIntExtra(Constant.TYPE, 1);
        if (sale_type == 2) {
            type_clothing_type_name.setTextColor(0xFF262626);
            type_shop_type_name.setTextColor(0xFFFF6537);
        }
    }

    @Override
    public void initListener() {
        findViewById(R.id.type_back).setOnClickListener(this);
        type_clothing_type.setOnClickListener(this);
        type_shop_type.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        int i = view.getId();
        if (i == R.id.type_back) {
            finish();

        } else if (i == R.id.type_clothing_type) {
            intent.putExtra(Constant.TYPE, 1);
            setResult(RESULT_OK, intent);
            finish();

        } else if (i == R.id.type_shop_type) {
            intent.putExtra(Constant.TYPE, 2);
            setResult(RESULT_OK, intent);
            finish();

        }
    }
}
