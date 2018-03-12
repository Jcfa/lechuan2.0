package com.poso2o.lechuan.manager.oa;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.oa.OaService;
import com.poso2o.lechuan.bean.oa.OaServiceInfo;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.wshop.OaAPI;
import com.poso2o.lechuan.util.TextUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * Created by mr zhang on 2018/2/6.
 */

public class OaServiceManager extends BaseManager {

    //公众号系统服务列表
    private static final int OA_SERVICE_ID = 2201;
    //公众号系统服务微信支付
    private static final int OA_SERVICE_WECHAT_ID = 2202;
    //公众号系统服务支付宝支付
    private static final int OA_SERVICE_ALI_ID = 2203;
    //公众号服务信息
    private static final int OA_SERVICE_INFO_ID = 2204;

    private static OaServiceManager oaServiceManager;
    private OaServiceManager(){
    }
    public static OaServiceManager getOaServiceManager(){
        if (oaServiceManager == null){
            synchronized (OaServiceManager.class){
                if (oaServiceManager == null)oaServiceManager = new OaServiceManager();
            }
        }
        return oaServiceManager;
    }

    /**
     * 公众号系统服务列表
     * @param baseActivity
     * @param iRequestCallBack
     * @param service_type  服务费用类型,1=商家【连锁版】，2=分销商【乐传】, 3=公众号文章发布数模板，4.简约模板  5.新品推荐  6.会员生日  7.文章模板 8.优惠卡卷
     */
    public void oaService(BaseActivity baseActivity,String service_type, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(OaAPI.OA_SERVICE_URL);
        defaultParam(request);
        request.add("service_type",service_type);

        baseActivity.request(OA_SERVICE_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                OaService oaService = new Gson().fromJson(response,OaService.class);
                iRequestCallBack.onResult(OA_SERVICE_ID,oaService);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(OA_SERVICE_ID,response);
            }
        },true,true);
    }

    /**
     * 购买服务微信支付
     * @param baseActivity
     * @param service_id
     * @param has_operationt
     * @param iRequestCallBack
     */
    public void wechatService(final BaseActivity baseActivity,String service_id,int has_operationt,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(OaAPI.OA_SERVICE_WECHAT_URL);
        defaultParam(request);
        request.add("service_id",service_id);
        if (has_operationt != -1)request.add("has_operationt",has_operationt + "");

        baseActivity.request(OA_SERVICE_WECHAT_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                if (TextUtil.isNotEmpty(response)) {
                    PayReq req = new PayReq();
                    try {
                        JSONObject data = new JSONObject(response);
                        req.appId = data.getString("appid");
                        req.nonceStr = data.getString("noncestr");
                        req.packageValue = data.getString("package");
                        req.partnerId = data.getString("partnerid");
                        req.prepayId = data.getString("prepayid");
                        req.sign = data.getString("sign");
                        req.timeStamp = data.getString("timestamp");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    iRequestCallBack.onResult(OA_SERVICE_WECHAT_ID,req);
                } else {
                    iRequestCallBack.onFailed(OA_SERVICE_WECHAT_ID,baseActivity.getString(R.string.toast_wx_pay_fail));
                }
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(OA_SERVICE_WECHAT_ID,response);
            }
        },true,true);
    }

    /**
     * 公众号服务支付宝支付
     * @param baseActivity
     * @param service_id
     * @param has_operationt
     * @param iRequestCallBack
     */
    public void aliService(final BaseActivity baseActivity,String service_id,int has_operationt,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(OaAPI.OA_SERVICE_ALI_URL);
        defaultParam(request);
        request.add("service_id",service_id);
        if (has_operationt != -1)request.add("has_operationt",has_operationt + "");

        baseActivity.request(OA_SERVICE_ALI_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                if (response != null) {
                    iRequestCallBack.onResult(OA_SERVICE_ALI_ID,response);
                } else {
                    iRequestCallBack.onFailed(OA_SERVICE_ALI_ID,baseActivity.getString(R.string.toast_wx_pay_fail));
                }
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(OA_SERVICE_ALI_ID,response);
            }
        },true,true);
    }

    /**
     * 已购买的公众号服务信息
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void oaServiceInfo(BaseActivity baseActivity, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(OaAPI.OA_SERVICE_INFO_URL);
        defaultParam(request);

        baseActivity.request(OA_SERVICE_INFO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                OaServiceInfo serviceInfo = new Gson().fromJson(response,OaServiceInfo.class);
                iRequestCallBack.onResult(OA_SERVICE_INFO_ID,serviceInfo);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(OA_SERVICE_INFO_ID,response);
            }
        },true,true);
    }
}
