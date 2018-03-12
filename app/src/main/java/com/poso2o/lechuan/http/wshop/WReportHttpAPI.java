package com.poso2o.lechuan.http.wshop;

import com.poso2o.lechuan.http.HttpAPI;

/**
 * Created by mr zhang on 2017/12/12.
 */

public class WReportHttpAPI {

    //三项统计：销售额、完成率、毛利润
    public static final String W_BOSS_TOTAL = HttpAPI.SERVER_MAIN_API + "ReportManage.htm?Act=bossSaleTotal";

    //微店库存列表,跟微店商品列表是一样
//    public static final String W_STOCK_LIST = "http://wechat.poso2o.com/GoodsManage.htm?Act=query";
    public static final String W_STOCK_LIST = HttpAPI.SERVER_MAIN_API + "GoodsManage.htm?Act=list";

    //微店月损益表
    public static final String W_MONTH_PROFIT_LIST = HttpAPI.SERVER_MAIN_API + "MonthProfitLossManage.htm?Act=report";
    //月损益详情
    public static final String W_MONTH_PROFIT_INFO = HttpAPI.SERVER_MAIN_API + "MonthProfitLossManage.htm?Act=getMonthProfitLoss";
    //月损益设置
    public static final String W_MONTH_PROFIT_SET = HttpAPI.SERVER_MAIN_API + "MonthProfitLossManage.htm?Act=setMonthProfitLoss";

    //热销商品
    public static final String W_HOT_SALE_LIST = HttpAPI.SERVER_MAIN_API + "SaleGoodReportManage.htm?Act=report";
}
