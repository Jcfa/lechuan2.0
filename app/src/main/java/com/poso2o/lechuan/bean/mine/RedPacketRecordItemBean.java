package com.poso2o.lechuan.bean.mine;

/**
 * Created by Administrator on 2017-12-04.
 */

public class RedPacketRecordItemBean {
    private long build_time = 0;
    private int amount_type = 0;
    private double amount = 0;
    private String amount_id = "";
    private String news_id = "";
    private String news_img = "";
    private String news_title = "";
    private String logo = "";
    private String nick = "";

    public long getBuild_time() {
        return build_time;
    }

    public void setBuild_time(long build_time) {
        this.build_time = build_time;
    }

    public int getAmount_type() {
        return amount_type;
    }

    public void setAmount_type(int amount_type) {
        this.amount_type = amount_type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAmount_id() {
        return amount_id;
    }

    public void setAmount_id(String amount_id) {
        this.amount_id = amount_id;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
