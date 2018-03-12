package com.poso2o.lechuan.bean;

/**
 * Created by Administrator on 2017-12-01.
 */

public class TotalBean {
    public double total_general_commission = 0;
    public double total_settle_amount = 0;
    public double total_not_settle_amount = 0;
    public double total_commission_amount = 0;//总佣金
    public double total_order_amount = 0;//成交金额
    public double total_settle_commission_amount = 0;//结算佣金
    public double total_not_settle_commission_amount = 0;//未结算佣金
    public int rowcount = 0;
    public int pages = 0;
    public int currPage = 0;
    public int countPerPage = 0;

    public double getTotal_general_commission() {
        return total_general_commission;
    }

    public void setTotal_general_commission(double total_general_commission) {
        this.total_general_commission = total_general_commission;
    }

    public double getTotal_settle_amount() {
        return total_settle_amount;
    }

    public void setTotal_settle_amount(double total_settle_amount) {
        this.total_settle_amount = total_settle_amount;
    }

    public double getTotal_not_settle_amount() {
        return total_not_settle_amount;
    }

    public void setTotal_not_settle_amount(double total_not_settle_amount) {
        this.total_not_settle_amount = total_not_settle_amount;
    }

    public int getRowcount() {
        return rowcount;
    }

    public void setRowcount(int rowcount) {
        this.rowcount = rowcount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getCountPerPage() {
        return countPerPage;
    }

    public void setCountPerPage(int countPerPage) {
        this.countPerPage = countPerPage;
    }

    public double getTotal_commission_amount() {
        return total_commission_amount;
    }

    public void setTotal_commission_amount(double total_commission_amount) {
        this.total_commission_amount = total_commission_amount;
    }

    public double getTotal_order_amount() {
        return total_order_amount;
    }

    public void setTotal_order_amount(double total_order_amount) {
        this.total_order_amount = total_order_amount;
    }

    public double getTotal_settle_commission_amount() {
        return total_settle_commission_amount;
    }

    public void setTotal_settle_commission_amount(double total_settle_commission_amount) {
        this.total_settle_commission_amount = total_settle_commission_amount;
    }

    public double getTotal_not_settle_commission_amount() {
        return total_not_settle_commission_amount;
    }

    public void setTotal_not_settle_commission_amount(double total_not_settle_commission_amount) {
        this.total_not_settle_commission_amount = total_not_settle_commission_amount;
    }
}
