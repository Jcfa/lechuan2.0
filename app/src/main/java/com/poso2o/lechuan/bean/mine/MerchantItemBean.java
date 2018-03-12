package com.poso2o.lechuan.bean.mine;

import java.io.Serializable;

/**
 * 我的-未结佣金-商家信息
 * Created by Administrator on 2017-12-01.
 */

public class MerchantItemBean implements Serializable {
    private String shop_id = "";
    private String distributor_nick = "";
    private String distributor_name = "";
    private String distributor_logo = "";
    //    private double commission_amount_paid = 0;
    private String shop_logo = "";
    //    private double commission_amount_unpaid = 0;
    private String shop_nick = "";
    private String shop_name = "";
    private String distributor_id = "";
    //    private double commission_amount = 0;
    private double general_commission = 0;
    private double settle_amount = 0;
    private double not_settle_amount = 0;
    private long build_time = 0;

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getDistributor_nick() {
        return distributor_nick;
    }

    public void setDistributor_nick(String distributor_nick) {
        this.distributor_nick = distributor_nick;
    }

    public String getDistributor_name() {
        return distributor_name;
    }

    public void setDistributor_name(String distributor_name) {
        this.distributor_name = distributor_name;
    }

    public String getDistributor_logo() {
        return distributor_logo;
    }

    public void setDistributor_logo(String distributor_logo) {
        this.distributor_logo = distributor_logo;
    }


    public String getShop_logo() {
        return shop_logo;
    }

    public void setShop_logo(String shop_logo) {
        this.shop_logo = shop_logo;
    }


    public String getShop_nick() {
        return shop_nick;
    }

    public void setShop_nick(String shop_nick) {
        this.shop_nick = shop_nick;
    }

    public String getDistributor_id() {
        return distributor_id;
    }

    public void setDistributor_id(String distributor_id) {
        this.distributor_id = distributor_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public double getGeneral_commission() {
        return general_commission;
    }

    public void setGeneral_commission(double general_commission) {
        this.general_commission = general_commission;
    }

    public double getSettle_amount() {
        return settle_amount;
    }

    public void setSettle_amount(double settle_amount) {
        this.settle_amount = settle_amount;
    }

    public double getNot_settle_amount() {
        return not_settle_amount;
    }

    public void setNot_settle_amount(double not_settle_amount) {
        this.not_settle_amount = not_settle_amount;
    }

    public long getBuild_time() {
        return build_time;
    }

    public void setBuild_time(long build_time) {
        this.build_time = build_time;
    }
}
