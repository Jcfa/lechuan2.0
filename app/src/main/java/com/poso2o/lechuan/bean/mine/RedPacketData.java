package com.poso2o.lechuan.bean.mine;

import com.poso2o.lechuan.bean.TotalBean;

import java.util.List;

/**
 * Created by Administrator on 2017-12-01.
 */

public class RedPacketData {
    public TotalBean total;
    public List<RedPacketItemBean> list;

    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public List<RedPacketItemBean> getList() {
        return list;
    }

    public void setList(List<RedPacketItemBean> list) {
        this.list = list;
    }
}
