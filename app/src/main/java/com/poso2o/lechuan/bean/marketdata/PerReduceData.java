package com.poso2o.lechuan.bean.marketdata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/11/1.
 */

public class PerReduceData implements Serializable {
    //开始时间
    public String begin_time = "0";
    //结束时间
    public String end_time = "0";
    //创建时间
    public String build_time = "0";
    //具体满减
    public ArrayList<PromotionDetailData> promotionDetails = new ArrayList<>();

    public String promotion_id;
    public String promotion_name;
    public String shop_id;
}
