package com.poso2o.lechuan.http.realshop;

/**
 * Created by mr zhang on 2017/12/2.
 */

public class RMemberHttpAPI {

    //会员使用旧版服装版接口
    public static final String R_MAMBER_URL = "http://fuzhuang.poso2o.com/boss.htm?Act=";
    //实体店订单
    public static final String R_ORDER_URL = "http://fuzhuang.poso2o.com/order.htm?Act=";
    //查看所有目录接口
    public static final String R_ORDER_QUERY_URL = "http://fuzhuang.poso2o.com/directory.htm?Act=";

    //实体店订单详情
    public static final String O_ORDER_DETAIL_INFO = R_ORDER_URL + "view";
    //会员列表
    public static final String R_MEMBER_LIST = R_MAMBER_URL + "topClient";
    //会员详情
    public static final String R_MEMBER_INFO = R_MAMBER_URL + "topClientView";
    //实体店 我的订单信息
    public static final String O_REMBER_INFO = R_MAMBER_URL + "nowSales";
    //主界面销售额 目标率 完成度
    public static final String O_REMBER_MAIN_INFO = R_MAMBER_URL + "index";
    //库存统计
    public static final String O_REMBER_PAPER_INFO = R_MAMBER_URL + "numList";
    //库存详情
    public static final String O_REQUEST_PAPER_DETAIL = R_MAMBER_URL + "numTotalView";
    //畅销商品
    public static final String O_REMBER_SELL_INFO = R_MAMBER_URL + "topSales";
    //畅销商品详情
    public static final String O_REMBER_SELL_DETAIL_INFO = R_MAMBER_URL + "topSalesView";
    //查看所有目录列表
    public static final String O_REMBER_SELL_QUERY_INFO = R_ORDER_QUERY_URL + "list";
    //月损益表
    public static final String O_REMBER_MOTHS_INFO = R_MAMBER_URL + "topAccountMonths&sales=1";
    //月损益表详情
    public static final String O_REQUEST_MOTHS_DETAIL_INFO = R_MAMBER_URL + "topAccountMonthsView";
    //人员业绩
    public static final String O_POPLE_STAFF_INFO = R_MAMBER_URL + "topCzy";
    //人员业绩详情
    public static final String O_POPLE_STAFF_DETAIL_INFO = R_MAMBER_URL + "topCzyView";


}
