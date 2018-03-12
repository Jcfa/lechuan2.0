package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.db.annotation.Check;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.powerdata.PowerData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RStaffManager;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by mr zhang on 2017/12/13.
 *
 * 已废弃
 */

public class RPermissionActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //类型：1为编辑权限，0为添加职位
    public final static String R_PERMISSION_VIEW_TYPE = "permission_type";
    public final static String R_PERMISSION_VIEW_DATA = "permission_data";

    //返回
    private ImageView r_permission_back;
    //确定
    private Button r_permission_confirm;

    //权限名称
    private EditText r_permission_name;
    //清除权限名称
    private ImageView r_clear_permission_name;

    //产品管理
    private LinearLayout r_layout_goods_all;
    private CheckBox r_permission_product_all;
    //增加商品
    private LinearLayout layout_goods_add;
    private CheckBox r_checkbox_goods_add;
    //修改商品
    private LinearLayout layout_goods_edit;
    private CheckBox r_check_goods_edit;
    //删除商品
    private LinearLayout layout_goods_del;
    private CheckBox r_check_goods_del;

    //会员管理
    private LinearLayout r_layout_member_all;
    private CheckBox r_permission_member_all;
    //增加会员
    private LinearLayout layout_mem_add;
    private CheckBox r_check_mem_add;
    //编辑会员
    private LinearLayout layout_mem_edit;
    private CheckBox r_check_mem_edit;
    //删除会员
    private LinearLayout layout_mem_del;
    private CheckBox r_check_mem_del;
    //会员充值
    private LinearLayout r_layout_member_recharge;
    private CheckBox r_check_member_recharge;

    //订单
    private LinearLayout r_layout_order_all;
    private CheckBox r_permission_order_all;
    //增加订单
    private LinearLayout layout_order_add;
    private CheckBox r_check_order_add;
    //修改订单
    private LinearLayout r_layout_order_edit;
    private CheckBox r_check_order_edit;
    //删除订单
    private LinearLayout r_layout_order_del;
    private CheckBox r_check_order_del;

    //订单发货
    private LinearLayout r_layout_order_send;
    private CheckBox r_check_order_send;
    //订单查看
    private LinearLayout r_layout_order_check;
    private CheckBox r_check_order_check;
    //整单打折
    private LinearLayout r_layout_order_discount;
    private CheckBox r_check_order_discount;

    //主页统计
    private LinearLayout r_layout_boss_report;
    private CheckBox r_check_boss_report;

    //库存
    private LinearLayout r_layout_all_stock;
    private CheckBox r_permission_stock_all;
    //进货单
    private LinearLayout r_layout_order_ingoods;
    private CheckBox r_check_order_ingoods;
    //出货单
    private LinearLayout r_layout_order_outgoods;
    private CheckBox r_check_order_outgoods;
    //退货单
    private LinearLayout r_layout_order_return_goods;
    private CheckBox r_check_order_return_goods;
    //配货单
    private LinearLayout r_layout_order_match_goods;
    private CheckBox r_check_order_match_goods;
    //报损单
    private LinearLayout r_layout_order_bad_goods;
    private CheckBox r_check_order_bad_goods;
    //盘点单
    private LinearLayout r_layout_order_inventory;
    private CheckBox r_check_order_inventory;

    //采购
    private LinearLayout r_layout_all_transfer_purchase;
    private CheckBox r_permission_purchase_all;
    //供应商
    private LinearLayout r_layout_supplier;
    private CheckBox r_check_supplier;
    //采购合同
    private LinearLayout r_layout_purchase_order;
    private CheckBox r_check_purchase_order;
    //订货会
    private LinearLayout r_layout_dinghuohui;
    private CheckBox r_check_dinghuohui;

    //报表
    private LinearLayout r_layout_report_all;
    private CheckBox r_permission_report_all;
    //销售统计
    private LinearLayout r_layout_sale_report;
    private CheckBox r_check_sale_report;
    //商品统计
    private LinearLayout r_layout_goods_report;
    private CheckBox r_check_goods_report;
    //业绩统计
    private LinearLayout r_layout_assign_report;
    private CheckBox r_check_assign_report;

    //店铺
    private LinearLayout r_layout_shop_all;
    private CheckBox r_permission_shop_all;
    //店铺设置
    private LinearLayout r_layout_shop_set;
    private CheckBox r_check_shop_set;
    //用户设置
    private LinearLayout r_layout_user_set;
    private CheckBox r_check_user_set;
    //权限设置
    private LinearLayout r_layout_position_set;
    private CheckBox r_check_position_set;

    //添加？编辑  0为添加职位，1为编辑职位
    private int mType;

    //职位信息
    private PowerData powerData;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_r_permission;
    }

    @Override
    protected void initView() {
        context = this;

        r_permission_back = (ImageView) findViewById(R.id.r_permission_back);
        r_permission_confirm = (Button) findViewById(R.id.r_permission_confirm);

        r_permission_name = (EditText) findViewById(R.id.r_permission_name);
        r_clear_permission_name = (ImageView) findViewById(R.id.r_clear_permission_name);

        r_layout_goods_all = (LinearLayout) findViewById(R.id.r_layout_goods_all);
        r_permission_product_all = (CheckBox) findViewById(R.id.r_permission_product_all);

        layout_goods_add = (LinearLayout) findViewById(R.id.layout_goods_add);
        r_checkbox_goods_add = (CheckBox) findViewById(R.id.r_checkbox_goods_add);

        layout_goods_edit = (LinearLayout) findViewById(R.id.layout_goods_edit);
        r_check_goods_edit = (CheckBox) findViewById(R.id.r_check_goods_edit);

        layout_goods_del = (LinearLayout) findViewById(R.id.layout_goods_del);
        r_check_goods_del = (CheckBox) findViewById(R.id.r_check_goods_del);

        r_layout_member_all = (LinearLayout) findViewById(R.id.r_layout_member_all);
        r_permission_member_all = (CheckBox) findViewById(R.id.r_permission_member_all);

        layout_mem_add = (LinearLayout) findViewById(R.id.layout_mem_add);
        r_check_mem_add = (CheckBox) findViewById(R.id.r_check_mem_add);

        layout_mem_edit = (LinearLayout) findViewById(R.id.layout_mem_edit);
        r_check_mem_edit = (CheckBox) findViewById(R.id.r_check_mem_edit);

        layout_mem_del = (LinearLayout) findViewById(R.id.layout_mem_del);
        r_check_mem_del = (CheckBox) findViewById(R.id.r_check_mem_del);

        r_layout_order_all = (LinearLayout) findViewById(R.id.r_layout_order_all);
        r_permission_order_all = (CheckBox) findViewById(R.id.r_permission_order_all);

        layout_order_add = (LinearLayout) findViewById(R.id.layout_order_add);
        r_check_order_add = (CheckBox) findViewById(R.id.r_check_order_add);

        r_layout_order_edit = (LinearLayout) findViewById(R.id.r_layout_order_edit);
        r_check_order_edit = (CheckBox) findViewById(R.id.r_check_order_edit);

        r_layout_order_del = (LinearLayout) findViewById(R.id.r_layout_order_del);
        r_check_order_del = (CheckBox) findViewById(R.id.r_check_order_del);

        r_layout_member_recharge = (LinearLayout) findViewById(R.id.r_layout_member_recharge);
        r_check_member_recharge = (CheckBox) findViewById(R.id.r_check_member_recharge);

        r_layout_all_stock = (LinearLayout) findViewById(R.id.r_layout_all_stock);
        r_permission_stock_all = (CheckBox) findViewById(R.id.r_permission_stock_all);

        r_layout_order_send = (LinearLayout) findViewById(R.id.r_layout_order_send);
        r_check_order_send = (CheckBox) findViewById(R.id.r_check_order_send);

        r_layout_order_check = (LinearLayout) findViewById(R.id.r_layout_order_check);
        r_check_order_check = (CheckBox) findViewById(R.id.r_check_order_check);

        r_layout_order_discount = (LinearLayout) findViewById(R.id.r_layout_order_discount);
        r_check_order_discount = (CheckBox) findViewById(R.id.r_check_order_discount);

        r_layout_report_all = (LinearLayout) findViewById(R.id.r_layout_report_all);
        r_permission_report_all = (CheckBox) findViewById(R.id.r_permission_report_all);

        r_layout_boss_report = (LinearLayout) findViewById(R.id.r_layout_boss_report);
        r_check_boss_report = (CheckBox) findViewById(R.id.r_check_boss_report);

        r_layout_order_ingoods = (LinearLayout) findViewById(R.id.r_layout_order_ingoods);
        r_check_order_ingoods = (CheckBox) findViewById(R.id.r_check_order_ingoods);

        r_layout_order_outgoods = (LinearLayout) findViewById(R.id.r_layout_order_outgoods);
        r_check_order_outgoods = (CheckBox) findViewById(R.id.r_check_order_outgoods);

        r_layout_order_return_goods = (LinearLayout) findViewById(R.id.r_layout_order_return_goods);
        r_check_order_return_goods = (CheckBox) findViewById(R.id.r_check_order_return_goods);

        r_layout_order_match_goods = (LinearLayout) findViewById(R.id.r_layout_order_match_goods);
        r_check_order_match_goods = (CheckBox) findViewById(R.id.r_check_order_match_goods);

        r_layout_order_bad_goods = (LinearLayout) findViewById(R.id.r_layout_order_bad_goods);
        r_check_order_bad_goods = (CheckBox) findViewById(R.id.r_check_order_bad_goods);

        r_layout_order_inventory = (LinearLayout) findViewById(R.id.r_layout_order_inventory);
        r_check_order_inventory = (CheckBox) findViewById(R.id.r_check_order_inventory);

        r_layout_all_transfer_purchase = (LinearLayout) findViewById(R.id.r_layout_all_transfer_purchase);
        r_permission_purchase_all = (CheckBox) findViewById(R.id.r_permission_purchase_all);

        r_layout_supplier = (LinearLayout) findViewById(R.id.r_layout_supplier);
        r_check_supplier = (CheckBox) findViewById(R.id.r_check_supplier);

        r_layout_purchase_order = (LinearLayout) findViewById(R.id.r_layout_purchase_order);
        r_check_purchase_order = (CheckBox) findViewById(R.id.r_check_purchase_order);

        r_layout_dinghuohui = (LinearLayout) findViewById(R.id.r_layout_dinghuohui);
        r_check_dinghuohui = (CheckBox) findViewById(R.id.r_check_dinghuohui);

        r_layout_sale_report = (LinearLayout) findViewById(R.id.r_layout_sale_report);
        r_check_sale_report = (CheckBox) findViewById(R.id.r_check_sale_report);

        r_layout_goods_report = (LinearLayout) findViewById(R.id.r_layout_goods_report);
        r_check_goods_report = (CheckBox) findViewById(R.id.r_check_goods_report);

        r_layout_assign_report = (LinearLayout) findViewById(R.id.r_layout_assign_report);
        r_check_assign_report = (CheckBox) findViewById(R.id.r_check_assign_report);

        r_layout_shop_all = (LinearLayout) findViewById(R.id.r_layout_shop_all);
        r_permission_shop_all = (CheckBox) findViewById(R.id.r_permission_shop_all);

        r_layout_shop_set = (LinearLayout) findViewById(R.id.r_layout_shop_set);
        r_check_shop_set = (CheckBox) findViewById(R.id.r_check_shop_set);

        r_layout_user_set = (LinearLayout) findViewById(R.id.r_layout_user_set);
        r_check_user_set = (CheckBox) findViewById(R.id.r_check_user_set);

        r_layout_position_set = (LinearLayout) findViewById(R.id.r_layout_position_set);
        r_check_position_set = (CheckBox) findViewById(R.id.r_check_position_set);

    }

    @Override
    protected void initData() {
        switchType();
    }

    @Override
    protected void initListener() {
        r_permission_back.setOnClickListener(this);
        r_permission_confirm.setOnClickListener(this);

        r_clear_permission_name.setOnClickListener(this);

        r_layout_goods_all.setOnClickListener(this);
        layout_goods_add.setOnClickListener(this);
        layout_goods_edit.setOnClickListener(this);
        layout_goods_del.setOnClickListener(this);

        r_layout_member_all.setOnClickListener(this);
        layout_mem_add.setOnClickListener(this);
        layout_mem_edit.setOnClickListener(this);
        layout_mem_del.setOnClickListener(this);

        r_layout_order_all.setOnClickListener(this);
        layout_order_add.setOnClickListener(this);
        r_layout_order_edit.setOnClickListener(this);
        r_layout_order_del.setOnClickListener(this);

        r_layout_member_recharge.setOnClickListener(this);

        r_layout_order_send.setOnClickListener(this);
        r_layout_order_check.setOnClickListener(this);
        r_layout_order_discount.setOnClickListener(this);

        r_layout_boss_report.setOnClickListener(this);

        r_layout_all_stock.setOnClickListener(this);
        r_layout_order_ingoods.setOnClickListener(this);
        r_layout_order_outgoods.setOnClickListener(this);
        r_layout_order_return_goods.setOnClickListener(this);
        r_layout_order_match_goods.setOnClickListener(this);
        r_layout_order_bad_goods.setOnClickListener(this);
        r_layout_order_inventory.setOnClickListener(this);

        r_layout_all_transfer_purchase.setOnClickListener(this);
        r_layout_supplier.setOnClickListener(this);
        r_layout_purchase_order.setOnClickListener(this);
        r_layout_dinghuohui.setOnClickListener(this);

        r_layout_report_all.setOnClickListener(this);
        r_layout_sale_report.setOnClickListener(this);
        r_layout_goods_report.setOnClickListener(this);
        r_layout_assign_report.setOnClickListener(this);

        r_layout_shop_all.setOnClickListener(this);
        r_layout_shop_set.setOnClickListener(this);
        r_layout_user_set.setOnClickListener(this);
        r_layout_position_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.r_permission_back:
                finish();
                break;
            case R.id.r_permission_confirm:
                onOkClick();
                break;
            case R.id.r_clear_permission_name:
                r_permission_name.setText(null);
                break;
            case R.id.r_layout_goods_all:
                allGoodsCheck(!r_permission_product_all.isChecked());
                break;
            case R.id.layout_goods_add:
                r_checkbox_goods_add.setChecked(!r_checkbox_goods_add.isChecked());
                break;
            case R.id.layout_goods_edit:
                r_check_goods_edit.setChecked(!r_check_goods_edit.isChecked());
                break;
            case R.id.layout_goods_del:
                r_check_goods_del.setChecked(!r_check_goods_del.isChecked());
                break;
            case R.id.r_layout_member_all:
                //TODO
                allMemberCheck(!r_permission_member_all.isChecked());
                break;
            case R.id.layout_mem_add:
                r_check_mem_add.setChecked(!r_check_mem_add.isChecked());
                break;
            case R.id.layout_mem_edit:
                r_check_mem_edit.setChecked(!r_check_mem_edit.isChecked());
                break;
            case R.id.layout_mem_del:
                r_check_mem_del.setChecked(!r_check_mem_del.isChecked());
                break;
            case R.id.r_layout_order_all:
                //TODO
                allOrderCheck(!r_permission_order_all.isChecked());
                break;
            case R.id.layout_order_add:
                r_check_order_add.setChecked(!r_check_order_add.isChecked());
                break;
            case R.id.r_layout_order_edit:
                r_check_order_edit.setChecked(!r_check_order_edit.isChecked());
                break;
            case R.id.r_layout_order_del:
                r_check_order_del.setChecked(!r_check_order_del.isChecked());
                break;
            case R.id.r_layout_member_recharge:
                r_check_member_recharge.setChecked(!r_check_member_recharge.isChecked());
                break;
            case R.id.r_layout_order_send:
                r_check_order_send.setChecked(!r_check_order_send.isChecked());
                break;
            case R.id.r_layout_order_check:
                r_check_order_check.setChecked(!r_check_order_check.isChecked());
                break;
            case R.id.r_layout_order_discount:
                r_check_order_discount.setChecked(!r_check_order_discount.isChecked());
                break;
            case R.id.r_layout_boss_report:
                r_check_boss_report.setChecked(!r_check_boss_report.isChecked());
                break;
            case R.id.r_layout_all_stock:
                //TODO
                allStockCheck(!r_permission_stock_all.isChecked());
                break;
            case R.id.r_layout_order_ingoods:
                r_check_order_ingoods.setChecked(!r_check_order_ingoods.isChecked());
                break;
            case R.id.r_layout_order_outgoods:
                r_check_order_outgoods.setChecked(!r_check_order_outgoods.isChecked());
                break;
            case R.id.r_layout_order_return_goods:
                r_check_order_return_goods.setChecked(!r_check_order_return_goods.isChecked());
                break;
            case R.id.r_layout_order_bad_goods:
                r_check_order_bad_goods.setChecked(!r_check_order_bad_goods.isChecked());
                break;
            case R.id.r_layout_order_inventory:
                r_check_order_inventory.setChecked(!r_check_order_inventory.isChecked());
                break;
            case R.id.r_layout_all_transfer_purchase:
                //TODO
                allPurchaseCheck(!r_permission_purchase_all.isChecked());
                break;
            case R.id.r_layout_supplier:
                r_check_supplier.setChecked(!r_check_supplier.isChecked());
                break;
            case R.id.r_layout_purchase_order:
                r_check_purchase_order.setChecked(!r_check_purchase_order.isChecked());
                break;
            case R.id.r_layout_dinghuohui:
                r_check_dinghuohui.setChecked(!r_check_dinghuohui.isChecked());
                break;
            case R.id.r_layout_report_all:
                //TODO
                allReportCheck(!r_permission_report_all.isChecked());
                break;
            case R.id.r_layout_sale_report:
                r_check_sale_report.setChecked(!r_check_sale_report.isChecked());
                break;
            case R.id.r_layout_goods_report:
                r_check_goods_report.setChecked(!r_check_goods_report.isChecked());
                break;
            case R.id.r_layout_order_match_goods:
                r_check_order_match_goods.setChecked(!r_check_order_match_goods.isChecked());
                break;
            case R.id.r_layout_assign_report:
                r_check_assign_report.setChecked(!r_check_assign_report.isChecked());
                break;
            case R.id.r_layout_shop_all:
                //TODO
                allShopCheck(!r_permission_shop_all.isChecked());
                break;
            case R.id.r_layout_shop_set:
                r_check_shop_set.setChecked(!r_check_shop_set.isChecked());
                break;
            case R.id.r_layout_user_set:
                r_check_user_set.setChecked(!r_check_user_set.isChecked());
                break;
            case R.id.r_layout_position_set:
                r_check_position_set.setChecked(!r_check_position_set.isChecked());
                break;
        }
    }

    //商品管理全选
    private void allGoodsCheck(boolean b){
        r_permission_product_all.setChecked(b);
        r_checkbox_goods_add.setChecked(b);
        r_check_goods_edit.setChecked(b);
        r_check_goods_del.setChecked(b);
    }

    //会员管理全选
    private void allMemberCheck(boolean b){
        r_permission_member_all.setChecked(b);
        r_check_mem_add.setChecked(b);
        r_check_mem_del.setChecked(b);
        r_check_mem_edit.setChecked(b);
        r_check_member_recharge.setChecked(b);
    }

    //订单管理全选
    private void allOrderCheck(boolean b){
        r_permission_order_all.setChecked(b);
        r_check_order_add.setChecked(b);
        r_check_order_edit.setChecked(b);
        r_check_order_del.setChecked(b);
        r_check_order_send.setChecked(b);
        r_check_order_check.setChecked(b);
        r_check_order_discount.setChecked(b);
    }

    //库存全选
    private void allStockCheck(boolean b){
        r_permission_stock_all.setChecked(b);
        r_check_order_ingoods.setChecked(b);
        r_check_order_outgoods.setChecked(b);
        r_check_order_return_goods.setChecked(b);
        r_check_order_match_goods.setChecked(b);
        r_check_order_bad_goods.setChecked(b);
        r_check_order_inventory.setChecked(b);
    }

    //采购全选
    private void allPurchaseCheck(boolean b){
        r_permission_purchase_all.setChecked(b);
        r_check_supplier.setChecked(b);
        r_check_purchase_order.setChecked(b);
        r_check_dinghuohui.setChecked(b);
    }

    //报表全选
    private void allReportCheck(boolean b){
        r_permission_report_all.setChecked(b);
        r_check_boss_report.setChecked(b);
        r_check_sale_report.setChecked(b);
        r_check_goods_report.setChecked(b);
        r_check_assign_report.setChecked(b);
    }

    //店铺全选
    private void allShopCheck(boolean b){
        r_permission_shop_all.setChecked(b);
        r_check_shop_set.setChecked(b);
        r_check_user_set.setChecked(b);
        r_check_position_set.setChecked(b);
    }

    //添加职位？编辑职位
    private void switchType(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) mType = bundle.getInt(R_PERMISSION_VIEW_TYPE,0);
        if (mType == 0){
            //添加职位
            powerData = new PowerData();
        }else if (mType == 1){
            powerData = (PowerData) bundle.get(R_PERMISSION_VIEW_DATA);
            if (powerData == null)return;

            r_permission_name.setText(powerData.positionname);
            r_permission_name.setSelection(powerData.positionname.length());
            r_checkbox_goods_add.setChecked(isTrue(powerData.product_add));
            r_check_goods_edit.setChecked(isTrue(powerData.product_edit));
            r_check_goods_del.setChecked(isTrue(powerData.product_del));
            r_check_mem_add.setChecked(isTrue(powerData.client_add));
            r_check_mem_edit.setChecked(isTrue(powerData.client_edit));
            r_check_mem_del.setChecked(isTrue(powerData.client_del));
            r_check_order_add.setChecked(isTrue(powerData.order_add));
            r_check_order_edit.setChecked(isTrue(powerData.order_edit));
            r_check_order_del.setChecked(isTrue(powerData.order_del));
            r_check_member_recharge.setChecked(isTrue(powerData.client_payment));
            r_check_order_send.setChecked(isTrue(powerData.order_fh));
            r_check_order_check.setChecked(isTrue(powerData.order_query));
            r_check_order_discount.setChecked(isTrue(powerData.order_discount));
            r_check_boss_report.setChecked(isTrue(powerData.indextotal));
            r_check_order_ingoods.setChecked(isTrue(powerData.kc_add));
            r_check_order_outgoods.setChecked(isTrue(powerData.kc_del));
            r_check_order_return_goods.setChecked(isTrue(powerData.kc_back));
            r_check_order_bad_goods.setChecked(isTrue(powerData.kc_bug));
            r_check_order_inventory.setChecked(isTrue(powerData.kc_update));
            r_check_supplier.setChecked(isTrue(powerData.factory));
            r_check_purchase_order.setChecked(isTrue(powerData.factory_contract));
            r_check_dinghuohui.setChecked(isTrue(powerData.order_quote));
            r_check_sale_report.setChecked(isTrue(powerData.total_sales));
            r_check_goods_report.setChecked(isTrue(powerData.total_product));
            r_check_order_match_goods.setChecked(isTrue(powerData.kc_booking));
            r_check_assign_report.setChecked(isTrue(powerData.total_czy));
            r_check_shop_set.setChecked(isTrue(powerData.shop_seting));
            r_check_user_set.setChecked(isTrue(powerData.user_seting));
            r_check_position_set.setChecked(isTrue(powerData.user_position));
        }
    }

    //确定
    private void onOkClick(){
        String name = r_permission_name.getText().toString();
        if (name == null || name.equals("")){
            Toast.show(context,"请输入职位名称");
            r_permission_name.requestFocus();
            return;
        }
        if (mType == 0){
            //添加职位
            addPosition(name);
        }else {
            //编辑职位
            editPosition(name);
        }
    }

    //添加职位
    private void addPosition(String name){
        powerData.positionname = name;

        powerData.order_fh = zeroOrOne(r_check_order_send.isChecked());
        powerData.user_seting = zeroOrOne(r_check_user_set.isChecked());
        powerData.user_position = zeroOrOne(r_check_position_set.isChecked());
        powerData.order_quote = zeroOrOne(r_check_dinghuohui.isChecked());
        powerData.kc_add = zeroOrOne(r_check_order_ingoods.isChecked());
        powerData.product_add = zeroOrOne(r_checkbox_goods_add.isChecked());
        powerData.client_payment = zeroOrOne(r_check_member_recharge.isChecked());
        powerData.kc_update = zeroOrOne(r_check_order_inventory.isChecked());
        powerData.kc_del = zeroOrOne(r_check_order_outgoods.isChecked());
        powerData.order_edit = zeroOrOne(r_check_order_edit.isChecked());
        powerData.order_query = zeroOrOne(r_check_order_check.isChecked());
        powerData.order_discount = zeroOrOne(r_check_order_discount.isChecked());
        powerData.client_add = zeroOrOne(r_check_mem_add.isChecked());
        powerData.total_sales = zeroOrOne(r_check_sale_report.isChecked());
        powerData.indextotal = zeroOrOne(r_check_boss_report.isChecked());
        powerData.order_add = zeroOrOne(r_check_order_add.isChecked());
        powerData.shop_seting = zeroOrOne(r_check_shop_set.isChecked());
        powerData.client_edit = zeroOrOne(r_check_mem_edit.isChecked());
        powerData.kc_booking = zeroOrOne(r_check_order_match_goods.isChecked());
        powerData.factory = zeroOrOne(r_check_supplier.isChecked());
        powerData.total_product = zeroOrOne(r_check_goods_report.isChecked());
//        powerData.total_shop = zeroOrOne(r_checkbox_goods_add.isChecked());
        powerData.product_del = zeroOrOne(r_check_goods_del.isChecked());
        powerData.product_edit = zeroOrOne(r_check_goods_edit.isChecked());
        powerData.client_del = zeroOrOne(r_check_mem_del.isChecked());
        powerData.factory_contract = zeroOrOne(r_check_purchase_order.isChecked());
        powerData.order_del = zeroOrOne(r_check_order_del.isChecked());
        powerData.kc_bug = zeroOrOne(r_check_order_bad_goods.isChecked());
        powerData.total_czy = zeroOrOne(r_check_assign_report.isChecked());
        powerData.kc_back = zeroOrOne(r_check_order_return_goods.isChecked());

        showLoading("正在提交职位信息...");
        RStaffManager.getRStaffManager().rPositionAdd(this, powerData, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    //编辑职位
    private void editPosition(String name){
        powerData.positionname = name;

        powerData.order_fh = zeroOrOne(r_check_order_send.isChecked());
        powerData.user_seting = zeroOrOne(r_check_user_set.isChecked());
        powerData.user_position = zeroOrOne(r_check_position_set.isChecked());
        powerData.order_quote = zeroOrOne(r_check_dinghuohui.isChecked());
        powerData.kc_add = zeroOrOne(r_check_order_ingoods.isChecked());
        powerData.product_add = zeroOrOne(r_checkbox_goods_add.isChecked());
        powerData.client_payment = zeroOrOne(r_check_member_recharge.isChecked());
        powerData.kc_update = zeroOrOne(r_check_order_inventory.isChecked());
        powerData.kc_del = zeroOrOne(r_check_order_outgoods.isChecked());
        powerData.order_edit = zeroOrOne(r_check_order_edit.isChecked());
        powerData.order_query = zeroOrOne(r_check_order_check.isChecked());
        powerData.order_discount = zeroOrOne(r_check_order_discount.isChecked());
        powerData.client_add = zeroOrOne(r_check_mem_add.isChecked());
        powerData.total_sales = zeroOrOne(r_check_sale_report.isChecked());
        powerData.indextotal = zeroOrOne(r_check_boss_report.isChecked());
        powerData.order_add = zeroOrOne(r_check_order_add.isChecked());
        powerData.shop_seting = zeroOrOne(r_check_shop_set.isChecked());
        powerData.client_edit = zeroOrOne(r_check_mem_edit.isChecked());
        powerData.kc_booking = zeroOrOne(r_check_order_match_goods.isChecked());
        powerData.factory = zeroOrOne(r_check_supplier.isChecked());
        powerData.total_product = zeroOrOne(r_check_goods_report.isChecked());
//        powerData.total_shop = zeroOrOne(r_checkbox_goods_add.isChecked());
        powerData.product_del = zeroOrOne(r_check_goods_del.isChecked());
        powerData.product_edit = zeroOrOne(r_check_goods_edit.isChecked());
        powerData.client_del = zeroOrOne(r_check_mem_del.isChecked());
        powerData.factory_contract = zeroOrOne(r_check_purchase_order.isChecked());
        powerData.order_del = zeroOrOne(r_check_order_del.isChecked());
        powerData.kc_bug = zeroOrOne(r_check_order_bad_goods.isChecked());
        powerData.total_czy = zeroOrOne(r_check_assign_report.isChecked());
        powerData.kc_back = zeroOrOne(r_check_order_return_goods.isChecked());

        showLoading("正在提交职位信息...");
        RStaffManager.getRStaffManager().rPositionEdit(this, powerData, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    //传入0返回false，1返回true
    private boolean isTrue(String b){
        if (b.equals("0"))return false;
        return true;
    }

    //传入false返回0，传入true返回1
    private String zeroOrOne(boolean b){
        if (b)return "1";
        return "0";
    }

}
