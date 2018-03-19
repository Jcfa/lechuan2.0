package com.poso2o.lechuan.bean.orderInfo;

public class DataBean  {
    /**
     * order_date : 2018-03-09 17:38:31.0
     * payment_amount : 138.00
     * order_num : 1
     * order_id : 18030917383157672
     * salesname : 小小A
     * czy_name : 总账号
     */

    private String order_date;
    private String payment_amount;
    private String order_num;
    private String order_id;
    private String salesname;
    private String czy_name;

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSalesname() {
        return salesname;
    }

    public void setSalesname(String salesname) {
        this.salesname = salesname;
    }

    public String getCzy_name() {
        return czy_name;
    }

    public void setCzy_name(String czy_name) {
        this.czy_name = czy_name;
    }
}