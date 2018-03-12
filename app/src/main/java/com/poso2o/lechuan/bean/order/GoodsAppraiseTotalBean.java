package com.poso2o.lechuan.bean.order;


import java.io.Serializable;

/**
 * 商品购买评价
 * Created by Luo on 2017-12-11
 * 
 */
public class GoodsAppraiseTotalBean implements Serializable {

	public int currPage = 0; // 当前页
	public int countPerPage = 20; // 每页显示的记录数
	public int rowcount = 0;// 总记录数
	public int pages = 1;// 总页数

	/**
	 * 好评数
	*/
	public int total_appraise_grade_good_count= 0;
	
	/**
	 * 中评数
	*/ 
	public int total_appraise_grade_middle_count = 0;
	
	/**
	 * 差评数
	*/
	public int total_appraise_grade_bad_count = 0;
	
	/**
	 * 全部评价
	*/
	public int total_appraise_grade_count = 0;
	
}
