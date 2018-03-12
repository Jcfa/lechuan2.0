package com.poso2o.lechuan.bean.poster;

import java.io.Serializable;

/**
 * 我关注的人
 *
 * @author Luo
 * @version 1.0.0 2017-12-02
 */
public class MyFlowsDTO implements Serializable {

    /**
     * 关注的用户ID
     */
    public Long flow_uid = 0L;

    /**
     * 关注的名称
     */
    public String flow_nick = "";

    /**
     * 关注的LOGO
     */
    public String flow_logo = "";

    /**
     * 0=没有相互关注，1=已相互关注
     */
    public int mutual_flow = 0;

    /**
     * 关注时间
     */
    public Long build_time = 0L;

    /**
     * 0=商家,1=分销商,2=商家+分销商
     */
    public int flow_user_type = -1;

    public int has_flow = 0;

    /**
     * 用户简介
     */
    public String remark = "";


}
