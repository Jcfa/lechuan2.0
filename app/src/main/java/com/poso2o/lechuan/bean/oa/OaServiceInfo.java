package com.poso2o.lechuan.bean.oa;

import java.io.Serializable;

/**
 * Created by mr zhang on 2018/2/10.
 *
 * 已购买的公众号服务信息
 */

public class OaServiceInfo implements Serializable {
    public String build_time = "0";
    public String new_service_id = "0";
    public int news_operation_mode = 1;
    public int news_send_num = 1;
    public String news_service_date = "0";
    public int news_service_days = 0;
    public OaService.Item systemService;
    public int timing_release_times;
    public String uid;
}
