package com.poso2o.lechuan.bean.mine;

import com.poso2o.lechuan.bean.TotalBean;
import com.poso2o.lechuan.bean.goodsdata.Goods;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-12-06.
 */

public class GoodsData {
    public TotalBean total;
    public ArrayList<Goods> list;

    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public ArrayList<Goods> getList() {
        return list;
    }

    public void setList(ArrayList<Goods> list) {
        this.list = list;
    }
}
