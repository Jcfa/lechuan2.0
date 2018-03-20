package com.poso2o.lechuan.bean.orderInfo;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/16 0016.
 */

public class OrderInfoMemberBean {

    /**
     * code : success
     * msg : 会员管理
     * data : [{"nick":"郭","uid":"12345678908","amounts":"300.00",
     * "orderamount":"4806.00","order_amounts":"4806.00","ordernum":"2","points":"4806"}],
     * total : {}
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
    }

    public static class DataBean {
        /**
         * nick : 郭
         * uid : 12345678908
         * amounts : 300.00
         * orderamount : 4806.00
         * order_amounts : 4806.00
         * ordernum : 2
         * points : 4806
         */

        private String nick;
        private String uid;
        private String amounts;
        private String orderamount;
        private String order_amounts;
        private String ordernum;
        private String points;

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAmounts() {
            return amounts;
        }

        public void setAmounts(String amounts) {
            this.amounts = amounts;
        }

        public String getOrderamount() {
            return orderamount;
        }

        public void setOrderamount(String orderamount) {
            this.orderamount = orderamount;
        }

        public String getOrder_amounts() {
            return order_amounts;
        }

        public void setOrder_amounts(String order_amounts) {
            this.order_amounts = order_amounts;
        }

        public String getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(String ordernum) {
            this.ordernum = ordernum;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }
    }
}
