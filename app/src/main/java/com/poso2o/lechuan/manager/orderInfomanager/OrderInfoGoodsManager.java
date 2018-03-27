package com.poso2o.lechuan.manager.orderInfomanager;

import android.util.Log;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoSellBean;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoSellCountBean;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoSellDetailBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RMemberHttpAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by ${cbf} on 2018/3/13 0013.
 * 畅销商品和畅销详情界面接口
 */

public class OrderInfoGoodsManager extends BaseManager {
    public static final int ORDER_LIST = 2001;
    private static volatile OrderInfoGoodsManager infoSellManager;

    public static OrderInfoGoodsManager getOrderInfo() {
        if (infoSellManager == null) {
            synchronized (OrderInfoGoodsManager.class) {
                if (infoSellManager == null)
                    infoSellManager = new OrderInfoGoodsManager();
            }
        }
        return infoSellManager;
    }

    //畅销商品列表
    public void orderInfoGoodsApi(BaseActivity activity, String begin, String close, String currPage, final IRequestCallBack requestCallBack) {
        Request<String> request = getStringRequest(RMemberHttpAPI.O_REMBER_SELL_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("begin_date", begin);
        request.add("close_date", close);
        request.add("currPage", currPage);
        activity.request(ORDER_LIST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }

                OrderInfoSellBean sellCountBean = new Gson().fromJson(response, OrderInfoSellBean.class);
                requestCallBack.onResult(ORDER_LIST, sellCountBean);
            }

            @Override
            public void onFailed(int what, String response) {
                requestCallBack.onFailed(ORDER_LIST, response);
            }
        }, true, true);
    }

    //畅销商品详情
    public void orderInfoDetailSell(BaseActivity activity, String begin_date, String close_date, String guid,
                                    String colorid, String sizeid,
                                    final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(RMemberHttpAPI.O_REMBER_SELL_DETAIL_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("begin_date", begin_date);
        request.add("close_date", close_date);
        request.add("guid", guid);
        request.add("colorid", colorid);
        request.add("sizeid", sizeid);
        activity.request(ORDER_LIST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }
                OrderInfoSellDetailBean sellDetailBean = new Gson().fromJson(response, OrderInfoSellDetailBean.class);
                if (sellDetailBean != null)
                    callBack.onResult(ORDER_LIST, sellDetailBean);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(ORDER_LIST, response);

            }
        }, true, true);

    }
}
