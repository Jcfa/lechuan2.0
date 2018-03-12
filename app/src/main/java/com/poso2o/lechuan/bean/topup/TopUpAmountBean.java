package com.poso2o.lechuan.bean.topup;

/**
 * 充值金额
 * Created by Administrator on 2017-12-08.
 */

public class TopUpAmountBean {
    private int id = 0;
    private double amount = 0;
    private boolean checked = false;

    public TopUpAmountBean(int id, double amount, boolean checked) {
        this.id = id;
        this.amount = amount;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return (int)amount;
    }

    public String getAmountText() {
        if (amount == 0) {
            return "自定义";
        }
        return (int)amount + "元";
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
