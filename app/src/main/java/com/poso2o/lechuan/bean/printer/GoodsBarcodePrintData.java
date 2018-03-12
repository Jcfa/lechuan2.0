package com.poso2o.lechuan.bean.printer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/24.
 */

public class GoodsBarcodePrintData implements Serializable {
    public ArrayList<GoodsDetailsNumsData> nums; //商品颜色 尺寸 库存 条码
    public String name; //商品名
    public String bh; //货号
    public String guid;//商品id
    public String price; //商品价格
    public ArrayList<GoodsDetailsImgsData> imgs;//图片组
}
