package com.poso2o.lechuan.bean.order;

import android.app.Activity;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 订单管理
 *
 * Created by Luo on 2017-12-11
 */
public class OrderDTO implements Serializable {

	/** 店铺ID */
	public long shop_id=0L;

	/** 分店id */
	public long shop_branch_id=0L;

	/** 订单id */
	public long order_id= 0;

	/** 微信ID */
	public String openid="";

	/** 会员id */
	public long member_id=0L;

	/** 会员名称 */
	public String member_name="";

	/** 会员使用的积分 */
	public int member_use_integral=0;

	/** 会员使用积分的消费金额 */
	public double member_use_integral_amount= 0;

	/** 会员增加的积分 */
	public int member_increase_integral=0;

	/** 订单状态 1 待付款   2.已付款   3.已发货   4.已取消（申请退款  / 拒绝退款  / 已退款) 7.投诉中   8.已完成  9.已关闭*/
	public int order_state=1;

	/**退款状态:0=没有退款，1.申请退款  2.拒绝退款  3.已退款*/
	public int order_refund_state=0;

	/** 订单金额 */
	public double order_amount= 0;

	/** 订单折扣 */
	public double order_discount= 0;

	/** 订单总商品数量 */
	public int order_total_goods_munber=0;

	/** 优惠卷抵消金额 */
	public double coupon_offset_amount= 0;

	/** 优惠卷信息 */
	public ArrayList<OrderCouponDTO> order_coupon;

	/** 运费 */
	public double freight= 0;

	/** 应付金额 */
	public double order_payable_amount= 0;

	/** 实付金额 */
	public double order_paid_amount= 0;

	/** 总成本 */
	public double order_total_cost= 0;

	/** 1:现金    2：支付宝    3：微信     4：余额     5：刷卡 */
	public int payment_method_type=0;

	/** 支付方式名称 */
	public String payment_method_name="";

	/** 微信支付单号 */
	public String payment_wechat_order_id="";

	/** 付款时间 */
	public long payment_time= 0;

	/** 订单商品信息 */
	public ArrayList<OrderGoodsDTO> order_goods;

	/** 订单备注 */
	public String order_remark="";

	/** 买家留言 */
	public String member_remark="";

	/** 收货姓名 */
	public String receipt_name="";

	/** 收货地址-省份名称 */
	public String receipt_province_name="";

	/** 收货地址-市名称 */
	public String receipt_city_name="";

	/** 收货地址-区名称 */
	public String receipt_area_name="";

	/** 收货地址-地址 */
	public String receipt_address="";

	/** 联系手机 */
	public String receipt_mobile="";

	/** 联系电话 */
	public String receipt_tel="";

	/** 联系邮政编号 */
	public String receipt_zipcode="";

	/** 是否需要发物流  1=需要,0=不需要 */
	public int has_express=0;

	/** 快递结算方式ID */
	public int express_payment_type=0;

	/** 快递结算方式名称 */
	public String express_payment_name="";

	/** 快递公司ID */
	public String express_company_id="";

	/** 快递公司 */
	public String express_company="";

	/** 快递单号 */
	public String express_order_id="";

	/** 物流信息 */
	//public JSONArray express_logistics;
	public ArrayList<ExpressLogisticsDTO> express_logistics;

	/** 发货时间 */
	public long shipping_time= 0;

	/** 完成日期 */
	public long finish_time= 0;

	/** 创建时间 */
	public long build_time= 0;

	/** 收银员ID */
	public String build_czy="";

	/** 收银员名称 */
	public String build_czy_name="";

	/** 申请退款 */
	public OrderRefundDTO orderRefund;

	/** 是否评价：0=未评价，1=已评价 */
	public int has_appraise=0;

	/** 买家投诉内容 */
	public String complaint_remark="";

	/** 买家投诉日期 */
	public long complaint_time= 0;


	/**
	 * ========================本地数据处理
	 */

	/** 是否选中 */
	public boolean isChecked = true;

}
