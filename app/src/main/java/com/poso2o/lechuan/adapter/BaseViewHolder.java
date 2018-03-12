package com.poso2o.lechuan.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.poso2o.lechuan.util.ScreenInfo;

/**
 * Created by Jaydon on 2017/8/15.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    public View itemView;

    public boolean touchFlg = false;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    /**
     * 请输入dp值
     * 需要设置根布局id为layout_group
     */
    public void setPadding(int left, int top, int right, int bottom) {
        left = ScreenInfo.dpCpx(left);
        top = ScreenInfo.dpCpx(top);
        right = ScreenInfo.dpCpx(right);
        bottom = ScreenInfo.dpCpx(bottom);
        itemView.setPadding(left, top, right, bottom);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int resId) {
        return (T) itemView.findViewById(resId);
    }

    public void onItemSelected() {
        itemView.setBackgroundColor(Color.GRAY);
    }

    public void onItemClear() {
        itemView.setBackgroundColor(Color.WHITE);
    }

}
