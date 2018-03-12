package com.poso2o.lechuan.bean.oa;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/2/6.
 */

public class OaService implements Serializable {

    public ArrayList<Item> list = new ArrayList<>();

    public class Item implements Serializable{
        public String amount = "0";
        public String days = "0";
        public String news_amount = "0";
        public String num = "0";
        public String operation_amount = "0";
        public String remark = "";
        public String service_id = "0";
        public String service_name = "";
        public String service_type = "0";
        public String unit = "";
    }
}
