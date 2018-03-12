package com.poso2o.lechuan.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017-12-07.
 */

public class MyTabLayout extends LinearLayout {
    public MyTabLayout(Context context) {
        super(context);
        init();
    }

    public MyTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
    }
}
