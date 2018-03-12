package com.poso2o.lechuan.bean.goodsdata;


import com.poso2o.lechuan.bean.BaseBean;

import java.io.Serializable;

/**
 * Created by peng on 2017/6/11.
 */

public class GoodsSupplier extends BaseBean implements Serializable {

    public String shop_id = "";// 店铺id
    public String supplier_id = "0";// 供应商id
    public String supplier_name = "";// 供应商名称
    public String supplier_number = "";// 供应商编号
    public String supplier_shortname = "";// 供应商简称
    public String supplier_logo = "";// 供应商logo

    public String supplier_telephone = "";// 供应商电话号码
    public String supplier_contacts = "";// 供应商联系人
    public String supplier_contacts_mobile = "";// 供应商联系人手机
    public String supplier_mail = "";// 供应商联系人邮箱

    public String supplier_bank_name = "";// 开户行名称
    public String supplier_bank_account_id = "";// 银行账号
    public String supplier_bank_account_name = "";// 银行账户名称

    public int has_supplier_contract = 0;// 是否开启供应商合同 0关闭 1打开

    public long build_time = 0;// 创建时间
    public String build_czy = "";// 创建员工
    public String build_czy_name = "";// 创建员工名称
    public long modify_time = 0;// 修改时间
    public String modify_czy = "";// 修改员工
    public String modify_czy_name = "";// 修改员工名称

    public String goods_id = "";// 商品id
    public int has_default = 0;// 是否默认供应商合同 0 不默认 1 默认
    public int serial = 0;// 序号

    @Override
    public GoodsSupplier clone() throws CloneNotSupportedException {
        return (GoodsSupplier) super.clone();
    }
}