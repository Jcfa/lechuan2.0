package com.poso2o.lechuan.bean.powerdata;

import java.io.Serializable;

/**
 * Created by mr zhang on 2017/12/22.
 *
 * 职位权限数据类
 */

public class LocalPowerData implements Serializable {
    /**
     *  权限名称
     */
    public String power_name = "";
    /**
     *  权限类型
     */
    public int power_type = 0;
    /**
     *  权限是否选中
     */
    public boolean power_on = false;
}
