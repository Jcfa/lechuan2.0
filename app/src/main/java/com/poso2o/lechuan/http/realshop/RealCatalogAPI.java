package com.poso2o.lechuan.http.realshop;

import com.poso2o.lechuan.http.HttpAPI;

/**
 * Created by mr zhang on 2017/12/9.
 */

public class RealCatalogAPI {

    // 旧版服装版商品目录
    private static final String REAL_CATALOG = HttpAPI.REAL_API + "ProductService.htm?Act=";

    // 目录列表
    public static final String LIST = REAL_CATALOG + "directoryList";
    // 添加目录
    public static final String ADD = REAL_CATALOG + "directoryAdd";
    // 删除目录
    public static final String DEL = REAL_CATALOG + "directoryDel";
    // 编辑目录
    public static final String EDIT = REAL_CATALOG + "directoryEdit";
    // 目录排序
    public static final String SORT = REAL_CATALOG + "directorySetNo";
    // 移动商品
    public static final String MOVE = REAL_CATALOG + "batchSetFid";
}
