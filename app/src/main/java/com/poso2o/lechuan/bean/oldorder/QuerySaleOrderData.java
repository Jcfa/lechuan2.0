package com.poso2o.lechuan.bean.oldorder;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by mr zhang on 2017/8/15.
 */

public class QuerySaleOrderData implements Serializable {

    public Total total = new Total();
    public ArrayList<SaleOrderDate> list = new ArrayList<>();

    public class Total {
        public int currPage;
        public int pages;
        public int rowcount;
        public String total_amount;
        public String total_num;
    }
}
