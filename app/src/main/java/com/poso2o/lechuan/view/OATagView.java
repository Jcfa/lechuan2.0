package com.poso2o.lechuan.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.realshop.ArticleAdActivity;
import com.poso2o.lechuan.activity.realshop.ArticleSearchActivity;
import com.poso2o.lechuan.activity.realshop.OfficalAccountActivity;
import com.poso2o.lechuan.adapter.OAArticleAdapter;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.bean.articledata.AllArticleData;
import com.poso2o.lechuan.bean.articledata.ArticleData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.article.ArticleDataManager;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

/**
 * Created by mr zhang on 2017/10/23.
 */

public class OATagView extends BaseView {

    private View view ;
    private OfficalAccountActivity context;

    //下拉刷新
    private SwipeRefreshLayout article_swipe;
    //列表
    private RecyclerView oa_tag_article;

    //类型，0表示收藏，1表示时尚，2是美食，3是健康
    private int mType;

    //列表适配器
    private OAArticleAdapter adapter;

    //时尚页数据
    private ArrayList<ArticleData> fashionData ;
    //时尚文章当前页
    private int mCurrFashion = 1;
    //时尚文章总页数
    private int totalFashion = 1;

    //美食页数据
    private ArrayList<ArticleData> foodData;
    //美食文章当前页
    private int mCurrFood = 1;
    //美食文章总页数
    private int totalFood = 1;

    //健康页数据
    private ArrayList<ArticleData> healthyData;
    //健康文章当前页
    private int mCurrHealthy = 1;
    //健康文章总页数
    private int totalHealthy = 1;

    /**
     * 注意继承后 先走了初始化控件  data
     *
     * @param context
     */
    public OATagView(Context context , int type) {
        super(context);
        this.context = (OfficalAccountActivity) context;
        mType = type;
    }

    @Override
    public View initGroupView() {
        view = View.inflate(context, R.layout.view_oa_tag_view,null);
        return view;
    }

    @Override
    public void initView() {
        oa_tag_article = (RecyclerView) view.findViewById(R.id.oa_tag_article);
        article_swipe = (SwipeRefreshLayout) view.findViewById(R.id.article_swipe);
        article_swipe.setColorSchemeColors(Color.RED);
    }

    @Override
    public void initData() {
        initRecyclerview();
        switch (mType){
            case 0:
                article_swipe.setRefreshing(true);
                initFavoriteData();
                break;
            case 1:
                initFashionData();
                break;
            case 2:
                initFoodData();
                break;
            case 3:
                initHealthyData();
                break;
        }
        initRefresh();
    }

    //初始化列表数据
    private void initRecyclerview(){
        adapter = new OAArticleAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        oa_tag_article.setLayoutManager(linearLayoutManager);
        oa_tag_article.setAdapter(adapter);
        
        initAdapterListener();
    }

    //刷新页面
    private void initRefresh(){
        article_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                article_swipe.setRefreshing(true);
                if (mType == 0){
                    initFavoriteData();
                }else if (mType == 1){
                    initFashionData();
                }else if (mType == 2){
                    initFoodData();
                }else if (mType == 3){
                    initHealthyData();
                }
            }
        });
    }

    //加载收藏页数据
    private void initFavoriteData(){
        com.poso2o.lechuan.manager.rshopmanager.ArticleDataManager.getInstance().loadListData(context, mType, null, 1, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                context.dismissLoading();
                article_swipe.setRefreshing(false);
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                context.dismissLoading();
                article_swipe.setRefreshing(false);
                AllArticleData allArticleData = (AllArticleData) object;
                context.getFavoriteArt().clear();
                if (allArticleData != null){
                    context.setFavoriteArt(allArticleData.list);
                    if (adapter != null)adapter.notifyDatas(context.getFavoriteArt());
                }
            }
        });
    }

    //加载时尚页数据
    private void initFashionData(){
        mCurrFashion = 1;
        com.poso2o.lechuan.manager.rshopmanager.ArticleDataManager.getInstance().loadListData(context, mType, null, mCurrFashion, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                article_swipe.setRefreshing(false);
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                article_swipe.setRefreshing(false);
                AllArticleData allArticleData = (AllArticleData) object;
                if (allArticleData == null || allArticleData.total == null || allArticleData.list == null)return;
                totalFashion = allArticleData.total.pages;
                fashionData = allArticleData.list;
                if (adapter != null)adapter.notifyDatas(fashionData);
            }
        });
    }

    //加在更多时尚数据
    private void loadMoreFashion(){
        mCurrFashion++;
        com.poso2o.lechuan.manager.rshopmanager.ArticleDataManager.getInstance().loadListData(context, mType, null, mCurrFashion, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                article_swipe.setRefreshing(false);
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                article_swipe.setRefreshing(false);
                AllArticleData allArticleData = (AllArticleData) object;
                if (allArticleData == null)return;
                if (fashionData == null)fashionData = new ArrayList<ArticleData>();
                fashionData.addAll(allArticleData.list);
                if (adapter != null)adapter.notifyDatas(fashionData);
            }
        });
    }

    //加载美食页数据
    private void initFoodData(){
        mCurrFood = 1;
        com.poso2o.lechuan.manager.rshopmanager.ArticleDataManager.getInstance().loadListData(context, mType, null, mCurrFood, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                article_swipe.setRefreshing(false);
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                article_swipe.setRefreshing(false);
                AllArticleData allArticleData = (AllArticleData) object;
                if (allArticleData == null || allArticleData.total == null || allArticleData.list == null)return;
                totalFood = allArticleData.total.pages;
                foodData = allArticleData.list;
                if (adapter != null)adapter.notifyDatas(foodData);
            }
        });
    }

    //加载更多美食数据
    private void loadMoreFood(){
        mCurrFood++;
        com.poso2o.lechuan.manager.rshopmanager.ArticleDataManager.getInstance().loadListData(context, mType, null, mCurrFood, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                article_swipe.setRefreshing(false);
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                article_swipe.setRefreshing(false);
                AllArticleData allArticleData = (AllArticleData) object;
                if (allArticleData == null || allArticleData.total == null || allArticleData.list == null)return;
                if (foodData == null)foodData = new ArrayList<ArticleData>();
                foodData.addAll(allArticleData.list);
                if (adapter != null)adapter.notifyDatas(foodData);
            }
        });
    }

    //加载健康页数据
    private void initHealthyData(){
        mCurrHealthy = 1;
        com.poso2o.lechuan.manager.rshopmanager.ArticleDataManager.getInstance().loadListData(context, mType, null, mCurrHealthy, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                article_swipe.setRefreshing(false);
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                article_swipe.setRefreshing(false);
                AllArticleData allArticleData = (AllArticleData) object;
                if (allArticleData == null || allArticleData.total == null || allArticleData.list == null)return;
                totalHealthy = allArticleData.total.pages;
                healthyData = allArticleData.list;
                if (adapter != null)adapter.notifyDatas(healthyData);
            }
        });
    }

    //加载更多健康文章
    private void loadMoreHealthy(){
        mCurrHealthy++;
        com.poso2o.lechuan.manager.rshopmanager.ArticleDataManager.getInstance().loadListData(context, mType, null, mCurrHealthy, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                article_swipe.setRefreshing(false);
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                AllArticleData allArticleData = (AllArticleData) object;
                article_swipe.setRefreshing(false);
                if (allArticleData == null || allArticleData.total == null || allArticleData.list == null)return;
                if (healthyData == null)healthyData = new ArrayList<ArticleData>();
                healthyData.addAll(allArticleData.list);
                if (adapter != null)adapter.notifyDatas(healthyData);
            }
        });
    }
    
    private void initAdapterListener(){
        adapter.setOnArticleListener(new OAArticleAdapter.OnArticleListener() {
            @Override
            public void onFavoriteClick(int position, ArticleData articleData) {
                if (articleData.has_collect == 0){
                    //收藏文章
                    favoriteArticle(position,articleData);
                }else if (articleData.has_collect == 1){
                    //取消收藏文章
                    cancelFavorite(position,articleData);
                }
            }

            @Override
            public void onSendClick(int position, ArticleData articleData) {
            }

            @Override
            public void onArticleClick(int position, ArticleData articleData) {
                Intent intent = new Intent();
                intent.setClass(context, ArticleAdActivity.class);
                intent.putExtra(ArticleAdActivity.ART_DATA,articleData);
                intent.putExtra(ArticleSearchActivity.FROM_ART_SEARCH,false);
//                intent.putExtra(ArticleAdActivity.SENDING_ART_NUM,context.getSelectedArt().size());
//                context.startActivityForResult(intent,ArticleAdActivity.ART_DETAIL_CODE);
            }

            @Override
            public void onLoadMore(TextView textView, ProgressBar progressBar) {
                if (mType == 0){
                    textView.setText("数据已全部加在完毕");
                    progressBar.setVisibility(View.GONE);
                }else if (mType == 1){
                    if (mCurrFashion < totalFashion){
                        loadMoreFashion();
                        textView.setText("正在加载更多。。。");
                        progressBar.setVisibility(VISIBLE);
                    }else {
                        textView.setText("数据已全部加在完毕");
                        progressBar.setVisibility(View.GONE);
                    }
                }else if (mType == 2){
                    if (mCurrFood < totalFood){
                        loadMoreFood();
                        textView.setText("正在加载更多。。。");
                        progressBar.setVisibility(VISIBLE);
                    }else {
                        textView.setText("数据已全部加在完毕");
                        progressBar.setVisibility(View.GONE);
                    }
                }else if (mType == 3){
                    if (mCurrHealthy < totalHealthy){
                        loadMoreHealthy();
                        textView.setText("正在加载更多。。。");
                        progressBar.setVisibility(VISIBLE);
                    }else {
                        textView.setText("数据已全部加在完毕");
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    
    //收藏文章
    private void favoriteArticle(final int position, final ArticleData articleData){
        context.showLoading();
        com.poso2o.lechuan.manager.rshopmanager.ArticleDataManager.getInstance().collect(context, articleData.articles_id, 0, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                context.dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                context.dismissLoading();
                articleData.has_collect = 1;
                context.addFavoriteArt(articleData);
                if (adapter != null)adapter.notifyItemChanged(position);
            }
        });
    }

    //取消收藏
    private void cancelFavorite(final int position,final ArticleData articleData){
        context.showLoading();
        com.poso2o.lechuan.manager.rshopmanager.ArticleDataManager.getInstance().collect(context, articleData.articles_id, 1, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                context.dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                context.dismissLoading();
                articleData.has_collect = 0;
                context.cancelFavoriteArt(articleData);
                if (mType == 0){
                    notifyAdapter();
                }else if (adapter != null)adapter.notifyItemChanged(position);
            }
        });
    }

    private OnSendArticleListener onSendArticleListener;
    //添加发布
    public interface OnSendArticleListener{
        void onSendArticle(int position, ArticleData articleData);
    }
    public void setOnSendArticleListener(OnSendArticleListener onSendArticleListener){
        this.onSendArticleListener = onSendArticleListener;
    }

    //刷新列表状态,不加载网络数据
    public void notifyAdapter(){
        if (adapter != null)adapter.notifyAdapter();
    }
}
