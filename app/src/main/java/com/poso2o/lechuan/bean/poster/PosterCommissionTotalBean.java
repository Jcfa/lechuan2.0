package com.poso2o.lechuan.bean.poster;


import java.io.Serializable;

/**
 * 分销商佣金对账表(T_SHOP_DISTRIBUTION_ACCOUNT)
 * 
 * @author Luo
 * @version 1.0.0 2017-12-02
 */
public class PosterCommissionTotalBean implements Serializable {

    public int currPage = 0; // 当前页
    public int countPerPage = 20; // 每页显示的记录数
    public int rowcount = 0;// 总记录数
    public int pages = 1;// 总页数
	
	 /** 总佣金 */
     public Double total_commission_amount=0.00;
    
    /** 已结算佣金 */
    public Double total_settle_commission_amount=0.00;
    
    /** 未结算佣金 */
    public Double total_not_settle_commission_amount =0.00;

}
