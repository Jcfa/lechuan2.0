package com.poso2o.lechuan.bean.mine;

import com.poso2o.lechuan.bean.BaseBean;
import com.poso2o.lechuan.bean.TotalBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-12-01.
 */

public class PosterData {
    public TotalBean total;
    public ArrayList<PosterItemBean> list;

    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public ArrayList<PosterItemBean> getList() {
        return list;
    }

    public void setList(ArrayList<PosterItemBean> list) {
        this.list = list;
    }
}
