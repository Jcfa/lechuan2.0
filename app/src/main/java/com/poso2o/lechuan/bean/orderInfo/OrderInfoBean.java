package com.poso2o.lechuan.bean.orderInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/14 0014.
 * 我的订单信息
 */

public class OrderInfoBean {


    /**
     * code : success
     * msg : 实时订单
     */


    private TotalBean total;
    private List<DataBean> list;

    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return list;
    }

    public void setData(List<DataBean> list) {
        this.list = list;
    }

    public static class TotalBean {
        /**
         * currPage : 1
         * pages : 9
         * rowcount : 87
         * total_amount : 12515.87
         * total_num : 94
         */

        private String currPage;
        private String pages;
        private String rowcount;
        private String total_amount;
        private String total_num;

        public String getCurrPage() {
            return currPage;
        }

        public void setCurrPage(String currPage) {
            this.currPage = currPage;
        }

        public String getPages() {
            return pages;
        }

        public void setPages(String pages) {
            this.pages = pages;
        }

        public String getRowcount() {
            return rowcount;
        }

        public void setRowcount(String rowcount) {
            this.rowcount = rowcount;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getTotal_num() {
            return total_num;
        }

        public void setTotal_num(String total_num) {
            this.total_num = total_num;
        }
    }

}
