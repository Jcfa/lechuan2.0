package com.poso2o.lechuan.manager.oa;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.oa.OaTypeAndLables;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by mr zhang on 2018/3/16.
 *
 * 文章类型标签管理类
 */

public class OaTypesManager extends BaseManager {

    private static final int OA_TYPES_ID = 2601;

    public OaTypeAndLables oaTypeAndLables;

    private static OaTypesManager oaTypesManager;
    private OaTypesManager(){
    }
    public static OaTypesManager getOaTypesManager(){
        if (oaTypesManager == null){
            synchronized (OaTypesManager.class){
                if (oaTypesManager == null)oaTypesManager = new OaTypesManager();
            }
        }
        return oaTypesManager;
    }

    /**
     * 获取文章类型和标签数据
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void getTypesAndLabels(BaseActivity baseActivity,IRequestCallBack iRequestCallBack){
        if (oaTypeAndLables == null){
            typesAndLabels(baseActivity,iRequestCallBack);
        }else {
            iRequestCallBack.onResult(OA_TYPES_ID,oaTypeAndLables);
        }
    }

    /**
     * 请求文章类型和标签数据
     * @param baseActivity
     * @param iRequestCallBack
     */
    private void typesAndLabels(BaseActivity baseActivity,final IRequestCallBack iRequestCallBack){

        Request<String> request = getStringRequest(HttpAPI.ARTICLE_TYPE_API);
        defaultParamNoShop(request);

        baseActivity.request(OA_TYPES_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                oaTypeAndLables = new Gson().fromJson(response,OaTypeAndLables.class);
                iRequestCallBack.onResult(OA_TYPES_ID,oaTypeAndLables);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(OA_TYPES_ID,response);
            }
        },true,true);
    }

}
