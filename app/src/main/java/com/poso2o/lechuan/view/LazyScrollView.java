package com.poso2o.lechuan.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by mr zhang on 2018/3/22.
 *
 * 可监听滑动的ScrollView
 */

public class LazyScrollView extends ScrollView {

    private static final String tag="LazyScrollView";
    private Handler handler;
    private View view;
    public LazyScrollView(Context context) {
        super(context);
    }
    public LazyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public LazyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null)onScrollListener.onScroll(l,t,oldl,oldt);
    }

    /**
     * 定义接口
     * @author admin
     *
     */
    public interface OnScrollListener{
        void onScroll(int l, int t, int oldl, int oldt);
    }
    private OnScrollListener onScrollListener;
    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener=onScrollListener;
    }
}
