package com.poso2o.lechuan.bean.login;

import com.poso2o.lechuan.bean.BaseBean;

/**
 * Created by Administrator on 2017-11-27.
 */

public class LoginBean {
    //    public UserData data;
//
//    public UserData getData() {
//        return data;
//    }
//
//    public void setData(UserData data) {
//        this.data = data;
//    }
//
//    public class UserData {
    public String nick = "";
    public String uid = "";
    public String account = "";
    public String password = "";
    public String remark = "";//用户简介
    public int user_type = -1;//用户类型，0=商家,1=分销商,2=商家+分销商
    public String lechuan_service_date = "0";//乐传服务到期时间
    public int lechuan_service_days = 0;//乐传服务剩余天数
    public int has_lechuan_try = 0;//是否开通过乐传服务试用
    public String mobile = "";
    public int has_shop = 0;//是否有实体店，0=无，1=有
    public int has_webshop = 0;//是否有微店，0=无，1=有
    public String logo = "";
    public String background_logo = "";//相册封面图
    public String token = "";
    public String openid = "";//openId微信绑定标识
    public int has_shop_verify = 0;//微店认证状态,1=未认证，2=申请认证，3=认证通过，4=认证不通过
    public String shop_bank_binding_url = "";//绑定收款账号的二维码
//    }
}
