package com.poso2o.lechuan.bean.poster;


import java.io.Serializable;

/**
 * 分页数据
 *
 * Created by Luo on 2017-11-29.
 *
 */
public class PosterTotalBean implements Serializable {

	public int currPage = 0; // 当前页
	public int countPerPage = 20; // 每页显示的记录数
	public int rowcount = 0;// 总记录数
	public int pages = 1;// 总页数

	 /** 总佣金 */
	 public Double total_general_commission=0.00;
    
    /** 已结算金额 */
	public Double total_settle_amount=0.00;
    
    /** 未结算金额 */
	public Double total_not_settle_amount =0.00;

}
