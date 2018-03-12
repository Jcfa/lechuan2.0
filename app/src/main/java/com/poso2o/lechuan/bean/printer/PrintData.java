package com.poso2o.lechuan.bean.printer;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/12/25.
 */

public class PrintData implements Serializable {
    public String name; // 商品名
    public String bh; // 货号

    public String barcode;  // 条码
    public String spec_name = null; // 规格名称
    public String colorid;  // 颜色
    public String sizeid;  // 尺寸
    public String price;  // 价格

    public int print_num = 0;//打印数量

}
