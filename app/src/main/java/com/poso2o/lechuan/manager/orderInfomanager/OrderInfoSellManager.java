package com.poso2o.lechuan.manager.orderInfomanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoSellCountBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RMemberHttpAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by ${cbf} on 2018/3/13 0013.
 */

public class OrderInfoSellManager extends BaseManager {
    public static final int ORDER_LIST = 2001;
    private static volatile OrderInfoSellManager infoSellManager;

    public static OrderInfoSellManager getOrderInfo() {
        if (infoSellManager == null) {
            synchronized (OrderInfoSellManager.class) {
                if (infoSellManager == null)
                    infoSellManager = new OrderInfoSellManager();
            }
        }
        return infoSellManager;
    }

    public void orderInfoSell(BaseActivity activity, String begin, String close, final IRequestCallBack requestCallBack) {
        Request<String> request = getStringRequest(RMemberHttpAPI.O_REMBER_MAIN_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("begin_date", begin);
        request.add("close_date", close);
        activity.request(ORDER_LIST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }

                OrderInfoSellCountBean sellCountBean = new Gson().fromJson(response, OrderInfoSellCountBean.class);
                requestCallBack.onResult(ORDER_LIST, sellCountBean);
            }

            @Override
            public void onFailed(int what, String response) {
                requestCallBack.onFailed(ORDER_LIST, response);
            }
        }, true, true);

    }

}
