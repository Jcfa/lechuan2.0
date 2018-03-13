package com.poso2o.lechuan.bean.goodsdata;

import com.poso2o.lechuan.bean.printer.GoodsDetailsImgsData;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 商品
 *
 * Created by J on 2017-08-18.
 */
public class Goods implements Serializable, Cloneable {
    public String catalog_id = "";// 目录id
    public String catalog_name = "";// 目录名称

    public String goods_id = "";// 商品id
    public String goods_name = "";// 商品名称
    public String goods_no = "";// 货号
    public String main_picture = "";// 主图
    public ArrayList<GoodsMovePicture> goods_move_picture = new ArrayList<>();// 图片列表
    public ArrayList<GoodsSpec> goods_spec = new ArrayList<>();// 规格
    public String goods_unit = "";// 单位

    public Double goods_discount = 100.00;// 商品折扣

    public String goods_cost_section = "";// 成本价区间
    public String goods_price_section = "";// 售价区间
    public double commission_rate = 0;// 佣金比例
    public double commission_discount = 0;// 佣金折扣
    public double commission_amount = 0;// 佣金
    public double commission_price = 0.00;// 分销佣金单价
    public int goods_number = 0;// 商品总数量
    public int goods_sale_number = 0;// 销售商品总数量
    public String goods_cost_amount = "0";
    public Double goods_sale_amount = 0.00;// 销售商品总金额

    public int sale_type = 1;// 销售状态 1 上架 0 售罄
    public String remark = "";// 备注

    public long build_time = 0;// 创建时间
    public String build_czy = "";// 创建员工
    public String build_czy_name = "";// 创建员工名称
    public long modify_time = 0;// 修改时间
    public String modify_czy = "";// 修改员工
    public String modify_czy_name = "";// 修改员工名称


    public String share_url = "";

    /**
     * 是否高亮
     */
    public boolean isNameLight = false;
    public String light_name = "";// 高亮商品名称

    public boolean isNoLight = false;
    public String light_no = "";// 高亮货号

    @Deprecated
    public int goods_type = 1;// 商品类型 1=服装，2便利店
    @Deprecated
    public String goods_auxiliary_unit = "";// 商品辅助单位
    @Deprecated
    public int goods_auxiliary_unit_packingrate = 1;// 商品辅助单位-包装率  默认为1
    @Deprecated
    public String main_supplier_id = "0";// 主供应商id
    @Deprecated
    public String main_supplier_name = "";// 主供应商名称
    @Deprecated
    public ArrayList<GoodsSupplier> goods_supplier = new ArrayList<>();// 供应商列表


    /**
     * 参加的打折活动
     */
    public PromotionInfoData goodsPromotionInfo;

    @Override
    public Goods clone() throws CloneNotSupportedException {
        return (Goods) super.clone();
    }

//    public String getAuxiliaryUnitText() {
//        if (!TextUtils.isEmpty(goods_auxiliary_unit)) {
//            return "(" + goods_auxiliary_unit_packingrate + goods_unit + "/" + goods_auxiliary_unit + ")";
//        }
//        return "";
//    }

    /**
     * 旧服装版字段
     */
    public String bh;
    public String dat;
    public String fid;
    public String directory;  //目录名
    public String fprice;
    public String guid;
    public String image222;
    public ArrayList<GoodsDetailsImgsData> imgs = new ArrayList<>();
    public String kcnum = "0";
    public String name;
    public ArrayList<OldSpec> nums = new ArrayList<>();
    public String price;
    public String salesnum = "0";
    public String uid;
    public String unit;
    // 补充旧服装版库存字段
    public String totalamount;
    public String totalnum;

    //商品库存补充字段
    public ArrayList<OldSpec> lists = new ArrayList<>();
}