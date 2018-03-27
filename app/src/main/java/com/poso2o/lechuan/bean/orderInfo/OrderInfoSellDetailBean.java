package com.poso2o.lechuan.bean.orderInfo;

/**
 * Created by ${cbf} on 2018/3/17 0017.
 */

public class OrderInfoSellDetailBean {

    /**
     * total_amount : 384.00
     * kcnum : 17
     * price : 128.00
     * total_num : 3
     * sizeid : L
     * name : 小女生卡哇伊大码T恤
     * colorid : 彩色
     * bh : 457577478
     * image222 : http://img01.poso2o.com/20180112/0f421bd1d97f0906_222_222.jpg
     * fprice : 28.00
     */

    private String total_amount;
    private String kcnum;
    private String price;
    private String total_num;
    private String sizeid;
    private String name;
    private String colorid;
    private String bh;
    private String image222;
    private String fprice;

    public String getTotal_amount() {
        if (total_amount.equals("0.0"))
            return "0.00";
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getKcnum() {
        return kcnum;
    }

    public void setKcnum(String kcnum) {
        this.kcnum = kcnum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public String getSizeid() {
        return sizeid;
    }

    public void setSizeid(String sizeid) {
        this.sizeid = sizeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorid() {
        return colorid;
    }

    public void setColorid(String colorid) {
        this.colorid = colorid;
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

    public String getFprice() {
        return fprice;
    }

    public void setFprice(String fprice) {
        this.fprice = fprice;
    }
}
