package com.poso2o.lechuan.bean.vdian;

import com.poso2o.lechuan.util.NumberFormatUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/16 0016.
 */
public class OpenNumber implements Serializable {
    private String wechar_authentication_amount;
    private String remark;
    private String agency_amount;

    public String getWechar_authentication_amount() {
        return NumberFormatUtils.format(wechar_authentication_amount);
    }

    public void setWechar_authentication_amount(String wechar_authentication_amount) {
        this.wechar_authentication_amount = wechar_authentication_amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAgency_amount() {
        return NumberFormatUtils.format(agency_amount);
    }

    public void setAgency_amount(String agency_amount) {
        this.agency_amount = agency_amount;
    }
}
