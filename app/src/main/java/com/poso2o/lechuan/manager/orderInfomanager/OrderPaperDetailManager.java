package com.poso2o.lechuan.manager.orderInfomanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoSellBean;
import com.poso2o.lechuan.bean.orderInfo.OrderMemberDetailBean;
import com.poso2o.lechuan.bean.orderInfo.OrderMothsDetailBean;
import com.poso2o.lechuan.bean.orderInfo.OrderPaperDetailBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RMemberHttpAPI;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by ${cbf} on 2018/3/13 0013.
 * 库存管理详情
 */

public class OrderPaperDetailManager extends BaseManager {
    public static final int ORDER_LIST = 2001;
    private static volatile OrderPaperDetailManager infoSellManager;

    public static OrderPaperDetailManager getOrderInfo() {
        if (infoSellManager == null) {
            synchronized (OrderPaperDetailManager.class) {
                if (infoSellManager == null)
                    infoSellManager = new OrderPaperDetailManager();
            }
        }
        return infoSellManager;
    }

    //畅销商品列表
    public void orderPaperDetailApi(BaseActivity activity, String guid, final IRequestCallBack requestCallBack) {
        Request<String> request = getStringRequest(RMemberHttpAPI.O_REQUEST_PAPER_DETAIL);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("guid", guid);
        activity.request(ORDER_LIST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }

                OrderPaperDetailBean detailBean = new Gson().fromJson(response, OrderPaperDetailBean.class);
                requestCallBack.onResult(ORDER_LIST, detailBean);
            }

            @Override
            public void onFailed(int what, String response) {
                requestCallBack.onFailed(ORDER_LIST, response);
            }
        }, true, true);

    }

    //月损益表详情
    public void orderMothsDetailApi(BaseActivity activity, String shopid, String begin_date, final IRequestCallBack requestCallBack) {
        final Request<String> request = getStringRequest(RMemberHttpAPI.O_REQUEST_MOTHS_DETAIL_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", shopid);
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("begin_date", begin_date);
        activity.request(ORDER_LIST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }
                OrderMothsDetailBean mothsDetailBean = new Gson().fromJson(response, OrderMothsDetailBean.class);
                requestCallBack.onResult(ORDER_LIST, mothsDetailBean);
            }

            @Override
            public void onFailed(int what, String response) {
                requestCallBack.onFailed(ORDER_LIST, response);

            }
        }, true, true);
    }

    //会员管理详情 uid 会员id
    public void orderMemberDetailApi(BaseActivity activity, String uid, final IRequestCallBack callBack) {
        String todayDate = CalendarUtil.getTodayDate();
        Request<String> request = getStringRequest(RMemberHttpAPI.R_MEMBER_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("begin_date", "2015-01-01");
        request.add("close_date", todayDate);
        request.add("uid", uid);
        activity.request(ORDER_LIST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }
                OrderMemberDetailBean memberDetailBean = new Gson().fromJson(response, OrderMemberDetailBean.class);
                callBack.onResult(ORDER_LIST, memberDetailBean);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(ORDER_LIST, response);

            }
        }, true, true);

    }
}
