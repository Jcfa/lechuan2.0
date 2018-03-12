package com.poso2o.lechuan.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by mr zhang on 2017/11/4.
 */

public class MyLinearLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnabled = true;

    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean enable){
        this.isScrollEnabled = enable;
    }

    @Override
    public boolean canScrollVertically() {
        return super.canScrollVertically() && isScrollEnabled;
    }
}
