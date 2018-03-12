package com.poso2o.lechuan.bean.spec;

import com.poso2o.lechuan.util.TextUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-05-03.
 */

public class Spec implements Serializable, Cloneable {

    public String shop_id = "";//店铺id
    public String shop_branch_id = "";//分店id

    public String spec_id = "";//规格id
    public String spec_name = "";//规格名称

    public long build_time = 0;//创建时间
    public String build_czy = "";//创建员工
    public String build_czy_name = "";//创建员工名称

    @Override
    public Spec clone() throws CloneNotSupportedException {
        return (Spec) super.clone();
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof Spec) {
            Spec spec = (Spec) object;
            return TextUtil.equals(spec_id, spec.spec_id);
        }
        return false;
    }
}
