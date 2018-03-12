package com.poso2o.lechuan.bean.order;

import java.io.Serializable;

/**
 * 商品购买评价
 * Created by Luo on 2017-12-11
 * 
 */
public class GoodsAppraiseDTO implements Serializable {

	/** 评价id */
	public long appraise_id=0;
	
	/** 订单id */
	public long order_id=0;
	
	/** 店铺ID */
	public long shop_id=0L;
	
	/** 买家微信ID */
	public String openid = "";
	
	/** 买家名称 */
	public String buyer_nick = "";
	
	/** 买家Logo */
	public String buyer_logo = "";
    
	/** 商品ID */
	public long goods_id = 0L;

	/** 货号 */
	public String goods_no = "";

	/** 名称 */
	public String goods_name = "";

	/** 主图 */
	public String main_picture = "";

	/** 单位 */
	public String goods_unit = "";

	/** 商品规格id */
	public long goods_spec_id = 0L;

	/** 商品规格名称 */
	public String goods_spec_name = "";

	/** 销售价 */
	public Double spec_price = 0.00;

	/** 买家评价内容 */
	public String appraise_remark = "";

	/** 买家图片秀JSONArray */
	public String appraise_imgs = "";

	/** 综合评分(3=好，2=中，1=差) */
	public int appraise_grade = 0;

	/** 描述相符评分(1-5分) */
	public int appraise_goods_consistent = 0;

	/** 物流服务评分(1-5分) */
	public int appraise_logistics_service = 0;

	/** 客服态度评分(1-5分) */
	public int appraise_customer_service = 0;

	/** 评论时间 */
	public long build_time = 0L;

	public int comment_number;
}
