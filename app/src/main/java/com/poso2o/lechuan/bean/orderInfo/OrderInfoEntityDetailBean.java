package com.poso2o.lechuan.bean.orderInfo;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/17 0017.
 */

public class OrderInfoEntityDetailBean {

    /**
     * shoptel :
     * addr_city :
     * remark :
     * payment_jsfs : 现金
     * freightCost : 0.00
     * addr_province :
     * czyname : 总账号
     * shopname : 天天向上
     * order_id : 18030917383157672
     * express_id :
     * online : 0
     * czy : 13423678930
     * use_points_amount : 0
     * order_amount : 138.00
     * seller_uid : 13423678930
     * express_name :
     * buyer_uid :
     * sales : 1001
     * order_num : 1
     * addr_name :
     * order_date : 2018-03-09 17:38:31.0
     * <p>
     * products : [{"sizeid":"L","remark":"","no":"0","colorid":"蓝色","barcode":"00002","discount":
     * "100.00","fprice":"38.00","amount":"138.00","guid":"aa674378efd75556abd4b941cdfaa0c2"
     * ,"unit":"","num":"1","price":"138.00","name":"优衣库2015日系学院风连衣裙",
     * "bh":"00216","image222":"http://img01.poso2o.com/20180111/ad9acf95b3e01804_222_222.jpg"}]
     * <p>
     * order_discount : 100.00
     * addr_provincename :
     * addr_cityname :
     * addr_areaname :
     * order_flag : ORDER_CONFRIM
     * addr_area :
     * use_points : 0
     * buyer_nick :
     * addr_address :
     * salesname : 小小A
     * express_jsfs :
     * payment_amount : 138.00
     * addr_mobile :
     */

    private String shoptel;
    private String addr_city;
    private String remark;
    private String payment_jsfs;
    private String freightCost;
    private String addr_province;
    private String czyname;
    private String shopname;
    private String order_id;
    private String express_id;
    private String online;
    private String czy;
    private String use_points_amount;
    private String order_amount;
    private String seller_uid;
    private String express_name;
    private String buyer_uid;
    private String sales;
    private String order_num;
    private String addr_name;
    private String order_date;
    private String order_discount;
    private String addr_provincename;
    private String addr_cityname;
    private String addr_areaname;
    private String order_flag;
    private String addr_area;
    private String use_points;
    private String buyer_nick;
    private String addr_address;
    private String salesname;
    private String express_jsfs;
    private String payment_amount;
    private String addr_mobile;
    private List<ProductsBean> products;

    public String getShoptel() {
        return shoptel;
    }

    public void setShoptel(String shoptel) {
        this.shoptel = shoptel;
    }

    public String getAddr_city() {
        return addr_city;
    }

    public void setAddr_city(String addr_city) {
        this.addr_city = addr_city;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPayment_jsfs() {
        return payment_jsfs;
    }

    public void setPayment_jsfs(String payment_jsfs) {
        this.payment_jsfs = payment_jsfs;
    }

    public String getFreightCost() {
        return freightCost;
    }

    public void setFreightCost(String freightCost) {
        this.freightCost = freightCost;
    }

    public String getAddr_province() {
        return addr_province;
    }

    public void setAddr_province(String addr_province) {
        this.addr_province = addr_province;
    }

    public String getCzyname() {
        return czyname;
    }

    public void setCzyname(String czyname) {
        this.czyname = czyname;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getExpress_id() {
        return express_id;
    }

    public void setExpress_id(String express_id) {
        this.express_id = express_id;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getCzy() {
        return czy;
    }

    public void setCzy(String czy) {
        this.czy = czy;
    }

    public String getUse_points_amount() {
        return use_points_amount;
    }

    public void setUse_points_amount(String use_points_amount) {
        this.use_points_amount = use_points_amount;
    }

    public String getOrder_amount() {
        int anInt = Integer.parseInt(order_num);
        String price = getProducts().get(0).getPrice();
        if (anInt < 0)
            return "-" + price;
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getSeller_uid() {
        return seller_uid;
    }

    public void setSeller_uid(String seller_uid) {
        this.seller_uid = seller_uid;
    }

    public String getExpress_name() {
        return express_name;
    }

    public void setExpress_name(String express_name) {
        this.express_name = express_name;
    }

    public String getBuyer_uid() {
        return buyer_uid;
    }

    public void setBuyer_uid(String buyer_uid) {
        this.buyer_uid = buyer_uid;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getAddr_name() {
        return addr_name;
    }

    public void setAddr_name(String addr_name) {
        this.addr_name = addr_name;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_discount() {
        return order_discount;
    }

    public void setOrder_discount(String order_discount) {
        this.order_discount = order_discount;
    }

    public String getAddr_provincename() {
        return addr_provincename;
    }

    public void setAddr_provincename(String addr_provincename) {
        this.addr_provincename = addr_provincename;
    }

    public String getAddr_cityname() {
        return addr_cityname;
    }

    public void setAddr_cityname(String addr_cityname) {
        this.addr_cityname = addr_cityname;
    }

    public String getAddr_areaname() {
        return addr_areaname;
    }

    public void setAddr_areaname(String addr_areaname) {
        this.addr_areaname = addr_areaname;
    }

    public String getOrder_flag() {
        return order_flag;
    }

    public void setOrder_flag(String order_flag) {
        this.order_flag = order_flag;
    }

    public String getAddr_area() {
        return addr_area;
    }

    public void setAddr_area(String addr_area) {
        this.addr_area = addr_area;
    }

    public String getUse_points() {
        return use_points;
    }

    public void setUse_points(String use_points) {
        this.use_points = use_points;
    }

    public String getBuyer_nick() {
        return buyer_nick;
    }

    public void setBuyer_nick(String buyer_nick) {
        this.buyer_nick = buyer_nick;
    }

    public String getAddr_address() {
        return addr_address;
    }

    public void setAddr_address(String addr_address) {
        this.addr_address = addr_address;
    }

    public String getSalesname() {
        return salesname;
    }

    public void setSalesname(String salesname) {
        this.salesname = salesname;
    }

    public String getExpress_jsfs() {
        return express_jsfs;
    }

    public void setExpress_jsfs(String express_jsfs) {
        this.express_jsfs = express_jsfs;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getAddr_mobile() {
        return addr_mobile;
    }

    public void setAddr_mobile(String addr_mobile) {
        this.addr_mobile = addr_mobile;
    }

    public List<ProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBean> products) {
        this.products = products;
    }

    public static class ProductsBean {
        /**
         * sizeid : L
         * remark :
         * no : 0
         * colorid : 蓝色
         * barcode : 00002
         * discount : 100.00
         * fprice : 38.00
         * amount : 138.00
         * guid : aa674378efd75556abd4b941cdfaa0c2
         * unit :
         * num : 1
         * price : 138.00
         * name : 优衣库2015日系学院风连衣裙
         * bh : 00216
         * image222 : http://img01.poso2o.com/20180111/ad9acf95b3e01804_222_222.jpg
         */

        private String sizeid;
        private String remark;
        private String no;
        private String colorid;
        private String barcode;
        private String discount;
        private String fprice;
        private String amount;
        private String guid;
        private String unit;
        private String num;
        private String price;
        private String name;
        private String bh;
        private String image222;

        public String getSizeid() {
            return sizeid;
        }

        public void setSizeid(String sizeid) {
            this.sizeid = sizeid;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getColorid() {
            return colorid;
        }

        public void setColorid(String colorid) {
            this.colorid = colorid;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getFprice() {
            return fprice;
        }

        public void setFprice(String fprice) {
            this.fprice = fprice;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
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

        public String getBh() {
            return bh;
        }

        public void setBh(String bh) {
            this.bh = bh;
        }

        public String getImage222() {
            return image222;
        }

        public void setImage222(String image222) {
            this.image222 = image222;
        }
    }
}
