package com.poso2o.lechuan.manager.orderInfomanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoPrimeCostBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RMemberHttpAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by ${cbf} on 2018/3/16 0016.
 * 月损益表
 */

public class OrderInfoPrimeCostManager extends BaseManager {
    public static OrderInfoPrimeCostManager sInstance;
    private static int REQUSET_WHATE = 100;

    public static OrderInfoPrimeCostManager getsInstance() {
        if (sInstance == null) {
            synchronized (OrderInfoPrimeCostManager.class) {
                if (sInstance == null)
                    sInstance = new OrderInfoPrimeCostManager();
            }
        }
        return sInstance;
    }

    //接口方法和传参
    public void oPrimeCostInfo(BaseActivity activity, String begin_date, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(RMemberHttpAPI.O_REMBER_MOTHS_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("begin_date", begin_date);
        activity.request(REQUSET_WHATE, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }
                OrderInfoPrimeCostBean costBean = new Gson().fromJson(response, OrderInfoPrimeCostBean.class);
                callBack.onResult(REQUSET_WHATE, costBean);

            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(REQUSET_WHATE, response);

            }
        }, true, true);
    }
}
