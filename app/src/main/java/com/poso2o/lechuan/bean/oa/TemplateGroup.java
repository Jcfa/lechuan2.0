package com.poso2o.lechuan.bean.oa;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/2/7.
 * 模板组
 */

public class TemplateGroup implements Serializable {
    public String has_buy = "";
    public ArrayList<TemplateBean> templates = new ArrayList<>();
    public String service_type = "";
    public String group_name = "";
    public String group_id = "";
    public String amount = "";
    public String template_service_day = "";
    public String remark = "";
    public String unit = "";
}
