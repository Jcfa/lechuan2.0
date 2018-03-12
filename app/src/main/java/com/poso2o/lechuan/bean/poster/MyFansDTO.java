package com.poso2o.lechuan.bean.poster;

import java.io.Serializable;

/**
 * 我的粉丝
 *
 * @author Luo
 * @version 1.0.0 2017-12-02
 */
public class MyFansDTO implements Serializable {
    public boolean checked = false;
    /**
     * 粉丝的用户ID
     */
    public Long uid = 0L;

    /**
     * 粉丝的名称
     */
    public String nick = "";

    /**
     * 粉丝的LOGO
     */
    public String logo = "";

    /**
     * 0=没有相互关注，1=已相互关注
     */
    public Integer mutual_flow = 0;

    /**
     * 粉丝关注时间
     */
    public Long build_time = 0L;

    /**
     * 0=商家,1=分销商,2=商家+分销商
     */
    public int user_type = 2;

    /**
     * 粉丝发布文章数
     */
    public int news_number = 0;
    /**
     * 粉丝的粉丝数量
     */
    public int fans_number = 0;

    /**
     * 用户简介
     */
    public String remark = "";

    /**
     * 是否为我的分销商,0=不是我的分销商，1=已是我的分销商
     */
    public int has_distributor = 0;

}
