package com.poso2o.lechuan.http.realshop;

/**
 * Created by mr zhang on 2017/12/4.
 */

public class RReportHttpAPI {

    //统计数据访问主地址
    public static final String R_REPORT_URL = "http://fuzhuang.poso2o.com/boss.htm?Act=";
//    public static final String R_REPORT_URL = "http://192.168.10.153:8080/boss.htm?Act=";

    //三项统计
    public static final String R_REPORT_BOSS = R_REPORT_URL + "index";
    //畅销商品
    public static final String R_REPORT_HOT = R_REPORT_URL + "topSales";
    //热销商品详情
    public static final String R_HOT_INFO = R_REPORT_URL + "topSalesView";
    //库存统计
//    public static final String R_REPORT_STOCK = R_REPORT_URL + "numTotal";
    public static final String R_REPORT_STOCK = "http://fuzhuang.poso2o.com/boss.htm?Act=numList";
    //库存统计详情
    public static final String R_STOCK_DETAIL = "http://fuzhuang.poso2o.com/boss.htm?Act=numTotalView";
    //人员业绩
    public static final String R_REPORT_ACHIEVE = R_REPORT_URL + "topCzy";
    //人员业绩详情
    public static final String R_ACHIEVE_DETAIL = R_REPORT_URL + "topCzyView";
    //月损益表
    public static final String R_REPORT_PROFIT = R_REPORT_URL + "topAccountMonthsView";
    //月损益详情
    public static final String R_REPORT_PROFIT_INFO ="http://fuzhuang.poso2o.com/report.htm?Act=getMonthCost";
    //月损益设置
    public static final String R_REPORT_PROFIT_SET = "http://fuzhuang.poso2o.com/report.htm?Act=addAccountMonths";

    //交接明细
    public static final String R_REPORT_TRANSFER = "http://fuzhuang.poso2o.com/JiaoJie.htm?Act=query";
    //交接列表详情
    public static final String R_REPORT_TRANSFER_INFO = "http://fuzhuang.poso2o.com/JiaoJie.htm?Act=info";

    //设置员工月任务额
    public static final String R_REPORT_SET_TARGET = "http://fuzhuang.poso2o.com/czy.htm?Act=setAssignments";
    //批量设置员工月任务额
    public static final String R_REPORT_BATCH_SET_TARGET = "http://fuzhuang.poso2o.com/czy.htm?Act=batchSetAssignments";
//    public static final String R_REPORT_BATCH_SET_TARGET = "http://192.168.10.153:8080/czy.htm?Act=batchSetAssignments";
}
