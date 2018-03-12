package com.poso2o.lechuan.bean.mine;

/**
 * Created by Administrator on 2017-11-30.
 */

public class BrokerageDetailItemBean {
    public String order_id = "";
    public String shop_id = "";
    public String distributor_id = "";
    public String member_id = "";
    public String member_name = "";
    public String member_logo = "";
    public double order_amount = 0;
    public double commission_amount = 0;
    public long order_time = 0;
    public long payment_time = 0;
    /**
     * 状态  0=未结算，1=已结算
     */
    public int state = 0;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getDistributor_id() {
        return distributor_id;
    }

    public void setDistributor_id(String distributor_id) {
        this.distributor_id = distributor_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_logo() {
        return member_logo;
    }

    public void setMember_logo(String member_logo) {
        this.member_logo = member_logo;
    }

    public double getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(double order_amount) {
        this.order_amount = order_amount;
    }

    public double getCommission_amount() {
        return commission_amount;
    }

    public void setCommission_amount(double commission_amount) {
        this.commission_amount = commission_amount;
    }

    public long getOrder_time() {
        return order_time;
    }

    public void setOrder_time(long order_time) {
        this.order_time = order_time;
    }

    public long getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(long payment_time) {
        this.payment_time = payment_time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
