package com.poso2o.lechuan.bean.powerdata;

import java.io.Serializable;

/**
 * Created by Luo on 2017/1/10.
 */
public class PowerData implements Serializable, Cloneable {
    
    /*public String shop_id="";//店铺ID
    
    public String power_id="0";// 权限ID *//*
    public String power_name="";// 权限名称 *//*

    public String analysis_chart= "0";// 首页：分析图 *//*
    
    public String open_sale= "0";// 收银开单 *//*
    public String goods_discount= "0";// 商品打折 *//*
    public String goods_sales= "0";// 售罄操作 *//*
    public String goods_give= "0";// 赠送操作 *//*

    public String goods_add= "0";// 添加商品 *//*
    public String goods_del= "0";// 删除商品 *//*
    public String goods_edit= "0";// 修改商品  *//*
    public String goods_cost= "0";// 查看成本 *//*
    public String goods_directory= "0";// 目录设置 *//*

    public String member_add= "0";// 添加会员 *//*
    public String member_del= "0";//  删除会员 *//*
    public String member_edit= "0";// 修改会员 *//*
    public String member_addmount= "0";// 会员充值 *//*
    public String member_grade= "0";// 会员等级设置 *//*

    public String report_sales= "0";// 销售统计 *//*
    public String report_order= "0";//  查看订单 *//*
    public String report_goods= "0";// 商品统计 *//*
    public String report_achievement= "0";// 业绩统计 *//*
    public String report_profit= "0";//  月损益表 *//*
    public String report_member_amount= "0";// 充值记录 *//*

    public String order_refund= "0";// 退货操作 *//*

    public String inventory_update = "0";// 库存-盘点 *//*
    public String inventory_add = "0";// 库存-入仓 *//*
    public String inventory_apply = "0";// 库存-要货 *//*
    public String inventory_shipping = "0";// 库存-出货 *//*
    public String inventory_bug = "0";// 库存-报损 *//*

    public String purchase_order = "0";// 采购单 *//*
    public String inventory_warning = "0";// 库存-预警 *//*
    public String purchase_return_order = "0";// 采购-退货单 *//*

    public String transfer_detail= "0"; // 交接明细 *//*
    public String transfer_seting= "0";// 交接设置 *//*

    public String shop_information= "0";// 店铺信息 *//*
    public String power_setting= "0";// 员工权限 *//*

    public Long build_time=(long) 0;// 建立时间 */


    //旧版服装版数据
    public String order_fh = "";
    public String user_seting = "";
    public String user_position = "";
    public String order_quote = "";
    public String kc_add = "";
    public String product_add = "";
    public String client_payment = "";
    public String kc_update = "";
    public String kc_del = "";
    public String order_edit = "";
    public String positionid = "";
    public String order_query = "";
    public String order_discount = "";
    public String client_add = "";
    public String total_sales = "";
    public String indextotal = "";
    public String order_add = "";
    public String shop_seting = "";
    public String client_edit = "";
    public String kc_booking = "";
    public String factory = "";
    public String total_product = "";
    public String total_shop = "";
    public String positionname = "";
    public String product_del = "";
    public String product_edit = "";
    public String client_del = "";
    public String factory_contract = "";
    public String order_del = "";
    public String kc_bug = "";
    public String total_czy = "";
    public String kc_back = "";

    @Override
    public PowerData clone() {
        PowerData powerData = null;
        try {
            powerData = (PowerData) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return powerData;
    }
}
