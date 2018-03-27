package com.poso2o.lechuan.bean.mine;

/**
 * 用户信息详情
 * <p>
 * Created by Administrator on 2017-12-04.
 */

public class UserInfoBean {
    public int send_news_number = 0;//我的发布数量
    public int distributor_number = 0;
    public float commission_amount = 0;
    public int has_webshop = 0;
    public String openid = "";
    public int share_news_number = 0;
    public float commission_discount = 0;
    public String build_time = "";
    public String mobile = "";
    public String remark = "";
    public String has_flow = "";
    public String token = "";
    public String nick = "";
    public String uid = "";
    public int user_type = 0;
    public int order_appraise_number=0;
    public int flowme_number = 0;//关注我的数量
    public int collect_news_number = 0;
    public int myfans_number = 0;//我的粉丝数量
    public String logo = "";
    public String background_logo = "";//相册封面图
    public float commission_rate = 0;
    public float red_envelopes_amount = 0;
    public int has_shop = 0;
    public int has_commission = -1;//是否设置了佣金，0未设，1已设
    public int buy_service_id=0;//购买的公众号服务ID



    public String getHas_webshop() {
        return has_webshop + "";
    }

    public void setHas_webshop(String has_webshop) {
        this.has_webshop = Integer.parseInt(has_webshop);
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setHas_webshop(int has_webshop) {
        this.has_webshop = has_webshop;
    }

    public void setHas_shop(int has_shop) {
        this.has_shop = has_shop;
    }

    public int getOrder_appraise_number() {
        return order_appraise_number;
    }

    public void setOrder_appraise_number(int order_appraise_number) {
        this.order_appraise_number = order_appraise_number;
    }

    public String getBuild_time() {
        return build_time;
    }

    public void setBuild_time(String build_time) {
        this.build_time = build_time;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHas_flow() {
        return has_flow;
    }

    public void setHas_flow(String has_flow) {
        this.has_flow = has_flow;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public float getCommission_rate() {
        return commission_rate;
    }

    public void setCommission_rate(float commission_rate) {
        this.commission_rate = commission_rate;
    }

    public String getHas_shop() {
        return has_shop + "";
    }

    public void setHas_shop(String has_shop) {
        this.has_shop = Integer.parseInt(has_shop);
    }

    public int getSend_news_number() {
        return send_news_number;
    }

    public void setSend_news_number(int send_news_number) {
        this.send_news_number = send_news_number;
    }

    public int getDistributor_number() {
        return distributor_number;
    }

    public void setDistributor_number(int distributor_number) {
        this.distributor_number = distributor_number;
    }


    public int getShare_news_number() {
        return share_news_number;
    }

    public void setShare_news_number(int share_news_number) {
        this.share_news_number = share_news_number;
    }


    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getFlowme_number() {
        return flowme_number;
    }

    public void setFlowme_number(int flowme_number) {
        this.flowme_number = flowme_number;
    }

    public int getCollect_news_number() {
        return collect_news_number;
    }

    public void setCollect_news_number(int collect_news_number) {
        this.collect_news_number = collect_news_number;
    }

    public int getMyfans_number() {
        return myfans_number;
    }

    public void setMyfans_number(int myfans_number) {
        this.myfans_number = myfans_number;
    }


    public int getHas_commission() {
        return has_commission;
    }

    public void setHas_commission(int has_commission) {
        this.has_commission = has_commission;
    }

    public float getCommission_amount() {
        return commission_amount;
    }

    public void setCommission_amount(float commission_amount) {
        this.commission_amount = commission_amount;
    }

    public float getCommission_discount() {
        return commission_discount;
    }

    public void setCommission_discount(float commission_discount) {
        this.commission_discount = commission_discount;
    }

    public float getRed_envelopes_amount() {
        return red_envelopes_amount;
    }

    public void setRed_envelopes_amount(float red_envelopes_amount) {
        this.red_envelopes_amount = red_envelopes_amount;
    }
}
