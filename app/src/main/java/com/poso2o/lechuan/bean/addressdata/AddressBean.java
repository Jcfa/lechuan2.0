package com.poso2o.lechuan.bean.addressdata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Fynner on 2017/1/13.
 * <p>
 * area	Array
 * city	Array
 * province	Array
 */

public class AddressBean implements Serializable {
    public ArrayList<ProvinceData> province = new ArrayList<>();//省市集合
    public ArrayList<CityData> city = new ArrayList<>();//城市集合
    public ArrayList<AreaData> area = new ArrayList<>();//区域集合
}
