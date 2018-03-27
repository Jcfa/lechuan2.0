package com.poso2o.lechuan.bean.orderInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/19 0019.
 */

public class OrderIOnfoStaffDetailBean {

    /**
     * code : success
     * msg : 查看人员业绩详情
     * lists : [{"order_amounts":"1065.00","completion_rate":"10.650000","months":"201803","assignments":"10000"}]
     * total : {}
     */

    private TotalBean total;
    private List<ListsBean> list;


    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public List<ListsBean> getLists() {
        return list;
    }

    public void setLists(List<ListsBean> list) {
        this.list = list;
    }

    public static class TotalBean {
    }

    public static class ListsBean {
        /**
         * order_amounts : 1065.00
         * completion_rate : 10.650000
         * months : 201803
         * assignments : 10000
         */

        private String order_amounts;
        private String completion_rate;
        private String months;
        private String assignments;

        public String getOrder_amounts() {
            if (order_amounts == null || order_amounts.equals(""))
                return "0.00";
            return order_amounts + "";
        }

        public void setOrder_amounts(String order_amounts) {
            this.order_amounts = order_amounts;
        }

        public String getCompletion_rate() {
            double staff_dc = Double.parseDouble(completion_rate);
            BigDecimal bg1 = new BigDecimal(staff_dc);
            double value1 = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (completion_rate == null || completion_rate.equals(""))
                return "0.00";
            return value1 + "";
        }

        public void setCompletion_rate(String completion_rate) {
            this.completion_rate = completion_rate;
        }

        public String getMonths() {
            return months;
        }

        public void setMonths(String months) {
            this.months = months;
        }

        public String getAssignments() {
            if (assignments == null || assignments.equals(""))
                return "0.00";
            return assignments + ".00";
        }

        public void setAssignments(String assignments) {
            this.assignments = assignments;
        }
    }
}
