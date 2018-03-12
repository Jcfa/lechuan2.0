package com.poso2o.lechuan.bean.printer;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/12/22.
 */

public class PrintNumsItemData implements Serializable {
    public String barcode;  //条码
    public String num;      //库存
    public String price;  //销量
    public String spec_name; // 规格名称
    public boolean isSelect = false;
}
