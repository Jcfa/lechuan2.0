package com.poso2o.lechuan.bean.order;

import java.io.Serializable;

/**
 * 订单明细表(T_ORDER_DETAIL)
 * Created by Luo on 2017-12-11
 */
public class OrderGoodsDTO implements Serializable {

    /** 序号 */
    public int serial=0;

    /** 商品ID */
    public Long goods_id=0L;

    /** 货号 */
    public String goods_no="";

    /** 名称 */
    public String goods_name="";

    /** 主图 */
    public String main_picture="";

    /** 单位 */
    public String goods_unit="";

    /** 商品规格id */
    public Long goods_spec_id=0L;

    /** 商品规格名称 */
    public String goods_spec_name="";

    /** 条码 */
    public String spec_bar_code="";

    /** 吊牌价 */
    public double spec_tag_price=0.00;

    /** 销售价 */
    public double spec_price=0.00;

    /** 会员价格 */
    public double member_price=0.00;

    /** 成本价 */
    public double spec_cost=0.00;

    /** 商品折扣 */
    public double goods_discount= 100;

    /** 商品折扣单价 */
    public double goods_discount_price= 100;

    /** 购买数量 */
    public int purchase_munber=0;

    /** 总价格 */
    public double goods_amount=0.00;

    /** 是否已评价(1=已评价,0=未评价) */
    public int has_appraise=0;

    /** 推广资讯ID */
    public Long news_id=0L;

    /** 分销商ID */
    public Long distributor_id =0L;

    /** 佣金比例% */
    public double goods_commission_rate=0.00;

    /** 分销所得佣金 */
    public double goods_commission_amount=0.00;
    
}
