package com.poso2o.lechuan.http.vdian;

/**
 * Created by mr zhang on 2017/12/1.
 */

public class VdianGoodsAPI {

    public static final String URL = WShopHttpAPI.W_MAIN_API + "GoodsManage.htm?Act=";

    public static final String LIST = URL + "list";// 商品列表

    public static final String INFO = URL + "info";// 商品详情

    public static final String ADD = URL + "add";// 添加商品

    public static final String DEL = URL + "del";// 删除商品

    public static final String BATCH_DEL = URL + "batchDel";// 批量删除商品

    public static final String QUERY = URL + "query";// 商品条件查询

    public static final String EDIT = URL + "edit";// 编辑商品

//    public static final String VDIAN_BATCH_UP = "http://192.168.10.153:8080/GoodsManage.htm?Act=batchOnSale"; //批量上传商品
    public static final String BATCH_UP = URL + "batchOnSale"; //批量上传商品
}