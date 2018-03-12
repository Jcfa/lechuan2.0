package com.poso2o.lechuan.bean.supplier;

import com.poso2o.lechuan.bean.BaseBean;
import com.poso2o.lechuan.bean.spec.Spec;
import com.poso2o.lechuan.util.TextUtil;

import java.io.Serializable;

/**
 * 供应商
 * Created by Jaydon on 2017/6/8.
 */
public class Supplier extends BaseBean implements Serializable, Cloneable {

    public String shop_id = "";// 店铺id
    public String shop_branch_id = "";// 分店id
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

    public int total_goods_number=0;// 供应商品总数量
    public int total_order_number=0;// 采购总笔数
    public double total_order_amount=0.00;// 采购总金额

    public long build_time = 0;// 创建时间
    public String build_czy = "";// 创建员工
    public String build_czy_name = "";// 创建员工名称
    public long modify_time = 0;// 修改时间
    public String modify_czy = "";// 修改员工
    public String modify_czy_name = "";// 修改员工名称

    public String province_id="";// 省份id
    public String province_name="";// 省份名称
    public String city_id="";// 城市id
    public String city_name="";// 城市名称
    public String area_id="";// 市区ID
    public String area_name="";// 市区名称
    public String supplier_address="";// 详细地址

    @Override
    public Supplier clone() throws CloneNotSupportedException {
        return (Supplier) super.clone();
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof Supplier) {
            Supplier supplier = (Supplier) object;
            return TextUtil.equals(supplier_id, supplier.supplier_id);
        }
        return false;
    }
}
