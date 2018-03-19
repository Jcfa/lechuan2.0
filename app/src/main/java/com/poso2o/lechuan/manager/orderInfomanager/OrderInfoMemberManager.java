package com.poso2o.lechuan.manager.orderInfomanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoMemberBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RMemberHttpAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by ${cbf} on 2018/3/16 0016.
 * 会员管理
 */

public class OrderInfoMemberManager extends BaseManager {
    public static OrderInfoMemberManager sInstance;
    private int REQUEST_WHATE = 112;

    public static OrderInfoMemberManager getsInstance() {
        if (sInstance == null) {
            synchronized (OrderInfoMemberManager.class) {
                if (sInstance == null)
                    sInstance = new OrderInfoMemberManager();
            }
        }
        return sInstance;
    }

    public void oInfoMember(BaseActivity activity, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(RMemberHttpAPI.R_MEMBER_LIST);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        activity.request(REQUEST_WHATE, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }
                OrderInfoMemberBean infoMemberBean = new Gson().fromJson(response, OrderInfoMemberBean.class);
                callBack.onResult(what, infoMemberBean);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);

            }
        }, true, true);
    }
}
