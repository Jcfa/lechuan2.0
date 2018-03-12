package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * Created by mr zhang on 2017/12/8.
 */

public class RShopDesActivity extends BaseActivity {

    private Context context;

    //返回
    private ImageView des_shop_back;
    //前往购买
    private TextView go_to_buy;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_r_shop_des;
    }

    @Override
    protected void initView() {
        context = this;

        des_shop_back = (ImageView) findViewById(R.id.des_shop_back);
        go_to_buy = (TextView) findViewById(R.id.go_to_buy);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        des_shop_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        go_to_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("Android.intent.action.VIEW");
                Uri uri = Uri.parse("https://item.taobao.com/item.htm?spm=a1z10.1-c.w5003-16830243018.1.282abec6CYA2MI&id=528188323659&scene=taobao_shop");
                intent.setData(uri);
                intent.setClassName("com.taobao.taobao","com.taobao.tao.detail.activity.DetailActivity");
                startActivity(intent);
            }
        });
    }
}
