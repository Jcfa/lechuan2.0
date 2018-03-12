package com.poso2o.lechuan.bean.order;


import java.io.Serializable;

/**
 * 订单统计数据
 * Created by Luo on 2017-12-11
 *
 */
public class OrderTotalBean implements Serializable {

	public int currPage = 0; // 当前页
	public int countPerPage = 20; // 每页显示的记录数
	public int rowcount = 0;// 总记录数
	public int pages = 1;// 总页数

	/**
	 * 待付款--订单合计数 1
	*/
	public int total_order_pending_payment= 0; 
	
	/**
	 * 已付款--订单合计数 2
	*/ 
	public int total_order_paid = 0;
	
	/**
	 * 已发货--订单合计数 3
	*/
	public int total_order_shipped = 0;
	
	/**
	 * 已取消--订单合计数 4
	*/
	public int total_order_cancel = 0;
		
	/**
	 * 投诉中--订单合计数 7
	*/
	public int total_order_complaint= 0;
	
	/**
	 * 已完成--订单合计数 8
	*/
	public int total_order_finish = 0;
	
	/**
	 * 总订单合计数
	*/
	public int total_order_count = 0;
	
	/** 金额合计 */
	public double total_order_paid_amount= 0;

	/**待处理数*/
	public String order_refund_state_flow = "";

	/**已拒绝数*/
	public String order_refund_state_fail = "0";

	/**已退款数*/
	public String order_refund_state_success = "0";
}
