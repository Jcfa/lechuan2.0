package com.poso2o.lechuan.bean.goodsdata;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-04-24.
 */
public class Catalog implements Serializable, Cloneable {
    public String shop_id = "";//店铺id

    public String catalog_id = "";//目录id
    public String catalog_name = "";//目录名称

    public int catalog_goods_number = 0;//目录商品数量

    public double catalog_discount = 100.00;//目录折扣

    public long build_time = 0;// 创建时间
    public String build_czy = "";// 创建员工
    public String build_czy_name = "";// 创建员工名
    public long modify_time = 0;// 修改时间
    public String modify_czy = "";// 修改员工
    public String modify_czy_name = "";// 修改员工名

    public int serial = 0;// 序号


    /**
     * 旧服装版数据
     */
    public String directory = "";
    public String fid = "-1";
    public String productNum;

    @Override
    public Catalog clone() throws CloneNotSupportedException {
        return (Catalog) super.clone();
    }

    public String getNameAndNum() {
        return catalog_name + "(" + catalog_goods_number + ")";
    }

    public String getDirNameAndNum() {
        return directory + "(" + productNum + ")";
    }


}