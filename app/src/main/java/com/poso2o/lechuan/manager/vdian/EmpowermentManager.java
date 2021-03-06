package com.poso2o.lechuan.manager.vdian;

import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.http.CallServer;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * 授权管理
 *
 * Created by Administrator on 2018/3/15 0015.
 */
public class EmpowermentManager extends BaseManager {

    private int TRIALID = 1;

    public String SERVICE_TRY_USE_URL = HttpAPI.SERVER_MAIN_API + "SystemServiceManage.htm?Act=list";
    public String SERVICE_TRANSLATE_URL = HttpAPI.SERVER_MAIN_API + "BuyServiceManage.htm?Act=wxBuy";
    public String SERVICE_OPEN_STATE_URL = HttpAPI.SERVER_MAIN_API + "WeCharAgencyManage.htm?Act=getAgency";
    public String SERVICE_SET_AGENCY_URL = HttpAPI.SERVER_MAIN_API + "WeCharAgencyManage.htm?Act=setAgency";
    public String SERVICE_ON_PROBATION_URL = HttpAPI.SERVER_MAIN_API + "UserAccountManage.htm?Act=tryWeChat";//微店开通7天试用

    private static EmpowermentManager instance;

    private EmpowermentManager() {

    }

    public static EmpowermentManager getInstance() {
        if (instance == null) {
            synchronized (VdianCatalogManager.class) {
                if (instance == null) {
                    instance = new EmpowermentManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取服务信息
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void trialListDate(final BaseActivity baseActivity, final IRequestCallBack iRequestCallBack) {
        final Request<String> request = getStringRequest(SERVICE_TRY_USE_URL);
        request.add("uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("token", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("service_type", "3");
        defaultParam(request);
        baseActivity.request(TRIALID, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(TRIALID, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(TRIALID, response);
            }
        }, true, true);
    }

    //微店7天试用
    public void vdianOnProbation(final BaseActivity baseActivity, final IRequestCallBack iRequestCallBack) {
        final Request<String> request = getStringRequest(SERVICE_ON_PROBATION_URL);
//        request.add("uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
//        request.add("token", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        defaultParam(request);
        baseActivity.request(TRIALID, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(TRIALID, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(TRIALID, response);
            }
        }, true, true);
    }

    /**
     * 获取微信预支付单
     *
     * @param baseActivity
     * @param service_id
     * @param iRequestCallBack
     */
    public void trialTranslateDate(final BaseActivity baseActivity, String service_id, final IRequestCallBack iRequestCallBack) {
        final Request<String> request = getStringRequest(SERVICE_TRANSLATE_URL);
//        request.add("uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
//        request.add("token", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shop_branch_id", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("service_id", service_id);
        defaultParam(request);

        baseActivity.request(TRIALID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(TRIALID, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(TRIALID, response);
            }
        }, true, true);
    }

    /**
     * 未开通微店，缴费服务id+详情
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void UnpaidDate(final BaseActivity baseActivity, final IRequestCallBack iRequestCallBack) {
        final Request<String> request = getStringRequest(SERVICE_TRY_USE_URL);
        request.add("uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("token", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("service_type", "4");
        defaultParam(request);

        baseActivity.request(TRIALID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }
                iRequestCallBack.onResult(TRIALID, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(TRIALID, response);
            }
        }, true, true);
    }


    /**
     * 获取开通的状态
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void OpenStateDate(final BaseActivity baseActivity, final IRequestCallBack iRequestCallBack) {
        final Request<String> request = getStringRequest(SERVICE_OPEN_STATE_URL);
//        request.add("uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
//        request.add("token", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
//        request.add("shop_id", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        defaultParam(request);
        baseActivity.request(TRIALID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(TRIALID, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(TRIALID, response);
            }
        }, true, true);
    }

    /**
     * 设置联系人和联系方式
     * @param baseActivity
     * @param attn
     * @param mobile
     * @param iRequestCallBack
     */
    public void setAgency(final BaseActivity baseActivity,String attn,String mobile, final IRequestCallBack iRequestCallBack){
        final Request<String> request = getStringRequest(SERVICE_SET_AGENCY_URL);
        request.add("attn", attn);
        request.add("mobile", mobile);
        defaultParam(request);
        CallServer.getInstance().request(baseActivity, TRIALID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(TRIALID, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(TRIALID, response);
            }
        }, false, false);
    }

}
