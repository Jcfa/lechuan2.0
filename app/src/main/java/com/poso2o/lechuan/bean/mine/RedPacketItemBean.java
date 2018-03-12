package com.poso2o.lechuan.bean.mine;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-11-30.
 */

public class RedPacketItemBean implements Serializable {
    public static int INCOME_TYPE = 2;//收入类型
    public static int EXPEND_TYPE = 3;//支出类型
    private String news_img = "";
    private String news_title = "";
    private String news_id = "";
    private int amount_type = 0;//金额类型
    private long build_time = 0;
    private double amount = 0;

    public String getNews_img() {
        return news_img;
    }

    public void setNews_img(String news_img) {
        this.news_img = news_img;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public long getBuild_time() {
        return build_time;
    }

    public void setBuild_time(long build_time) {
        this.build_time = build_time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getAmount_type() {
        return amount_type;
    }

    public void setAmount_type(int amount_type) {
        this.amount_type = amount_type;
    }
}
