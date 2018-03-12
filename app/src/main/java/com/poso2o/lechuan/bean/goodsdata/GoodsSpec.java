package com.poso2o.lechuan.bean.goodsdata;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-04-24.
 */
public class GoodsSpec implements Serializable, Cloneable {
    public String goods_id = "";//商品id
    public String goods_spec_id = "";//规格id
    public String goods_spec_name = "";//商品规格名称
    public String shop_branch_id = "";//分店id
    public String shop_id = "";//店铺id

    public int serial = 0;//序号

    public String spec_bar_code = "";//条码
    public String spec_colour = "";//颜色
    public String spec_size = "";//尺码
    public String spec_name = "";//规格

    public double spec_price = 0.00;//规格价格
    public double spec_member_price = 0.00;//会员价格
    public double spec_cost = 0.00;//规格成本
    public double spec_tag_price = 0.00;//吊牌价格

    public String goods_unit = "";// 单位
    public String goods_auxiliary_unit = "";// 商品辅助单位
    public int goods_auxiliary_unit_packingrate = 1;// 商品辅助单位-包装率  默认为1

    public int spec_number = 0;//规格数量
    public int spec_sale_number = 0;//规格销售数量


    //缓冲数据
    public static final String NOT_SPEC = "无规格";

    public String getSpecName() {
        StringBuilder name = new StringBuilder();
        if (!TextUtils.isEmpty(spec_name) || !TextUtils.isEmpty(goods_unit)) {
            if (!TextUtils.isEmpty(spec_name)) {
                name.append(spec_name).append("/");
            }
            if (!TextUtils.isEmpty(goods_auxiliary_unit)) {
                name.append(goods_auxiliary_unit);
            } else if (!TextUtils.isEmpty(goods_unit)) {
                name.append(goods_unit);
            } else if (name.length() > 0) {
                name.deleteCharAt(name.length() - 1);
            }
        } else {
            if (!TextUtils.isEmpty(spec_colour)) {
                name.append(spec_colour).append("/");
            }
            if (!TextUtils.isEmpty(spec_size)) {
                name.append(spec_size);
            } else if (name.length() > 0) {
                name.deleteCharAt(name.length() - 1);
            }
        }
        return name.toString();
    }

    @Override
    public GoodsSpec clone() throws CloneNotSupportedException {
        return (GoodsSpec) super.clone();
    }
}