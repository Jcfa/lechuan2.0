package com.poso2o.lechuan.bean.poster;

import com.poso2o.lechuan.bean.BaseBean;

/**
 * 分销商佣金对账表(T_SHOP_DISTRIBUTION_ACCOUNT)
 *
 * @author Luo
 * @version 1.0.0 2017-12-02
 */
public class PosterCommissionDTO extends BaseBean {

	 /** 订单号 */
    public Long order_id=0L;
    
    /** 店铺ID */
    public Long shop_id=0L;
    
    /** 分销商ID */
    public Long distributor_id=0L;

    /** 所属推广文章ID */
    public Long news_id=0L;

    /** 会员id */
	public Long member_id=0L;

	/** 会员名称 */
	public String member_name="";
	
	/** 会员头像 */
	public String member_logo="";
	
    /** 订单金额 */
    public Double order_amount=0.00;
    
    /** 所得佣金 */
    public Double commission_amount=0.00;
    
    /** 订单时间 */
    public Long order_time=0L;
    
    /** 支付时间 */
    public Long payment_time=0L;
        
    /** 状态  0=未结算，1=已结算 */
    public Integer state =0;

}
