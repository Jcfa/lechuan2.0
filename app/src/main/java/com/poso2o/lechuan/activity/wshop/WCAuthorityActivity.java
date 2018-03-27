package com.poso2o.lechuan.activity.wshop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;

/**
 * Created by mr zhang on 2017/12/5.
 */

public class WCAuthorityActivity extends BaseActivity {

    //绑定类型：一、微信公众号绑定；二、资讯公众号绑定
    public static final String BIND_TYPE = "bind_type";

    private Context context;

    //返回
    private ImageView wechat_authorize_back;
    //网页
    private WebView authorize_wechat;

    //应对方案以及服务端返回数据的特殊情况制定的反人类的解决方法
    private String mUrl = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_authorize_wechat;
    }

    @Override
    protected void initView() {
        wechat_authorize_back = (ImageView) findViewById(R.id.wechat_authorize_back);
        authorize_wechat = (WebView) findViewById(R.id.authorize_wechat);
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initData() {
        authorize_wechat.getSettings().setJavaScriptEnabled(true);//支持js
        authorize_wechat.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持弹窗

        authorize_wechat.getSettings().setUseWideViewPort(true);//将图片调整到webview大小
        authorize_wechat.getSettings().setLoadWithOverviewMode(true);//缩放至屏幕大小

        authorize_wechat.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片
        authorize_wechat.getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式

        authorize_wechat.requestFocus();
        authorize_wechat.getSettings().setSupportZoom(true);
        authorize_wechat.getSettings().setLoadWithOverviewMode(true);
        authorize_wechat.getSettings().setDomStorageEnabled(true);

        //缩放操作
        authorize_wechat.getSettings().setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        authorize_wechat.getSettings().setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        authorize_wechat.getSettings().setDisplayZoomControls(false); //隐藏原生的缩放控件

        authorize_wechat.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

        if (Build.VERSION.SDK_INT >= 21) {
            authorize_wechat.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            mUrl = HttpAPI.SERVER_MAIN_API + "casparManage.htm?Act=goAuthor&shop_id=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        } else {
            int type = bundle.getInt(BIND_TYPE);
            if (type == 1) {
                mUrl = HttpAPI.SERVER_MAIN_API + "casparManage.htm?Act=goAuthor&shop_id=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID) + "&has_news=1";
            } else {
                mUrl = HttpAPI.SERVER_MAIN_API + "casparManage.htm?Act=goAuthor&shop_id=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID) + "&has_news=" + type;
            }
        }
        authorize_wechat.loadUrl(mUrl);
        authorize_wechat.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mUrl = url;
                if (!url.contains("https")) {
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:window.local_obj.showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }
        });
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            if (html.contains("SUCCESS")) {
                WCAuthorityActivity.this.setResult(RESULT_OK);
                WCAuthorityActivity.this.finish();
            }
        }
    }

    @Override
    public void initListener() {
        wechat_authorize_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
