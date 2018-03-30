package com.poso2o.lechuan.util;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.poso2o.lechuan.tool.print.Print;

/**
 * Created by mr zhang on 2018/3/29.
 * <p>
 * 可监听滑动的webview
 */

public class ScrollWebView extends WebView {

    public OnScrollChangeListener listener;

    public ScrollWebView(Context context) {
        super(context);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float webcontent = getContentHeight() * getScale();// webview的高度
        float webnow = getHeight() + getScrollY();// 当前webview的高度
        if (webcontent - webnow < 1) {
            // 已经处于底端
            if (listener != null)listener.onPageEnd(l, t, oldl, oldt);
        } else if (getScrollY() == 0) {
            // Log.i("TAG1", "已经处于顶端");
            if (listener != null)listener.onPageTop(l, t, oldl, oldt);
        } else if (webcontent - webnow > 3){
            if (listener != null)listener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.listener = listener;
    }

    public interface OnScrollChangeListener {
        void onPageEnd(int l, int t, int oldl, int oldt);

        void onPageTop(int l, int t, int oldl, int oldt);

        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
