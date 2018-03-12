package com.poso2o.lechuan.tool.listener;

import android.view.View;

import java.util.Calendar;

/**
 * Created by lenovo on 2016/12/9.
 */

public abstract class NoDoubleClickListener implements View.OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }
    public  abstract void onNoDoubleClick(View v);

}
