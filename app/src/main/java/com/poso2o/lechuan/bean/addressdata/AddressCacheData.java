package com.poso2o.lechuan.bean.addressdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Fynner on 2017/1/14.
 */

public class AddressCacheData implements Serializable {
    public ArrayList<ProvinceData> provinceDatas;
    public ConcurrentHashMap<String, ArrayList<CityData>> cityMapDatas;
    public ConcurrentHashMap<String, ArrayList<AreaData>> areaMapDatas;

    public String version = "";

}
