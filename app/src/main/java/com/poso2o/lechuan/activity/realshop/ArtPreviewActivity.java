package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.oa.ArticleAdActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.bean.articledata.ArticleData;
import com.poso2o.lechuan.tool.print.Print;

/**
 * Created by mr zhang on 2017/12/5.
 */

public class ArtPreviewActivity extends BaseActivity {

    private Context context;

    //返回
    private ImageView art_preview_back;
    //网页
    private WebView art_preview_web;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_art_preview;
    }

    @Override
    protected void initView() {
        context = this;

        art_preview_back = (ImageView) findViewById(R.id.art_preview_back);
        art_preview_web = (WebView) findViewById(R.id.art_preview_web);
    }

    @Override
    protected void initData() {
        WebSettings settings = art_preview_web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        art_preview_web.loadUrl("file:///android_asset/android_web_view.html");
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        final String str = (String) bundle.get(ArticleAdActivity.ART_DATA);
        art_preview_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) art_preview_web.loadUrl("javascript:setBODYHTML('" + str + "')");
            }
        });
    }

    @Override
    protected void initListener() {
        art_preview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (art_preview_web != null){
            art_preview_web.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            art_preview_web.clearHistory();
            ((ViewGroup)art_preview_web.getParent()).removeView(art_preview_web);
            art_preview_web.destroy();
            art_preview_web = null;
        }
        super.onDestroy();
    }
}
