package com.poso2o.lechuan.http.realshop;

/**
 * Created by mr zhang on 2017/12/9.
 */

public class RealSizeAPI {

    private static final String REAL_SIZE = "http://fuzhuang.poso2o.com/ProductService.htm?Act=";

    // 尺码列表
    public static final String LIST = REAL_SIZE + "sizeIdList";
    // 添加尺码
    public static final String ADD = REAL_SIZE + "sizeIdAdd";
    // 删除尺码
    public static final String DEL = REAL_SIZE + "sizeIdDel";
}
