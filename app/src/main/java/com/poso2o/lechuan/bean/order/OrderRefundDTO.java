package com.poso2o.lechuan.bean.order;

import java.io.Serializable;

/**
 * 订单退款信息(T_ORDER_REFUND)
 *
 * Created by Luo on 2017-12-11
 */
public class OrderRefundDTO implements Serializable {

	/** 订单号 */
    public long order_id=0L;

    /**是否需要退货   :  0.不需要  1=需要*/
    public int refund_code=0;
        
    /** 申请退款原因备注 */
    public String refund_remark="";
    
    /** 商家答复 */
    public String reply_remark="";
     
    /** 申请退款金额 */
    public double refund_amount=0.00;
    
    /** 申请退款时间 */
    public long refund_time=0L;
    
    /** 退还商家快递公司 */
    public String refund_express_id="";
    
    /** 退还商家快递单 */
    public String refund_express_dh="";

}
