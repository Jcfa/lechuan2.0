package com.poso2o.lechuan.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.poso2o.lechuan.view.ViewPagerImageView;

import java.util.List;

/**
 * 案例
 * Created by Administrator on 2017-11-27.
 */

public class CaseViewPagerAdapter extends PagerAdapter {
    private List<ViewPagerImageView> mViewList;

    public CaseViewPagerAdapter(List<ViewPagerImageView> mViewList) {
        this.mViewList = mViewList;
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position).getRootView());
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position).getRootView());
    }

}
