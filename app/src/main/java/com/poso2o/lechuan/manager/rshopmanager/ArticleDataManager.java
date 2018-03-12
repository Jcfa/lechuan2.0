package com.poso2o.lechuan.manager.rshopmanager;
import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.articledata.AllArticleData;
import com.poso2o.lechuan.bean.articledata.ArticleData;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.tool.print.Print;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.ArrayList;


/**
 * Created by mr zhang on 2017/12/5.
 */

public class ArticleDataManager extends BaseManager {

    /**
     * 文章类型
     */
    public static final int COLLECT = 0;// 收藏
    public static final int FASHION = 1;// 时尚
    public static final int CATE = 2;// 美食
    public static final int HEALTH = 3;// 健康

    //发布历史记录
    public static final String OA_SEND_HISTORY_URL = HttpAPI.SERVER_MAIN_API + "WxNewsManage.htm?Act=history";
    public static final int OA_SEND_HISTORY_ID = 4;
    //文章发布
    public static final String OA_SEND_ARTICLE_URL = HttpAPI.SERVER_MAIN_API + "WxNewsManage.htm?Act=sendArticles";
    public static final int OA_SEND_ARTICLE_ID = 5;

    private static ArticleDataManager articleDataManager;

    private final int TAG_ARTICLE_LIST_ID = 121;

    private final int TAG_COLLECT_ID = 122;

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
    public void loadListData(final BaseActivity baseActivity, final int articles_type, String keywords, int currPage, final IRequestCallBack iRequestCallBack) {
        Request<String> request;
        if (articles_type == 22){
            //搜索
            request = getStringRequest(HttpAPI.ARTICLES_LIST_API);
        }else if (articles_type == COLLECT) {
            request = getStringRequest(HttpAPI.ARTICLES_COLLECT_LIST_API);
        } else {
            request = getStringRequest(HttpAPI.ARTICLES_LIST_API);
            request.add("articles_type", articles_type);
        }
        request.add("keywords", keywords);
        request.add("currPage", currPage);
        defaultParam(request);
        baseActivity.request(TAG_ARTICLE_LIST_ID, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                AllArticleData allArticleData = gson.fromJson(response, AllArticleData.class);
                iRequestCallBack.onResult(TAG_ARTICLE_LIST_ID,allArticleData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(TAG_ARTICLE_LIST_ID,response);
            }
        }, true, false);
    }

    /**
     * 收藏
     * @param baseActivity
     * @param articles_id
     * @param has_collect
     */
    public void collect(BaseActivity baseActivity, long articles_id, int has_collect, final IRequestCallBack iRequestCallBack) {
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
                iRequestCallBack.onResult(TAG_COLLECT_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(TAG_COLLECT_ID,response);
            }
        }, true, false);
    }

    /**
     * 公众号历史发布
     * @param baseActivity
     * @param currPage
     * @param iRequestCallBack
     */
    public void sendArtHistory(final BaseActivity baseActivity,String currPage,final IRequestCallBack iRequestCallBack){

        Request<String> request = getStringRequest(OA_SEND_HISTORY_URL);
        defaultParam(request);
        request.add("currPage",currPage);

        baseActivity.request(OA_SEND_HISTORY_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(OA_SEND_HISTORY_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(OA_SEND_HISTORY_ID,response);
            }
        },true,true);
    }

    /**
     * 发布文章
     * @param baseActivity
     * @param articleDatas
     * @param iRequestCallBack
     */
    public void sendArticle(final BaseActivity baseActivity, ArrayList<ArticleData> articleDatas,final IRequestCallBack iRequestCallBack){

        Request<String> request = getStringRequest(OA_SEND_ARTICLE_URL);
        defaultParam(request);
        ArrayList<ArticleData> temp = new ArrayList<>();
        for (ArticleData articleData : articleDatas){
            ArticleData article = new ArticleData();
            article.articles_id = articleData.articles_id;
            article.articles_type = articleData.articles_type;
            article.title = articleData.title;
            article.author = articleData.author;
            article.digest = articleData.digest;
            article.pic = articleData.pic;
            article.content = articleData.ad_content;
            article.build_time = articleData.build_time;
            article.readnums = articleData.readnums;
            article.collectnums = articleData.collectnums;
            article.has_collect = articleData.has_collect;
            article.sharenums = articleData.sharenums;
            article.articles_state = articleData.articles_state;
            temp.add(article);
        }
        request.add("articles",new Gson().toJson(temp));

        baseActivity.request(OA_SEND_ARTICLE_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(OA_SEND_ARTICLE_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(OA_SEND_ARTICLE_ID,response);
            }
        },true,true);
    }
}
