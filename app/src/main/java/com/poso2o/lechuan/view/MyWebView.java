package com.poso2o.lechuan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class MyWebView extends WebView {
    public ScrollInterface mScrollInterface;
    public MyWebView (Context context) {
        super(context);
    }
    public MyWebView (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public MyWebView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //实时滑动监控
    //参数l代表滑动后当前位置，old代表原来原值
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mScrollInterface.onSChanged(l, t, oldl, oldt);
    }

    //供外部调用，监控滑动
    public void setOnCustomScroolChangeListener(ScrollInterface scrollInterface) {
        this.mScrollInterface = scrollInterface;
    }
    public interface ScrollInterface {
        public void onSChanged(int l, int t, int oldl, int oldt);
    }

}