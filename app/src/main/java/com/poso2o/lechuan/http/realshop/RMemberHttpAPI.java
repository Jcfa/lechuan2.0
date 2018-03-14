package com.poso2o.lechuan.http.realshop;

/**
 * Created by mr zhang on 2017/12/2.
 */

public class RMemberHttpAPI {

    //会员使用旧版服装版接口
    public static final String R_MAMBER_URL = "http://fuzhuang.poso2o.com/boss.htm?Act=";

    //会员列表
    public static final String R_MEMBER_LIST = R_MAMBER_URL + "topClient";
    //会员详情
    public static final String R_MEMBER_INFO = R_MAMBER_URL + "topClientView";
    //畅销商品
    public static final String O_REMBER_INFO = R_MAMBER_URL + "nowSales";
    //主界面销售额 目标率 完成度
    public static final String O_REMBER_MAIN_INFO = R_MAMBER_URL + "index";

}
