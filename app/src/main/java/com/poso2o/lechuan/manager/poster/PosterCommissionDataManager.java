package com.poso2o.lechuan.manager.poster;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.poster.PosterCommissionQueryDTO;
import com.poso2o.lechuan.bean.poster.PosterCommentsDTO;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.tool.print.Print;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * 广告佣金
 * Created by Luo on 2017-12-01.
 */

public class PosterCommissionDataManager<T> extends BaseManager {
    public static PosterCommissionDataManager mPosterCommissionDataManager;
    /**
     * 获取广告佣金列表
     */
    private final int TAG_POSTER_ID = 1;

    public static PosterCommissionDataManager getPosterCommissionDataManager() {
        if (mPosterCommissionDataManager == null) {
            mPosterCommissionDataManager = new PosterCommissionDataManager();
        }
        return mPosterCommissionDataManager;
    }

    /**
     * 获取广告佣金列表
     *
     * @param baseActivity
     * @param news_id        广告ID（必填）
     * @param news_id        资讯ID
     * @param currPage       查看页码 （必填）
     * @param callBack       回调
     */
    public void loadPosterCommissionList(final BaseActivity baseActivity, final String news_id, final String currPage, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_COMMISSION_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("news_id", news_id);
        request.add("currPage", currPage);
        Print.println("URL:" + request.getCacheKey().toString());
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                Gson gson = new Gson();
                PosterCommissionQueryDTO posterQueryDTO = gson.fromJson(response, PosterCommissionQueryDTO.class);
                callBack.onResult(TAG_POSTER_ID, posterQueryDTO);
            }
            @Override
            public void onFailed(int what, String response) {
                //Toast.show(baseActivity, response);
                callBack.onFailed(TAG_POSTER_ID, response);
            }
        }, true, true);
    }

}
