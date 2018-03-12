package com.poso2o.lechuan.bean.goodsdata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/17.
 */

public class AllHotSaleProduct implements Serializable {

    public Total total;
    public ArrayList<HotSaleProduct> list = new ArrayList<>();

    public class Total{
        public int countPerPage;
        public int currPage;
        public int pages;
        public int rowcount;
        public double total_sale_amount;
        public double total_sale_number;
    }
}
