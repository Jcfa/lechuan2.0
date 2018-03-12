package com.poso2o.lechuan.http.realshop;

import com.poso2o.lechuan.http.HttpAPI;

/**
 * Created by mr zhang on 2017/12/9.
 */

public class RealGoodsAPI {

    // 旧版服装版商品
    private static final String URL = HttpAPI.REAL_API + "ProductService.htm?Act=";

    // 商品列表
    public static final String LIST = URL + "query";
    // 添加商品
    public static final String ADD = URL + "add2";
    // 删除商品
    public static final String BATCH_DEL = URL + "batchDelete";
    // 编辑商品
    public static final String EDIT = URL + "edit";
}
