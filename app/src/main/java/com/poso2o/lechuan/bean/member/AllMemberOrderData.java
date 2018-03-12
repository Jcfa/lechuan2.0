package com.poso2o.lechuan.bean.member;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/28.
 */

public class AllMemberOrderData implements Serializable {
    //新服装版数据
    public ArrayList<MemberOrder> list = new ArrayList<>();

    //兼容旧服装版数据
    public String address = "";
    public String areaname = "";
    public String cityname = "";
    public String groupname = "";
    public String mobile = "";
    public String nick = "";
    public String provincename = "";
    public String uid = "";
    public String userLogo = "";
    public ArrayList<MemberOrder> lists = new ArrayList<>();
}
