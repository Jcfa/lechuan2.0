package com.poso2o.lechuan.tool.recycler;

import android.support.v7.widget.RecyclerView;

/**
 * 列表滑动到底部监听
 * Created by Jaydon on 2017/10/31.
 */
public abstract class OnSlideToBottomListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (isSlideToBottom(recyclerView)) {
            slideToBottom();
        }
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    protected abstract void slideToBottom();
}
