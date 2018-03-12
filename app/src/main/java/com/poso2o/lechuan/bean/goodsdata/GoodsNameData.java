package com.poso2o.lechuan.bean.goodsdata;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/12/19.
 */
public class GoodsNameData implements Serializable {
    public GoodsNameData(String goodsName) {
        this.goodsName = goodsName;
    }

    public String goodsName;
    public String goodsNameLight;
}
