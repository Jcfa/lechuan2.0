package com.poso2o.lechuan.activity.wshop;

import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * 微店
 *
 * Created by Jaydon on 2018/3/13.
 */
public class VdianActivity extends BaseActivity {

    /**
     * 商品搜索
     */
    private LinearLayout shop_search_group;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_vdian;
    }

    @Override
    protected void initView() {
        shop_search_group = findView(R.id.shop_search_group);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
