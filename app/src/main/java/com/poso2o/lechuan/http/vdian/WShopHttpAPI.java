package com.poso2o.lechuan.http.vdian;

/**
 * Created by mr zhang on 2017/12/1.
 */

public class WShopHttpAPI {

    public static final String W_MAIN_API = "http://wechat.poso2o.com/";

    public static final String W_SHOP = "ShopManage.htm?Act=";

    //微店详情
    public static final String W_SHOP_INFO = W_MAIN_API + W_SHOP + "info";

    //微店头像修改
    public static final String W_SHOP_UPDATE_LOGO = W_MAIN_API + W_SHOP + "uploadLogo";

    //微店信息修改
    public static final String W_SHOP_EDIT_URL = "http://wechat.poso2o.com/ShopManage.htm?Act=save";

    //微店导入商品
    public static final String W_SHOP_IMPORT_GOODS = "http://wechat.poso2o.com/GoodsManage.htm?Act=importGoods";

    //微店公众号绑定信息(公众号助手使用)
    public static final String W_BINGING_STATE = "http://wechat.poso2o.com/AuthorizerTokenManage.htm?Act=getAuthorizerSubscribeInfo";

    //提交或修改绑定的微信支付相关id
    public static final String W_COMMIT_BIND_PAY = W_MAIN_API + "AuthorizerTokenManage.htm?Act=setWxPay";

    //绑定微信支付详情接口
    public static final String W_BIND_PAY_INFO = W_MAIN_API + "AuthorizerTokenManage.htm?Act=getAuthorizerServiceInfo";
}
