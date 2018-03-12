package com.poso2o.lechuan.bean.order;

import java.io.Serializable;

/**
 * 当天订单销售统计
 *
 * Created by Luo on 2017-12-11.
 *
 */
public class TodayOrderSaleReportDTO implements Serializable {

	/** 当天销售金额 */
	public double total_today_order_amount = 0;
	
	/**
	 * 当天销售订单数
	*/
	public int total_today_order_count = 0;

	/**
	 * 当天客评数
	*/
	public int total_today_appraise = 0;
	
	/**
	 * 待付款--订单合计数 代码1
	*/
	public int total_order_pending_payment= 0; 
	
	/**
	 * 已付款(待发货)--订单合计数 代码2
	*/ 
	public int total_order_paid = 0;
	
	/**
	 * 已发货--订单合计数 代码3
	*/
	public int total_order_shipped = 0;
	
	/**
	 * 已取消(退款中)--订单合计数 代码4
	*/
	public int total_order_cancel = 0;
		
	/**
	 * 投诉中--订单合计数 代码7
	*/
	public int total_order_complaint= 0;
	
	/**
	 * 已完成--订单合计数 代码8
	*/
	public int total_order_finish = 0;

	/**
	 * 已关闭-订单合计数 代码9
	*/
	public int total_order_close = 0;

}
