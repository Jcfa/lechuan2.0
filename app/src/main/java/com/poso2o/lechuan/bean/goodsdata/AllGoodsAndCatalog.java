package com.poso2o.lechuan.bean.goodsdata;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/11/27.
 */

public class AllGoodsAndCatalog {

    public ArrayList<Catalog> catalogs = new ArrayList<>();

    public ArrayList<Goods> goods = new ArrayList<>();

    //旧服装版数据（服务端应该改一下，统一字段
    public ArrayList<Catalog> directory = new ArrayList<>();
    public ArrayList<Goods> list = new ArrayList<>();
}
