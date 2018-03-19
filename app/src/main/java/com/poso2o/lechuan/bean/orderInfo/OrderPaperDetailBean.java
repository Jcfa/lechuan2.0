package com.poso2o.lechuan.bean.orderInfo;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/16 0016.
 */

public class OrderPaperDetailBean {


    /**
     * bh : 5755458
     * price : 88.00
     * lists : [{"sales_num":"0","guid":"180112368051916818022","colorid":"彩色","num":"802","sizeid":"L"},
     * {"sales_num":"0","guid":"180112368051916818022","colorid":"浅蓝色","num":"0","sizeid":"L"}]
     * name : 格子衫 衬衫 女生
     * fprice : 20.00
     * image222 : http://img01.poso2o.com/20180112/0a9c7d6fbb536e6c_222_222.jpg
     */

    private String bh;
    private String price;
    private String name;
    private String fprice;
    private String image222;
    private List<ListsBean> lists;

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

    public String getFprice() {
        return fprice;
    }

    public void setFprice(String fprice) {
        this.fprice = fprice;
    }

    public String getImage222() {
        return image222;
    }

    public void setImage222(String image222) {
        this.image222 = image222;
    }

    public List<ListsBean> getLists() {
        return lists;
    }

    public void setLists(List<ListsBean> lists) {
        this.lists = lists;
    }

    public static class ListsBean {
        /**
         * sales_num : 0
         * guid : 180112368051916818022
         * colorid : 彩色
         * num : 802
         * sizeid : L
         */

        private String sales_num;
        private String guid;
        private String colorid;
        private String num;
        private String sizeid;

        public String getSales_num() {
            return sales_num;
        }

        public void setSales_num(String sales_num) {
            this.sales_num = sales_num;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getColorid() {
            return colorid;
        }

        public void setColorid(String colorid) {
            this.colorid = colorid;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getSizeid() {
            return sizeid;
        }

        public void setSizeid(String sizeid) {
            this.sizeid = sizeid;
        }
    }
}
