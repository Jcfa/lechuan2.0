package com.poso2o.lechuan.bean.shopdata;

import java.io.Serializable;

/**
 * Created by mr zhang on 2017/8/24.
 * 店铺信息
 */

public class ShopData implements Serializable {

    //具体地址
    public String address = "";
    //地区id
    public String area_id = "";
    //地区名称
    public String area_name = "";
    //创建账号
    public String build_czy = "";
    //创建账号名称
    public String build_czy_name = "";
    //创建时间
    public String build_time = "";
    //城市id
    public String city_id = "";
    //城市名称
    public String city_name = "";
    //修改人
    public String modify_czy = "";
    public String modify_czy_name = "";
    public String modify_time = "";
    //省份id
    public String province_id = "";
    //省份名称
    public String province_name = "";
    public String shop_branch_atate = "";
    public String shop_branch_closetime = "";
    public String shop_branch_contacts = "";
    //分店id
    public String shop_branch_id = "";
    public String shop_branch_introduction = "";
    //分店logo
    public String shop_branch_logo = "";
    public String shop_branch_mobile = "";
    public String shop_branch_name = "";
    public String shop_branch_opentime = "";
    public String shop_branch_real_name = "";
    //分店电话
    public String shop_branch_tel = "";
    public String shop_branch_type = "";
    public String shop_id = "";
    public String shop_state = "";

    //到期时间
    public String expire_time  = "";

    //运费
    public String freight = "";
    public String freight_add = "";
    public String freight_addnum = "";
    public String freight_num = "";

    public String shop_closetime = "";
    public String shop_contacts = "";
    public String shop_introduction = "";
    public String shop_logo = "";
    public String shop_mobile = "";
    public String shop_name = "";
    public String shop_opentime = "";
    public String shop_real_name = "";
    public String shop_tel = "";
    public String shop_url = "";

    /** 是否开通微店试用  0=未开通,1=已开通 */
    public int has_webshop_try = 0;

    /** 是否开通微店 0=未开，1=已开 */
    public int has_webshop = 0;

    /** 微店购买服务ID */
    public int webshop_service_id = 0;

    /** 微店购买服务名称 */
    public String webshop_service_name = "";

    /** 微店到期时间*/
    public String webshop_service_date = "";

    /** 微店剩余 (天数) */
    public int webshop_service_days = 0;

    /** 是否已绑定信息收款账号  1=已绑定，0=未绑定 */
    public int has_bank_binding = 0;

    /** 提现银行ID */
    public String shop_bank_code = "";

    /** 商家提现银行-名称 */
    public String shop_bank_name = "";

    /** 商家提现银行-户名 */
    public String shop_bank_account_name = "";//微信收款帐号的昵称

    /** 商家提现银行-账号 */
    public String shop_bank_account_no = "";
    /**
     * 微店-店铺信息显示的服务套餐名称、服务到期时间
     */
    public String buy_service_name = "";
    public int buy_service_id = 0;
    public int buy_service_days = 0;


    // 旧服装版补充字段
    public String area = "";
    public String areaname = "";
    public String attn = "";
    public String city = "";
    public String cityname = "";
    public String dat = "";
    public String description = "";
    public String mobile = "";
    public String nick = "";
    public String pic222_222 = "";
    public String province = "";
    public String provincename = "";
    public String remark = "";
    public String shopid = "";
    public String shopname = "";
    public String shoptype = "";
    public String tel = "";
    public String uid = "";

}
