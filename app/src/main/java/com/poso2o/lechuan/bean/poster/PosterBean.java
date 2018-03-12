package com.poso2o.lechuan.bean.poster;

import java.io.Serializable;

/**
 * 广告列表信息
 * Created by Luo on 2017-11-29.
 */

public class PosterBean implements Serializable {
    private String type = "";//0：佣金、1：红包
    private String follow = "";//0：未关注、1：关注
    private String url = ""; //图片地址

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
