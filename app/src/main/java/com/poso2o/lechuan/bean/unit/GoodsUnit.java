package com.poso2o.lechuan.bean.unit;

import com.poso2o.lechuan.util.TextUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-04-24.
 */

public class GoodsUnit implements Serializable, Cloneable {
    public String unit_id = "";//单位id
    public String unit_name = "";//单位名称

    public String shop_id = "";//店铺id

    public long build_time = 0;//创建时间
    public String build_czy = "";//创建员工
    public String build_czy_name = "";//创建员工名称

    public long modify_time = 0;//修改时间
    public String modify_czy = "";//修改员工
    public String modify_czy_name = "";//修改员工名称

    @Override
    public GoodsUnit clone() throws CloneNotSupportedException {
        return (GoodsUnit) super.clone();
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof GoodsUnit) {
            GoodsUnit unit = (GoodsUnit) object;
            return TextUtil.equals(unit_id, unit.unit_id);
        }
        return false;
    }
}
