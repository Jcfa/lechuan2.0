package com.poso2o.lechuan.http.realshop;

/**
 * Created by mr zhang on 2017/12/9.
 */

public class RealColorAPI {

    private static final String REAL_COLOR = "http://fuzhuang.poso2o.com/ProductService.htm?Act=";

    // 实体店商品颜色
    public static final String LIST = REAL_COLOR + "colorIdList";
    // 添加颜色
    public static final String ADD = REAL_COLOR + "colorIdAdd";
    // 删除颜色
    public static final String DEL = REAL_COLOR + "colorIdDel";
}
