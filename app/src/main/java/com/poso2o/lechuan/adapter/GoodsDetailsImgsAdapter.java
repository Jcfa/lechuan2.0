package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import com.poso2o.lechuan.view.GoodsDetailsImgsItemView;

import java.util.ArrayList;

/**
 *
 * Created by lenovo on 2016/12/21.
 */
public class GoodsDetailsImgsAdapter extends PagerAdapter {

    private ArrayList<GoodsDetailsImgsItemView> viewDatas;

    public GoodsDetailsImgsAdapter(Context context, ArrayList<GoodsDetailsImgsItemView> viewDatas) {
        this.viewDatas = new ArrayList<>();
        if (viewDatas != null) {
            this.viewDatas.addAll(viewDatas);
        }
    }

    @Override
    public int getCount() {
        return viewDatas == null ? 0 : viewDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewDatas.get(position).getRootView());
        return viewDatas.get(position).getRootView();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(viewDatas.get(position).getRootView());// 删除页卡 
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    public void notify(ArrayList<GoodsDetailsImgsItemView> viewDatas) {
        this.viewDatas.clear();
        if (viewDatas != null) {
            this.viewDatas.addAll(viewDatas);
        }
        notifyDataSetChanged();
    }

}
