package com.poso2o.lechuan.bean.orderInfo;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/16 0016.
 * 月损益表的实体类
 */

public class OrderInfoPrimeCostBean {

    /**
     * code : success
     * msg : 月损益表排名
     * data : [{"del_amount":"0.00","total_primecost":"0.00","shopname":"天鹅湖","shoptype":"","shopid":"13016079579",
     * "total_profit":"0.00","add_amounts":"0.00"}]
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
         * del_amount : 0.00
         * total_primecost : 0.00
         * shopname : 天鹅湖
         * shoptype :
         * shopid : 13016079579
         * total_profit : 0.00
         * add_amounts : 0.00
         */

        private String del_amount;
        private String total_primecost;
        private String shopname;
        private String shoptype;
        private String shopid;
        private String total_profit;
        private String add_amounts;

        public String getDel_amount() {
            return del_amount;
        }

        public void setDel_amount(String del_amount) {
            this.del_amount = del_amount;
        }

        public String getTotal_primecost() {
            return total_primecost;
        }

        public void setTotal_primecost(String total_primecost) {
            this.total_primecost = total_primecost;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getShoptype() {
            return shoptype;
        }

        public void setShoptype(String shoptype) {
            this.shoptype = shoptype;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public String getTotal_profit() {
            return total_profit;
        }

        public void setTotal_profit(String total_profit) {
            this.total_profit = total_profit;
        }

        public String getAdd_amounts() {
            return add_amounts;
        }

        public void setAdd_amounts(String add_amounts) {
            this.add_amounts = add_amounts;
        }
    }
}
