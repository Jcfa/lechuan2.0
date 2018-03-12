package com.poso2o.lechuan.bean.goodsdata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/17.
 */

public class AllGoodsStockData implements Serializable {

    public GoodsStockTotal total;
    public ArrayList<Goods> list = new ArrayList<>();

    public class GoodsStockTotal{
        public int countPerPage;
//        public int currPage;
//        public int pages;
//        public int rowcount;
        public String total_goods_cost_amount;
        public String total_goods_number;

        //旧版数据
        public String currPage = "";
        public String totalamounts = "";
        public String totalnums = "";
        public String pages = "";
        public String rowcount = "";
    }
}
