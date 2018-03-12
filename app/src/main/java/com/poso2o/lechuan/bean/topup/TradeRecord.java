package com.poso2o.lechuan.bean.topup;

import java.io.Serializable;
import java.math.BigDecimal;


public class TradeRecord implements Serializable {
    
    /**
     * 交易号
     */
    public String tradeNo;
    
    /**
     * 备注
     */
    public String remark;
    
    /**
     * 微信id
     */
    public String openId;
    
    /**
     * 支付宝帐号
     */
    public String alipayBankCard;
    
    /**
     * 支付宝姓名
     */
    public String alipayAccountName;
    
    /**
     * 提现到账状态
     */
    public boolean isState;
    
    /**
     * 充值时间
     */
    public long dat;
    
    /**
     * 充值金额
     */
    public BigDecimal totalFee;
    
}
