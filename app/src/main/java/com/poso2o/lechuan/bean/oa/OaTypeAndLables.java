package com.poso2o.lechuan.bean.oa;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/3/16.
 *
 * 文章类型和标签
 */

public class OaTypeAndLables {

    public Types types;
    public Lables labels;

    public class Types{
        public ArrayList<OaTypeBean> list = new ArrayList<>();
    }

    public class Lables{
        public ArrayList<OaLablesBean> list = new ArrayList<>();
    }
}
