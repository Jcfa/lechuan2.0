/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.fragment.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poso2o.lechuan.base.BaseFragment;


/**
 * 通用阴影背景
 *
 * @author 郑洁东
 * @date 创建时间：2016-10-24
 */
@SuppressWarnings("deprecation")
public class PlaceholderFragment extends BaseFragment {

    private View.OnClickListener onClickListener;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = new View(getActivity());
        view.setBackgroundColor(0x7F000000);
        view.setOnClickListener(onClickListener);
        return view;
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

    public void setOnClickListener(View.OnClickListener l) {
        onClickListener = l;
    }
}