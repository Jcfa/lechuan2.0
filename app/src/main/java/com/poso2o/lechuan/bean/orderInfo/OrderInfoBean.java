package com.poso2o.lechuan.bean.orderInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/14 0014.
 * 我的订单信息
 */

public class OrderInfoBean  implements Serializable{


    /**
     * code : success
     * msg : 实时订单
     * data : [{"order_date":"2018-03-09 17:38:31.0","payment_amount":"138.00","order_num":"1","order_id":"18030917383157672","salesname":"小小A","czy_name":"总账号"},{"order_date":"2018-03-09 15:29:49.0","payment_amount":"123.00","order_num":"1","order_id":"18030915294979248","salesname":"小小A","czy_name":"总账号"},{"order_date":"2018-03-09 09:42:49.0","payment_amount":"128.00","order_num":"1","order_id":"18030909424911520","salesname":"总账号","czy_name":"总账号"},{"order_date":"2018-03-09 09:37:43.0","payment_amount":"128.00","order_num":"1","order_id":"18030909374376130","salesname":"总账号","czy_name":"总账号"},{"order_date":"2018-03-09 09:01:19.0","payment_amount":"128.00","order_num":"1","order_id":"18030909011912889","salesname":"总账号","czy_name":"总账号"},{"order_date":"2018-03-09 09:00:55.0","payment_amount":"128.00","order_num":"1","order_id":"18030909005518457","salesname":"总账号","czy_name":"总账号"},{"order_date":"2018-03-08 19:45:03.0","payment_amount":"-128.00","order_num":"-1","order_id":"18030819450393654","salesname":"总账号","czy_name":"总账号"},{"order_date":"2018-03-08 13:39:45.0","payment_amount":"128.00","order_num":"1","order_id":"18030813394529996","salesname":"总账号","czy_name":"总账号"},{"order_date":"2018-03-05 09:32:48.0","payment_amount":"268.00","order_num":"1","order_id":"18030509324870101","salesname":"小小A","czy_name":"总账号"},{"order_date":"2018-03-02 14:03:35.0","payment_amount":"100.00","order_num":"1","order_id":"18030214031843249","salesname":"总账号","czy_name":"总账号"}]
     * total : {"currPage":"1","pages":"9","rowcount":"87","total_amount":"12515.87","total_num":"94"}
     */


    private TotalBean total;
    private List<DataBean> data;

    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
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
