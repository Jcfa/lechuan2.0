package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.poso2o.lechuan.base.BaseView;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/10/23.
 */

public class ArticlePagerAdapter extends PagerAdapter {

    private ArrayList<BaseView> tagViews;

    public ArticlePagerAdapter(ArrayList<BaseView> tagViews) {
        this.tagViews = tagViews;
    }

    @Override
    public int getCount() {
        if (tagViews == null)return 0;
        return tagViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(tagViews.get(position).getRootView());
        return tagViews.get(position).getRootView();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(tagViews.get(position).getRootView());
    }
}
