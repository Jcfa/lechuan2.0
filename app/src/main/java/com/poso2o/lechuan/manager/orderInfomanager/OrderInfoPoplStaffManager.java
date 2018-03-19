package com.poso2o.lechuan.manager.orderInfomanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.orderInfo.OrderIOnfoStaffDetailBean;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoPoplStaffBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RMemberHttpAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by ${cbf} on 2018/3/19 0019.
 * 人员业绩
 */

public class OrderInfoPoplStaffManager extends BaseManager {
    public static OrderInfoPoplStaffManager sInstance;
    public int REQUEST = 116;

    public static OrderInfoPoplStaffManager getsInstance() {
        if (sInstance == null) {
            synchronized (OrderInfoPoplStaffManager.class) {
                if (sInstance == null)
                    sInstance = new OrderInfoPoplStaffManager();
            }
        }
        return sInstance;
    }

    //人员业绩列表
    public void poplStaffApi(BaseActivity activity, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(RMemberHttpAPI.O_POPLE_STAFF_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        activity.request(REQUEST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }
                OrderInfoPoplStaffBean poplStaffBean = new Gson().fromJson(response, OrderInfoPoplStaffBean.class);
                callBack.onResult(REQUEST, poplStaffBean);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(REQUEST, response);

            }
        }, true, true);
    }

    //人员业绩详情  sales员工id
    public void poplStaffDetailApi(BaseActivity activity, String sales, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(RMemberHttpAPI.O_POPLE_STAFF_DETAIL_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sales", sales);
        activity.request(REQUEST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }
                OrderIOnfoStaffDetailBean staffDetailBean = new Gson().fromJson(response, OrderIOnfoStaffDetailBean.class);
                callBack.onResult(REQUEST, staffDetailBean);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(REQUEST, response);

            }
        }, true, true);
    }
}
