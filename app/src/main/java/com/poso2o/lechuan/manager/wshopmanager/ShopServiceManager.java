package com.poso2o.lechuan.manager.wshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.shopdata.AllServiceBean;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.tool.print.Print;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by mr zhang on 2017/12/8.
 */

public class ShopServiceManager extends BaseManager {

    //乐传服务；试用七天
    public static final String SERVICE_TRY_USE_URL = HttpAPI.SERVER_MAIN_API + "UserAccountManage.htm?Act=tryLeChuan";
    public static final int SERVICE_TRY_USE_ID = 1;

    //系统服务列表
    public static final String SERVICE_LIST_URL = HttpAPI.SERVER_MAIN_API + "SystemServiceManage.htm?Act=list";
    public static final int SERVICE_LIST_ID = 2;

    private static volatile ShopServiceManager shopServiceManager;
    public static ShopServiceManager getShopServiceManager(){
        if (shopServiceManager == null){
            synchronized (ShopServiceManager.class){
                if (shopServiceManager == null){
                    shopServiceManager = new ShopServiceManager();
                }
            }
        }
        return shopServiceManager;
    }

    /**
     * 试用七天（乐传分销）
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void tryUseService(final BaseActivity baseActivity, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(SERVICE_TRY_USE_URL);
        defaultParam(request);

        baseActivity.request(SERVICE_TRY_USE_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(SERVICE_TRY_USE_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(SERVICE_TRY_USE_ID,response);
            }
        },true,true);
    }

    /**
     * 购买服务列表
     * @param baseActivity
     * @param service_type        服务费用类型,1=商家，2=分销商（必填）
     * @param iRequestCallBack
     */
    public void serviceList(final BaseActivity baseActivity,String service_type,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(SERVICE_LIST_URL);
        defaultParam(request);
        request.add("service_type",service_type);

        baseActivity.request(SERVICE_LIST_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Print.println("测试服务列表：" + response);
                AllServiceBean allServiceBean = new Gson().fromJson(response,AllServiceBean.class);
                iRequestCallBack.onResult(SERVICE_LIST_ID,allServiceBean);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(SERVICE_LIST_ID,response);
            }
        },true,true);
    }
}
