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
     * lists : [{"order_date":"2016-12-02 19:24:51.0","payment_amount":"3917.55","order_id":"161202192490529","order_num":"6"},{"order_date":"2016-11-23 20:50:01.0","payment_amount":"3229.00","order_id":"161123205017205","order_num":"6"},{"order_date":"2016-11-23 20:46:39.0","payment_amount":"3167.00","order_id":"161123204682894","order_num":"5"},{"order_date":"2016-10-09 10:17:07.0","payment_amount":"2814.70","order_id":"161009101784121","order_num":"4"},{"order_date":"2016-11-30 09:59:43.0","payment_amount":"2309.60","order_id":"161130095987740","order_num":"3"},{"order_date":"2016-10-19 09:46:29.0","payment_amount":"1903.00","order_id":"161019094690014","order_num":"3"},{"order_date":"2016-10-14 12:19:57.0","payment_amount":"1537.38","order_id":"161014121944784","order_num":"2"},{"order_date":"2016-09-28 10:34:41.0","payment_amount":"1473.50","order_id":"160928103442714","order_num":"3"},{"order_date":"2016-09-28 10:39:59.0","payment_amount":"1473.50","order_id":"160928104065421","order_num":"3"},{"order_date":"2016-11-01 13:37:56.0","payment_amount":"1409.80","order_id":"161101133759751","order_num":"2"},{"order_date":"2016-09-30 00:41:19.0","payment_amount":"1409.80","order_id":"160930004191888","order_num":"2"},{"order_date":"2016-11-24 16:35:15.0","payment_amount":"1409.10","order_id":"161124163593849","order_num":"2"},{"order_date":"2016-11-14 21:04:57.0","payment_amount":"1407.00","order_id":"161114210465714","order_num":"2"},{"order_date":"2016-11-10 23:02:38.0","payment_amount":"1407.00","order_id":"161110230270459","order_num":"2"},{"order_date":"2016-10-15 11:27:09.0","payment_amount":"1404.90","order_id":"161015112767206","order_num":"2"},{"order_date":"2016-10-14 16:31:38.0","payment_amount":"1270.71","order_id":"161014163126564","order_num":"2"},{"order_date":"2016-10-13 15:39:18.0","payment_amount":"1270.08","order_id":"161013153979260","order_num":"2"},{"order_date":"2016-11-08 11:08:54.0","payment_amount":"1265.67","order_id":"161108110897026","order_num":"2"},{"order_date":"2016-10-08 16:29:10.0","payment_amount":"1198.33","order_id":"161008162954381","order_num":"2"},{"order_date":"2016-09-28 09:58:17.0","payment_amount":"882.40","order_id":"160928095848482","order_num":"2"},{"order_date":"2017-03-20 19:19:01.0","payment_amount":"878.00","order_id":"170320191951107","order_num":"1"},{"order_date":"2016-10-15 09:10:36.0","payment_amount":"706.30","order_id":"161015091076183","order_num":"1"},{"order_date":"2016-10-12 14:13:10.0","payment_amount":"704.20","order_id":"161012141376436","order_num":"1"},{"order_date":"2016-10-15 12:28:06.0","payment_amount":"704.20","order_id":"161015122848243","order_num":"1"},{"order_date":"2016-10-17 14:11:43.0","payment_amount":"703.50","order_id":"161017141197315","order_num":"1"},{"order_date":"2016-10-17 09:54:09.0","payment_amount":"702.10","order_id":"161017095361573","order_num":"1"},{"order_date":"2016-11-23 20:53:45.0","payment_amount":"631.00","order_id":"161123205399862","order_num":"1"},{"order_date":"2016-10-17 14:11:55.0","payment_amount":"-703.50","order_id":"TH161017141197315","order_num":"-1"},{"order_date":"2016-10-15 12:33:19.0","payment_amount":"-704.20","order_id":"TH161015122848243","order_num":"-1"},{"order_date":"2016-11-10 23:03:33.0","payment_amount":"-1407.00","order_id":"TH161110230270459","order_num":"-2"}]
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
