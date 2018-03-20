package com.poso2o.lechuan.manager.orderInfomanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoPaperBean;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoSellCountBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RMemberHttpAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by ${cbf} on 2018/3/13 0013.
 */

public class OrderInfoPaperManager extends BaseManager {
    public static final int ORDER_LIST = 2001;
    private static volatile OrderInfoPaperManager infoSellManager;

    public static OrderInfoPaperManager getsInsatcne() {
        if (infoSellManager == null) {
            synchronized (OrderInfoPaperManager.class) {
                if (infoSellManager == null)
                    infoSellManager = new OrderInfoPaperManager();
            }
        }
        return infoSellManager;
    }

    //畅销商品列表
    public void orderInfoPaperApi(BaseActivity activity, final IRequestCallBack requestCallBack) {
        Request<String> request = getStringRequest(RMemberHttpAPI.O_REMBER_PAPER_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        activity.request(ORDER_LIST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }

                OrderInfoPaperBean sellCountBean = new Gson().fromJson(response, OrderInfoPaperBean.class);
                requestCallBack.onResult(ORDER_LIST, sellCountBean);
            }

            @Override
            public void onFailed(int what, String response) {
                requestCallBack.onFailed(ORDER_LIST, response);
            }
        }, true, true);

    }
}
