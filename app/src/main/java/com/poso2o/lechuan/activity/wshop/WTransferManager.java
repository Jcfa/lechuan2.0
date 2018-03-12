package com.poso2o.lechuan.activity.wshop;

import com.poso2o.lechuan.base.BaseManager;

/**
 * Created by mr zhang on 2017/12/12.
 */

public class WTransferManager extends BaseManager {

    private static volatile WTransferManager wTransferManager;
    public static WTransferManager getwTransferManager(){
        if (wTransferManager == null){
            synchronized (WTransferManager.class){
                if (wTransferManager == null)wTransferManager = new WTransferManager();
            }
        }
        return wTransferManager;
    }

}
