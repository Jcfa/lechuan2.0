package com.poso2o.lechuan.http.wshop;

import com.poso2o.lechuan.http.HttpAPI;

/**
 * Created by mr zhang on 2018/3/10.
 */

public class OaRenewalsAPI {

    public static final String RENEWALS = "WxNewsManage.htm?Act=";

    //稿件列表
    public static final String RENEWALS_LIST_URL = HttpAPI.SERVER_MAIN_API + RENEWALS + "query";
    //添加稿件
    public static final String RENEWALS_ADD_URL = HttpAPI.SERVER_MAIN_API + RENEWALS + "add";
    //修改稿件
    public static final String RENEWALS_EDIT_URL = HttpAPI.SERVER_MAIN_API + RENEWALS + "edit";
    //删除稿件
    public static final String RENEWALS_DEL_URL = HttpAPI.SERVER_MAIN_API + RENEWALS + "del";
    //稿件详情
    public static final String RENEWALS_INFO_URL = HttpAPI.SERVER_MAIN_API + RENEWALS + "info";
}
