package com.poso2o.lechuan.activity.official;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.oa.TemplateBean;

/**
 * Created by mr zhang on 2018/2/8.
 * 模板实例
 */

public class ModelInfoActivity extends BaseActivity {

    public static final String MODEL_INFO_DATA = "model_info";

    private WebView model_info_web;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_model_info;
    }

    @Override
    protected void initView() {
        model_info_web = (WebView) findViewById(R.id.model_info_web);
    }

    @Override
    protected void initData() {
        WebSettings settings = model_info_web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        model_info_web.loadUrl("file:///android_asset/android_web_view.html");

        initModelInfo();
    }

    @Override
    protected void initListener() {
        //返回
        findViewById(R.id.model_info_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initModelInfo(){
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)return;
        TemplateBean bean = (TemplateBean) bundle.get(MODEL_INFO_DATA);
        if (bean == null)return;
        final String str = bean.content;
        model_info_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) model_info_web.loadUrl("javascript:setBODYHTML('" + str + "')");
            }
        });
    }
}
