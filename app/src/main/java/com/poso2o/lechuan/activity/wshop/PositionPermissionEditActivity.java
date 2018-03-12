package com.poso2o.lechuan.activity.wshop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.powerdata.PowerData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RStaffManager;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by mr zhang on 2017/8/8.
 * 职位权限编辑，或者添加职位
 */

public class PositionPermissionEditActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //类型：1为编辑权限，0为添加职位
    public  int viewType = 0;
    public final static String PERMISSION_VIEW_TYPE = "permission_type";
    public final static String PERMISSION_VIEW_DATA = "permission_data";

    private PowerData powerData;

    //返回
    private ImageView position_permission_back;
    //确定
    private Button position_permission_confirm;
    //职位名称
    private EditText position_permission_name;
    //职位名称清除按钮
    private ImageView clear_permission_name;

    //商品全选
    private LinearLayout layout_goods_all;
    private CheckBox permission_product_all;
    //商品打折
    private LinearLayout layout_goods_discount;
    private CheckBox checkbox_goods_discount;
    //售罄
    private LinearLayout layout_up_down_goods;
    private CheckBox check_up_down_goods;
    //赠送
    private LinearLayout layout_goods_give;
    private CheckBox check_goods_give;
    //添加
    private LinearLayout layout_goods_add;
    private CheckBox check_goods_add;
    //删除
    private LinearLayout layout_goods_del;
    private CheckBox check_goods_del;
    //修改
    private LinearLayout layout_goods_edit;
    private CheckBox check_goods_edit;
    //查看成本
    private LinearLayout layout_goods_cost;
    private CheckBox check_goods_cost;
    //目录设置
    private LinearLayout layout_goods_catalog;
    private CheckBox check_goods_catalog;

    //会员全选
    private LinearLayout layout_all_member;
    private CheckBox permission_member_all;
    //添加会员
    private LinearLayout layout_member_add;
    private CheckBox check_member_add;
    //编辑会员
    private LinearLayout layout_member_edit;
    private CheckBox check_member_edit;
    //删除会员
    private LinearLayout layout_member_del;
    private CheckBox check_member_del;
    //会员充值
    private LinearLayout layout_member_recharge;
    private CheckBox check_member_recharge;
    //会员等级设置
    private LinearLayout layout_member_rank;
    private CheckBox check_member_rank;

    //报表全选
    private LinearLayout layout_all_report;
    private CheckBox permission_forms_all;
    //销售统计
    private LinearLayout layout_sale_report;
    private CheckBox check_sale_report;
    //商品统计
    private LinearLayout layout_goods_report;
    private CheckBox check_goods_report;
    //业绩统计
    private LinearLayout layout_assign_report;
    private CheckBox check_assign_report;
    //月损益表
    private LinearLayout layout_month_profit;
    private CheckBox check_month_profit;
    //订单查看
    private LinearLayout layout_sale_order;
    private CheckBox check_sale_order;
    //充值记录
    private LinearLayout layout_recharge_record;
    private CheckBox check_recharge_record;

    //交接采购全选
    private LinearLayout layout_all_transfer_purchase;
    private CheckBox permission_purchase_all;
    //交接明细
    private LinearLayout layout_transfer_detail;
    private CheckBox check_transfer_detail;
    //交接设置
    private LinearLayout layout_transfer_set;
    private CheckBox check_transfer_set;
    //采购单
    private LinearLayout layout_purchase_order;
    private CheckBox check_purchase_order;
    //采购退货单
    private LinearLayout layout_purchase_return;
    private CheckBox check_purchase_return;

    //库存全选
    private LinearLayout layout_all_stock;
    private CheckBox permission_stock_all;
    //盘点
    private LinearLayout layout_inventory;
    private CheckBox check_inventory;
    //入仓
    private LinearLayout layout_warehousing;
    private CheckBox check_warehousing;
    //要货
    private LinearLayout layout_want_goods;
    private CheckBox check_want_goods;
    //报损
    private LinearLayout layout_report_bad;
    private CheckBox check_report_bad;
    //出货
    private LinearLayout layout_out_goods;
    private CheckBox check_out_goods;
    //预警
    private LinearLayout layout_stock_warning;
    private CheckBox check_stock_warning;

    //其他全选
    private LinearLayout layout_all_other;
    private CheckBox permission_other_all;
    //分析图
    private LinearLayout layout_analysis_chart;
    private CheckBox check_analysis_chart;
    //收银开单
    private LinearLayout layout_cashier;
    private CheckBox check_cashier;
    //退货操作
    private LinearLayout layout_refund;
    private CheckBox check_refund;
    //店铺信息
    private LinearLayout layout_shop_info;
    private CheckBox check_shop_info;
    //员工权限
    private LinearLayout layout_staff_power;
    private CheckBox check_staff_power;

    //已选择商品权限数量,总数为8项
    private int mGoodsPower ;
    //已选择会员权限数量，总数为5项
    private int mMemberPower ;
    //已选择报表权限数量，总数为6项
    private int mReportPower ;
    //已选择交接采购权限数量，总数为4项
    private int mPurchasePower ;
    //已选择库存权限数量，总数为6项
    private int mStockPower ;
    //已选择其他权限数量，总数为5项
    private int mOtherPower ;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_position_permission;
    }

    @Override
    public void initView() {

        context = this;

        position_permission_back = (ImageView) findViewById(R.id.position_permission_back);

        position_permission_confirm = (Button) findViewById(R.id.position_permission_confirm);

        position_permission_name = (EditText) findViewById(R.id.position_permission_name);

        clear_permission_name = (ImageView) findViewById(R.id.clear_permission_name);

        layout_goods_all = (LinearLayout) findViewById(R.id.layout_goods_all);

        permission_product_all = (CheckBox) findViewById(R.id.permission_product_all);

        layout_goods_discount = (LinearLayout) findViewById(R.id.layout_goods_discount);

        checkbox_goods_discount = (CheckBox) findViewById(R.id.checkbox_goods_discount);

        layout_up_down_goods = (LinearLayout) findViewById(R.id.layout_up_down_goods);

        check_up_down_goods = (CheckBox) findViewById(R.id.check_up_down_goods);

        layout_goods_give = (LinearLayout) findViewById(R.id.layout_goods_give);

        check_goods_give = (CheckBox) findViewById(R.id.check_goods_give);

        layout_goods_add = (LinearLayout) findViewById(R.id.layout_goods_add);

        check_goods_add = (CheckBox) findViewById(R.id.check_goods_add);

        layout_goods_del = (LinearLayout) findViewById(R.id.layout_goods_del);

        check_goods_del = (CheckBox) findViewById(R.id.check_goods_del);

        layout_goods_edit = (LinearLayout) findViewById(R.id.layout_goods_edit);

        check_goods_edit =(CheckBox) findViewById(R.id.check_goods_edit);

        layout_goods_cost = (LinearLayout) findViewById(R.id.layout_goods_cost);

        check_goods_cost = (CheckBox) findViewById(R.id.check_goods_cost);

        layout_goods_catalog = (LinearLayout) findViewById(R.id.layout_goods_catalog);

        check_goods_catalog = (CheckBox) findViewById(R.id.check_goods_catalog);

        layout_all_member = (LinearLayout) findViewById(R.id.layout_all_member);

        permission_member_all = (CheckBox) findViewById(R.id.permission_member_all);

        layout_member_add = (LinearLayout) findViewById(R.id.layout_member_add);

        check_member_add = (CheckBox) findViewById(R.id.check_member_add);

        layout_member_edit = (LinearLayout) findViewById(R.id.layout_member_edit);

        check_member_edit = (CheckBox) findViewById(R.id.check_member_edit);

        layout_member_del = (LinearLayout) findViewById(R.id.layout_member_del);

        check_member_del = (CheckBox) findViewById(R.id.check_member_del);

        layout_member_recharge = (LinearLayout) findViewById(R.id.layout_member_recharge);

        check_member_recharge = (CheckBox) findViewById(R.id.check_member_recharge) ;

        layout_member_rank = (LinearLayout) findViewById(R.id.layout_member_rank);

        check_member_rank = (CheckBox) findViewById(R.id.check_member_rank);

        layout_all_report = (LinearLayout) findViewById(R.id.layout_all_report);

        permission_forms_all = (CheckBox) findViewById(R.id.permission_forms_all);

        layout_sale_report = (LinearLayout) findViewById(R.id.layout_sale_report);

        check_sale_report = (CheckBox) findViewById(R.id.check_sale_report);

        layout_goods_report = (LinearLayout) findViewById(R.id.layout_goods_report);

        check_goods_report = (CheckBox) findViewById(R.id.check_goods_report);

        layout_assign_report = (LinearLayout) findViewById(R.id.layout_assign_report);

        check_assign_report = (CheckBox) findViewById(R.id.check_assign_report);

        layout_month_profit = (LinearLayout) findViewById(R.id.layout_month_profit);

        check_month_profit = (CheckBox) findViewById(R.id.check_month_profit);

        layout_sale_order = (LinearLayout) findViewById(R.id.layout_sale_order);

        check_sale_order = (CheckBox) findViewById(R.id.check_sale_order);

        layout_recharge_record = (LinearLayout) findViewById(R.id.layout_recharge_record);

        check_recharge_record = (CheckBox) findViewById(R.id.check_recharge_record);

        layout_all_transfer_purchase = (LinearLayout) findViewById(R.id.layout_all_transfer_purchase);

        permission_purchase_all = (CheckBox) findViewById(R.id.permission_purchase_all);

        layout_transfer_detail = (LinearLayout) findViewById(R.id.layout_transfer_detail);

        check_transfer_detail = (CheckBox) findViewById(R.id.check_transfer_detail);

        layout_transfer_set = (LinearLayout) findViewById(R.id.layout_transfer_set);

        check_transfer_set = (CheckBox) findViewById(R.id.check_transfer_set);

        layout_purchase_order = (LinearLayout) findViewById(R.id.layout_purchase_order);

        check_purchase_order = (CheckBox) findViewById(R.id.check_purchase_order);

        layout_purchase_return = (LinearLayout) findViewById(R.id.layout_purchase_return);

        check_purchase_return = (CheckBox) findViewById(R.id.check_purchase_return);

        layout_all_stock = (LinearLayout) findViewById(R.id.layout_all_stock);

        permission_stock_all = (CheckBox) findViewById(R.id.permission_stock_all);

        layout_inventory = (LinearLayout) findViewById(R.id.layout_inventory);

        check_inventory = (CheckBox) findViewById(R.id.check_inventory);

        layout_warehousing = (LinearLayout) findViewById(R.id.layout_warehousing);

        check_warehousing = (CheckBox) findViewById(R.id.check_warehousing);

        layout_want_goods = (LinearLayout) findViewById(R.id.layout_want_goods);

        check_want_goods = (CheckBox) findViewById(R.id.check_want_goods);

        layout_report_bad = (LinearLayout) findViewById(R.id.layout_report_bad);

        check_report_bad = (CheckBox) findViewById(R.id.check_report_bad);

        layout_out_goods = (LinearLayout) findViewById(R.id.layout_out_goods);

        check_out_goods = (CheckBox) findViewById(R.id.check_out_goods);

        layout_stock_warning = (LinearLayout) findViewById(R.id.layout_stock_warning);

        check_stock_warning = (CheckBox) findViewById(R.id.check_stock_warning);

        layout_all_other = (LinearLayout) findViewById(R.id.layout_all_other);

        permission_other_all = (CheckBox) findViewById(R.id.permission_other_all);

        layout_analysis_chart = (LinearLayout) findViewById(R.id.layout_analysis_chart);

        check_analysis_chart = (CheckBox) findViewById(R.id.check_analysis_chart);

        layout_cashier = (LinearLayout) findViewById(R.id.layout_cashier);

        check_cashier = (CheckBox) findViewById(R.id.check_cashier);

        layout_refund = (LinearLayout) findViewById(R.id.layout_refund);

        check_refund = (CheckBox) findViewById(R.id.check_refund);

        layout_shop_info = (LinearLayout) findViewById(R.id.layout_shop_info);

        check_shop_info = (CheckBox) findViewById(R.id.check_shop_info);

        layout_staff_power = (LinearLayout) findViewById(R.id.layout_staff_power);

        check_staff_power= (CheckBox) findViewById(R.id.check_staff_power);

    }

    @Override
    public void initData() {
        switchType();
    }

    @Override
    public void initListener() {
        position_permission_back.setOnClickListener(this);
        position_permission_confirm.setOnClickListener(this);

        clear_permission_name.setOnClickListener(this);

        layout_goods_all.setOnClickListener(this);
        layout_goods_discount.setOnClickListener(this);
        layout_up_down_goods.setOnClickListener(this);
        layout_goods_give.setOnClickListener(this);
        layout_goods_add.setOnClickListener(this);
        layout_goods_edit.setOnClickListener(this);
        layout_goods_del.setOnClickListener(this);
        layout_goods_cost.setOnClickListener(this);
        layout_goods_catalog.setOnClickListener(this);

        layout_all_member.setOnClickListener(this);
        layout_member_add.setOnClickListener(this);
        layout_member_edit.setOnClickListener(this);
        layout_member_del.setOnClickListener(this);
        layout_member_recharge.setOnClickListener(this);
        layout_member_rank.setOnClickListener(this);

        layout_all_report.setOnClickListener(this);
        layout_sale_report.setOnClickListener(this);
        layout_goods_report.setOnClickListener(this);
        layout_assign_report.setOnClickListener(this);
        layout_month_profit.setOnClickListener(this);
        layout_sale_order.setOnClickListener(this);
        layout_recharge_record.setOnClickListener(this);

        layout_all_transfer_purchase.setOnClickListener(this);
        layout_transfer_detail.setOnClickListener(this);
        layout_transfer_set.setOnClickListener(this);
        layout_purchase_order.setOnClickListener(this);
        layout_purchase_return.setOnClickListener(this);

        layout_all_stock.setOnClickListener(this);
        layout_inventory.setOnClickListener(this);
        layout_warehousing.setOnClickListener(this);
        layout_want_goods.setOnClickListener(this);
        layout_report_bad.setOnClickListener(this);
        layout_out_goods.setOnClickListener(this);
        layout_stock_warning.setOnClickListener(this);

        layout_all_other.setOnClickListener(this);
        layout_analysis_chart.setOnClickListener(this);
        layout_cashier.setOnClickListener(this);
        layout_refund.setOnClickListener(this);
        layout_shop_info.setOnClickListener(this);
        layout_staff_power.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.clear_permission_name :
                position_permission_name.setText(null);
                break;
            case R.id.position_permission_back:
                finish();
                break;
            case R.id.position_permission_confirm:
                commitPower();
                break;
            case R.id.layout_goods_all:
                allGoodsSelect();
                break;
            case R.id.layout_goods_discount:
                singleSelect(checkbox_goods_discount,1);
                break;
            case R.id.layout_up_down_goods:
                singleSelect(check_up_down_goods,1);
                break;
            case R.id.layout_goods_give:
                singleSelect(check_goods_give,1);
                break;
            case R.id.layout_goods_add:
                singleSelect(check_goods_add,1);
                break;
            case R.id.layout_goods_edit:
                singleSelect(check_goods_edit,1);
                break;
            case R.id.layout_goods_del:
                singleSelect(check_goods_del,1);
                break;
            case R.id.layout_goods_cost:
                singleSelect(check_goods_cost,1);
                break;
            case R.id.layout_goods_catalog:
                singleSelect(check_goods_catalog,1);
                break;
            case R.id.layout_all_member:
                allMemberSelect();
                break;
            case R.id.layout_member_add:
                singleSelect(check_member_add,2);
                break;
            case R.id.layout_member_edit:
                singleSelect(check_member_edit,2);
                break;
            case R.id.layout_member_del:
                singleSelect(check_member_del,2);
                break;
            case R.id.layout_member_recharge:
                singleSelect(check_member_recharge,2);
                break;
            case R.id.layout_member_rank:
                singleSelect(check_member_rank,2);
                break;
            case R.id.layout_all_report:
                allReportSelect();
                break;
            case R.id.layout_sale_report:
                singleSelect(check_sale_report,3);
                break;
            case R.id.layout_goods_report:
                singleSelect(check_goods_report,3);
                break;
            case R.id.layout_assign_report:
                singleSelect(check_assign_report,3);
                break;
            case R.id.layout_month_profit:
                singleSelect(check_month_profit,3);
                break;
            case R.id.layout_sale_order:
                singleSelect(check_sale_order,3);
                break;
            case R.id.layout_recharge_record:
                singleSelect(check_recharge_record,3);
                break;
            case R.id.layout_all_transfer_purchase:
                allTransferPurchase();
                break;
            case R.id.layout_transfer_detail:
                singleSelect(check_transfer_detail,4);
                break;
            case R.id.layout_transfer_set:
                singleSelect(check_transfer_set,4);
                break;
            case R.id.layout_purchase_order:
                singleSelect(check_purchase_order,4);
                break;
            case R.id.layout_purchase_return:
                singleSelect(check_purchase_return,4);
                break;
            case R.id.layout_all_stock:
                allStock();
                break;
            case R.id.layout_inventory:
                singleSelect(check_inventory,5);
                break;
            case R.id.layout_warehousing:
                singleSelect(check_warehousing,5);
                break;
            case R.id.layout_want_goods:
                singleSelect(check_want_goods,5);
                break;
            case R.id.layout_report_bad:
                singleSelect(check_report_bad,5);
                break;
            case R.id.layout_out_goods:
                singleSelect(check_out_goods,5);
                break;
            case R.id.layout_stock_warning:
                singleSelect(check_stock_warning,5);
                break;
            case R.id.layout_all_other:
                allOther();
                break;
            case R.id.layout_analysis_chart:
                singleSelect(check_analysis_chart,6);
                break;
            case R.id.layout_cashier:
                singleSelect(check_cashier,6);
                break;
            case R.id.layout_refund:
                singleSelect(check_refund,6);
                break;
            case R.id.layout_shop_info:
                singleSelect(check_shop_info,6);
                break;
            case R.id.layout_staff_power:
                singleSelect(check_staff_power,6);
                break;
        }
    }

    //判断该页面的类型
    private void switchType(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) viewType = bundle.getInt(PERMISSION_VIEW_TYPE,0);
        if (viewType == 0){
            //添加职位
            powerData = new PowerData();
        }else if (viewType == 1){
            //编辑职位权限
            powerData = (PowerData) bundle.get(PERMISSION_VIEW_DATA);
            if (powerData == null)return;

            position_permission_name.setText(powerData.positionname);
            position_permission_name.setSelection(powerData.positionname.length());
        }
    }

    //商品全选
    private void allGoodsSelect(){
        if (permission_product_all.isChecked()){
            permission_product_all.setChecked(false);
            checkbox_goods_discount.setChecked(false);
            check_up_down_goods.setChecked(false);
            check_goods_give.setChecked(false);
            check_goods_add.setChecked(false);
            check_goods_del.setChecked(false);
            check_goods_edit.setChecked(false);
            check_goods_cost.setChecked(false);
            check_goods_catalog.setChecked(false);
            mGoodsPower = 0 ;
        }else {
            permission_product_all.setChecked(true);
            checkbox_goods_discount.setChecked(true);
            check_up_down_goods.setChecked(true);
            check_goods_give.setChecked(true);
            check_goods_add.setChecked(true);
            check_goods_del.setChecked(true);
            check_goods_edit.setChecked(true);
            check_goods_cost.setChecked(true);
            check_goods_catalog.setChecked(true);
            mGoodsPower = 8;
        }
    }

    //会员全选
    private void allMemberSelect(){
        if (permission_member_all.isChecked()){
            permission_member_all.setChecked(false);
            check_member_add.setChecked(false);
            check_member_edit.setChecked(false);
            check_member_del.setChecked(false);
            check_member_recharge.setChecked(false);
            check_member_rank.setChecked(false);
            mMemberPower = 0;
        }else {
            permission_member_all.setChecked(true);
            check_member_add.setChecked(true);
            check_member_edit.setChecked(true);
            check_member_del.setChecked(true);
            check_member_recharge.setChecked(true);
            check_member_rank.setChecked(true);
            mMemberPower = 5;
        }
    }

    //报表全选
    private void allReportSelect(){
        if (permission_forms_all.isChecked()){
            permission_forms_all.setChecked(false);
            check_sale_report.setChecked(false);
            check_goods_report.setChecked(false);
            check_assign_report.setChecked(false);
            check_month_profit.setChecked(false);
            check_sale_order.setChecked(false);
            check_recharge_record.setChecked(false);
            mReportPower = 0;
        }else {
            permission_forms_all.setChecked(true);
            check_sale_report.setChecked(true);
            check_goods_report.setChecked(true);
            check_assign_report.setChecked(true);
            check_month_profit.setChecked(true);
            check_sale_order.setChecked(true);
            check_recharge_record.setChecked(true);
            mReportPower = 6;
        }
    }

    //交接采购全选
    private void allTransferPurchase(){
        if (permission_purchase_all.isChecked()){
            permission_purchase_all.setChecked(false);
            check_transfer_detail.setChecked(false);
            check_transfer_set.setChecked(false);
            check_purchase_order.setChecked(false);
            check_purchase_return.setChecked(false);
            mPurchasePower = 0;
        }else {
            permission_purchase_all.setChecked(true);
            check_transfer_detail.setChecked(true);
            check_transfer_set.setChecked(true);
            check_purchase_order.setChecked(true);
            check_purchase_return.setChecked(true);
            mPurchasePower = 4;
        }
    }

    //库存全选
    private void allStock(){
        if (permission_stock_all.isChecked()) {
            permission_stock_all.setChecked(false);
            check_inventory.setChecked(false);
            check_warehousing.setChecked(false);
            check_want_goods.setChecked(false);
            check_report_bad.setChecked(false);
            check_out_goods.setChecked(false);
            check_stock_warning.setChecked(false);
            mStockPower = 0;
        }else {
            permission_stock_all.setChecked(true);
            check_inventory.setChecked(true);
            check_warehousing.setChecked(true);
            check_want_goods.setChecked(true);
            check_report_bad.setChecked(true);
            check_out_goods.setChecked(true);
            check_stock_warning.setChecked(true);
            mStockPower = 6;
        }
    }

    //其他全选
    private void allOther(){
        if (permission_other_all.isChecked()){
            permission_other_all.setChecked(false);
            check_analysis_chart.setChecked(false);
            check_cashier.setChecked(false);
            check_refund.setChecked(false);
            check_shop_info.setChecked(false);
            check_staff_power.setChecked(false);
            mOtherPower = 0;
        }else {
            permission_other_all.setChecked(true);
            check_analysis_chart.setChecked(true);
            check_cashier.setChecked(true);
            check_refund.setChecked(true);
            check_shop_info.setChecked(true);
            check_staff_power.setChecked(true);
            mOtherPower = 5;
        }
    }

    //单项选中,第二个参数表示当前的单项权限的分类,1为商品，2为会员，3为报表，4为交接采购，5为库存，6为其他;第三个参数为当前权限分类已选择的数量
    private void singleSelect(CheckBox checkBox,int powerCatalog){
        if (powerCatalog == 1){
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                mGoodsPower--;
            }else {
                checkBox.setChecked(true);
                mGoodsPower++;
            }
            if (mGoodsPower == 8){
                permission_product_all.setChecked(true);
            }else if (mGoodsPower == 7){
                permission_product_all.setChecked(false);
            }
        }else if (powerCatalog == 2){
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                mMemberPower--;
            }else {
                checkBox.setChecked(true);
                mMemberPower++;
            }
            if (mMemberPower == 5){
                permission_member_all.setChecked(true);
            }else if (mMemberPower == 4){
                permission_member_all.setChecked(false);
            }
        }else if (powerCatalog == 3){
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                mReportPower--;
            }else {
                checkBox.setChecked(true);
                mReportPower++;
            }
            if (mReportPower == 6){
                permission_forms_all.setChecked(true);
            }else if (mReportPower == 5){
                permission_forms_all.setChecked(false);
            }
        }else if (powerCatalog == 4){
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                mPurchasePower--;
            }else {
                checkBox.setChecked(true);
                mPurchasePower++;
            }
            if (mPurchasePower == 4){
                permission_purchase_all.setChecked(true);
            }else if (mPurchasePower == 3){
                permission_purchase_all.setChecked(false);
            }
        }else if (powerCatalog == 5){
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                mStockPower--;
            }else {
                checkBox.setChecked(true);
                mStockPower++;
            }
            if (mStockPower == 6){
                permission_stock_all.setChecked(true);
            }else if (mStockPower == 5){
                permission_stock_all.setChecked(false);
            }
        }else if (powerCatalog == 6){
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                mOtherPower--;
            }else {
                checkBox.setChecked(true);
                mOtherPower++;
            }
            if (mOtherPower == 4){
                permission_other_all.setChecked(true);
            }else if (mOtherPower == 3){
                permission_other_all.setChecked(false);
            }
        }
    }

    //添加或修改职务
    private void commitPower(){
        String name = position_permission_name.getText().toString();
        if (TextUtil.isEmpty(name)){
            Toast.show(context,"请输入职位名称");
            position_permission_name.requestFocus();
            return;
        }
        showLoading("正在提交数据...");

        PowerData power = new PowerData();

        if (viewType == 0){
            RStaffManager.getRStaffManager().rPositionAdd(this, power, new IRequestCallBack() {
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
        }else if (viewType == 1){
            RStaffManager.getRStaffManager().rPositionEdit(this, power, new IRequestCallBack() {
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
    }

}
