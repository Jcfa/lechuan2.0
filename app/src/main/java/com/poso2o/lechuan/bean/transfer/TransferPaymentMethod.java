package com.poso2o.lechuan.bean.transfer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by peng on 2017/7/3.
 */
public class TransferPaymentMethod implements Serializable{

    /** 交接id */
    public String transfer_id = "0";

    /** 1:现金 2：支付宝 3：微信 4：余额 5：刷卡 */
    public Integer payment_method_type = 0;

    /** 支付类型名称 */
    public String payment_method_name = "";

    /** 合计金额 */
    public Double total_amount = (double) 0;

    /** 订单详情 */
    public ArrayList<TransferDetailOrders> orders;

}
