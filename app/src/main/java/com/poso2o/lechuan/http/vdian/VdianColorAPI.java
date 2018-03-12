package com.poso2o.lechuan.http.vdian;

import static com.poso2o.lechuan.http.HttpAPI.SERVER_MAIN_API;

/**
 * Created by Jaydon on 2017/12/15.
 */
public class VdianColorAPI {

    // 颜色地址
    private static final String URL = SERVER_MAIN_API + "GoodsColourTemplateManage.htm?Act=";

    // 颜色列表
    public static final String LIST = URL + "list";
    // 增加颜色
    public static final String ADD = URL + "add";
    // 删除颜色
    public static final String DEL = URL + "del";
    // 修改颜色
    public static final String EDIT = URL + "edit";
}
