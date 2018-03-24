package com.poso2o.lechuan.bean.wopenaccountdata;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class OpenStandBean implements Serializable {
    private String attn;
    private String shop_id;
    private String amount;
    private String service_name;
    private String payment_time;
    private String build_time;
    private String mobile;
    private int state = 0;

    public static int WAIT_OPEN_STATE = 0;//未缴费、未开通
    public static int PAY_COSE_STATE = 1;//已经缴费、未开通
    public static int HAS_OPENED_STATE = 2;//已经开通

    public String getAttn() {
        return attn;
    }

    public void setAttn(String attn) {
        this.attn = attn;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

    public String getBuild_time() {
        return build_time;
    }

    public void setBuild_time(String build_time) {
        this.build_time = build_time;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
