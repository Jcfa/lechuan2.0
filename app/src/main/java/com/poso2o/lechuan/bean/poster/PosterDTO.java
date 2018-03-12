package com.poso2o.lechuan.bean.poster;

import com.poso2o.lechuan.util.NumberUtils;

import java.io.Serializable;

/**
 * 文章发布(T_NEWS)
 *
 * @author Luo
 * @version 1.0.0 2017-12-02
 */
public class PosterDTO implements Serializable {

    /**
     * 文章ID
     */
    public Long news_id = 0L;

    /**
     * 账号id
     */
    public Long uid = 0L;

    /**
     * 0=商家,1=分销商,2=商家+分销商
     */
    public int user_type = 0;

    /**
     * 作者名称
     */
    public String nick = "";

    /**
     * 作者LOGO
     */
    public String logo = "";

    /**
     * 封面图
     */
    public String news_img = "";

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
    public Long goods_id = 0L;

    /**
     * 佣金商品名称
     */
    public String goods_name = "";

    /**
     * 销售价
     */
    public Double goods_price = 0.00;

    /**
     * 商品IMG
     */
    public String goods_img = "";

    /**
     * 商品URL
     */
    public String goods_url = "";

    /**
     * 所得佣金
     */
    public Double goods_commission_amount = 0.00;

    /**
     * 佣金比例%
     */
    public Double goods_commission_rate = 0.00;

    /**
     * 0=没有红包,1=有红包
     */
    public int has_red_envelopes = 0;

    /**
     * 红包总金额 (单位分)
     */
    public double red_envelopes_amount = 0;

    /**
     * 红包总个数
     */
    public int red_envelopes_number = 0;

    /**
     * 红包剩余个数
     */
    public int red_envelopes_surplus_number = 0;

    /**
     * 阅读数
     */
    public int read_num = 0;

    /**
     * 收藏数
     */
    public int collect_num = 0;

    /**
     * 转发数
     */
    public int share_num = 0;

    /**
     * 点赞数
     */
    public int like_num = 0;

    /**
     * 评论数
     */
    public int comment_num = 0;

    /**
     * 建立时间
     */
    public Long build_time = 0L;

    /**
     * 资讯URL
     */
    public String url = "";

    /**
     * 是否已收藏 1=已收藏,0=未收藏
     */
    public int has_collect = 0;

    /**
     * 是否已点赞  1=已点赞 ,0=未点赞
     */
    public int has_like = 0;

    /**
     * 0=没有佣金,1=有佣金
     */
    public int has_commission = 0;

    /**
     * 是否已关注  1=已关注 ,0=未关注
     */
    public int has_flow = 0;

    /**
     * 是否已抢红包 1=已抢红包,0=未抢红包
     */
    public int has_myget_red_envelopes = 0;

    /**
     * 获取剩余金额
     *
     * @return
     */
    public String getSurplusAmount() {
        double sA = red_envelopes_amount / red_envelopes_number * red_envelopes_surplus_number;
        return NumberUtils.format2(sA);
    }
}
