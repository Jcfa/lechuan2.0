package com.poso2o.lechuan.bean.news;

import java.io.Serializable;

/**
 * 资讯
 * Created by Jaydon on 2017/12/5.
 */
public class News implements Serializable, Cloneable {

    /**
     * 封面图
     */
    public String news_img = "";

    /**
     * 晒图图片集合
     */
    public String news_imgs = "";

    /**
     * 封面图本地路径
     */
    public String news_img_path = "";

    /**
     * 标题
     */
    public String news_title = "";

    /**
     * 描述
     */
    public String news_describe = "";

    /**
     * 正文
     */
    public String articles = "";

    /**
     * 0=没有推广商品,1=有推广商品
     */
    public int has_goods = 0;

    /**
     * 佣金商品ID
     */
    public String goods_id = "";

    /**
     * 佣金商品名称
     */
    public String goods_name = "";

    /**
     * 销售价
     */
    public double goods_price = 0.00;

    /**
     * 价格区间
     */
    public String goods_price_section = "";

    /**
     * 商品IMG
     */
    public String goods_img = "";

    /**
     * 商品URL
     */
    public String goods_url = "";

    /**
     * 佣金比例%
     */
    public double goods_commission_rate = 100.00;

    /**
     * 所得佣金
     */
    public double goods_commission_amount = 0.00;

    /**
     * 0=没有红包,1=有红包
     */
    public int has_red_envelopes = 0;

    /**
     * 红包总金额
     */
    public double red_envelopes_amount = 0.00;

    /**
     * 红包总个数
     */
    public int red_envelopes_number = 0;

    /**
     * 是否随机红包  0=随机金额，1=固定金额
     */
    public int has_random_red = 0;

    /**
     * 资讯URL
     */
    public String url = "";

    @Override
    public News clone() {
        try {
            return (News) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
