package com.poso2o.lechuan.bean.mine;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-12-07.
 */

public class AcceptDetailData {
    public RedpacketTotalBean total=new RedpacketTotalBean();
    public ArrayList<AcceptDetailItemBean> redenvelopes=new ArrayList<>();

    public RedpacketTotalBean getTotal() {
        return total;
    }

    public void setTotal(RedpacketTotalBean total) {
        this.total = total;
    }

    public ArrayList<AcceptDetailItemBean> getList() {
        return redenvelopes;
    }

    public void setList(ArrayList<AcceptDetailItemBean> list) {
        this.redenvelopes = list;
    }
}
