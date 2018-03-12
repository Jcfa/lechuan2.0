package com.poso2o.lechuan.http.vdian;

import static com.poso2o.lechuan.http.HttpAPI.SERVER_MAIN_API;

/**
 * Created by mr zhang on 2017/12/9.
 */

public class VdianCatalogAPI {

    // 目录地址
    private static final String URL = SERVER_MAIN_API + "GoodsCatalogManage.htm?Act=";

    // 目录列表
    public static final String LIST = URL + "list";
    // 增加目录
    public static final String ADD = URL + "add";
    // 删除目录
    public static final String DEL = URL + "del";
    // 修改目录
    public static final String EDIT = URL + "edit";
    // 目录排序
    public static final String SORT = URL + "setCatalogSort";
    // 移动商品
    public static final String MOVE = VdianGoodsAPI.URL + "batchEditCatalog";
}
