package com.poso2o.lechuan.manager.news;

import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.news.News;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.util.LogUtils;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.tools.MultiValueMap;

import java.util.Set;

/**
 * 资讯发布管理
 * Created by Jaydon on 2017/12/5.
 */
public class NewsManager extends BaseManager {

    private static NewsManager newsManager;

    private final int TAG_NEWS_ADD_ID = 161;

    public static NewsManager getInstance() {
        if (newsManager == null) {
            synchronized (NewsManager.class) {
                if (newsManager == null) {
                    newsManager = new NewsManager();
                }
            }
        }
        return newsManager;
    }

    /**
     * 新增发布
     */
    public void add(final BaseActivity baseActivity, News news, final OnNewsAddCallback callback) {
        Request<String> request = getStringRequest(HttpAPI.NEWS_ADD_API);
        request.add("news_img", news.news_img);
        request.add("news_imgs", news.news_imgs);
        request.add("news_title", news.news_title);
        request.add("news_describe", news.news_describe);
        request.add("articles", news.articles.replaceAll("\n","<br>"));
        request.add("has_goods", news.has_goods);
        request.add("goods_id", news.goods_id);
        request.add("goods_name", news.goods_name);
        request.add("goods_price", news.goods_price);
        request.add("goods_img", news.goods_img);
        request.add("goods_url", news.goods_url);
        request.add("goods_commission_rate", news.goods_commission_rate);
        request.add("has_red_envelopes", news.has_red_envelopes);
        request.add("has_random_red", news.has_random_red);
        request.add("red_envelopes_amount", news.red_envelopes_amount);
        request.add("red_envelopes_number", news.red_envelopes_number);
        request.add("url", news.url);
        defaultParam(request);

        // TODO 测试代码
        MultiValueMap<String, Object> params = request.getParamKeyValues();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            LogUtils.i(key + ":" + params.getValue(key) + "\n");
        }

        baseActivity.request(TAG_NEWS_ADD_ID, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                callback.onSucceed();
            }

            @Override
            public void onFailed(int what, String response) {
                callback.onFail(response);
            }
        }, false, false);
    }
    
    public interface OnNewsAddCallback {

        void onSucceed();

        void onFail(String failMsg);
    }
}