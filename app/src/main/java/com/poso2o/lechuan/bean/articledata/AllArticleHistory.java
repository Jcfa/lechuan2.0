package com.poso2o.lechuan.bean.articledata;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/10/26.
 */

public class AllArticleHistory {

    public Total total;
    public ArrayList<ArticleHistory> list = new ArrayList<>();

    public class Total{
        public int countPerPage;
        public int currPage;
        public int pages;
        public int rowcount;
    }
}
