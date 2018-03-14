package com.poso2o.lechuan.bean.orderInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/13 0013.
 * 畅销商品
 */

public class OrderInfoSellBean implements Serializable{

    /**
     * code : success
     * data : [{"bh":"x001","colorid":"黑","guid":"a8efccec0954b14a9c71e7e1e3d75339",
     * "image222":"http://img01.poso2o.com/20171126/efcb9d5529836ce7_222_222.jpg",
     * "kcnum":"3","name":"鞋","price":"330.00","salesamount":"660.00","sizeid":"-","totalnum":"2"},
     * {"bh":"100125","colorid":"黑","guid":"24d48d9238ace0c8464c518ad9e9894d","image222"
     * :"http://img01.poso2o.com/20171102/4bbff0efa4a2417b_222_222.jpg","kcnum":"0","name":"
     * 韩国打底衫","price":"450.00","salesamount":"450.00","sizeid":"-","totalnum":"1"}]
     * msg : 参数错误
     * total : {"currPage":"1","pages":"1","rowcount":"5"}
     */

    private String code;
    private String msg;
    private TotalBean total;
    private List<DataBean> data;

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
         * pages : 1
         * rowcount : 5
         */

        private String currPage;
        private String pages;
        private String rowcount;

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
    }

    public static class DataBean {
        /**
         * bh : x001
         * colorid : 黑
         * guid : a8efccec0954b14a9c71e7e1e3d75339
         * image222 : http://img01.poso2o.com/20171126/efcb9d5529836ce7_222_222.jpg
         * kcnum : 3
         * name : 鞋
         * price : 330.00
         * salesamount : 660.00
         * sizeid : -
         * totalnum : 2
         */

        private String bh;
        private String colorid;
        private String guid;
        private String image222;
        private String kcnum;
        private String name;
        private String price;
        private String salesamount;
        private String sizeid;
        private String totalnum;

        public String getBh() {
            return bh;
        }

        public void setBh(String bh) {
            this.bh = bh;
        }

        public String getColorid() {
            return colorid;
        }

        public void setColorid(String colorid) {
            this.colorid = colorid;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getImage222() {
            return image222;
        }

        public void setImage222(String image222) {
            this.image222 = image222;
        }

        public String getKcnum() {
            return kcnum;
        }

        public void setKcnum(String kcnum) {
            this.kcnum = kcnum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSalesamount() {
            return salesamount;
        }

        public void setSalesamount(String salesamount) {
            this.salesamount = salesamount;
        }

        public String getSizeid() {
            return sizeid;
        }

        public void setSizeid(String sizeid) {
            this.sizeid = sizeid;
        }

        public String getTotalnum() {
            return totalnum;
        }

        public void setTotalnum(String totalnum) {
            this.totalnum = totalnum;
        }
    }
}
