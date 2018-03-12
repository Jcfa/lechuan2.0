package com.poso2o.lechuan.bean.member;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-04-24.
 */
public class Member implements Serializable{
    public String member_id = "";//会员id
    public String member_name = "";//会员名称
    public int birthday_year = 2017;//会员生日-年
    public int birthday_month = 5;//会员生日-月
    public int birthday_day = 4;//会员生日-日
    public String member_mobile = "";//会员手机号码
    public String member_photo = "";//会员头像
    public String member_card = "";//会员卡

    public String shop_id = "";//店铺id
    public String shop_branch_id = "";//创建分店

    public String member_level_id = "";//会员等级
    public String member_level_name = "";//会员等级名称
    public double member_discount = 100.00;//会员折扣

    public String member_type = "";//会员状态
    public String identification = "";//会员标识

    public String member_accounts = "";//会员帐号
    public String member_password = "";//会员密码

    public double member_balance = 0.00;//会员余额
    public int member_balance_type = 1;//会员余额状态 (0 冻结 1 正常)

    public double member_recharge_all_amount = 0.00;//会员充值总额（赠送金额+充值金额）
    public double member_recharge_amount = 0.00;//会员充值金额
    public double member_give_amount = 0.00;//会员赠送金额

    public int member_integral = 0;//会员积分

    public double member_consume = 0.00;//会员消费金额
    public int member_purchase_number = 0;//会员购买总数量

    public String member_purchase_analysis = "";//会员购买分析会员喜好
    public String member_purchase_analysis_resulet = "";//会员种类
    public double member_average_consumption = 0.00;//会员平均消费水平

    public int has_wechat = 0;//是否绑定 0 未绑定 1绑定
    public String wechat = "";//微信

    public int password_finish = 1;//是否绑定密码

    public String province = "";//省份id
    public String provincename = "";//省份名称
    public String city = "";//城市id
    public String cityname = "";//城市名称
    public String area = "";//区域id
    public String areaname = "";//区域名称
    public String member_address = "";//会员具体地址

    public long build_time = 0;//创建时间
    public String build_czy = "";//创建员工
    public String build_czy_name = "";//创建员工名称

    public long modify_time = 0;//修改时间
    public String modify_czy = "";//修改员工
    public String modify_czy_name = "";//修改员工名称
    public String member_remark = "";//备注

    //---------------------------------------------------------
    public String beforeLevelId = ""; //修改前的groupid


    //旧版服装版数据结构
    public String nick = "";
    public String uid = "";
    public String amounts = "0.00";
    public String orderamount = "0.00";
    public String order_amounts = "";
    public String ordernum = "0";
    public String points = "0";
}
