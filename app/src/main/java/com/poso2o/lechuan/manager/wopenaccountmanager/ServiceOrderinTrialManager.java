package com.poso2o.lechuan.manager.wopenaccountmanager;

import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by Administrator on 2018/3/15 0015.
 */

public class ServiceOrderinTrialManager extends BaseManager {
    private int TRIALID=1;

    public String SERVICE_TRY_USE_URL = HttpAPI.SERVER_MAIN_API + "SystemServiceManage.htm?Act=list";
    public String SERVICE_TRANSLATE_URL=HttpAPI.SERVER_MAIN_API + "BuyServiceManage.htm?Act=wxBuy";
    public String SERVICE_OPEN_STATE_URL=HttpAPI.SERVER_MAIN_API + "WeCharAgencyManage.htm?Act=getAgency";

    //获取服务信息
    public void TrialListDate(final BaseActivity baseActivity,final IRequestCallBack iRequestCallBack){
        SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);

        final Request<String> request = getStringRequest(SERVICE_TRY_USE_URL);
        request.add("uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("token", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("service_type","3");
        defaultParam(request);

        baseActivity.request(TRIALID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(TRIALID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(TRIALID,response);
            }
        },true,true);
    }



    //获取微信预支付单
    public void TrialTranslateDate(final BaseActivity baseActivity,String service_id,final IRequestCallBack iRequestCallBack){
        SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);

        final Request<String> request = getStringRequest(SERVICE_TRANSLATE_URL);
        request.add("uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("token", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shop_branch_id",SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("service_id",service_id);
        defaultParam(request);

        baseActivity.request(TRIALID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(TRIALID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(TRIALID,response);
            }
        },true,true);
    }

    //未开通微店，缴费服务id+详情
    public void UnpaidDate(final BaseActivity baseActivity,final IRequestCallBack iRequestCallBack){
        SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);

        final Request<String> request = getStringRequest(SERVICE_TRY_USE_URL);
        request.add("uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("token", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("service_type","4");
        defaultParam(request);

        baseActivity.request(TRIALID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }
                iRequestCallBack.onResult(TRIALID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(TRIALID,response);
            }
        },true,true);
    }


    //获取开通的状态

    public void OpenStateDate(final BaseActivity baseActivity,final IRequestCallBack iRequestCallBack){
        SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);

        final Request<String> request = getStringRequest(SERVICE_OPEN_STATE_URL);
        request.add("uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("token", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shop_id",SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        defaultParam(request);

        baseActivity.request(TRIALID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(TRIALID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(TRIALID,response);
            }
        },true,true);
    }


}
