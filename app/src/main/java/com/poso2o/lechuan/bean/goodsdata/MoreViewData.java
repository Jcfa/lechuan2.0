package com.poso2o.lechuan.bean.goodsdata;

/**
 * Created by lenovo on 2016/12/20.
 */
public class MoreViewData {
    public String colorid;
    public String sizeid;
    public String num;
    public String barcode;

    @Override
    protected MoreViewData clone() throws CloneNotSupportedException {
        try {
            return (MoreViewData) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
