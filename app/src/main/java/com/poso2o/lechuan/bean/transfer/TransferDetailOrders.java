package com.poso2o.lechuan.bean.transfer;

import java.io.Serializable;

/**
 * Created by peng on 2017/7/3.
 */

public class TransferDetailOrders implements Serializable{
    /** 交接id */
    public String transfer_id = "0";

    /** 序号 */
    public int serial =  0;

    /** 1:现金 2：支付宝 3：微信 4：余额 5：刷卡 */
    public Integer payment_method_type = 0;

    /** 支付类型名称 */
    public String payment_method_name = "";

    /** 订单号 */
    public Long order_id = (long) 0;

    /** 订单时间 */
    public Long order_date = (long) 0;

    /** 订单价格 */
    public Double order_amount = (double) 0;

    /** 备注信息 */
    public String remark = "";

}
