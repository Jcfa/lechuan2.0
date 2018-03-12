package com.poso2o.lechuan.bean.goodsdata;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-04-24.
 */
public class GoodsMovePicture implements Serializable {
    public String goods_id = "";// 商品id

    public String picture_type = "png";// 图片类型
    public int serial = 0;// 序号
    public String picture_url = "";// 图片url

    public String shop_id = "";// 店铺id
    public String shop_branch_id = "";// 分店id

    // 本地路径
    public String path;

    @Override
    public GoodsMovePicture clone() throws CloneNotSupportedException {
        return (GoodsMovePicture) super.clone();
    }
}
