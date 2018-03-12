package com.poso2o.lechuan.manager.wshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.bosstotal.BossTotalBean;
import com.poso2o.lechuan.bean.goodsdata.AllGoodsStockData;
import com.poso2o.lechuan.bean.goodsdata.AllHotSaleProduct;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsAllData;
import com.poso2o.lechuan.bean.monthprofit.AllMonthProfitData;
import com.poso2o.lechuan.bean.monthprofit.MonthProfitData;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.wshop.WReportHttpAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mr zhang on 2017/12/12.
 */

public class WReportManager extends BaseManager {

    //三项统计: 销售额，完成率，毛利润
    public static final int W_BOSS_TOTAL_ID = 20022;
    //商品库存列表
    public static final int W_STOCK_LIST_ID = 20023;
    //月损益表列表
    public static final int W_MONTH_PROFIT_LIST_ID = 20024;
    //月损益详情
    public static final int W_MONTH_PROFIT_INFO_ID = 20025;
    //月损益设置
    public static final int W_MONTH_PROFIT_SET_ID = 20026;
    //热销商品
    public static final int W_HOT_SALE_ID = 20027;

    //库存商品数据集合
    private ArrayList<Goods> stockGoods = new ArrayList<>();

    private static volatile WReportManager wReportManager;
    public static WReportManager getwReportManager(){
        if (wReportManager == null){
            synchronized (WReportManager.class){
                if (wReportManager == null)wReportManager = new WReportManager();
            }
        }
        return wReportManager;
    }

    /**
     * 三项统计：目标额，完成率，毛利润
     * @param baseActivity
     * @param begin_time
     * @param end_time
     * @param iRequestCallBack
     */
    public void wBossTotal(final BaseActivity baseActivity,String begin_time,String end_time,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(WReportHttpAPI.W_BOSS_TOTAL);
        defaultParam(request);
//        request.add("begin_time",begin_time);
//        request.add("end_time",end_time);

        baseActivity.request(W_BOSS_TOTAL_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                BossTotalBean totalBean = new Gson().fromJson(response,BossTotalBean.class);
                iRequestCallBack.onResult(W_BOSS_TOTAL_ID,totalBean);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_BOSS_TOTAL_ID,response);
            }
        },true,true);
    }

    /**
     * 微店商品库存，其实就是商品接口
     * @param baseActivity
     * @param currPage
     * @param sort
     * @param iRequestCallBack
     */
    public void wStockList(final BaseActivity baseActivity,String currPage,String sort, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(WReportHttpAPI.W_STOCK_LIST);
        defaultParam(request);
        request.add("shop_branch_id",SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        request.add("currPage", currPage);
        request.add("sort", sort);

        baseActivity.request(W_STOCK_LIST_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                AllGoodsStockData goodsAllData = new Gson().fromJson(response,AllGoodsStockData.class);
                iRequestCallBack.onResult(W_STOCK_LIST_ID,goodsAllData);
                stockGoods.clear();
                if (goodsAllData != null)stockGoods.addAll(goodsAllData.list);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_STOCK_LIST_ID,response);
            }
        },true,true);
    }

    /**
     * 月损益列表
     * @param baseActivity
     * @param year
     * @param iRequestCallBack
     */
    public void wMonthProfitList(final BaseActivity baseActivity,String year,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(WReportHttpAPI.W_MONTH_PROFIT_LIST);
        defaultParam(request);
        request.add("year", year);

        baseActivity.request(W_MONTH_PROFIT_LIST_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                AllMonthProfitData allMonthProfitData = new Gson().fromJson(response,AllMonthProfitData.class);
                iRequestCallBack.onResult(W_MONTH_PROFIT_LIST_ID,allMonthProfitData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_MONTH_PROFIT_LIST_ID,response);
            }
        },true,true);
    }

    /**
     * 查询某月损益详情
     * @param baseActivity
     * @param months
     * @param iRequestCallBack
     */
    public void wMonthProfitInfo(final BaseActivity baseActivity,String months,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(WReportHttpAPI.W_MONTH_PROFIT_INFO);
        defaultParam(request);
        request.add("months", months);

        baseActivity.request(W_MONTH_PROFIT_INFO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                MonthProfitData monthProfitData = new Gson().fromJson(response,MonthProfitData.class);
                iRequestCallBack.onResult(W_MONTH_PROFIT_INFO_ID,monthProfitData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_MONTH_PROFIT_INFO_ID,response);
            }
        },true,true);
    }

    /**
     * 设置月损益
     * @param baseActivity
     * @param monthProfitData
     * @param iRequestCallBack
     */
    public void wMonthProfitSet(final BaseActivity baseActivity,MonthProfitData monthProfitData,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(WReportHttpAPI.W_MONTH_PROFIT_SET);
        defaultParam(request);
        request.add("months", monthProfitData.months);
        request.add("total_amount", monthProfitData.total_amount);
        request.add("total_cost", monthProfitData.total_cost);
        request.add("shop_rent", monthProfitData.shop_rent);
        request.add("water_electricity", monthProfitData.water_electricity);
        request.add("property_management_fee", monthProfitData.property_management_fee);
        request.add("tax", monthProfitData.tax);
        request.add("promotion_fee", monthProfitData.promotion_fee);
        request.add("staff_wages", monthProfitData.staff_wages);
        request.add("staff_commmission", monthProfitData.staff_commmission);
        request.add("other_expenses", monthProfitData.other_expenses);
        request.add("total_profit", monthProfitData.total_profit);
        request.add("net_profit", monthProfitData.net_profit);

        baseActivity.request(W_MONTH_PROFIT_SET_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                MonthProfitData monthProfitData = new Gson().fromJson(response,MonthProfitData.class);
                iRequestCallBack.onResult(W_MONTH_PROFIT_SET_ID,monthProfitData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_MONTH_PROFIT_SET_ID,response);
            }
        },true,true);
    }

    /**
     * 微店热销商品
     * @param baseActivity
     * @param currPage
     * @param begin_time
     * @param end_time
     * @param sort
     * @param distributor_id      分销商id，选填
     * @param iRequestCallBack
     */
    public void wHotSale(final BaseActivity baseActivity,String currPage,String begin_time,String end_time,String sort,String distributor_id,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(WReportHttpAPI.W_HOT_SALE_LIST);
        defaultParam(request);
        request.add("currPage", currPage);
        request.add("begin_time", begin_time);
        request.add("end_time", end_time);
        request.add("sort", sort);
//        request.add("distributor_id", distributor_id);

        baseActivity.request(W_HOT_SALE_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                AllHotSaleProduct allHotSaleProduct = new Gson().fromJson(response,AllHotSaleProduct.class);
                iRequestCallBack.onResult(W_HOT_SALE_ID,allHotSaleProduct);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_HOT_SALE_ID,response);
            }
        },true,true);
    }

    //商品库存本地排序
    public ArrayList<Goods> sortStockGoods(final String sort){
        if (stockGoods.size() <= 1)return stockGoods;
        ArrayList<Goods> sortGoodsDatas = new ArrayList<>();
        sortGoodsDatas.addAll(stockGoods);

        Collections.sort(sortGoodsDatas, new Comparator<Goods>() {

            @Override
            public int compare(Goods o1, Goods o2) {
                Integer num1 = o1.goods_number;
                Integer num2 = o2.goods_number;
                if (sort.equals("ASC")) {// 升序
                    return num1.compareTo(num2);
                } else {// 降序
                    return num2.compareTo(num1);
                }
            }
        });
        return sortGoodsDatas;
    }
}
