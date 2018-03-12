package com.poso2o.lechuan.bean.version;

/**
 * Created by Administrator on 2017-12-21.
 */

public class VersionBean {
    // 下载地址
    public String downloadurl = "";
    // 版本名称
    public String version = "";
    // 版本号
    public String number = "-1";
    //是否强制更新
    public String update = "0";
    //更新内容
    public String des = "";

    public int getNumber() {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getUpdate() {
        try {
            return Integer.parseInt(update);
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
