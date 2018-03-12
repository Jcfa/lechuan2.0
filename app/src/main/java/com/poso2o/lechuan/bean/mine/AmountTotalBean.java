package com.poso2o.lechuan.bean.mine;

/**
 *
 * Created by Administrator on 2017-12-08.
 */

public class AmountTotalBean {
    private double total_amount_add = 0;//充值总额
    private double total_amount = 0;//红包余额
    private double total_amount_del = 0;//支出总额
    private double total_red_envelopes_amount = 0;//收入总额

    public double getTotal_amount_add() {
        return total_amount_add;
    }

    public void setTotal_amount_add(double total_amount_add) {
        this.total_amount_add = total_amount_add;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public double getTotal_amount_del() {
        return total_amount_del;
    }

    public void setTotal_amount_del(double total_amount_del) {
        this.total_amount_del = total_amount_del;
    }

    public double getTotal_red_envelopes_amount() {
        return total_red_envelopes_amount;
    }

    public void setTotal_red_envelopes_amount(double total_red_envelopes_amount) {
        this.total_red_envelopes_amount = total_red_envelopes_amount;
    }
}
