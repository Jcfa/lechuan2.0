package com.poso2o.lechuan.bean.bosstotal;

/**
 * Created by mr zhang on 2017/8/15.
 */

public class BossTotalBean {

    public long shop_branch_id;
    public double completion_rate = 0.0;
    public double remaining_assignment = 0.0;
    public long shop_id;
    public int assignments;
    public int sale_cost;
    public double gross_profit = 0.0;
    public double break_even_amount = 0.0;
    public double sale_amounts = 0.0;
    public String shop_branch_name;

    //旧版数据
    public String nick = "";
    public String selling_cost = "";
    public String assignmemt = "";
    public String order_amounts = "";
    public String today_sales_number = "0"; //当天售出数量
    public String total_goods_number = "0"; //剩余库存
    public String today_sales_amounts = ""; //当天交接班汇总
}
