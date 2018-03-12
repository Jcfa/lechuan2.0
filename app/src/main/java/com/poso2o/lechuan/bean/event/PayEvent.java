package com.poso2o.lechuan.bean.event;

/**
 * 支付结果事件
 * Created by Administrator on 2017-12-15.
 */

public class PayEvent {
    public static final int SUCCESS = 1;
    public static final int WEIXIN_TYPE = 1;//微信支付
    public static final int ALIPAY_TYPE = 2;//支付宝支付

    public PayEvent(int code, int payType) {
        this.code = code;
        this.payType = payType;
    }

    public int code = 0;//0失败，1成功
    public int payType = 0;//1微信，2支付宝
}
