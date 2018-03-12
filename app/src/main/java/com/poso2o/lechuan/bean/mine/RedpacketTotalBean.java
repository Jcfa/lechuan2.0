package com.poso2o.lechuan.bean.mine;

/**
 * Created by Administrator on 2017-12-07.
 */

public class RedpacketTotalBean {
    private int red_envelopes_number = 0;//红包数量
    private int has_red_envelopes = 0;
    private int red_envelopes_surplus_number = 0;//红包剩余数量
    private String news_img = "";
    private String news_title = "";
    private String news_id = "";
    private double red_envelopes_amount = 0;//红包总金额

    public int getRed_envelopes_number() {
        return red_envelopes_number;
    }

    public void setRed_envelopes_number(int red_envelopes_number) {
        this.red_envelopes_number = red_envelopes_number;
    }

    public int getHas_red_envelopes() {
        return has_red_envelopes;
    }

    public void setHas_red_envelopes(int has_red_envelopes) {
        this.has_red_envelopes = has_red_envelopes;
    }

    public int getRed_envelopes_surplus_number() {
        return red_envelopes_surplus_number;
    }

    public void setRed_envelopes_surplus_number(int red_envelopes_surplus_number) {
        this.red_envelopes_surplus_number = red_envelopes_surplus_number;
    }

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

    public double getRed_envelopes_amount() {
        return red_envelopes_amount;
    }

    public void setRed_envelopes_amount(double red_envelopes_amount) {
        this.red_envelopes_amount = red_envelopes_amount;
    }
}
