package com.poso2o.lechuan.http.vdian;

import static com.poso2o.lechuan.http.HttpAPI.SERVER_MAIN_API;

/**
 * Created by Jaydon on 2017/12/15.
 */
public class VdianSupplierAPI {

    // 规格地址
    private static final String URL = SERVER_MAIN_API + "supplierManage.htm?Act=";

    // 规格列表
    public static final String LIST = URL + "list";
    // 增加规格
    public static final String ADD = URL + "add";
    // 删除规格
    public static final String DEL = URL + "del";
    // 修改规格
    public static final String EDIT = URL + "edit";
    // 加载银行
    public static final String BANK = SERVER_MAIN_API + "BankManage.htm?Act=list";
}
