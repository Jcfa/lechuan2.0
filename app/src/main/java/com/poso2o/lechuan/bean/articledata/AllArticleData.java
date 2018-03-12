package com.poso2o.lechuan.bean.articledata;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/10/23.
 */

public class AllArticleData {

    public ArticleTotal total;

    public ArrayList<ArticleData> list = new ArrayList<>();

    public class ArticleTotal{
        public int countPerPage;
        public int currPage;
        public int pages;
        public int rowcount;
    }
}
