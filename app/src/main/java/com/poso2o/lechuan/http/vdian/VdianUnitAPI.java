package com.poso2o.lechuan.http.vdian;

import static com.poso2o.lechuan.http.HttpAPI.SERVER_MAIN_API;

/**
 * Created by Jaydon on 2017/12/15.
 */
public class VdianUnitAPI {

    // 单位地址
    private static final String URL = SERVER_MAIN_API + "GoodsUnitTemplateManage.htm?Act=";

    // 单位列表
    public static final String LIST = URL + "list";
    // 增加单位
    public static final String ADD = URL + "add";
    // 删除单位
    public static final String DEL = URL + "del";
    // 修改单位
    public static final String EDIT = URL + "edit";
}
