package com.poso2o.lechuan.manager.wshopmanager;

import com.poso2o.lechuan.base.BaseManager;

/**
 * Created by mr zhang on 2017/12/12.
 *
 * 微店员工管理，暂时没接口，待定
 */

public class WStaffManager extends BaseManager {

    private static volatile WStaffManager wStaffManager;
    public static WStaffManager getwStaffManager(){
        if (wStaffManager == null){
            synchronized (WStaffManager.class){
                wStaffManager = new WStaffManager();
            }
        }
        return wStaffManager;
    }
}
