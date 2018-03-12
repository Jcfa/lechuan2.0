package com.poso2o.lechuan.bean.mine;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-12-05.
 */

public class InvitationBean implements Serializable {
    public static final String INVITATION_BIND_CODE = "10";//邀请分销
    public static final String INVITATION_UNBIND_CODE = "11";//解除分销
    public static final String INVITATION_UNBIND_AGREE_CODE = "12";//解除分销成功

    public String code="";
    public String nick = "";
    public String uid = "";
    public String logo = "";
    public String url = "";
    public double commission_discount=0;
    public double commission_rate=0;
}
