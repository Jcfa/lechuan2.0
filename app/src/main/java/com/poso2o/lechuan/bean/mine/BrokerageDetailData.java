package com.poso2o.lechuan.bean.mine;

import com.poso2o.lechuan.bean.TotalBean;

import java.util.ArrayList;

/**
 * 佣金详情数据
 * Created by Administrator on 2017-12-02.
 */

public class BrokerageDetailData {
    public TotalBean total;
    public ArrayList<BrokerageDetailItemBean> list;

    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public ArrayList<BrokerageDetailItemBean> getList() {
        return list;
    }

    public void setList(ArrayList<BrokerageDetailItemBean> list) {
        this.list = list;
    }
}
