package com.poso2o.lechuan.bean.oa;

import com.poso2o.lechuan.bean.article.Article;

import java.io.Serializable;

/**
 * Created by mr zhang on 2018/3/12.
 *
 * 稿件箱实体
 */

public class RenewalsBean implements Serializable {

    //原文章
    public Article articles = new Article();
    public String authorizer_appid = "";
    public String authorizer_media_id = "";
    public String build_time = "";
    public String news_id = "";
    public String sendflag = "";
    public String shop_id = "";
}
