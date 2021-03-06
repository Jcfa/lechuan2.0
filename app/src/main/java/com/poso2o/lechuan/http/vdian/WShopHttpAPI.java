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
    public static final String W_SHOP_EDIT_URL = W_MAIN_API + "ShopManage.htm?Act=save";

    //微店导入商品,上架
    public static final String W_SHOP_IMPORT_GOODS_ONLINE = W_MAIN_API + "GoodsManage.htm?Act=batchOnLine";
    //下架
    public static final String W_SHOP_IMPORT_GOODS_OFFLINE = W_MAIN_API + "GoodsManage.htm?Act=batchOffLine";

    //微店公众号绑定信息(微店使用)
    public static final String W_BINGING_STATE = W_MAIN_API + "AuthorizerTokenManage.htm?Act=getAuthorizerServiceInfo";
    //微店公众号绑定信息(公众号助手使用)
    public static final String G_BINGING_STATE = W_MAIN_API + "AuthorizerTokenManage.htm?Act=getAuthorizerNewsInfo";

    //提交或修改绑定的微信支付相关id
    public static final String W_COMMIT_BIND_PAY = W_MAIN_API + "AuthorizerTokenManage.htm?Act=setWxPay";

    //绑定微信支付详情接口
    public static final String W_BIND_PAY_INFO = W_MAIN_API + "AuthorizerTokenManage.htm?Act=getAuthorizerServiceInfo";

    // 绑定收款帐号
    public static final String SET_BANK_ACCOUNT = W_MAIN_API + "ShopManage.htm?Act=setBankAccount";
    //乐传帐号详情
    public static final String LC_ACCOUNT_DETAIL = W_MAIN_API + "UserAccountManage.htm?Act=info";
}
