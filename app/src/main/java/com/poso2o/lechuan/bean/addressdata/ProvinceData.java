package com.poso2o.lechuan.bean.addressdata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Fynner on 2017/1/13.
 * provinceid	110000
 * provincename	北京市
 */

public class ProvinceData implements Serializable {
    public String provinceid = "";
    public String provincename = "";
    public ArrayList<CityData> city =new ArrayList<>();
}
