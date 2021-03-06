package com.poso2o.lechuan.activity.oa;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.orderinfo.OrderInfoMainActivity;
import com.poso2o.lechuan.activity.realshop.ArticleSearchActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.fragment.oa.ArticleFiltrateFragment;
import com.poso2o.lechuan.fragment.oa.ArticleFiltrateManager;
import com.poso2o.lechuan.fragment.oa.OAArticleFragment;
import com.poso2o.lechuan.fragment.oa.OAPublishFragment;
import com.poso2o.lechuan.fragment.oa.OARenewalsFragment;
import com.poso2o.lechuan.fragment.oa.OaSetFragment;
import com.poso2o.lechuan.fragment.oa.OaSetModelFragment;
import com.poso2o.lechuan.fragment.oa.PublishDraftFragment;
import com.poso2o.lechuan.manager.article.ArticleDataManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 微信公众号助手
 * <p>
 * Created by Jaydon on 2018/1/26.
 */
public class OAHelperActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 发布页标题标签
     */
    private View oa_title_tag;

    /**
     * 发布页标题标签:发布
     */
    private TextView oa_title_publish;

    /**
     * 发布页标题标签:稿件箱
     */
    private TextView oa_title_draft;

    /**
     * 底部按钮：资讯，发布，设置
     */
    private TextView oa_article, oa_publish, oa_setup;

    /**
     * 发布文章的数量
     */
    private TextView oa_publish_num;

    /**
     * 标题栏按钮：搜索，筛选
     */
    private ImageView oa_article_search, oa_article_filtrate;

    /**
     * 资讯界面
     */
    private OAArticleFragment oaArticleFragment;

    /**
     * 发布界面
     */
    private OAPublishFragment oaPublishFragment;

    /**
     * 设置页
     */
    private OaSetFragment oaSetFragment;

    /**
     * 文章筛选
     */
    private ArticleFiltrateManager articleFiltrateManager;

    /**
     * 页面类型：1为发布页，2为设置页 ,默认发布页
     */
    private int viewType = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_oa_helper;
    }

    @Override
    protected void initView() {
        oa_title_tag = findView(R.id.oa_title_tag);
        oa_title_publish = findView(R.id.oa_title_publish);
        oa_title_draft = findView(R.id.oa_title_draft);

        oa_article_search = findView(R.id.oa_article_search);
        oa_article_filtrate = findView(R.id.oa_article_filtrate);

        oa_publish_num = findView(R.id.oa_publish_num);

        oa_article = findView(R.id.oa_article);
        oa_publish = findView(R.id.oa_publish);
        oa_setup = findView(R.id.oa_setup);
    }

    @Override
    protected void initData() {
        //初始化返回键
        setTitle("");

        oaArticleFragment = new OAArticleFragment();

        oaPublishFragment = new OAPublishFragment();

        oaSetFragment = new OaSetFragment();

        addFragment(R.id.oa_content, oaPublishFragment);

    }

    @Override
    protected void initListener() {
        oa_title_publish.setOnClickListener(this);
        oa_title_draft.setOnClickListener(this);
        oa_article_search.setOnClickListener(this);
        oa_article_filtrate.setOnClickListener(this);
        oa_article.setOnClickListener(this);
        oa_publish.setOnClickListener(this);
        oa_setup.setOnClickListener(this);
        findView(R.id.iv_back).setOnClickListener(this);

        oaArticleFragment.setOnPageChangeListener(new OAArticleFragment.OnPageChangeListener() {
            @Override
            public void onPageChange(int page) {
                if (page == 0) {
                    setSearchVisible(View.INVISIBLE);
                } else {
                    setSearchVisible(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.oa_article:// 资讯
                clickArticle();
                break;

            case R.id.oa_publish:// 发布
                clickPublish();
                break;

            case R.id.oa_setup:// 设置
                clickSet();
                break;

            case R.id.oa_article_search:// 搜索 TODO
                clickSearchArt();
                break;

            case R.id.oa_article_filtrate:// 筛选
                showFiltrateView();
                break;

            case R.id.oa_title_publish:
                publishOrOa();
                break;

            case R.id.oa_title_draft:
                renewalsOrMudel();
                break;
            case R.id.iv_back:
                goBack();
                break;
        }
    }

    public void setPublishTitle() {
        oa_title_publish.setTextColor(getColorValue(R.color.colorOrange));
        oa_title_publish.setBackgroundResource(R.drawable.shape_oa_title_tag_select);
        oa_title_draft.setTextColor(getColorValue(R.color.textGray));
        oa_title_draft.setBackgroundColor(0x00000000);

    }

    public void setDraftTitle() {
        oa_title_draft.setTextColor(getColorValue(R.color.colorOrange));
        oa_title_draft.setBackgroundResource(R.drawable.shape_oa_title_tag_select);
        oa_title_publish.setTextColor(getColorValue(R.color.textGray));
        oa_title_publish.setBackgroundColor(0x00000000);
    }

    /**
     * 显示筛选视图 TODO
     */
    private void showFiltrateView() {
        if (articleFiltrateManager == null) {
            articleFiltrateManager = new ArticleFiltrateManager(this);
        }
        articleFiltrateManager.show(new ArticleFiltrateFragment.Callback() {

            @Override
            public void onFinish() {
                oaArticleFragment.reLoadData();
            }
        });
    }

    private void switchTag(TextView tv, int resId) {
        oa_article.setTextColor(getColorValue(R.color.textGray));
        oa_article.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.icon_info_off, 0, 0);
        oa_publish.setTextColor(getColorValue(R.color.textGray));
        oa_publish.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.icon_wechat_off, 0, 0);
        oa_setup.setTextColor(getColorValue(R.color.textGray));
        oa_setup.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.icon_oa_setup_off, 0, 0);
        tv.setTextColor(getColorValue(R.color.textGreen));
        tv.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
    }

    /**
     * 发布还是公众号
     */
    private void publishOrOa() {
        setPublishTitle();
        if (viewType == 1) {
            // 发布编辑
            oaPublishFragment.setFragment(0);
        } else if (viewType == 2) {
            //公众号
            oaSetFragment.setFragment(0);
        }
    }

    /**
     * 草稿箱还是模板
     */
    private void renewalsOrMudel() {
        setDraftTitle();
        if (viewType == 1) {
            // 稿件箱
            oaPublishFragment.setFragment(1);
        } else if (viewType == 2) {
            //模板
            oaSetFragment.setFragment(1);
        }
    }

    /**
     * 点击资讯
     */
    private void clickArticle() {
        viewType = 0;
        if (!oaArticleFragment.isVisible()) {
            oa_title_tag.setVisibility(GONE);
            oa_article_search.setVisibility(VISIBLE);
            oa_article_filtrate.setVisibility(VISIBLE);
            setTitle("资讯");
            replaceFragment(R.id.oa_content, oaArticleFragment);
            switchTag(oa_article, R.mipmap.icon_info_on);
        }
    }

    /**
     * 点击底部发布
     */
    private void clickPublish() {
        viewType = 1;
        setArtNum();
        if (!oaPublishFragment.isVisible()) {
            oa_title_tag.setVisibility(VISIBLE);
            oa_article_search.setVisibility(GONE);
            oa_article_filtrate.setVisibility(GONE);
            oa_title_publish.setText("发布");
            oa_title_draft.setText("草稿箱");
            setTitle("");
            replaceFragment(R.id.oa_content, oaPublishFragment);
            switchTag(oa_publish, R.mipmap.icon_wechat_on);
            if (oaPublishFragment.getPosition() == 0) {
                setPublishTitle();
            } else {
                setDraftTitle();
            }
        }
    }

    /**
     * 点击底部设置
     */
    private void clickSet() {
        viewType = 2;
        if (!oaSetFragment.isVisible()) {
            oa_title_tag.setVisibility(VISIBLE);
            oa_article_search.setVisibility(GONE);
            oa_article_filtrate.setVisibility(GONE);
            oa_title_publish.setText("公众号");
            oa_title_draft.setText("模板");
            setTitle("");
            replaceFragment(R.id.oa_content, oaSetFragment);
            switchTag(oa_setup, R.mipmap.icon_oa_setup_on);
            if (oaSetFragment.getPosition() == 0) {
                setPublishTitle();
            } else {
                setDraftTitle();
            }
        }
    }

    /**
     * 点击搜索文章
     */
    private void clickSearchArt() {
        Intent intent = new Intent();
        intent.setClass(this, ArticleSearchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        if (requestCode == 8001) {
            //购买服务成功
        } else if (requestCode == 8002) {
            //购买或续费模板成功
//            if (oaSetModelFragment != null)oaSetModelFragment.requestData();
        }
    }

    @Override
    public void onEvent(EventBean event) {
        super.onEvent(event);
        if (event.code == EventBean.CODE_TO_COLLECT_LIST) {
            toSelectArt();
        } else if (event.code == EventBean.CODE_ART_NUM_CHANGE) {
            setArtNum();
        }
    }

    //是否还有公众号服务
    private boolean lessDay() {
        return SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SERVICE_DAYS) > 0 ? true : false;
    }

    //跳转到续费页面
    public void toRenewals() {
        replaceFragment(R.id.oa_content, new OARenewalsFragment());
    }

    //跳转到资讯页收藏列表
    public void toSelectArt() {
        oa_article.performClick();
        oaArticleFragment.toCollectList();
    }

    //搜索、筛选按钮的隐藏、显示操作
    private void setSearchVisible(int visible) {
        oa_article_search.setVisibility(visible);
        oa_article_filtrate.setVisibility(visible);
    }

    //设置文章数
    private void setArtNum() {
        int num = ArticleDataManager.getInstance().getSelectData().size();
        if (num == 0 || viewType == 1) {
            oa_publish_num.setVisibility(GONE);
        } else {
            oa_publish_num.setVisibility(VISIBLE);
            oa_publish_num.setText(num + "");
        }
    }


    private void goBack() {
        startActivity(OrderInfoMainActivity.class);
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
