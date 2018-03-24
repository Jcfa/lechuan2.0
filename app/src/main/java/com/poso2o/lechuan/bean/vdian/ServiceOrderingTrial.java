package com.poso2o.lechuan.bean.vdian;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/15 0015.
 */

public class ServiceOrderingTrial implements Serializable {
    public double original_amount;//原价
    public double amount;//应补
    public double deduction_amount;//应扣减
    public String remark;//说明
    public int days;//天数
    public int service_id;//服务的id
    public String service_name;//服务的名字
    public int service_type;//服务的序号
    public String unit;//年
}
