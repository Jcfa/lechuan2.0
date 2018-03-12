package com.poso2o.lechuan.manager.poster;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.poster.MyFlowsQueryDTO;
import com.poso2o.lechuan.bean.poster.PosterQueryDTO;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.tool.print.Print;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * 广告我的关注
 * Created by Luo on 2017-12-01.
 */

public class MyFlowsDataManager<T> extends BaseManager {
    public static MyFlowsDataManager mMyFlowsDataManager;
    /**
     * 获取广告 - 我的关注
     */
    private final int TAG_POSTER_ID = 1;

    public static MyFlowsDataManager getMyFlowsDataManager() {
        if (mMyFlowsDataManager == null) {
            mMyFlowsDataManager = new MyFlowsDataManager();
        }
        return mMyFlowsDataManager;
    }

    /**
     * 获取广告 - 我的关注
     *
     * @param baseActivity
     * @param currPage       查看页码 （必填）
     * @param callBack       回调
     */
    public void loadFlowsData(final BaseActivity baseActivity,  final String currPage, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_MY_FLOWS_API);
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
                MyFlowsQueryDTO posterQueryDTO = gson.fromJson(response, MyFlowsQueryDTO.class);
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
