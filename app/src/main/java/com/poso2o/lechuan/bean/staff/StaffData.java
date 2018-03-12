package com.poso2o.lechuan.bean.staff;

import java.io.Serializable;

/**
 * Created by mr zhang on 2017/8/16.
 */

public class StaffData implements Serializable{
//    public String build_czy;
//    public String build_czy_name;
//    public long build_time;
//    public String modify_czy;
//    public String modify_czy_name;
//    public long modify_time;
//    public String shop_branch_id;
//    public String shop_branch_name;
//    public String shop_id;
//    public String staff_account;
//    public String staff_id;
//    public String staff_mobile;
//    public String staff_name;
//    public String staff_password;
//    public String staff_power_id;
//    public String staff_power_name;
//    public String staff_salary;
//    public double staff_target_assignments;
//    public int staff_type;

    //旧服装版员工数据结构
    public String password = "";
    public String czy = "";
    public String cdate = "";
    public String assignments = "";
    public String positionid = "";
    public String shopname = "";
    public String positionname = "";
    public String mobile = "";
    public String shopid = "";
    public String zw = "";
    public String realname = "";
    //本地数据，编辑时是否修改了工号
    public boolean edit_no = false;
}
