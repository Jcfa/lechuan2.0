package com.poso2o.lechuan.bean.member;

import java.io.Serializable;

/**
 * Created by mr zhang on 2017/8/28.
 */

public class MemberOrder implements Serializable {
    //新服装版数据
    public String finish_time;
    public double order_paid_amount;
    public int order_total_munber;
    public String sale_order_id;

    //兼容旧服装版数据
    public String order_date = "";
    public String order_id = "";
    public String order_num = "";
    public String payment_amount = "";
}
