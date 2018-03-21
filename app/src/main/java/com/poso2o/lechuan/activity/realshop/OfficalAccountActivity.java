package com.poso2o.lechuan.activity.realshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.oa.ArticleAdActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.articledata.ArticleData;
import com.poso2o.lechuan.view.OAInformationView;
import com.poso2o.lechuan.view.OfficialAccountsIssueView;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/12/5.
 * 公众号
 */

public class OfficalAccountActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    public static final String SENDING_ARTICLE_DATA = "sending_article_data";

    //要发布文章的数量
    private TextView article_send_num;

    //选择要发布的文章集合
    private ArrayList<ArticleData> selectedArt = new ArrayList<>();

    //内容视图
    private FrameLayout oa_content;

    //资讯
    private TextView oa_info;

    //发布
    private TextView oa_issue;

    //资讯视图
    private OAInformationView officialAccountsInfoView;

    //发布视图
    private OfficialAccountsIssueView officialAccountsIssueView;

    //添加发布的文章是否发生变动
    private boolean isChangeArt;
    private ArtSelectBroadcast broadcast;
    private SearchArtChangeBroadcast searchBroadcast;

    //收藏的文章集合
    private ArrayList<ArticleData> favoriteArt = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_official_accounts;
    }

    @Override
    protected void initView() {
        context = this;
        oa_content = (FrameLayout) findViewById(R.id.oa_content);
        oa_info = (TextView) findViewById(R.id.oa_info);
        oa_issue = (TextView) findViewById(R.id.oa_issue);
        article_send_num = (TextView) findViewById(R.id.article_send_num);
    }

    @Override
    protected void initData() {
        officialAccountsInfoView = new OAInformationView(this);
        officialAccountsIssueView = new OfficialAccountsIssueView(this);

        oa_info.setTextColor(getColorValue(R.color.textGreen));
        oa_info.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_info_on, 0, 0, 0);
        oa_content.addView(officialAccountsInfoView.getRootView());

        //注册发布文章变动广播通知
        broadcast = new ArtSelectBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ArticleAdActivity.AD_ARTICLE);
        registerReceiver(broadcast,filter);

        searchBroadcast = new SearchArtChangeBroadcast();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(ArticleSearchActivity.SEARCH_ARTICLE_CHANGE_BROAD);
        registerReceiver(searchBroadcast,filter1);
    }

    @Override
    protected void initListener() {
        oa_info.setOnClickListener(this);
        oa_issue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.oa_info:
                showOfficialAccountsInfoView();
                break;
            case R.id.oa_issue:
                showOfficialAccountsIssueView();
                break;
        }
    }

    @Override
    public void onResume() {
        if (isChangeArt && officialAccountsInfoView != null){
            //刷新所有列表
            officialAccountsInfoView.notifyAllAdapter();
        }
        if (isChangeArt && officialAccountsIssueView != null && officialAccountsIssueView.getRootView().isShown()){
            officialAccountsIssueView.notifyArtList();
            isChangeArt = false;
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast);
        unregisterReceiver(searchBroadcast);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)return;
        switch (requestCode){
            case OfficialAccountsIssueView.CODE_TO_ART_AD:
                freshArticle(data);
                break;
            case OfficialAccountsIssueView.CODE_TO_WC_AUTHORITY:
                if (officialAccountsIssueView != null)officialAccountsIssueView.authorizeState();
                break;
        }
    }

    public void showOfficialAccountsInfoView() {
        oa_info.setTextColor(getColorValue(R.color.textGreen));
        oa_info.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_info_on, 0, 0, 0);
        oa_issue.setTextColor(getColorValue(R.color.textGray));
        oa_issue.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_wechat_off, 0, 0, 0);
        switchView(officialAccountsInfoView);
        if (isChangeArt && officialAccountsInfoView != null){
            //刷新所有列表
            officialAccountsInfoView.notifyAllAdapter();
            isChangeArt = false;
        }
    }

    public void showOfficialAccountsIssueView() {
        oa_issue.setTextColor(getColorValue(R.color.textGreen));
        oa_issue.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_wechat_on, 0, 0, 0);
        oa_info.setTextColor(getColorValue(R.color.textGray));
        oa_info.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_info_off, 0, 0, 0);
        switchView(officialAccountsIssueView);
        if (isChangeArt){
            officialAccountsIssueView.notifyArtList();
            isChangeArt = false;
        }
    }

    private void switchView(BaseView baseView) {
        oa_content.removeAllViews();
        oa_content.addView(baseView.getRootView());
    }

    //添加发布
    public void addSelectArt(ArticleData articleData){
        selectedArt.add(articleData);
        article_send_num.setVisibility(View.VISIBLE);
        article_send_num.setText(selectedArt.size() + "");
        articleData.inLine = true;
        isChangeArt = true;
    }

    //删除发布
    public void removeSelectArt(ArticleData articleData){
        selectedArt.remove(articleData);
        articleData.inLine = false;
        if (selectedArt.size() == 0){
            article_send_num.setVisibility(View.GONE);
        } else {
            article_send_num.setText(selectedArt.size() + "");
        }
        isChangeArt = true;
    }

    //获取当前选择要发布的文章
    public ArrayList<ArticleData> getSelectedArt(){
        for (ArticleData articleData : selectedArt){
            articleData.cover_pic_url = articleData.pic;
        }
        return selectedArt;
    }

    //跳转文章详情添加文章变动通知
    class ArtSelectBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            isChangeArt = true;
            Bundle bundle = intent.getExtras();
            if (bundle == null)return;
            if (bundle.get(ArticleAdActivity.AD_ARTICLE) == null)return;
            ArticleData articleData = (ArticleData) bundle.get(ArticleAdActivity.AD_ARTICLE);
            for (ArticleData articleData1:selectedArt){
                if (articleData1.articles_id.equals(articleData.articles_id)){
                    articleData1.isAd = articleData.isAd;
                    articleData1.ad_content = articleData.ad_content;
                    articleData1.ad_des = articleData.ad_des;
                    articleData1.pic_path = articleData.pic_path;
                    break;
                }
            }
            ArticleData temp = null;
            for (ArticleData art : selectedArt){
                if (art.articles_id.equals(articleData.articles_id)){
                    temp = art;
                    break;
                }
            }
            if (temp == null){
                //添加
                addSelectArt(articleData);
            }else {
                //删除
                removeSelectArt(temp);
            }
        }
    }

    //跳转文章搜索添加或移除发布文章通知
    class SearchArtChangeBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            isChangeArt = true;
            Bundle bundle = intent.getExtras();
            if (bundle == null)return;
            if (bundle.get(SENDING_ARTICLE_DATA) == null)return;
            ArticleData articleData = (ArticleData) bundle.get(SENDING_ARTICLE_DATA);
            ArticleData temp = null;
            for (ArticleData art : selectedArt){
                if (art.articles_id.equals(articleData.articles_id)){
                    temp = art;
                    break;
                }
            }
            if (temp == null){
                //添加
                addSelectArt(articleData);
            }else {
                //删除
                removeSelectArt(temp);
            }
        }
    }

    //赋值收藏文章集合
    public void setFavoriteArt(ArrayList<ArticleData> art){
        favoriteArt.addAll(art);
    }

    //获取收藏文章集合
    public ArrayList<ArticleData> getFavoriteArt(){
        return favoriteArt;
    }

    //收藏一篇文章
    public void addFavoriteArt(ArticleData articleData){
        favoriteArt.add(articleData);
    }

    //取消收藏一篇文章
    public void cancelFavoriteArt(ArticleData articleData){
        ArticleData temp = null;
        for (ArticleData art : favoriteArt){
            if (art.articles_id.equals(articleData.articles_id))temp = art;
        }
        favoriteArt.remove(temp);
    }

    //在发布页点进去的文章详情页，修改过广告内容，回来后同步数据
    private void freshArticle(Intent data){
        if (data == null)return;
        ArticleData article = (ArticleData) data.getExtras().get(ArticleAdActivity.ART_DATA);
        for (ArticleData art : selectedArt){
            if (art.articles_id.equals(article.articles_id)){
                art.ad_des = article.ad_des;
                art.pic_path = article.pic_path;
                art.ad_content = article.content;
                art.isAd = article.isAd;
                art.goods = article.goods;
                break;
            }
        }
        if (officialAccountsIssueView != null)officialAccountsIssueView.notifyArtList();
    }
}
