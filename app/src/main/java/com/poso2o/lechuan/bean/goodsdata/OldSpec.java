package com.poso2o.lechuan.bean.goodsdata;

import com.poso2o.lechuan.util.TextUtil;

import java.io.Serializable;

/**
 * Created by mr zhang on 2017/12/11.
 */
public class OldSpec implements Serializable {
    public String colorid;
    public String sizeid;
    public String num = "0";
    public String barcode;
    public String guid;
    public String salesnum = "0";
    //库存补充字段,实际上跟上面的salesnum是同一个(无奈。。。)
    public String sales_num;

    public String getSpecName() {
        if (TextUtil.isNotEmpty(colorid) && TextUtil.isNotEmpty(sizeid)) {
            return colorid + "/" + salesnum;
        } else if (TextUtil.isNotEmpty(colorid)) {
            return colorid;
        } else if (TextUtil.isNotEmpty(sizeid)) {
            return sizeid;
        }
        return "-";
    }
}
