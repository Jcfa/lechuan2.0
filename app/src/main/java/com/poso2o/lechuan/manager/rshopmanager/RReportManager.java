package com.poso2o.lechuan.manager.rshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.bosstotal.BossTotalBean;
import com.poso2o.lechuan.bean.goodsdata.AllGoodsStockData;
import com.poso2o.lechuan.bean.goodsdata.AllHotSaleProduct;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.HotSaleProduct;
import com.poso2o.lechuan.bean.monthprofit.AllMonthProfitData;
import com.poso2o.lechuan.bean.monthprofit.MonthProfitData;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.bean.staffreport.AchieveDetailData;
import com.poso2o.lechuan.bean.staffreport.AllStaffReportData;
import com.poso2o.lechuan.bean.staffreport.StaffReportData;
import com.poso2o.lechuan.bean.transfer.Transfer;
import com.poso2o.lechuan.bean.transfer.TransferAllData;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RReportHttpAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mr zhang on 2017/12/4.
 */

public class RReportManager extends BaseManager{
    //三项统计
    public static final int R_REPORT_TOTAL_ID = 5001;
    //交接班明细
    public static final int R_TRANSFER_ID = 5008;
    //热销商品
    public static final int R_REPORT_HOT_ID = 5002;
    //商品库存
    public static final int R_REPORT_STOCK_ID = 5003;
    //月损益表
    public static final int R_REPORT_PROFIT_ID = 5004;
    //人员业绩
    public static final int R_REPORT_ACHIEVE_ID= 5005;
    //人员业绩设置
    public static final int R_REPORT_SET_ACHIEVE_ID = 5006;
    //批量设置人员业绩
    public static final int R_REPORT_BATCH_ACHIEVE_ID = 5007;
    //月损益表详情
    public static final int R_REPORT_PROFIT_INFO_ID = 5010;
    //月损益设置
    public static final int R_REPORT_PROFIT_SET_ID = 5011;

    //交接班详情
    public static final int R_TRANSFER_INFO_ID = 5009;
    //热销商品详情
    public static final int R_HOT_INFO_ID = 5012;
    //商品库存详情
    public static final int R_STOCK_DETAIL = 5013;
    //人员业绩详情
    public static final int R_ACHIEVE_DETAIL_ID = 5014;

    //商品库存数据集合
    private ArrayList<Goods> stockList = new ArrayList<>();

    private static volatile RReportManager rReportManager;
    public static RReportManager getRReportManger(){
        if (rReportManager == null){
            synchronized (RReportManager.class){
                if (rReportManager == null)rReportManager = new RReportManager();
            }
        }
        return rReportManager;
    }

    /**
     * 数据统计
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void totalReport(final BaseActivity baseActivity, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RReportHttpAPI.R_REPORT_BOSS);
        defaultParam(request);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        baseActivity.request(R_REPORT_TOTAL_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                System.out.println("测试统计：" + response);
                BossTotalBean bossTotalBean = new Gson().fromJson(response,BossTotalBean.class);
                iRequestCallBack.onResult(R_REPORT_TOTAL_ID,bossTotalBean);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_REPORT_TOTAL_ID,response);
            }
        },true,true);
    }

    /**
     * 商品库存
     * @param baseActivity
     * @param currPage
     * @param iRequestCallBack
     */
    public void goodsStock(final BaseActivity baseActivity,String currPage, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RReportHttpAPI.R_REPORT_STOCK);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("currPage", currPage);

        baseActivity.request(R_REPORT_STOCK_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                System.out.println("测试库存：" + response);
                if (response.startsWith("[") && response.endsWith("]")){
                    response = "{\nlist\n:" + response + "}";
                }
                AllGoodsStockData allGoodsStockData = new Gson().fromJson(response,AllGoodsStockData.class);
                iRequestCallBack.onResult(R_REPORT_STOCK_ID,allGoodsStockData);
                stockList.clear();
                if (allGoodsStockData != null)stockList.addAll(allGoodsStockData.list);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_REPORT_STOCK_ID,response);
            }
        },true,true);
    }

    /**
     * 请求库存详情
     * @param baseActivity
     * @param guid
     * @param iRequestCallBack
     */
    public void stockDetail(BaseActivity baseActivity, String guid, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RReportHttpAPI.R_STOCK_DETAIL);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
//        request.add("begin_date", begin_date);
//        request.add("close_date", close_date);
        request.add("guid", guid);

        baseActivity.request(R_STOCK_DETAIL, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Goods goods = new Gson().fromJson(response,Goods.class);
                iRequestCallBack.onResult(R_STOCK_DETAIL,goods);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onResult(R_STOCK_DETAIL,response);
            }
        },true,true);
    }

    /**
     *月损益表
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void monthProfit(final BaseActivity baseActivity,String begin_date,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RReportHttpAPI.R_REPORT_PROFIT);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("begin_date", begin_date);

        baseActivity.request(R_REPORT_PROFIT_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                System.out.println("测试月损益：" + response);
                if (response.startsWith("[") && response.endsWith("]")){
                    response = "{\nlist\n:" + response + "}";
                }
                AllMonthProfitData allMonthProfitData = new Gson().fromJson(response,AllMonthProfitData.class);
                iRequestCallBack.onResult(R_REPORT_PROFIT_ID,allMonthProfitData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_REPORT_PROFIT_ID,response);
            }
        },true,true);
    }

    /**
     *月损益详情
     * @param baseActivity
     * @param months
     * @param iRequestCallBack
     */
    public void monthProfitInfo(final BaseActivity baseActivity,String months,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RReportHttpAPI.R_REPORT_PROFIT_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("months", months);

        baseActivity.request(R_REPORT_PROFIT_INFO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                MonthProfitData monthProfitData = new Gson().fromJson(response,MonthProfitData.class);
                iRequestCallBack.onResult(R_REPORT_PROFIT_INFO_ID,monthProfitData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_REPORT_PROFIT_INFO_ID,response);
            }
        },true,true);
    }

    /**
     * 设置月损益
     * @param baseActivity
     * @param monthProfitData
     * @param iRequestCallBack
     */
    public void rMonthProfitSet(final BaseActivity baseActivity,MonthProfitData monthProfitData,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RReportHttpAPI.R_REPORT_PROFIT_SET);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        request.add("months", monthProfitData.months);
        request.add("zj_cost", monthProfitData.zj_cost);
        request.add("gl_cost", monthProfitData.gl_cost);
        request.add("gz_cost", monthProfitData.gz_cost);
        request.add("sd_cost", monthProfitData.sd_cost);
        request.add("sl_cost", monthProfitData.sl_cost);
        request.add("tc_cost", monthProfitData.tc_cost);
        request.add("tg_cost", monthProfitData.tg_cost);
        request.add("zs_cost", monthProfitData.zs_cost);

        baseActivity.request(R_REPORT_PROFIT_SET_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(R_REPORT_PROFIT_SET_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_REPORT_PROFIT_SET_ID,response);
            }
        },true,true);
    }

    /**
     * 人员业绩
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void achievemence(final BaseActivity baseActivity,String begin_date,String close_date, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RReportHttpAPI.R_REPORT_ACHIEVE);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        if (!begin_date.equals("-1")){
            request.add("begin_date", begin_date);
            request.add("close_date", close_date);
        }

        baseActivity.request(R_REPORT_ACHIEVE_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                if (response.startsWith("[") && response.endsWith("]")){
                    response = "{\nlist\n:" + response + "}";
                }
                AllStaffReportData allStaffReportData = new Gson().fromJson(response,AllStaffReportData.class);
                iRequestCallBack.onResult(R_REPORT_ACHIEVE_ID,allStaffReportData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_REPORT_ACHIEVE_ID,response);
            }
        },true,true);
    }

    /**
     * 人员业绩详情
     * @param baseActivity
     * @param sales
     * @param begin_date
     * @param close_date
     * @param iRequestCallBack
     */
    public void achieveDetail(BaseActivity baseActivity, String sales, String begin_date, String close_date, final IRequestCallBack iRequestCallBack){

        Request<String> request = getStringRequest(RReportHttpAPI.R_ACHIEVE_DETAIL);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sales",sales);
        request.add("begin_date",begin_date);
        request.add("close_date",close_date);

        baseActivity.request(R_ACHIEVE_DETAIL_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                AchieveDetailData achieveDetailData = new Gson().fromJson(response,AchieveDetailData.class);
                iRequestCallBack.onResult(R_ACHIEVE_DETAIL_ID,achieveDetailData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_ACHIEVE_DETAIL_ID,response);
            }
        },true,true);
    }

    /**
     * 热销商品
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void hotSale(final BaseActivity baseActivity, String currPage,String begin_date,String close_date,String sort, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RReportHttpAPI.R_REPORT_HOT);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("currPage", currPage);
        request.add("order", "totalnum");
        request.add("sort", sort);
        request.add("begin_date", begin_date);
        request.add("close_date", close_date);

        baseActivity.request(R_REPORT_HOT_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                System.out.println("测试热销：" + response);
                if (response.startsWith("[") && response.endsWith("]")){
                    response = "{\nlist\n:" + response + "}";
                }
                AllHotSaleProduct allHotSaleProduct = new Gson().fromJson(response,AllHotSaleProduct.class);
                iRequestCallBack.onResult(R_REPORT_HOT_ID,allHotSaleProduct);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_REPORT_HOT_ID,response);
            }
        },true,true);
    }

    /**
     * 热销商品详情
     * @param baseActivity
     * @param begin_date
     * @param close_date
     * @param guid
     * @param colorid
     * @param sizeid
     * @param iRequestCallBack
     */
    public void hotSalesInfo(BaseActivity baseActivity, String begin_date, String close_date, String guid, String colorid, String sizeid, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RReportHttpAPI.R_HOT_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("guid", guid);
        request.add("colorid", colorid);
        request.add("sizeid", sizeid);
        request.add("begin_date", begin_date);
        request.add("close_date", close_date);

        baseActivity.request(R_HOT_INFO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                if (response.contains("参数错误")){
                    iRequestCallBack.onResult(R_HOT_INFO_ID,null);
                    return;
                }
                HotSaleProduct hotSaleProduct = new Gson().fromJson(response,HotSaleProduct.class);
                iRequestCallBack.onResult(R_HOT_INFO_ID,hotSaleProduct);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_HOT_INFO_ID,response);
            }
        },true,true);
    }

    /**
     * 交接明细列表
     * @param baseActivity
     * @param currPage
     * @param begin_date
     * @param close_date
     * @param czyid
     * @param iRequestCallBack
     */
    public void rTransfer(final BaseActivity baseActivity,String currPage,String begin_date,String close_date,String czyid,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RReportHttpAPI.R_REPORT_TRANSFER);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("currPage", currPage);
        request.add("begin_date", begin_date);
        request.add("close_date", close_date);
        request.add("czyid", czyid);

        baseActivity.request(R_TRANSFER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                System.out.println("测试热销：" + response);
                if (response.startsWith("[") && response.endsWith("]")){
                    response = "{\nlist\n:" + response + "}";
                }
                TransferAllData transferAllData = new Gson().fromJson(response,TransferAllData.class);
                iRequestCallBack.onResult(R_TRANSFER_ID,transferAllData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_TRANSFER_ID,response);
            }
        },true,true);
    }

    /**
     * 交接班详情
     * @param baseActivity
     * @param begin_date
     * @param close_date
     * @param czyid
     * @param iRequestCallBack
     */
    public void rTransferInfo(final BaseActivity baseActivity,String begin_date,String close_date,String czyid,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RReportHttpAPI.R_REPORT_TRANSFER_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("begin_date", begin_date);
        request.add("close_date", close_date);
        request.add("czyid", czyid);

        baseActivity.request(R_TRANSFER_INFO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                System.out.println("测试热销：" + response);
                Transfer transfer = new Gson().fromJson(response,Transfer.class);
                iRequestCallBack.onResult(R_TRANSFER_INFO_ID,transfer);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_TRANSFER_INFO_ID,response);
            }
        },true,true);
    }

    /**
     * 设置员工任务额
     * @param baseActivity
     * @param czy_bh
     * @param assignments
     * @param iRequestCallBack
     */
    public void setAchieve(final BaseActivity baseActivity, String czy_bh,String assignments,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RReportHttpAPI.R_REPORT_SET_TARGET);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy_bh", czy_bh);
        request.add("assignments", assignments);

        baseActivity.request(R_REPORT_SET_ACHIEVE_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(R_REPORT_SET_ACHIEVE_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_REPORT_SET_ACHIEVE_ID,response);
            }
        },true,true);
    }

    /**
     * 批量设置员工任务额
     * @param baseActivity
     * @param datas
     * @param iRequestCallBack
     */
    public void batchSetAchieve(final BaseActivity baseActivity, String datas,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RReportHttpAPI.R_REPORT_BATCH_SET_TARGET);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("datas", datas);

        baseActivity.request(R_REPORT_BATCH_ACHIEVE_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(R_REPORT_BATCH_ACHIEVE_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_REPORT_BATCH_ACHIEVE_ID,response);
            }
        },true,true);
    }

    //商品库存本地排序
    public ArrayList<Goods> sortStockGoods(final String sort){
        if (stockList.size() <= 1)return stockList;
        ArrayList<Goods> sortGoodsDatas = new ArrayList<>();
        sortGoodsDatas.addAll(stockList);

        Collections.sort(sortGoodsDatas, new Comparator<Goods>() {

            @Override
            public int compare(Goods o1, Goods o2) {
                Integer num1 = Integer.parseInt(o1.totalnum);
                Integer num2 = Integer.parseInt(o2.totalnum);
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
