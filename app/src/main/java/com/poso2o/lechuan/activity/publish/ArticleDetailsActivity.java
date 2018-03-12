package com.poso2o.lechuan.activity.publish;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.bean.news.News;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommonDialog;
import com.poso2o.lechuan.dialog.PublishSucceedDialog;
import com.poso2o.lechuan.manager.news.NewsManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.poso2o.lechuan.activity.publish.PublishActivity.PUBLISH_TYPE;
import static com.poso2o.lechuan.activity.publish.PublishActivity.SELECT_ARTICLE;
import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 文章详情视图
 */
public class ArticleDetailsActivity extends BaseActivity implements OnClickListener {

    /**
     * 添加广告请求码
     */
    private static final int REQUEST_ADD_AD = 1;

    /**
     * 文章内容
     */
    private Article article;

    /**
     * 发布内容
     */
    private News news;

    /**
     * 网页视图
     */
    private WebView article_details_web;

    /**
     * 发布按钮
     */
    private TextView article_details_publish;

    /**
     * 添加广告
     */
    private TextView article_details_add_ad;

    /**
     * 底部广告详情
     */
    private View article_details_ad_group;// 图文
    private ImageView article_details_ad_img;// 图片
    private TextView article_details_ad_edit;// 编辑按钮
    private TextView article_details_ad_describe;// 描述
    private View article_details_ad_goods_info;// 商品
    private ImageView article_details_ad_goods_icon;// 商品图片
    private TextView article_details_ad_goods_name;// 商品名称
    private TextView article_details_ad_goods_price;// 商品价格
    private ImageView article_details_ad_goods_cancel;// 取消商品
    private View article_details_ad_redbag_info;// 红包
    private TextView article_details_ad_redbag_tag;// 红包标签
    private ImageView article_details_ad_redbag_cancel;// 取消红包

    /**
     * 滚动视图
     */
    private ScrollView article_details_scroll;// 红包

    /**
     * 底部功能条
     */
    private View article_details_bottom_bar;
    private TextView article_details_publish2;// 发布

    @Override
    public int getLayoutResId() {
        return R.layout.activity_article_details;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        // 滚动控制
        article_details_web = findView(R.id.article_details_web);
        article_details_publish = findView(R.id.article_details_publish);
        article_details_add_ad = findView(R.id.article_details_add_ad);

        article_details_ad_group = findView(R.id.article_details_ad_group);
        article_details_ad_img = findView(R.id.article_details_ad_img);
        article_details_ad_edit = findView(R.id.article_details_ad_edit);
        article_details_ad_describe = findView(R.id.article_details_ad_describe);
        article_details_ad_goods_info = findView(R.id.article_details_ad_goods_info);
        article_details_ad_goods_icon = findView(R.id.article_details_ad_goods_icon);
        article_details_ad_goods_name = findView(R.id.article_details_ad_goods_name);
        article_details_ad_goods_price = findView(R.id.article_details_ad_goods_price);
        article_details_ad_goods_cancel = findView(R.id.article_details_ad_goods_cancel);
        article_details_ad_redbag_info = findView(R.id.article_details_ad_redbag_info);
        article_details_ad_redbag_tag = findView(R.id.article_details_ad_redbag_tag);
        article_details_ad_redbag_cancel = findView(R.id.article_details_ad_redbag_cancel);

        article_details_scroll = findView(R.id.article_details_scroll);

        article_details_bottom_bar = findView(R.id.article_details_bottom_bar);
        article_details_publish2 = findView(R.id.article_details_publish2);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        setTitle(R.string.title_article_details);
        // 上一级页面传来的数据
        Intent intent = getIntent();
        article = (Article) intent.getSerializableExtra(Constant.DATA);// 文章信息
        news = new News();
//        if (TextUtil.isEmpty(article.articles_url)) {// TODO 测试代码
//            article.articles_url = "http://mp.weixin.qq.com/s/XUACZ4IWGv6DlrSUdpUTNw";
//        }
        if (article != null) {
            news.news_title = article.title;
            news.articles = article.content;
            news.url = article.articles_url;
        }

        // 支持javascript
        article_details_web.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        article_details_web.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        article_details_web.getSettings().setBuiltInZoomControls(true);
        // 扩大比例的缩放
        article_details_web.getSettings().setUseWideViewPort(true);
        // 清除浏览器缓存
        article_details_web.clearCache(true);
        // 自适应屏幕
        article_details_web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 优先使用缓存
        article_details_web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 不使用缓存
        article_details_web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 启用支持javascript
        WebSettings settings = article_details_web.getSettings();
        settings.setLoadWithOverviewMode(true);

        // WebView加载web资源
        article_details_web.loadUrl(article.articles_url);
//        article_details_web.loadDataWithBaseURL(null, article.content, "text/html", "UTF-8", null);
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        article_details_web.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        // 判断页面加载过程
        article_details_web.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    // 网页加载完成
//                } else {
//                    // 加载中
//                }
            }
        });
    }

    /**
     * 初始化监听
     */
    @Override
    public void initListener() {
        article_details_publish.setOnClickListener(this);
        article_details_publish2.setOnClickListener(this);
        article_details_add_ad.setOnClickListener(this);
        article_details_ad_edit.setOnClickListener(this);
        article_details_ad_goods_cancel.setOnClickListener(this);
        article_details_ad_redbag_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.article_details_publish:// 发布
            case R.id.article_details_publish2:
                publishAdvertising();
                break;

            case R.id.article_details_add_ad:// 添加广告
            case R.id.article_details_ad_edit:// 编辑广告
                startActivityForResult(AddAdvertisingActivity.class, news, REQUEST_ADD_AD);
                break;

            case R.id.article_details_ad_goods_cancel:// 取消商品
                news.goods_id = "";
                news.goods_name = "";
                news.goods_price_section = "";
                news.goods_price = 0;
                news.goods_img = "";
                news.goods_url = "";
                news.goods_commission_rate = 100;
                news.goods_commission_amount = 0;
                news.has_goods = 0;
                refreshView();
                break;

            case R.id.article_details_ad_redbag_cancel:// 取消红包
                news.has_red_envelopes = 0;
                news.red_envelopes_amount = 0;
                news.red_envelopes_number = 0;
                refreshView();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (news.has_goods == 1) {
            showAbandonEditDialog();
        } else if (news.has_red_envelopes == 1) {
            showAbandonEditDialog();
        } else if (TextUtil.isNotEmpty(news.news_img)) {
            showAbandonEditDialog();
        } else if (TextUtil.isNotEmpty(news.news_describe)) {
            showAbandonEditDialog();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 显示放弃编辑对话框
     */
    private void showAbandonEditDialog() {
        CommonDialog dialog = new CommonDialog(this, R.string.dialog_abandon_edit_content);
        dialog.show(new CommonDialog.OnConfirmListener() {

            @Override
            public void onConfirm() {
                finish();
            }
        });
    }

    private void publishAdvertising() {
        if (TextUtil.isEmpty(news.news_img)) {
            Toast.show(activity, "请添加广告内容");
            return;
        }
        NewsManager.getInstance().add(this, news, new NewsManager.OnNewsAddCallback() {

            @Override
            public void onSucceed() {
                new PublishSucceedDialog(activity).show(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        SharedPreferencesUtils.put(PUBLISH_TYPE, SELECT_ARTICLE);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }

            @Override
            public void onFail(String failMsg) {
                Toast.show(activity, failMsg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD_AD:
                    news = (News) data.getSerializableExtra(Constant.DATA);
                    if (news != null) {
                        refreshView();
                    }
                    break;
            }
        }
    }

    /**
     * 有相关信息则显示，如果没有就隐藏
     */
    private void refreshView() {
        article_details_bottom_bar.setVisibility(VISIBLE);
        article_details_add_ad.setVisibility(GONE);
        article_details_ad_group.setVisibility(VISIBLE);
        if (TextUtil.isNotEmpty(news.news_img_path)) {// 图片
            article_details_ad_img.setVisibility(VISIBLE);
            Glide.with(this).load(news.news_img_path).into(article_details_ad_img);
        } else if (TextUtil.isNotEmpty(news.news_img)) {
            article_details_ad_img.setVisibility(VISIBLE);
            Glide.with(this).load(news.news_img).into(article_details_ad_img);
        } else {
            article_details_ad_img.setVisibility(GONE);
        }
        if (TextUtil.isNotEmpty(news.news_describe)) {// 描述
            article_details_ad_describe.setVisibility(VISIBLE);
            article_details_ad_describe.setText(news.news_describe);
        } else {
            article_details_ad_describe.setVisibility(GONE);
        }
        if (news.has_goods == 1) {
            article_details_ad_goods_info.setVisibility(VISIBLE);
            Glide.with(this).load(news.goods_img).into(article_details_ad_goods_icon);
            article_details_ad_goods_name.setText(TextUtil.trimToEmpty(news.goods_name));
            article_details_ad_goods_price.setText(TextUtil.trimToEmpty(news.goods_price_section));
        } else {
            article_details_ad_goods_info.setVisibility(GONE);
        }
        if (news.has_red_envelopes == 1) {
            article_details_ad_redbag_info.setVisibility(VISIBLE);
            String text;
            if (news.has_random_red == 0) {
                text = getString(R.string.add_redbag_random_number_money);
            } else {
                text = getString(R.string.add_redbag_number_money);
            }
            text = String.format(text, news.red_envelopes_amount, news.red_envelopes_number);
            article_details_ad_redbag_tag.setText(text);
        } else {
            article_details_ad_redbag_info.setVisibility(GONE);
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                article_details_scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);
    }
}
