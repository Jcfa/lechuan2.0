package com.poso2o.lechuan.bean.event;

/**
 * Created by Administrator on 2017-12-09.
 */

public class PushMessageEvent {
    /**
     * 分销商【分销邀请】
     */
    public static final String MIPUSH_DISTRIBUTOR_SHARE = "10";

    /**
     * 分销商【申请解绑】
     */
    public static final String MIPUSH_BINDING_APPLY = "11";

    /**
     * 分销商【同意解绑】
     */
    public static final String MIPUSH_BINDING_SUCCESS = "12";
    public final String data;
    public final String code;

    public PushMessageEvent(String data, String code) {
        this.data = data;
        this.code = code;
    }
}
