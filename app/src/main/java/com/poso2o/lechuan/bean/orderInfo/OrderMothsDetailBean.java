package com.poso2o.lechuan.bean.orderInfo;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/17 0017.
 */

public class OrderMothsDetailBean {

    /**
     * total : {}
     * list : [{"uid":"13423678930","clear_profit":"1123.0","months":"201803","sales_amount":"1677.0","del_amount":"0.0","create_date":"201803","primecost_amount":"554.0"},{"uid":"13423678930","clear_profit":"0.0","months":"201802","sales_amount":"0.0","del_amount":"0.0","create_date":"201802","primecost_amount":"0.0"},{"uid":"13423678930","clear_profit":"7575.44","months":"201801","sales_amount":"10838.87","del_amount":"0.0","create_date":"201801","primecost_amount":"3263.42"}]
     */

    private TotalBean total;
    private List<ListBean> list;

    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class TotalBean {
    }

    public static class ListBean {
        /**
         * uid : 13423678930
         * clear_profit : 1123.0
         * months : 201803
         * sales_amount : 1677.0
         * del_amount : 0.0
         * create_date : 201803
         * primecost_amount : 554.0
         */

        private String uid;
        private String clear_profit;
        private String months;
        private String sales_amount;
        private String del_amount;
        private String create_date;
        private String primecost_amount;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getClear_profit() {
            if (clear_profit.equals("0.0"))
                return "0.00";
            return clear_profit;
        }

        public void setClear_profit(String clear_profit) {
            this.clear_profit = clear_profit;
        }

        public String getMonths() {
            return months;
        }

        public void setMonths(String months) {
            this.months = months;
        }

        public String getSales_amount() {
            if (sales_amount.equals("0.0"))
                return "0.00";
            return sales_amount;
        }

        public void setSales_amount(String sales_amount) {
            this.sales_amount = sales_amount;
        }

        public String getDel_amount() {
            if (del_amount.equals("0.0"))
                return "0.00";
            return del_amount;
        }

        public void setDel_amount(String del_amount) {
            this.del_amount = del_amount;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getPrimecost_amount() {
            if (primecost_amount.equals("0.0"))
                return "0.00";
            return primecost_amount;
        }

        public void setPrimecost_amount(String primecost_amount) {
            this.primecost_amount = primecost_amount;
        }
    }
}
