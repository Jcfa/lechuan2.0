package com.poso2o.lechuan.manager.rshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.oldorder.QuerySaleOrderData;
import com.poso2o.lechuan.bean.oldorder.SaleOrderDate;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by mr zhang on 2017/12/15.
 */

public class ROrderManager extends BaseManager {

    //订单查询
    public static final String R_ORDER_QUERY_URL = "http://fuzhuang.poso2o.com/boss.htm?Act=nowSales";
    public static final int R_ORDER_QUERY_ID = 10029;
    //订单详情
    public static final String R_ORDER_INFO_URL = "http://fuzhuang.poso2o.com/order.htm?Act=view";
    public static final int R_ORDER_INFO_ID = 10030;

    private static volatile ROrderManager rOrderManager;
    public static ROrderManager getrOrderManager(){
        if (rOrderManager == null){
            synchronized (ROrderManager.class){
                if (rOrderManager == null)rOrderManager = new ROrderManager();
            }
        }
        return rOrderManager;
    }

    /**
     * 订单查询（关键词为空时返回所有，包括分页）
     * @param baseActivity
     * @param currPage
     * @param begin_date
     * @param close_date
     * @param keywords
     * @param iRequestCallBack
     */
    public void queryOrder(final BaseActivity baseActivity, int currPage, String begin_date, String close_date, String keywords, final IRequestCallBack iRequestCallBack){

        Request<String> request = getStringRequest(R_ORDER_QUERY_URL);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        request.add("currPage", currPage + "");
        request.add("begin_date", begin_date);
        request.add("close_date", close_date);
        if (TextUtil.isNotEmpty(keywords)) request.add("keywords", keywords);

        baseActivity.request(R_ORDER_QUERY_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                QuerySaleOrderData saleOrderData = new Gson().fromJson(response,QuerySaleOrderData.class);
                iRequestCallBack.onResult(R_ORDER_QUERY_ID,saleOrderData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_ORDER_QUERY_ID,response);
            }
        },true,true);
    }

    /**
     * 获取订单详情
     * @param baseActivity
     * @param begin_date
     * @param close_date
     * @param order_id
     * @param iRequestCallBack
     */
    public void orderInfo(final BaseActivity baseActivity,String begin_date,String close_date,String order_id,final IRequestCallBack iRequestCallBack){

        Request<String> request = getStringRequest(R_ORDER_INFO_URL);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        request.add("order_id", order_id);
        request.add("begin_date", begin_date);
        request.add("close_date", close_date);

        baseActivity.request(R_ORDER_INFO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                SaleOrderDate saleOrderData = new Gson().fromJson(response,SaleOrderDate.class);
                iRequestCallBack.onResult(R_ORDER_INFO_ID,saleOrderData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_ORDER_INFO_ID,response);
            }
        },true,true);
    }
}
