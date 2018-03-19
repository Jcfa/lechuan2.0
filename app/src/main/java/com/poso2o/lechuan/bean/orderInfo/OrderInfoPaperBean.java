package com.poso2o.lechuan.bean.orderInfo;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/15 0015.
 * 库存管理
 */

public class OrderInfoPaperBean {


    /**
     * code : success
     * msg : 库存统计
     * total : {"totalnums":"401","totalamounts":"0.00"}
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
         * totalnums : 401
         * totalamounts : 0.00
         */

        private String totalnums;
        private String totalamounts;

        public String getTotalnums() {
            return totalnums;
        }

        public void setTotalnums(String totalnums) {
            this.totalnums = totalnums;
        }

        public String getTotalamounts() {
            return totalamounts;
        }

        public void setTotalamounts(String totalamounts) {
            this.totalamounts = totalamounts;
        }
    }

    public static class DataBean {
        /**
         * totalamount : 0.00
         * image222 : http://img01.poso2o.com/20180315/d0528ae7347bbd83_222_222.jpg
         * totalnum : 9
         * bh : T8849
         * price : 0.00
         * name : 短袖T恤
         * guid : 180315714172373395417
         * fprice : 0.00
         */

        private String totalamount;
        private String image222;
        private String totalnum;
        private String bh;
        private String price;
        private String name;
        private String guid;
        private String fprice;

        public String getTotalamount() {
            return totalamount;
        }

        public void setTotalamount(String totalamount) {
            this.totalamount = totalamount;
        }

        public String getImage222() {
            return image222;
        }

        public void setImage222(String image222) {
            this.image222 = image222;
        }

        public String getTotalnum() {
            return totalnum;
        }

        public void setTotalnum(String totalnum) {
            this.totalnum = totalnum;
        }

        public String getBh() {
            return bh;
        }

        public void setBh(String bh) {
            this.bh = bh;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getFprice() {
            return fprice;
        }

        public void setFprice(String fprice) {
            this.fprice = fprice;
        }
    }
}
