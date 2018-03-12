package com.poso2o.lechuan.bean.mine;

import com.poso2o.lechuan.bean.TotalBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-12-01.
 */

public class MerchantData {

    public TotalBean total;
    public ArrayList<MerchantItemBean> list;

    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public ArrayList<MerchantItemBean> getList() {
        return list;
    }

    public void setList(ArrayList<MerchantItemBean> list) {
        this.list = list;
    }
}
