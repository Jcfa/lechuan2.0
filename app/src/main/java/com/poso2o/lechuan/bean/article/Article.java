package com.poso2o.lechuan.bean.article;

import java.io.Serializable;

/**
 * 文章
 * Created by Jaydon on 2017/12/1.
 */
public class Article implements Serializable, Cloneable {

    /**
     * 资讯ID
     */
    public String articles_id = "";

    /**
     * 类型:1=时尚 2=美食 3=健康 99=自编文章
     */
    public int articles_type = 0;

    /**
     * 标题
     */
    public String title = "";

    /**
     * 作者
     */
    public String author = "";

    /**
     * 简介
     */
    public String digest = "";

    /**
     * 内容
     */
    public String content = "";

    /**
     * 封面
     */
    public String pic = "";
    public String cover_pic_url = "";

    /**
     * 日期
     */
    public long builde_time = 0L;

    /**
     * 阅读数
     */
    public long readnums = 0L;

    /**
     * 收藏数
     */
    public long collectnums = 0L;

    /**
     * 是否已收藏 1=已收藏,0=未收藏
     */
    public int has_collect = 0;

    /**
     * 转发数
     */
    public long sharenums = 0L;

    /**
     * 资讯详情URL
     */
    public String articles_url = "";

    /**
     * 是否收藏
     * @return
     */
    public boolean isCollect() {
        return has_collect == 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Article)) return false;
        Article article = (Article) obj;
        return articles_id == article.articles_id;
    }

    //添加的广告ID，在添加草稿的时候才会一起提交到服务器
    public String ad_id = "";
    //添加的广告名称，在添加草稿的时候才会一起提交到服务器
    public String ad_name = "";
}
