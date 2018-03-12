package com.poso2o.lechuan.bean.articledata;

import com.poso2o.lechuan.bean.goodsdata.Goods;

import java.io.Serializable;

/**
 * Created by mr zhang on 2017/10/23.
 */

public class ArticleData implements Serializable{
    public Long articles_id=0L;

    /** 类型:1=时尚 2=美食 3=健康 */
    public Integer articles_type =0;

    /** 标题 */
    public String title="";

    /** 作者 */
    public String author="";

    /** 简介 */
    public String digest="";

    /** 封面图 */
    public String pic="";

    /** 正文 */
    public String content="";

    /** 日期 */
    public Long build_time =0L;

    /** 阅读数 */
    public Long readnums =0L;

    /** 收藏数 */
    public Long collectnums =0L;

    /** 是否已收藏 1=已收藏,0=未收藏 */
    public int has_collect =0;

    /** 转发数 */
    public Long sharenums =0L;

    /** 发布状态，0=未发布，1=已发布 */
    public int articles_state =0;

    //本地添加字段 ：是否已添加广告
    public boolean isAd = false;

    //历史发布文章里面用到的字段
    public String cover_pic_url = "";

    //本地添加字段，广告的内容
    public String ad_content = "";

    //本地添加字段，广告的文字
    public String ad_des = "";

    //本地添加字段，广告的封面图本地路径
    public String pic_path = "";

    //本地字段，是否在发布文章队列中
    public boolean inLine = false;

    //本地字段，添加的商品
    public Goods goods ;

}
