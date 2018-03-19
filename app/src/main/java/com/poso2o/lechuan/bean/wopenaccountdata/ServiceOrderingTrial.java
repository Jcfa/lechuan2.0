package com.poso2o.lechuan.bean.wopenaccountdata;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/15 0015.
 */

public class ServiceOrderingTrial implements Serializable {
    private String amount;//多少钱
    private String remark;//说明
    private String days;//天数
    private String service_id;//服务的id
    private String service_name;//服务的名字
    private String service_type;//服务的序号
    private String unit;//年


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
