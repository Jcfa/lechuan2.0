package com.poso2o.lechuan.bean.monthprofit;

import java.io.Serializable;

/**
 * Created by mr zhang on 2017/8/16.
 */

public class MonthProfitData implements Serializable{
    public String months;
    public double net_profit;
    public double other_expenses;
    public double promotion_fee;
    public double property_management_fee;
    public String shop_branch_id;
    public String shop_id;
    public double shop_rent;
    public double staff_commmission;
    public double staff_wages;
    public double tax;
    public double total_amount;
    public double total_cost;
    public double total_profit;
    public double water_electricity;


    //补充旧服装版列表项数据
    public String uid;
    public String clear_profit = "";
    public String del_amount = "";
    public String sales_amount = "";
    public String create_date = "";
    public String primecost_amount = "";

    /*"clear_profit": "0.00",
        "gl_cost": "0.00",
        "gz_cost": "0.00",
        "primecost_amount": "2521.83",
        "profit": "0.00",
        "sales_amount": "17793.60",
        "sd_cost": "0.00",
        "sl_cost": "0.00",
        "tc_cost": "0.00",
        "tg_cost": "0.00",
        "zj_cost": "0.00",
        "zs_cost": "0.00"*/
    //旧服装版详情补充字段
    public String gl_cost = "0.00";
    public String gz_cost = "0.00";
    public String profit = "0.00";
    public String sd_cost = "0.00";
    public String sl_cost = "0.00";
    public String tc_cost = "0.00";
    public String tg_cost = "0.00";
    public String zj_cost = "0.00";
    public String zs_cost = "0.00";
}
