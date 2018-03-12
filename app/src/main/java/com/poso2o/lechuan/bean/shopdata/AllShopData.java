package com.poso2o.lechuan.bean.shopdata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/24.
 */

public class AllShopData implements Serializable {
    public ArrayList<ShopData> shops = new ArrayList<>();
    public ArrayList<ShopData> wxshops = new ArrayList<>();
}
