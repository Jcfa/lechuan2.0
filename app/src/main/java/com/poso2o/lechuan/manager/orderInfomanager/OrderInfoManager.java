package com.poso2o.lechuan.manager.orderInfomanager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.orderInfo.DataBean;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoBean;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoEntityDetailBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RMemberHttpAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/14 0014.
 * 我的订单信息
 */

public class OrderInfoManager extends BaseManager {
    public static final int ORDER_LIST = 2001;
    public static volatile OrderInfoManager infoManager;

    public static OrderInfoManager getInfoManager() {
        if (infoManager == null) {
            synchronized (OrderInfoManager.class) {
                if (infoManager == null)
                    infoManager = new OrderInfoManager();
            }
        }
        return infoManager;
    }

    public void myOrderInfo(BaseActivity activity, String beginTime, String endTime, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(RMemberHttpAPI.O_REMBER_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("begin_date", beginTime);
        request.add("close_date", endTime);
        activity.request(ORDER_LIST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }
                OrderInfoBean infoBean = new Gson().fromJson(response, OrderInfoBean.class);
                callBack.onResult(ORDER_LIST, infoBean);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(ORDER_LIST, response);

            }
        }, true, true);

    }

    public void orderEntityDetailApi(BaseActivity activity, String order_id, final IRequestCallBack callBack) {
        final Request<String> request = getStringRequest(RMemberHttpAPI.O_ORDER_DETAIL_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("order_id", order_id);
        request.add("version", "1");
        activity.request(ORDER_LIST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }
                OrderInfoEntityDetailBean detailBean=new Gson().fromJson(response,OrderInfoEntityDetailBean.class);
                callBack.onResult(ORDER_LIST,detailBean);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(ORDER_LIST,response);

            }
        }, true, true);
    }

}
