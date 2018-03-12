package com.poso2o.lechuan.bean.printer;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/12/21.
 *  barcode	1234567890123
 *  colorid	dchj
 *  num	0
 *  sizeid	投影仪
 */
public class GoodsDetailsNumsData implements Serializable {
    public String barcode;  //条码
    public String spec_name; // 规格名称
    public String num;      //库存
//    public String salesnum;  //销量
    public String price; //商品价格
}
