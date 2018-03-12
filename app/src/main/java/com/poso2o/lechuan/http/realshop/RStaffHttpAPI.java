package com.poso2o.lechuan.http.realshop;

/**
 * Created by mr zhang on 2017/12/2.
 */

public class RStaffHttpAPI {

    //员工地址
    private static final String R_STAFF_URL = "http://fuzhuang.poso2o.com/czy.htm?Act=";

    //员工列表
    public static final String R_STAFF_LIST = R_STAFF_URL + "list";
    //增加员工
    public static final String R_STAFF_ADD = R_STAFF_URL + "add";
    //编辑员工
    public static final String R_STAFF_EDIT = R_STAFF_URL + "edit";
    //删除员工
    public static final String R_STAFF_DEL = R_STAFF_URL + "del";



    //权限（职位）地址
    private static final String R_POSITION_URL = "http://fuzhuang.poso2o.com/position.htm?Act=";

    //权限列表
    public static final String R_POSITION_LIST = R_POSITION_URL + "list";
    //增加权限
    public static final String R_POSITION_ADD = R_POSITION_URL + "add";
    //编辑权限
    public static final String R_POSITION_EDIT = R_POSITION_URL + "edit";
    //删除权限
    public static final String R_POSITION_DEL = R_POSITION_URL + "del";

}
