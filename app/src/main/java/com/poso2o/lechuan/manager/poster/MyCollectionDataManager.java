package com.poso2o.lechuan.manager.poster;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.poster.PosterQueryDTO;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.tool.print.Print;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * 广告我的收藏
 * Created by Luo on 2017-12-01.
 */

public class MyCollectionDataManager<T> extends BaseManager {
    public static MyCollectionDataManager mMyCollectionDataManager;
    /**
     * 获取广告 - 我发布的资讯列表+分页查询
     */
    private final int TAG_POSTER_ID = 1;

    public static MyCollectionDataManager getMyCollectionDataManager() {
        if (mMyCollectionDataManager == null) {
            mMyCollectionDataManager = new MyCollectionDataManager();
        }
        return mMyCollectionDataManager;
    }

    /**
     * 获取广告 - 我发布的资讯列表+分页查询
     *
     * @param baseActivity
     * @param currPage       查看页码 （必填）
     * @param callBack       回调
     */
    public void loadCollectionData(final BaseActivity baseActivity,  final String currPage, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_COLLECT_NEWS_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("currPage", currPage);
        Print.println("URL:" + request.getCacheKey().toString());
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                Gson gson = new Gson();
                PosterQueryDTO posterQueryDTO = gson.fromJson(response, PosterQueryDTO.class);
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
