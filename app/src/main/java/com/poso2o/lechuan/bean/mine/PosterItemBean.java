package com.poso2o.lechuan.bean.mine;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-11-30.
 */

public class PosterItemBean implements Serializable {
    private String url = "";
    private String news_id = "";
    private String news_img = "";
    private String news_title = "";//文章标题
    private String news_describe = "";//文章内容
    private String articles = "";
    private int has_goods = 0;
    private double goods_price = 0;
    private String goods_img = "";
    private String goods_url = "";
    private double goods_commission_rate = 0;
    private double goods_commission_amount = 0;
    private int red_envelopes_surplus_number = 0;
    private int like_num = 0;
    private int comment_num = 0;
    private int red_envelopes_number = 0;
    private int share_num = 0;
    private int read_num = 0;
    private int collect_num = 0;
    private int has_red_envelopes = 0;
    private int has_commission = 0;
    private int has_collect = 0;
    private String goods_id = "";
    private String goods_name = "";
    private double red_envelopes_amount = 0;
    private String nick = "";
    private String logo = "";
    private String user_type = "";
    private long build_time = 0;
    private String uid = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getNews_describe() {
        return news_describe;
    }

    public void setNews_describe(String news_describe) {
        this.news_describe = news_describe;
    }

    public String getArticles() {
        return articles;
    }

    public void setArticles(String articles) {
        this.articles = articles;
    }


    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public String getGoods_url() {
        return goods_url;
    }

    public void setGoods_url(String goods_url) {
        this.goods_url = goods_url;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public long getBuild_time() {
        return build_time;
    }

    public void setBuild_time(long build_time) {
        this.build_time = build_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getHas_goods() {
        return has_goods;
    }

    public void setHas_goods(int has_goods) {
        this.has_goods = has_goods;
    }

    public double getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(double goods_price) {
        this.goods_price = goods_price;
    }

    public double getGoods_commission_rate() {
        return goods_commission_rate;
    }

    public void setGoods_commission_rate(double goods_commission_rate) {
        this.goods_commission_rate = goods_commission_rate;
    }

    public int getRed_envelopes_surplus_number() {
        return red_envelopes_surplus_number;
    }

    public void setRed_envelopes_surplus_number(int red_envelopes_surplus_number) {
        this.red_envelopes_surplus_number = red_envelopes_surplus_number;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public int getRed_envelopes_number() {
        return red_envelopes_number;
    }

    public void setRed_envelopes_number(int red_envelopes_number) {
        this.red_envelopes_number = red_envelopes_number;
    }

    public int getShare_num() {
        return share_num;
    }

    public void setShare_num(int share_num) {
        this.share_num = share_num;
    }

    public int getRead_num() {
        return read_num;
    }

    public void setRead_num(int read_num) {
        this.read_num = read_num;
    }

    public int getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(int collect_num) {
        this.collect_num = collect_num;
    }

    public int getHas_red_envelopes() {
        return has_red_envelopes;
    }

    public void setHas_red_envelopes(int has_red_envelopes) {
        this.has_red_envelopes = has_red_envelopes;
    }

    public int getHas_commission() {
        return has_commission;
    }

    public void setHas_commission(int has_commission) {
        this.has_commission = has_commission;
    }

    public int getHas_collect() {
        return has_collect;
    }

    public void setHas_collect(int has_collect) {
        this.has_collect = has_collect;
    }

    public double getRed_envelopes_amount() {
        return red_envelopes_amount;
    }

    public void setRed_envelopes_amount(double red_envelopes_amount) {
        this.red_envelopes_amount = red_envelopes_amount;
    }

    public double getGoods_commission_amount() {
        return goods_commission_amount;
    }

    public void setGoods_commission_amount(double goods_commission_amount) {
        this.goods_commission_amount = goods_commission_amount;
    }
}
