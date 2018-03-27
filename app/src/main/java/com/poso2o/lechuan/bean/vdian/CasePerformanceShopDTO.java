package com.poso2o.lechuan.bean.vdian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-03-24.
 */

public class CasePerformanceShopDTO implements Serializable{
    public String shop_id = "";
    public String shop_name = "";
    public String logo = "";
    public String remark = "";
    public String wechar_id = "";
    public String news_num = "";
    public String shop_nick = "";
    public ArrayList<CasePerformancePicDTO> pics = new ArrayList<>();
}
