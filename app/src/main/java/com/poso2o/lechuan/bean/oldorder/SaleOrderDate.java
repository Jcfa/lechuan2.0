package com.poso2o.lechuan.bean.oldorder;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/15.
 */

public class SaleOrderDate {

    //订单列表项字段
    public String czy_name;

    //列表项和详情共有字段
    public String order_date;
    public String  order_id;
    public String order_num;
    public String salesname;
    public String payment_amount;

    //详情补充字段
    /*"addr_address": "",
        "addr_area": "",
        "addr_areaname": "",
        "addr_city": "",
        "addr_cityname": "",
        "addr_mobile": "",
        "addr_name": "",
        "addr_province": "",
        "addr_provincename": "",
        "buyer_nick": "",
        "buyer_uid": "",
        "czy": "13423678930",
        "czyname": "总账号",
        "express_id": "",
        "express_jsfs": "",
        "express_name": "",
        "freightCost": "0.00",
        "online": "0",
        "order_amount": "100.00",
        "order_date": "2017-12-07 18:56:41.0",
        "order_discount": "100.00",
        "order_flag": "ORDER_CONFRIM",
        "order_id": "17120718564153102",
        "order_num": "1",
        "payment_amount": "100.00",
        "payment_jsfs": "现金",
        "products"[]

        "remark": "",
        "sales": "1001",
        "salesname": "小A",
        "seller_uid": "13423678930",
        "shopname": "Zclothes",
        "shoptel": "",
        "use_points": "0",
        "use_points_amount": "0"*/

    public String addr_address = "";
    public String addr_area = "";
    public String addr_areaname = "";
    public String addr_city = "";
    public String addr_cityname = "";
    public String addr_mobile = "";
    public String addr_name = "";
    public String addr_province = "";
    public String addr_provincename = "";
    public String buyer_nick = "";
    public String buyer_uid = "";
    public String czy = "";
    public String czyname = "";
    public String express_id = "";
    public String express_jsfs = "";
    public String express_name = "";
    public String freightCost = "";
    public String online = "";
    public String order_amount = "";
    public String order_discount = "";
    public String order_flag = "";
    public String payment_jsfs = "";

    public String remark = "";
    public String sales = "";
    public String seller_uid = "";
    public String shopname = "";
    public String shoptel = "";
    public String use_points = "";
    public String use_points_amount = "";
    public ArrayList<SaleOrderGood> products;
}
