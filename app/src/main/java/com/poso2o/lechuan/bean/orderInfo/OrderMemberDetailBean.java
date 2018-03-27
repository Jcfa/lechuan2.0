package com.poso2o.lechuan.bean.orderInfo;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/17 0017.
 */

public class OrderMemberDetailBean {

    /**
     * uid : 13400000001
     * provincename :
     * nick : 001
     * areaname :
     * address :
     * userLogo :
     * groupname : 金牌会员
     * cityname :
     * lists : [{"order_date":"2016-12-02 19:24:51.0","payment_amount":"3917.55","order_id":"
     * 161202192490529","order_num":"6"},{"order_date":"2016-11-23 20:50:01.0","payment_amount":"3229.00","
     * order_id":"161123205017205","order_num":"6"},{"order_date":"2016-11-23 20:46:39.0",
     * "payment_amount":"3167.00","order_id":"161123204682894","order_num":"5"},{"order_date
     * mobile : 13400000001
     */

    private String uid;
    private String provincename;
    private String nick;
    private String areaname;
    private String address;
    private String userLogo;
    private String groupname;
    private String cityname;
    private String mobile;
    private List<ListsBean> lists;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public String getGroupname() {
        if (groupname == null || groupname.equals(""))
            return "普通会员";
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<ListsBean> getLists() {
        return lists;
    }

    public void setLists(List<ListsBean> lists) {
        this.lists = lists;
    }

    public static class ListsBean {
        /**
         * order_date : 2016-12-02 19:24:51.0
         * payment_amount : 3917.55
         * order_id : 161202192490529
         * order_num : 6
         */

        private String order_date;
        private String payment_amount;
        private String order_id;
        private String order_num;

        public String getOrder_date() {
            return order_date;
        }

        public void setOrder_date(String order_date) {
            this.order_date = order_date;
        }

        public String getPayment_amount() {
            return payment_amount;
        }

        public void setPayment_amount(String payment_amount) {
            this.payment_amount = payment_amount;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }
    }
}
