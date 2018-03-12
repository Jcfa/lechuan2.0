package com.poso2o.lechuan.manager.system;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.system.AddressDTO;
import com.poso2o.lechuan.bean.system.ExpressQueryDTO;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * 系统-地址&快递公司信息
 * Created by Luo on 2017-12-14.
 */

public class SystemDataManager<T> extends BaseManager {
    public static SystemDataManager SystemDataManager;
    /**
     * 系统 - 地址
     */
    private final int TAG_ORDER_ID = 1;

    public static SystemDataManager getSystemDataManager() {
        if (SystemDataManager == null) {
            SystemDataManager = new SystemDataManager();
        }
        return SystemDataManager;
    }

    /**
     * 地址信息
     *
     * @param baseActivity
     * @param callBack      回调
     */
    public void loadAddress(final BaseActivity baseActivity, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.SYSTEM_ADDRESS_API);
        request = defaultParam(request);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                AddressDTO datas = gson.fromJson(response, AddressDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 物流公司
     *
     * @param baseActivity
     * @param callBack      回调
     */
    public void loadExpress(final BaseActivity baseActivity, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.SYSTEM_EXPRESS_API);
        request = defaultParam(request);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                ExpressQueryDTO datas = gson.fromJson(response, ExpressQueryDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

}
