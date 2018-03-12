package com.poso2o.lechuan.bean.poster;


import java.io.Serializable;

/**
 * 评论别人的购买评价----分页数据
 * 
 * @author peng
 *
 */
public class ShopDetailsCommentsTotalBean implements Serializable {

	public int currPage = 0; // 当前页
	public int countPerPage = 20; // 每页显示的记录数
	public int rowcount = 0;// 总记录数
	public int pages = 1;// 总页数
	
}
