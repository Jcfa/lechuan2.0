package com.poso2o.lechuan.bean.addressdata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Fynner on 2017/1/13.
 * cityid	110100
 * cityname	市辖区
 * provinceid	110000
 */

public class CityData implements Serializable {
    public String cityid = "";
    public String cityname = "";
    public String provinceid = "";
    public ArrayList<AreaData> areas =new ArrayList<AreaData>();
}
