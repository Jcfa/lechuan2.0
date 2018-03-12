package com.poso2o.lechuan.http.realshop;

import com.poso2o.lechuan.http.HttpAPI;

/**
 * Created by mr zhang on 2017/12/6.
 */

public class CompanyAuthorityAPI {

    //企业认证相关接口
    public static final String COMPANY_AUTHORITY_MAIN = HttpAPI.SERVER_MAIN_API + "AuthenticationManage.htm?Act=";

    //提交资料
    public static final String COMPANY_AUTHORITY_COMMIT = COMPANY_AUTHORITY_MAIN + "save";
//    public static final String COMPANY_AUTHORITY_COMMIT = "http://192.168.10.153:8080/AuthenticationManage.htm?Act=save";
    //查看资质认证信息
    public static final String COMPANY_AUTHORITY_INFO = COMPANY_AUTHORITY_MAIN + "info";
}
