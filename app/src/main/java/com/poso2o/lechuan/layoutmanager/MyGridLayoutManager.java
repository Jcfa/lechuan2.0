package com.poso2o.lechuan.layoutmanager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created by mr zhang on 2017/7/29.
 */

public class MyGridLayoutManager extends GridLayoutManager {

    private boolean isScrollEnabled = true;

    public MyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        isScrollEnabled = scrollEnabled;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
