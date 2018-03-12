package com.poso2o.lechuan.bean.size;

import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-05-03.
 */
public class GoodsSize implements Serializable, Cloneable {
    public String shop_id = "";// 店铺id
    public String shop_branch_id = "";// 分店id

    public String goods_size_id = "";// 尺码id
    public String goods_size_name = "";// 尺码名称

    public int goods_size_total_stock = 0;// 该尺码总库存量
    public int goods_size_total_sale = 0;// 该尺码总销量

    public long build_time = 0;// 创建时间
    public String build_czy = "";// 创建员工
    public String build_czy_name = "";//创建员工名称

    // 旧服装版数据
    public String sizeid = "";//这里指size的名称

    @Override
    public GoodsSize clone() throws CloneNotSupportedException {
        return (GoodsSize) super.clone();
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof GoodsSize) {
            GoodsSize size = (GoodsSize) object;
            if (SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE)) {
                return TextUtil.equals(goods_size_id, size.goods_size_id);
            } else {
                return TextUtil.equals(sizeid, size.sizeid);
            }
        }
        return false;
    }
}
