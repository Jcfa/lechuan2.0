package com.poso2o.lechuan.manager.oa;

import android.content.Context;
import android.telecom.CallScreeningService;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.oa.TemplateGroups;
import com.poso2o.lechuan.http.CallServer;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.main.UserDataManager;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.StringRequest;

/**
 * 公众号助手-模版
 * Created by Administrator on 2018-02-05.
 */

public class TemplateDataManager extends BaseManager {
    private static TemplateDataManager mInstance;

    private TemplateDataManager() {
    }

    public static TemplateDataManager getInstance() {
        synchronized (TemplateDataManager.class) {
            if (mInstance == null) {
                mInstance = new TemplateDataManager();
            }
            return mInstance;
        }
    }

    /**
     * 我购买的模版
     */
    public void getMyTemplateGroups(BaseActivity baseActivity, final IRequestCallBack callBack) {
        Request request = new StringRequest(HttpAPI.OA_FREE_EDIT_MY_TEMPLATE_API, RequestMethod.POST);
        request = defaultParam(request);
        CallServer.getInstance().request(baseActivity, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                TemplateGroups templateGroups=new Gson().fromJson(response,TemplateGroups.class);
                callBack.onResult(what, templateGroups);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, false, false);
    }

    /**
     * 设置默认模板
     * @param baseActivity
     * @param template_id
     * @param iRequestCallBack
     */
    public void setDefaultModel(BaseActivity baseActivity,String group_id,String template_id,final IRequestCallBack iRequestCallBack){
        Request<String> request = getStringRequest(HttpAPI.OA_SET_DEFAULT_MODEL);
        defaultParamNoShop(request);
        request.add("template_id",template_id);
        request.add("group_id",group_id);

        baseActivity.request(001, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(001,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(001,response);
            }
        },true,true);
    }
}
