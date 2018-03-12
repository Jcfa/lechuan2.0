package com.poso2o.lechuan.bean.marketdata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/11/4.
 */

public class MarketingDGData implements Serializable {
    public String begin_time;
    public String build_time;
    public String end_time;
    public String goods_num;
    public ArrayList<DiscountGoodsData> promotionGoodsDetails = new ArrayList<>();
    public String promotion_id;
    public String promotion_name;
    public String shop_id;
}
