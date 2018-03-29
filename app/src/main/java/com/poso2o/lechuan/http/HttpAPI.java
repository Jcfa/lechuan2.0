package com.poso2o.lechuan.http;

/**
 * Created by Administrator on 2017-11-24.
 */

public class HttpAPI {
    //http://fuzhuang.poso2o.com/user.htm?Act=login&loginname=13016079579&password=123456
    public static String SERVER_MAIN_API = "http://wechat.poso2o.com/";
    //    public static String LC_SERVER_MAIN_API = "http://wechat.poso2o.com/";
    public static String FZ_SERVER_MAIN_API = "http://fuzhuang.poso2o.com/";
//public static String SERVER_MAIN_API = "http://192.168.10.153:8080/";

    /**
     * 实体店商品相关URL
     */
    public static String REAL_API = "http://fuzhuang.poso2o.com/";

    /**
     * 实体店商品相关URL
     */
    public static String TEST_API = "http://192.168.10.153:8080/";

    /**
     * 版本更新url
     */
    public static String VERSION_API = "http://www.poso2o.com/android/lechuan_version.js";
    /**
     * 登录
     */
    // http://fuzhuang.poso2o.com/user.htm?Act=login&loginname=13016079579&password=123456
    public static String LOGIN_API = SERVER_MAIN_API + "UserManage.htm?Act=login";
    public static String FZ_LOGIN_API = FZ_SERVER_MAIN_API + "user.htm?Act=login";
    public static String FZ_REGISTER_API = FZ_SERVER_MAIN_API + "UserManage.htm?Act=regShop";
//    public static String LOGIN_API = SERVER_MAIN_API + "user.htm?Act=login";

    /**
     * 注册
     */
    public static String REGISTER_API = SERVER_MAIN_API + "UserManage.htm?Act=reg";
    public static String REGISTER_SHOP_API = SERVER_MAIN_API + "UserManage.htm?Act=regShop";

    /**
     * 重置密码
     */
    public static String RESET_PASSWORD_API = SERVER_MAIN_API + "UserManage.htm?Act=forgetPW";
    /**
     * 修改密码
     */
    public static String UPDATE_PASSWORD_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=editToken";
    /**
     * 验证码
     */
    public static String VERIFICATION_CODE_API = SERVER_MAIN_API + "SMSManage.htm";

    /**
     * 微信登录
     */
    public static String WEIXIN_LOGIN_API = SERVER_MAIN_API + "UserManage.htm?Act=openidLogin";

    /**
     * 微信绑定
     */
    public static String WEIXIN_BIND_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=wexinBinding";

    /**
     * 地址
     */
    public static String ADDRESS_API = SERVER_MAIN_API + "AddressManage.htm?Act=address";

    // TODO 交易相关 ===================================================================

    /**
     * 微信充值支付
     */
    public static String WEIXIN_PAY_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=wxBuyAmount";
    /**
     * 微信购买服务支付
     */
    public static String WEIXIN_SERVICE_PAY_API = SERVER_MAIN_API + "BuyServiceManage.htm?Act=wxBuy";
    /**
     * 微信提现
     */
    public static String WEIXIN_WITHDRAWAL_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=withdrawals";
    /**
     * 支付宝充值支付
     */
    public static String ALIPAY_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=aliBuyAmount";
    /**
     * 支付宝购买服务支付
     */
    public static String ALIPAY_SERVICE_API = SERVER_MAIN_API + "BuyServiceManage.htm?Act=aliBuy";
    /**
     * 支付宝支付
     */
    public static String AMOUNT_HISTORY_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=myAmountHistory";

    // TODO 发布相关 ===================================================================

    /**
     * 咨询发布
     */
    public static String NEWS_ADD_API = SERVER_MAIN_API + "NewsManage.htm?Act=add";

    /**
     * 文章
     */
    public static String ARTICLES_LIST_API = SERVER_MAIN_API + "ArticlesManage.htm?Act=query";

    /**
     * 文章类型、标签
     */
    public static String ARTICLE_TYPE_API = SERVER_MAIN_API + "ArticlesManage.htm?Act=typesAndLabels";

    /**
     * 收藏列表
     */
    public static String ARTICLES_COLLECT_LIST_API = SERVER_MAIN_API + "ArticlesManage.htm?Act=myCollect";

    /**
     * 收藏
     */
    public static String ARTICLES_COLLECT_API = SERVER_MAIN_API + "ArticlesManage.htm?Act=collect";

    /**
     * 取消收藏
     */
    public static String ARTICLES_UNCOLLECT_API = SERVER_MAIN_API + "ArticlesManage.htm?Act=unCollect";

    /**
     * 根据商品id获取商品推广图片
     */
    public static final String OA_GOODS_PIC_URL = SERVER_MAIN_API + "ImgManage.htm?Act=uploadWeixiGoodsImage";

    /**
     * 发布文章
     */
    public static final String OA_SEND_ARTICLE_URL = SERVER_MAIN_API + "WxNewsManage.htm?Act=sendArticles";

    /**
     * 分销商店铺列表
     */
    public static String DIST_SHOP_LIST_API = SERVER_MAIN_API + "distributorManage.htm?Act=distributorShops";


    // TODO ========================================== START 广告 ==========================================

    /**
     * 我转发的广告
     */
    public static String MY_FORWARD_LIST_API = SERVER_MAIN_API + "NewsManage.htm?Act=myShareNews";

    /**
     * 获取广告 - 资讯列表+分页查询
     */
    public static String POSTER_API = SERVER_MAIN_API + "NewsManage.htm?Act=query";
    /**
     * 获取广告 - 广告资讯详情
     */
    public static String POSTER_INFO_API = SERVER_MAIN_API + "NewsManage.htm?Act=info";
    /**
     * 获取广告 - 点赞广告资讯
     */
    public static String POSTER_LIKE_API = SERVER_MAIN_API + "NewsManage.htm?Act=like";
    /**
     * 获取广告 - 取消点赞广告资讯
     */
    public static String POSTER_UNLIKE_API = SERVER_MAIN_API + "NewsManage.htm?Act=unLike";
    /**
     * 获取广告 - 添加收藏广告资讯
     */
    public static String POSTER_COLLECT_API = SERVER_MAIN_API + "NewsManage.htm?Act=collect";
    /**
     * 获取广告 - 取消收藏广告资讯
     */
    public static String POSTER_UNCOLLECT_API = SERVER_MAIN_API + "NewsManage.htm?Act=unCollect";
    /**
     * 获取广告 - 记录转发广告资讯
     */
    public static String POSTER_RECORD_SHARE_API = SERVER_MAIN_API + "NewsManage.htm?Act=share";
    /**
     * 广告评论 - 获取广告评论列表
     */
    public static String POSTER_COMMENTS_API = SERVER_MAIN_API + "NewsCommentsManage.htm?Act=query";
    /**
     * 广告评论 - 添加广告评论
     */
    public static String POSTER_ADD_COMMENTS_API = SERVER_MAIN_API + "NewsCommentsManage.htm?Act=add";
    /**
     * 关注
     */
    public static String POSTER_FLOW_API = SERVER_MAIN_API + "UserFlowManage.htm?Act=add";
    public static String POSTER_UNFLOW_API = SERVER_MAIN_API + "UserFlowManage.htm?Act=del";
    /**
     * 广告红包 - 获取广告红包列表
     */
    public static String POSTER_RED_BAG_API = SERVER_MAIN_API + "NewsRedEnvelopesManage.htm?Act=list";
    /**
     * 广告红包 - 添加红包
     */
    public static String POSTER_ADD_RED_BAG_API = SERVER_MAIN_API + "NewsRedEnvelopesManage.htm?Act=add";
    /**
     * 广告佣金 - 获取广告佣金列表
     */
    public static String POSTER_COMMISSION_API = SERVER_MAIN_API + "distributorAccountManage.htm?Act=newsCommissionOrders";
    /**
     * 广告我的发布 - 分页查询
     */
    public static String POSTER_MY_SEND_NEWS_API = SERVER_MAIN_API + "NewsManage.htm?Act=mySendNews";
    /**
     * 广告我的收藏 - 分页查询
     */
    public static String POSTER_COLLECT_NEWS_API = SERVER_MAIN_API + "NewsManage.htm?Act=myCollectNews";
    /**
     * 广告我的关注 - 分页查询
     */
    public static String POSTER_MY_FLOWS_API = SERVER_MAIN_API + "UserFlowManage.htm?Act=myFlows";
    /**
     * 广告我的粉丝 - 分页查询
     */
    public static String POSTER_MY_FANS_API = SERVER_MAIN_API + "UserFlowManage.htm?Act=myFans";
    /**
     * 广告我的发布 - 删除
     */
    public static String POSTER_DEL_MY_SEND_NEWS_API = SERVER_MAIN_API + "NewsManage.htm?Act=del";

    /**
     * 接受分销邀请
     */
    public static String POSTER_ACCEPT_INVITED_API = SERVER_MAIN_API + "distributorManage.htm?Act=binding";
    /**
     * 广告我的粉丝 - 发送分销邀请
     */
    public static String POSTER_SEND_INVITED_API = SERVER_MAIN_API + "distributorManage.htm?Act=pushShare";
    /**
     * 广告公开发布 - 分页查询
     */
    public static String POSTER_OPEN_PUBLISH_API = SERVER_MAIN_API + "OpenInformationManage.htm?Act=openNews";
    /**
     * 广告公开关注 - 分页查询
     */
    public static String POSTER_OPEN_FLOWS_API = SERVER_MAIN_API + "OpenInformationManage.htm?Act=openFlows";
    /**
     * 广告公开粉丝 - 分页查询
     */
    public static String POSTER_OPEN_FANS_API = SERVER_MAIN_API + "OpenInformationManage.htm?Act=openFans";
    /**
     * 广告店铺评论 - 分页查询
     */
    public static String POSTER_SHOP_COMMENT_API = SERVER_MAIN_API + "OpenInformationManage.htm?Act=openGoodsAppraise";
    /**
     * 广告店铺评论 - 购买评论列表 分页查询
     */
    public static String POSTER_SHOP_DETAILS_COMMENT_API = SERVER_MAIN_API + "AppraiseCommentsManage.htm?Act=query";
    /**
     * 广告店铺评论 - 添加购买评论列表
     */
    public static String POSTER_ADD_SHOP_DETAILS_COMMENTS_API = SERVER_MAIN_API + "AppraiseCommentsManage.htm?Act=add";


    // TODO ========================================== END 广告 ==========================================


    // TODO ========================================== START 订单 ==========================================

    /**
     * 订单 - 当天订单销售统计
     */
    public static String ORDER_TODAY_ORDER_SALE_REPORT_API = SERVER_MAIN_API + "OrderManage.htm?Act=todayOrderSaleReport";

    /**
     * 订单 - 订单查找+分页
     */
    public static String ORDER_QUERY_API = SERVER_MAIN_API + "OrderManage.htm?Act=query";

    /**
     * 订单 - 卖家【查看订单详情】
     */
    public static String ORDER_INFO_API = SERVER_MAIN_API + "OrderManage.htm?Act=info";

    /**
     * 订单 - 卖家【作废订单】
     */
    public static String ORDER_DEL_API = SERVER_MAIN_API + "OrderManage.htm?Act=del";

    /**
     * 订单 - 卖家【发货确认】
     */
    public static String ORDER_SHIPPING_CONFRIM_API = SERVER_MAIN_API + "OrderManage.htm?Act=shippingConfrim";

    /**
     * 订单 - 卖家【拒绝退款】
     */
    public static String ORDER_REFUND_FAIL_API = SERVER_MAIN_API + "OrderManage.htm?Act=refundFail";

    /**
     * 订单 - 卖家【同意退款】
     */
    public static String ORDER_REFUND_SUCCESS_API = SERVER_MAIN_API + "OrderManage.htm?Act=refundSuccess";

    /**
     * 订单 - 按时间统计订单个数
     */
    public static String ORDER_TOTAL_ORDER_COUNT_API = SERVER_MAIN_API + "OrderManage.htm?Act=totalOrderCount";

    /**
     * 订单 - 卖家 购买评论+分页
     */
    public static String ORDER_GOODS_APPRAISE_API = SERVER_MAIN_API + "GoodsAppraiseManage.htm?Act=goodsAppraise";

    /**
     * 修改订单备注信息
     */
    public static String ORDER_EDIT_REMARK_API = SERVER_MAIN_API + "OrderManage.htm?Act=editOrderRemark";

    /**
     * 修改物流信息
     */
    public static String ORDER_EDIT_EXPRESS_API = SERVER_MAIN_API + "OrderManage.htm?Act=editExpress";

    /**
     * 修改收货信息
     */
    public static String ORDER_EDIT_ADDRESS_API = SERVER_MAIN_API + "OrderManage.htm?Act=editReceiptAddress";

    /**
     * 订单物流信息
     */
    public static String ORDER_EXPRESS_LOGISTICS_API = SERVER_MAIN_API + "OrderManage.htm?Act=express_logistics";

    /**
     * 修改订单商品价格
     */
    public static String ORDER_EDIT_GOODS_PRICE_API = SERVER_MAIN_API + "OrderManage.htm?Act=editGoodsDiscountPrice";

    /**
     * 一键改价
     */
    public static String ORDER_EDIT_ORDER_AMOUNT_API = SERVER_MAIN_API + "OrderManage.htm?Act=editOrderDiscountAmount";

    /**
     * 修改订单运费
     */
    public static String ORDER_EDIT_FREIGHT_API = SERVER_MAIN_API + "OrderManage.htm?Act=editFreight";

    // TODO ========================================== END   订单 ==========================================

    // TODO ========================================== START   系统-地址&快递公司信息 ==========================================

    /**
     * 系统 - 地址信息
     */
    public static String SYSTEM_ADDRESS_API = SERVER_MAIN_API + "AddressManage.htm?";

    /**
     * 系统 - 物流公司
     */
    public static String SYSTEM_EXPRESS_API = SERVER_MAIN_API + "ExpressManage.htm?";

    // TODO ========================================== START   系统-地址&快递公司信息 ==========================================

    /**
     * 我的-未结佣金-分销商列表
     */
    public static String MY_DISTRIBUTION_LIST_API = SERVER_MAIN_API + "distributorManage.htm?Act=query";
    /**
     * 我的-未结佣金-商家列表
     */
    public static String MY_MERCHANT_LIST_API = SERVER_MAIN_API + "distributorManage.htm?Act=distributorShops";//"distributorAccountManage.htm?Act=distributorCommission";
    /**
     * 我的-红包余额-红包收支
     */
    public static String MY_REDPACKET_LIST_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=myRedEnvelopes";
    /**
     * 按商家查看佣金详情记录
     */
    public static String MY_MERCHANT_BROKERAGE_LIST_API = SERVER_MAIN_API + "distributorAccountManage.htm?Act=shopCommissionOrders";
    /**
     * 按分销商查看佣金详情记录
     */
    public static String MY_DISTRIBUTION_BROKERAGE_LIST_API = SERVER_MAIN_API + "distributorAccountManage.htm?Act=distributorCommissionOrders";
    /**
     * 按文章查看佣金详情记录
     */
    public static String MY_ESSAY_BROKERAGE_LIST_API = SERVER_MAIN_API + "distributorAccountManage.htm?Act=newsCommissionOrders";
    /**
     * 按文章查看红包详情记录
     */
    public static String MY_ESSAY_RED_PACKET_LIST_API = SERVER_MAIN_API + "NewsRedEnvelopesManage.htm?Act=list";
    /**
     * 修改手机号
     */
    public static String MY_EDIT_PHONE_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=editMobile";
    /**
     * 修改昵称
     */
    public static String MY_EDIT_NICK_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=editNick";
    /**
     * 修改头像
     */
    public static String MY_EDIT_LOGO_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=editLogo";
    /**
     * 设置封面背景图
     */
    public static String MY_EDIT_BACKGROUND_LOGO_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=editBackgroundLogo";
    /**
     * 修改简介
     */
    public static String MY_EDIT_DESCRIPTION_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=editRemark";
    /**
     * 充值，收入，支出明细
     */
    public static String MY_RED_PACKET_RECORD_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=myAmountHistory";
    /**
     * 充值，收入，支出金额统计
     */
    public static String MY_AMOUNT_TOTAL_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=AmountTotal";
    /**
     * 获取用户信息
     */
    public static String MY_GET_USER_INFO_API = SERVER_MAIN_API + "UserAccountManage.htm?Act=info";
    /**
     * 佣金结算
     */
    public static String MY_COMMISSION_PAYMENT_API = SERVER_MAIN_API + "distributorAccountManage.htm?Act=commissionPayment";
    /**
     * 解除分销
     */
    public static String MY_RELIEVE_DISTRIBUTION_API = SERVER_MAIN_API + "distributorManage.htm?Act=unBinding";
    /**
     * 邀请分销 ,分享的的html地址
     */
    public static String MY_INVITATION_DISTRIBUTION_API = SERVER_MAIN_API + "distributorManage.htm?Act=share";
    /**
     * 单品佣金设置
     */
    public static String MY_GOODS_COMMISSION_SET_API = SERVER_MAIN_API + "distributorManage.htm?Act=updateGoodsCommissionRate";
    /**
     * 通用佣金设置
     */
    public static String MY_COMMISSION_SET_API = SERVER_MAIN_API + "distributorManage.htm?Act=SaveCommissionRate";

    /**
     * 我已购买的模版
     */
    public static String OA_FREE_EDIT_MY_TEMPLATE_API = SERVER_MAIN_API + "TemplateAdManage.htm?Act=myGroups";

    /**
     * 设置默认模板
     */
    public static String OA_SET_DEFAULT_MODEL = SERVER_MAIN_API + "TemplateAdManage.htm?Act=setMyAD";

}
