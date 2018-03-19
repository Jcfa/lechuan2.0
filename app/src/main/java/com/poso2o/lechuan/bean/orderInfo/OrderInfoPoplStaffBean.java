package com.poso2o.lechuan.bean.orderInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/19 0019.
 */

public class OrderInfoPoplStaffBean {

    /**
     * total : {"total_assignments":119000,"total_order_amounts":1677}
     * list : [{"czy":"1001","order_amounts":"1065.00","completion_rate":"10.650000",
     * "realname":"小小A","assignments":"10000"},{"czy":"1002","order_amounts":"0.00","
     * completion_rate":"0.000000","realname":"小B","assignments":"6000"},
     * "总账号","assignments":"100000"}]
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
        /**
         * total_assignments : 119000
         * total_order_amounts : 1677
         */

        private String total_assignments;
        private String total_order_amounts;

        public String getTotal_assignments() {
            if (total_assignments == null || total_assignments.equals(""))
                return "0.0";
            return total_assignments;
        }

        public void setTotal_assignments(String total_assignments) {
            this.total_assignments = total_assignments;
        }

        public String getTotal_order_amounts() {
            if (total_order_amounts == null || total_order_amounts.equals(""))
                return "0.0";
            return total_order_amounts;
        }

        public void setTotal_order_amounts(String total_order_amounts) {
            this.total_order_amounts = total_order_amounts;
        }
    }

    public static class ListBean {
        /**
         * czy : 1001
         * order_amounts : 1065.00
         * completion_rate : 10.650000
         * realname : 小小A
         * assignments : 10000
         */

        private String czy;
        private String order_amounts;
        private String completion_rate;
        private String realname;
        private String assignments;

        public String getCzy() {
            return czy;
        }

        public void setCzy(String czy) {
            this.czy = czy;
        }

        public String getOrder_amounts() {
            BigDecimal bd = new BigDecimal(order_amounts);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            return String.valueOf(bd);
        }

        public void setOrder_amounts(String order_amounts) {
            this.order_amounts = order_amounts;
        }

        public String getCompletion_rate() {
            BigDecimal bd = new BigDecimal(completion_rate);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            return String.valueOf(bd);
        }

        public void setCompletion_rate(String completion_rate) {
            this.completion_rate = completion_rate;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getAssignments() {
            BigDecimal bd = new BigDecimal(assignments);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            return String.valueOf(bd);
        }

        public void setAssignments(String assignments) {
            this.assignments = assignments;
        }
    }
}
