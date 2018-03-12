package com.poso2o.lechuan.manager.poster;

import android.util.Log;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.bean.poster.PosterQueryDTO;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.Toast;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.tools.MultiValueMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Luo on 2017-12-01.
 */

public class PosterDataManager<T> extends BaseManager {
    public static PosterDataManager mPosterDataManager;
    /**
     * 获取广告标识
     */
    private final int TAG_POSTER_ID = 1;

    public static PosterDataManager getPosterDataManager() {
        if (mPosterDataManager == null) {
            mPosterDataManager = new PosterDataManager();
        }
        return mPosterDataManager;
    }

    /**
     * 获取广告列表
     *
     * @param baseActivity
     * @param currPage       查看页码
     * @param keywords       关键字
     * @param callBack       回调
     */
    public void loadPosterData(final BaseActivity baseActivity, final String currPage, final String keywords, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("currPage", currPage);
        request.add("keywords", keywords);

        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Print.println("response:" + response);
                Gson gson = new Gson();
                PosterQueryDTO posterQueryDTO = gson.fromJson(response, PosterQueryDTO.class);
                callBack.onResult(what, posterQueryDTO);
            }
            @Override
            public void onFailed(int what, String response) {
                Toast.show(baseActivity, response);
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 获取广告详情
     */
    public void loadPosterInfo(final BaseActivity baseActivity, final String news_id, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_INFO_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("news_id", news_id);
        Print.println("URL:" + request.getCacheKey().toString());
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                Gson gson = new Gson();
                PosterDTO posterDTO = gson.fromJson(response, PosterDTO.class);
                callBack.onResult(TAG_POSTER_ID, posterDTO);
            }
            @Override
            public void onFailed(int what, String response) {
                //Toast.show(baseActivity, response);
                callBack.onFailed(TAG_POSTER_ID, response);
            }
        }, true, true);
    }

    /**
     * 关注 - 关注用户
     *
     * @param baseActivity
     * @param flow_uid       广告ID（必填）
     * @param callBack       回调
     */
    public void loadFollowPoster(final BaseActivity baseActivity, final String flow_uid, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_FLOW_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("flow_uid", flow_uid);
        Print.println("URL:" + request.getCacheKey().toString());
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                callBack.onResult(TAG_POSTER_ID, response);
                //Gson gson = new Gson();
                //PosterDTO posterDTO = gson.fromJson(response, PosterDTO.class);
                //callBack.onResult(TAG_POSTER_ID, posterDTO);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_POSTER_ID, response);
            }
        }, true, true);
    }

    /**
     * 关注 - 取消关注用户
     *
     * @param baseActivity
     * @param flow_uid       广告ID（必填）
     * @param callBack       回调
     */
    public void loadUnFollowPoster(final BaseActivity baseActivity, final String flow_uid, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_UNFLOW_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("flow_uid", flow_uid);
        Print.println("URL:" + request.getCacheKey().toString());
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                callBack.onResult(TAG_POSTER_ID, response);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_POSTER_ID, response);
            }
        }, true, true);
    }


    /**
     * 点赞广告资讯
     *
     * @param baseActivity
     * @param news_id       广告ID（必填）
     * @param callBack       回调
     */
    public void loadLikePoster(final BaseActivity baseActivity, final String news_id, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_LIKE_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("news_id", news_id);
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                callBack.onResult(TAG_POSTER_ID, response);
                //Gson gson = new Gson();
                //PosterDTO posterDTO = gson.fromJson(response, PosterDTO.class);
                //callBack.onResult(TAG_POSTER_ID, posterDTO);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_POSTER_ID, response);
            }
        }, true, true);
    }

    /**
     * 取消点赞广告资讯
     *
     * @param baseActivity
     * @param news_id       广告ID（必填）
     * @param callBack       回调
     */
    public void loadUnLikePoster(final BaseActivity baseActivity, final String news_id, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_UNLIKE_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("news_id", news_id);
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                callBack.onResult(TAG_POSTER_ID, response);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_POSTER_ID, response);
            }
        }, true, true);
    }

    /**
     * 添加收藏广告资讯
     *
     * @param baseActivity
     * @param news_id       广告ID（必填）
     * @param callBack       回调
     */
    public void loadCollectPoster(final BaseActivity baseActivity, final String news_id, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_COLLECT_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("news_id", news_id);
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                callBack.onResult(TAG_POSTER_ID, response);
                //Gson gson = new Gson();
                //PosterDTO posterDTO = gson.fromJson(response, PosterDTO.class);
                //callBack.onResult(TAG_POSTER_ID, posterDTO);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_POSTER_ID, response);
            }
        }, true, true);
    }

    /**
     * 取消收藏广告资讯
     *
     * @param baseActivity
     * @param news_id       广告ID（必填）
     * @param callBack       回调
     */
    public void loadUnCollectPoster(final BaseActivity baseActivity, final String news_id, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_UNCOLLECT_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("news_id", news_id);
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                callBack.onResult(TAG_POSTER_ID, response);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_POSTER_ID, response);
            }
        }, true, true);
    }

    /**
     * 记录转发广告资讯
     *
     * @param baseActivity
     * @param news_id       广告ID（必填）
     * @param callBack       回调
     */
    public void loadRecordSharePoster(final BaseActivity baseActivity, final String news_id, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_RECORD_SHARE_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("news_id", news_id);
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                callBack.onResult(TAG_POSTER_ID, response);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_POSTER_ID, response);
            }
        }, true, true);
    }


}
