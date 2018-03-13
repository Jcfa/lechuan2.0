package com.poso2o.lechuan.activity.orderinfo;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.base.BaseFragment;

/**
 * Created by ${cbf} on 2018/3/12 0012.
 * 畅销列表数据
 */

public class OrderInfoPaperFragment extends BaseFragment {
    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView tv = new TextView(getActivity());
        tv.setText("畅销界面");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(16);
        return tv;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
