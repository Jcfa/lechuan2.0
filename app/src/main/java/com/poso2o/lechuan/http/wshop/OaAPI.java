package com.poso2o.lechuan.http.wshop;

/**
 * Created by mr zhang on 2018/2/5.
 */

public class OaAPI {
    //域名
    private static final String V_MAIN_URL = "http://wechat.poso2o.com/";

    //模板组列表
    public static final String MODEL_GROUPS_URL = V_MAIN_URL + "TemplateAdManage.htm?Act=groups";

    //组内模板列表
    public static final String MODEL_GROUP_LIST_URL = V_MAIN_URL + "TemplateAdManage.htm?Act=list";

    //单个模板详情
    public static final String MODEL_GROUP_INFO_URL = V_MAIN_URL + "TemplateAdManage.htm?Act=info";

    //已购买的模板组列表
    public static final String MODEL_GROUP_BOUGHT_URL = V_MAIN_URL + "TemplateAdManage.htm?Act=myGroups";

    //公众号系统服务列表
    public static final String OA_SERVICE_URL = V_MAIN_URL + "SystemServiceManage.htm?Act=list";

    //公众号服务微信支付
    public static final String OA_SERVICE_WECHAT_URL =  V_MAIN_URL + "BuyServiceManage.htm?Act=wxBuy";

    //公众号服务支付宝支付
    public static final String OA_SERVICE_ALI_URL = V_MAIN_URL + "BuyServiceManage.htm?Act=aliBuy";

    //公众号服务信息
    public static final String OA_SERVICE_INFO_URL = V_MAIN_URL +  "AdOperationModeManage.htm?Act=getOperationMode";
}
