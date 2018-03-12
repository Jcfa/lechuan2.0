package com.poso2o.lechuan.bean.goodsdata;

import java.io.Serializable;

/**
 * Created by mr zhang on 2017/8/17.
 */

public class HotSaleProduct implements Serializable {

    public double cost_amount;
    public String goods_id = "";
    public String goods_name = "";
    public String goods_no = "";
    public double goods_offset_discount_amount;
    public double goods_offset_discount_price;
    public String goods_spec_id = "";
    public String goods_spec_name = "";
    public String goods_unit = "";
    public String main_picture = "";
    public double profit_amount;
    public double sale_amount;
    public int sale_munber;
    public double spec_cost;
    public int spec_stock_munber;
    public double spec_price;

    //旧服装版本补充字段
    public String bh = "";
    public String colorid = "";
    public String guid = "";
    public String image222 = "";
    public String kcnum = "";
    public String name = "";
    public String price = "";
    public String salesamount = "";
    public String sizeid = "";
    public String totalnum = "";
    //旧服装版详情补充字段
    public String fprice = "";
    public String total_amount = "";
    public String total_num = "";
}
