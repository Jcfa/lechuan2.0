package com.poso2o.lechuan.bean.color;

import android.text.TextUtils;

import com.poso2o.lechuan.util.SharedPreferencesUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-05-03.
 */
public class GoodsColor implements Serializable, Cloneable {

    public String shop_id = "";//店铺id
    public String shop_branch_id = "";//分店id

    public String goods_colour_id = "";//颜色id
    public String goods_colour_name = "";//颜色名称

    public long build_time = 0;//创建时间
    public String build_czy = "";//创建员工
    public String build_czy_name = "";//创建员工名称

    //旧服装版数据
    public String colorid = "";

    @Override
    public GoodsColor clone() throws CloneNotSupportedException {
        return (GoodsColor) super.clone();
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof GoodsColor) {
            GoodsColor colour = (GoodsColor) object;
            if (SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE)){
                return TextUtils.equals(goods_colour_id, colour.goods_colour_id);
            }else {
                return TextUtils.equals(colorid, colour.colorid);
            }
        }
        return false;
    }

}