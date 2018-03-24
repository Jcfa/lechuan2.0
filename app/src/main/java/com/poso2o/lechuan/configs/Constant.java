package com.poso2o.lechuan.configs;

/**
 * 常量管理
 * Created by Jaydon on 2017/8/11.
 */
public interface Constant {

    /**
     * 开发模式
     */
    boolean DEV_MODE = true;

    /**
     * 序列化数据的tag
     */
    int ADD = 1;

    int UPDATE = 2;

    String DATA = "data";

    String NAME = "name";

    String CATALOG = "catalog";

    String GOODS = "goods";

    String SHOP = "shop";

    String TYPE = "type";

    int SELF_ARTICLE = 99;

    /**
     * 添加商品是否缓存
     */
    String ADD_GOODS_SAVE = "add_goods_save";

    /**
     * 记录商品类型
     */
    String GOODS_TYPE = "goods_type";

    /**
     * 帐号类型
     */
    int MERCHANT_TYPE = 0;//商家
    int DISTRIBUTION_TYPE = 1;//分销商
    int COMMON_TYPE = 2;//普通用户
//    int MULTIPLE_TYPE = 3;//商家+分销商
//    int MERCHANT_TYPE = 0;


    // TODO 广播相关 =======================================================================

    /**
     * 微信充值广播
     */
    String BROADCAST_WEIXIN_TOP_UP = "poso2o.lechuan.broadcast.weixin.top.up";

    /**
     * 收藏广播
     */
    String BROADCAST_COLLECT = "poso2o.lechuan.broadcast.article.collect";

    /**
     * 取消收藏广播
     */
    String BROADCAST_UNCOLLECT = "poso2o.lechuan.broadcast.article.uncollect";

    /**
     * 选择文章变更广播
     */
    String BROADCAST_SELECT_ARTICLE = "poso2o.lechuan.broadcast.article.select.article";
    String LECHUAN_ROOT_DIR = "lechuan/qrcode";
}
