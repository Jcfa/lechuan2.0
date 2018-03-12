package com.poso2o.lechuan.manager.article;

import android.util.Log;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.bean.article.ArticleBean;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.ArrayList;

/**
 * 文章管理
 * Created by Administrator on 2017-11-25.
 */
public class ArticleDataManager extends BaseManager {

    /**
     * 文章类型
     */
    public static final int COLLECT = 0;// 收藏
    public static final int FASHION = 1;// 时尚
    public static final int CATE = 2;// 美食
    public static final int HEALTH = 3;// 健康
    public static final int FASHIAN = 4;// 健康

    private static ArticleDataManager articleDataManager;

    private final int TAG_ARTICLE_LIST_ID = 121;

    private final int TAG_COLLECT_ID = 122;

    /**
     * 公众号助手选中的数据
     */
    private ArrayList<Article> oaSelectData = new ArrayList<>();

    public static ArticleDataManager getInstance() {
        if (articleDataManager == null) {
            synchronized (ArticleDataManager.class) {
                if (articleDataManager == null) {
                    articleDataManager = new ArticleDataManager();
                }
            }
        }
        return articleDataManager;
    }

    /**
     * 加载文章列表
     */
    public void loadListData(final BaseActivity baseActivity, final int articles_type, String keywords, final int pageType, final OnLoadListDataCallback callback) {
        if (pageType == FIRST) {
            currPage = 0;
        }
        Request<String> request;
        if (articles_type == COLLECT) {
            request = getStringRequest(HttpAPI.ARTICLES_COLLECT_LIST_API);
        } else {
            request = getStringRequest(HttpAPI.ARTICLES_LIST_API);
            request.add("articles_type", articles_type);
        }
        request.add("keywords", keywords);
        request.add("currPage", ++currPage);
        defaultParam(request);
        baseActivity.request(TAG_ARTICLE_LIST_ID, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                ArticleBean articleBean = gson.fromJson(response, ArticleBean.class);
                if (articleBean != null && articleBean.list != null) {
                    if (pageType == NEXT && articleBean.list.size() == 0) {
                        currPage--;
                        callback.onFail(baseActivity.getString(R.string.toast_no_more_data));
                    } else {
                        callback.onSucceed(articleBean.list);
                    }
                } else {
                    callback.onFail(baseActivity.getString(R.string.toast_load_articles_fail));
                }
            }

            @Override
            public void onFailed(int what, String response) {
                callback.onFail(response);
            }
        }, true, false);
    }

    /**
     * 收藏
     * @param baseActivity
     * @param articles_id
     * @param has_collect
     */
    public void collect(BaseActivity baseActivity, long articles_id, int has_collect, final OnCollectCallback onCollectCallback) {
        Request<String> request;
        if (has_collect == 1) {
            request = getStringRequest(HttpAPI.ARTICLES_UNCOLLECT_API);
        } else {
            request = getStringRequest(HttpAPI.ARTICLES_COLLECT_API);
        }
        request.add("articles_id", articles_id);
        defaultParam(request);
        baseActivity.request(TAG_COLLECT_ID, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Log.i(this.getClass().getSimpleName(), response);
                onCollectCallback.onSucceed(null);
            }

            @Override
            public void onFailed(int what, String response) {
                onCollectCallback.onFail(response);
            }
        }, true, false);
    }

    /**
     * 获取选择的文章
     * @return
     */
    public ArrayList<Article> getSelectData(){
        return oaSelectData;
    }

    /**
     * 添加选择数据
     */
    public void addSelectData(Article article) {
        oaSelectData.add(article);
    }

    /**
     * 删除选择数据
     */
    public void removeSelectData(Article article) {
        article = findSelectData(article);
        if (article != null) {
            oaSelectData.remove(article);
        }
    }

    /**
     * 查找选择数据
     */
    public Article findSelectData(Article article) {
        for (Article item : oaSelectData) {
            if (item.articles_id == article.articles_id) {
                return item;
            }
        }
        return null;
    }

    /**
     * 文章上移
     */
    public ArrayList<Article> upArticle(Article article){
        if (oaSelectData.size() > 1){
            int index = 0;
            for (int i = 0 ; i<oaSelectData.size(); i++){
                if (oaSelectData.get(i).articles_id == article.articles_id){
                    index = i;
                    break;
                }
            }
            if (index != 0){
                //该文章没有排在第一位
                oaSelectData.remove(index);
                oaSelectData.add(index-1,article);
            }
        }
        return oaSelectData;
    }

    /**
     * 文章下移
     */
    public ArrayList<Article> downArticle(Article article){
        if (oaSelectData.size() > 1){
            int index = 0;
            for (int i = 0 ; i<oaSelectData.size(); i++){
                if (oaSelectData.get(i).articles_id == article.articles_id){
                    index = i;
                    break;
                }
            }
            if (index != oaSelectData.size()-1){
                //该文章没有排在最后一位
                oaSelectData.remove(index);
                oaSelectData.add(index+1,article);
            }
        }
        return oaSelectData;
    }

    /**
     * 文章设为封面
     */
    public ArrayList<Article> topArticle(Article article){
        if (oaSelectData.size() > 1){
            oaSelectData.remove(article);
            oaSelectData.add(0,article);
        }
        return oaSelectData;
    }

    /**
     * 加载数据列表回调
     */
    public interface OnLoadListDataCallback {

        void onSucceed(ArrayList<Article> articles);

        void onFail(String failMsg);
    }

    /**
     * 收藏回调
     */
    public interface OnCollectCallback {

        void onSucceed(Article article);

        void onFail(String failMsg);
    }

}
