package com.poso2o.lechuan.http.vdian;

import static com.poso2o.lechuan.http.HttpAPI.SERVER_MAIN_API;

/**
 * Created by Jaydon on 2017/12/15.
 */
public class VdianSizeAPI {

    // 尺码地址
    private static final String URL = SERVER_MAIN_API + "GoodsSizeTemplateManage.htm?Act=";

    // 尺码列表
    public static final String LIST = URL + "list";
    // 增加尺码
    public static final String ADD = URL + "add";
    // 删除尺码
    public static final String DEL = URL + "del";
    // 修改尺码
    public static final String EDIT = URL + "edit";
}
