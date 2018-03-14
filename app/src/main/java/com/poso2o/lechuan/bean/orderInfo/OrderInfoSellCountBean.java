package com.poso2o.lechuan.bean.orderInfo;

import java.io.Serializable;

/**
 * Created by ${cbf} on 2018/3/13 0013.
 * 销售统计
 */

public class OrderInfoSellCountBean implements Serializable {

    /**
     * code : success
     * msg : 销售统计
     * data : {"nick":"时装周","completion_rate":0,"remaining_assignment":
     * "110693.00","assignment":"0.00","gross_profit":"110693.00","order_amounts
     * ":"110693.00","today_sales_number":0,"total_goods_number":376,"selling_cost":"0.00","today_sales_amounts":0}
     * total : {}
     */
    private String code;
    private String msg;
    private DataBean data;
    private TotalBean total;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public static class DataBean {
        /**
         * nick : 时装周
         * completion_rate : 0
         * remaining_assignment : 110693.00
         * assignment : 0.00
         * gross_profit : 110693.00
         * order_amounts : 110693.00
         * today_sales_number : 0
         * total_goods_number : 376
         * selling_cost : 0.00
         * today_sales_amounts : 0
         */

        private String nick;
        private String completion_rate;
        private String remaining_assignment;
        private String assignment;
        private String gross_profit;
        private String order_amounts;
        private int today_sales_number;

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getCompletion_rate() {
            if (completion_rate == null && completion_rate.equals("0.00"))
                return "0.00";
            return completion_rate;
        }

        public void setCompletion_rate(String completion_rate) {
            this.completion_rate = completion_rate;
        }

        public String getRemaining_assignment() {
            return remaining_assignment;
        }

        public void setRemaining_assignment(String remaining_assignment) {
            this.remaining_assignment = remaining_assignment;
        }

        public String getAssignment() {
            if (assignment == null && assignment.equals("0.00"))
                return "0.00";
            return assignment;
        }

        public void setAssignment(String assignment) {
            this.assignment = assignment;
        }

        public String getGross_profit() {
            if (gross_profit == null && gross_profit.equals("0.00"))
                return "0.00%";
                return gross_profit;
        }

        public void setGross_profit(String gross_profit) {
            this.gross_profit = gross_profit;
        }

        public String getOrder_amounts() {
            if (order_amounts == null && order_amounts.equals("0.00"))
                return "0.00";
            return order_amounts;
        }

        public void setOrder_amounts(String order_amounts) {
            this.order_amounts = order_amounts;
        }

        public int getToday_sales_number() {
            return today_sales_number;
        }

        public void setToday_sales_number(int today_sales_number) {
            this.today_sales_number = today_sales_number;
        }
    }

    public static class TotalBean {
    }
}
