package com.poso2o.lechuan.bean.event;

/**
 * Created by Administrator on 2017-12-25.
 */

public class EventBean {
    public static final int COMMENT_ID = 1;//评论
    public static final int REDBAG_STATUS_ID = 2;//红包状态
    public static final int PRAISE_ID = 3;//赞
    //删除目录
    public static final int CODE_CATALOG_DEL = 1002;
    //添加目录
    public static final int CODE_CATALOG_ADD = 1001;
    //更新目录
    public static final int CODE_CATALOG_UPDATE = 1003;
    //更新微单订单页数据
    public static final int CODE_V_ORDER_UPDATE = 1004;
    //更新发布文章列表
    public static final int CODE_PUBLISH_UPDATE = 1005;
    //点击选文，跳转到资讯收藏列表
    public static final int CODE_TO_COLLECT_LIST = 1006;
    //发布文章数变化
    public static final int CODE_ART_NUM_CHANGE = 1007;
    public int code = 0;
    public int id = -1;

    public EventBean(int code) {
        this.code = code;
    }

    public EventBean(int code, int id) {
        this.code = code;
        this.id = id;
    }
}
