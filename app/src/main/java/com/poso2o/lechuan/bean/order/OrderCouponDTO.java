package com.poso2o.lechuan.bean.order;

import java.io.Serializable;

/**
 * 优惠卷(T_ORDER_COUNPON)
 *
 * Created by Luo on 2017-12-11
 */
public class OrderCouponDTO implements Serializable {
    
    /** 订单id */
    public long sale_order_id=0;
    
    /** 序号 */
    public int serial=0;
    
    /** 店铺id */
    public long shop_id=0L;
    
    /** 分店id */
    public long shop_branch_id=0L;
    
    /** 优惠卷id */
    public long coupon_id=0L;
    
    /** 优惠卷名 */
    public String coupon_name="";
    
    /** 优惠卷码 */
    public long coupon_code=0L;
    
    /** 优惠卷类型 */
    public int coupon_type=0;
    
    /** 优惠卷来源 */
    public String coupon_source="";
    
    /** 抵消金额 */
    public Double offset_amount=0.00;

}
