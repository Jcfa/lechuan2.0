package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.PowerAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.powerdata.LocalPowerCatalogData;
import com.poso2o.lechuan.bean.powerdata.LocalPowerData;
import com.poso2o.lechuan.bean.powerdata.PowerData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RStaffManager;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/12/22.
 */

public class EditPositionActivity extends BaseActivity implements View.OnClickListener{

    //类型：1为编辑权限，0为添加职位
    public final static String E_PERMISSION_VIEW_TYPE = "permission_type";
    public final static String E_PERMISSION_VIEW_DATA = "permission_data";

    private Context context;

    //返回
    private ImageView edit_permission_back;
    //确定
    private Button edit_permission_confirm;

    //职位名称
    private EditText edit_permission_name;
    //清除职位名称
    private ImageView clear_permission_name;

    //权限列表
    private ExpandableListView edit_power_list;

    //职位信息
    private PowerData powerData;
    //添加？编辑  0为添加职位，1为编辑职位
    private int mType;

    private PowerAdapter powerAdapter;

    private ArrayList<LocalPowerCatalogData> power_catalog = new ArrayList<>();
    private ArrayList<ArrayList<LocalPowerData>> power_list = new ArrayList<>();
    //商品分类列表
    private LocalPowerCatalogData goodsAll;
    //会员分类列表
    private LocalPowerCatalogData memAll;
    //订单分类列表
    private LocalPowerCatalogData orderAll;
    //库存分类列表
    private LocalPowerCatalogData stockAll;
    //采购分类列表
    private LocalPowerCatalogData purchaseAll;
    //报表分类列表
    private LocalPowerCatalogData reportAll;
    //店铺分类列表
    private LocalPowerCatalogData shopAll;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_position;
    }

    @Override
    protected void initView() {
        context = this;
        edit_permission_back = (ImageView) findViewById(R.id.edit_permission_back);

        edit_permission_confirm = (Button) findViewById(R.id.edit_permission_confirm);

        edit_permission_name = (EditText) findViewById(R.id.edit_permission_name);
        clear_permission_name = (ImageView) findViewById(R.id.clear_permission_name);

        edit_power_list = (ExpandableListView) findViewById(R.id.edit_power_list);
    }

    @Override
    protected void initData() {
        switchType();
    }

    @Override
    protected void initListener() {
        edit_permission_back.setOnClickListener(this);
        edit_permission_confirm.setOnClickListener(this);
        clear_permission_name.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_permission_back:
                finish();
                break;
            case R.id.edit_permission_confirm:
                onOkClick();
                break;
            case R.id.clear_permission_name:
                edit_permission_name.setText(null);
                break;
        }
    }

    //添加职位？编辑职位
    private void switchType(){

        goodsAll = new LocalPowerCatalogData();
        goodsAll.power_catalog_name = "商品管理";

        memAll = new LocalPowerCatalogData();
        memAll.power_catalog_name = "会员管理";

        orderAll = new LocalPowerCatalogData();
        orderAll.power_catalog_name = "订单管理";

        stockAll = new LocalPowerCatalogData();
        stockAll.power_catalog_name = "库存";

        purchaseAll = new LocalPowerCatalogData();
        purchaseAll.power_catalog_name = "采购";

        reportAll = new LocalPowerCatalogData();
        reportAll.power_catalog_name = "报表";

        shopAll = new LocalPowerCatalogData();
        shopAll.power_catalog_name = "店铺";

        power_catalog.add(goodsAll);
        power_catalog.add(memAll);
        power_catalog.add(orderAll);
        power_catalog.add(stockAll);
        power_catalog.add(purchaseAll);
        power_catalog.add(reportAll);
        power_catalog.add(shopAll);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) mType = bundle.getInt(E_PERMISSION_VIEW_TYPE,0);
        if (mType == 0){
            //添加职位
            powerData = new PowerData();
            initAddData();
        }else if (mType == 1){
            powerData = (PowerData) bundle.get(E_PERMISSION_VIEW_DATA);
            if (powerData == null)return;
            initEditPower();
        }

        powerAdapter = new PowerAdapter(this);
        edit_power_list.setAdapter(powerAdapter);
        powerAdapter.notifyAdapter(power_catalog,power_list);

        //去掉默认箭头
        edit_power_list.setGroupIndicator(null);
        //默认打开所有项
        for (int i=0;i< powerAdapter.getGroupCount();i++){
            edit_power_list.expandGroup(i);
        }

        edit_power_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                boolean b = !power_catalog.get(i).is_all_on;
                power_catalog.get(i).is_all_on = b;
                ArrayList<LocalPowerData> catalog = power_list.get(i);
                for (LocalPowerData powerData : catalog){
                    powerData.power_on = b;
                }
                powerAdapter.notifyDataSetChanged();
                return true;
            }
        });

        edit_power_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                power_list.get(i).get(i1).power_on = !power_list.get(i).get(i1).power_on;
                selectAll(i);
                powerAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    //初始化添加职位的权限列表
    private void initAddData(){
        //power_type 为  0：商品管理； 1：会员管理；2：订单管理；3：库存；4：采购；5：报表；6：店铺
        ArrayList<LocalPowerData> goodsList = new ArrayList<>();

        LocalPowerData addGoods = new LocalPowerData();
        addGoods.power_name = "增加商品";
        addGoods.power_type = 0;
        addGoods.power_on = false;
        goodsList.add(addGoods);

        LocalPowerData editGoods = new LocalPowerData();
        editGoods.power_name = "修改商品";
        editGoods.power_type = 0;
        editGoods.power_on = false;
        goodsList.add(editGoods);

        LocalPowerData delGodos = new LocalPowerData();
        delGodos.power_name = "删除商品";
        delGodos.power_type = 0;
        delGodos.power_on = false;
        goodsList.add(delGodos);

        ArrayList<LocalPowerData> memberList = new ArrayList<>();
        LocalPowerData addMem = new LocalPowerData();
        addMem.power_name = "增加会员";
        addMem.power_type = 1;
        addMem.power_on = false;
        memberList.add(addMem);

        LocalPowerData editMem = new LocalPowerData();
        editMem.power_name = "修改会员";
        editMem.power_type = 1;
        editMem.power_on = false;
        memberList.add(editMem);

        LocalPowerData delMem = new LocalPowerData();
        delMem.power_name = "删除会员";
        delMem.power_type = 1;
        delMem.power_on = false;
        memberList.add(delMem);

        LocalPowerData chargeMem = new LocalPowerData();
        chargeMem.power_name = "会员充值";
        chargeMem.power_type = 1;
        chargeMem.power_on = false;
        memberList.add(chargeMem);

        ArrayList<LocalPowerData> orderList = new ArrayList<>();
        LocalPowerData addOrder = new LocalPowerData();
        addOrder.power_name = "增加订单";
        addOrder.power_type = 2;
        addOrder.power_on = false;
        orderList.add(addOrder);

        LocalPowerData editOrder = new LocalPowerData();
        editOrder.power_name = "修改订单";
        editOrder.power_type = 2;
        editOrder.power_on = false;
        orderList.add(editOrder);

        LocalPowerData delOrder = new LocalPowerData();
        delOrder.power_name = "删除订单";
        delOrder.power_type = 2;
        delOrder.power_on = false;
        orderList.add(delOrder);

        LocalPowerData orderSend = new LocalPowerData();
        orderSend.power_name = "订单发货";
        orderSend.power_type = 2;
        orderSend.power_on = false;
        orderList.add(orderSend);

        LocalPowerData checkOrder = new LocalPowerData();
        checkOrder.power_name = "订单查看";
        checkOrder.power_type = 2;
        checkOrder.power_on = false;
        orderList.add(checkOrder);

        LocalPowerData discountOrder = new LocalPowerData();
        discountOrder.power_name = "整单打折";
        discountOrder.power_type = 2;
        discountOrder.power_on = false;
        orderList.add(discountOrder);

        ArrayList<LocalPowerData> stockList = new ArrayList<>();
        LocalPowerData inGoods = new LocalPowerData();
        inGoods.power_name = "进货单";
        inGoods.power_type = 3;
        inGoods.power_on = false;
        stockList.add(inGoods);

        LocalPowerData outGoods = new LocalPowerData();
        outGoods.power_name = "出货单";
        outGoods.power_type = 3;
        outGoods.power_on = false;
        stockList.add(outGoods);

        LocalPowerData returnGoods = new LocalPowerData();
        returnGoods.power_name = "退货单";
        returnGoods.power_type = 3;
        returnGoods.power_on = false;
        stockList.add(returnGoods);

        LocalPowerData matchGoods = new LocalPowerData();
        matchGoods.power_name = "配货单";
        matchGoods.power_type = 3;
        matchGoods.power_on = false;
        stockList.add(matchGoods);

        LocalPowerData badGoods = new LocalPowerData();
        badGoods.power_name = "报损单";
        badGoods.power_type = 3;
        badGoods.power_on = false;
        stockList.add(badGoods);

        LocalPowerData inventeryGoods = new LocalPowerData();
        inventeryGoods.power_name = "盘点单";
        inventeryGoods.power_type = 3;
        inventeryGoods.power_on = false;
        stockList.add(inventeryGoods);

        ArrayList<LocalPowerData> purchaseList = new ArrayList<>();
        LocalPowerData supplier = new LocalPowerData();
        supplier.power_name = "供应商";
        supplier.power_type = 4;
        supplier.power_on = false;
        purchaseList.add(supplier);

        LocalPowerData purchasecontract = new LocalPowerData();
        purchasecontract.power_name = "采购合同";
        purchasecontract.power_type = 4;
        purchasecontract.power_on = false;
        purchaseList.add(purchasecontract);

        LocalPowerData dinghuohui = new LocalPowerData();
        dinghuohui.power_name = "订货会";
        dinghuohui.power_type = 4;
        dinghuohui.power_on = false;
        purchaseList.add(dinghuohui);

        ArrayList<LocalPowerData> reportList = new ArrayList<>();
        LocalPowerData bossTotal = new LocalPowerData();
        bossTotal.power_name = "主页统计";
        bossTotal.power_type = 5;
        bossTotal.power_on = false;
        reportList.add(bossTotal);

        LocalPowerData saleReport = new LocalPowerData();
        saleReport.power_name = "销售统计";
        saleReport.power_type = 5;
        saleReport.power_on = false;
        reportList.add(saleReport);

        LocalPowerData goodsReport = new LocalPowerData();
        goodsReport.power_name = "商品统计";
        goodsReport.power_type = 5;
        goodsReport.power_on = false;
        reportList.add(goodsReport);

        LocalPowerData assignReport = new LocalPowerData();
        assignReport.power_name = "业绩统计";
        assignReport.power_type = 5;
        assignReport.power_on = false;
        reportList.add(assignReport);

        LocalPowerData monthProfit = new LocalPowerData();
        monthProfit.power_name = "月损益表";
        monthProfit.power_type = 5;
        monthProfit.power_on = false;
        reportList.add(monthProfit);

        ArrayList<LocalPowerData> shopList = new ArrayList<>();
        LocalPowerData shopSet = new LocalPowerData();
        shopSet.power_name = "店铺设置";
        shopSet.power_type = 6;
        shopSet.power_on = false;
        shopList.add(shopSet);

        LocalPowerData userSet = new LocalPowerData();
        userSet.power_name = "用户设置";
        userSet.power_type = 6;
        userSet.power_on = false;
        shopList.add(userSet);

        LocalPowerData powerSet = new LocalPowerData();
        powerSet.power_name = "权限设置";
        powerSet.power_type = 6;
        powerSet.power_on = false;
        shopList.add(powerSet);

        power_list.add(goodsList);
        power_list.add(memberList);
        power_list.add(orderList);
        power_list.add(stockList);
        power_list.add(purchaseList);
        power_list.add(reportList);
        power_list.add(shopList);

        goodsAll.is_all_on = false;
        memAll.is_all_on = false;
        orderAll.is_all_on = false;
        stockAll.is_all_on = false;
        purchaseAll.is_all_on = false;
        reportAll.is_all_on = false;
        shopAll.is_all_on = false;
    }

    //初始化编辑权限列表
    private void initEditPower(){
        edit_permission_name.setText(powerData.positionname);
        edit_permission_name.setSelection(powerData.positionname.length());

        //power_type 为  0：商品管理； 1：会员管理；2：订单管理；3：库存；4：采购；5：报表；6：店铺
        ArrayList<LocalPowerData> goodsList = new ArrayList<>();

        LocalPowerData addGoods = new LocalPowerData();
        addGoods.power_name = "增加商品";
        addGoods.power_type = 0;
        addGoods.power_on = isTrue(powerData.product_add);
        goodsList.add(addGoods);

        LocalPowerData editGoods = new LocalPowerData();
        editGoods.power_name = "修改商品";
        editGoods.power_type = 0;
        editGoods.power_on = isTrue(powerData.product_edit);
        goodsList.add(editGoods);

        LocalPowerData delGodos = new LocalPowerData();
        delGodos.power_name = "删除商品";
        delGodos.power_type = 0;
        delGodos.power_on = isTrue(powerData.product_del);
        goodsList.add(delGodos);

        ArrayList<LocalPowerData> memberList = new ArrayList<>();
        LocalPowerData addMem = new LocalPowerData();
        addMem.power_name = "增加会员";
        addMem.power_type = 1;
        addMem.power_on = isTrue(powerData.client_add);
        memberList.add(addMem);

        LocalPowerData editMem = new LocalPowerData();
        editMem.power_name = "修改会员";
        editMem.power_type = 1;
        editMem.power_on = isTrue(powerData.client_edit);
        memberList.add(editMem);

        LocalPowerData delMem = new LocalPowerData();
        delMem.power_name = "删除会员";
        delMem.power_type = 1;
        delMem.power_on = isTrue(powerData.client_del);
        memberList.add(delMem);

        LocalPowerData chargeMem = new LocalPowerData();
        chargeMem.power_name = "会员充值";
        chargeMem.power_type = 1;
        chargeMem.power_on = isTrue(powerData.client_payment);
        memberList.add(chargeMem);

        ArrayList<LocalPowerData> orderList = new ArrayList<>();
        LocalPowerData addOrder = new LocalPowerData();
        addOrder.power_name = "增加订单";
        addOrder.power_type = 2;
        addOrder.power_on = isTrue(powerData.order_add);
        orderList.add(addOrder);

        LocalPowerData editOrder = new LocalPowerData();
        editOrder.power_name = "修改订单";
        editOrder.power_type = 2;
        editOrder.power_on = isTrue(powerData.order_edit);
        orderList.add(editOrder);

        LocalPowerData delOrder = new LocalPowerData();
        delOrder.power_name = "删除订单";
        delOrder.power_type = 2;
        delOrder.power_on = isTrue(powerData.order_del);
        orderList.add(delOrder);

        LocalPowerData orderSend = new LocalPowerData();
        orderSend.power_name = "订单发货";
        orderSend.power_type = 2;
        orderSend.power_on = isTrue(powerData.order_fh);
        orderList.add(orderSend);

        LocalPowerData checkOrder = new LocalPowerData();
        checkOrder.power_name = "订单查看";
        checkOrder.power_type = 2;
        checkOrder.power_on = isTrue(powerData.order_query);
        orderList.add(checkOrder);

        LocalPowerData discountOrder = new LocalPowerData();
        discountOrder.power_name = "整单打折";
        discountOrder.power_type = 2;
        discountOrder.power_on = isTrue(powerData.order_discount);
        orderList.add(discountOrder);

        ArrayList<LocalPowerData> stockList = new ArrayList<>();
        LocalPowerData inGoods = new LocalPowerData();
        inGoods.power_name = "进货单";
        inGoods.power_type = 3;
        inGoods.power_on = isTrue(powerData.kc_add);
        stockList.add(inGoods);

        LocalPowerData outGoods = new LocalPowerData();
        outGoods.power_name = "出货单";
        outGoods.power_type = 3;
        outGoods.power_on = isTrue(powerData.kc_del);
        stockList.add(outGoods);

        LocalPowerData returnGoods = new LocalPowerData();
        returnGoods.power_name = "退货单";
        returnGoods.power_type = 3;
        returnGoods.power_on = isTrue(powerData.kc_back);
        stockList.add(returnGoods);

        LocalPowerData matchGoods = new LocalPowerData();
        matchGoods.power_name = "配货单";
        matchGoods.power_type = 3;
        matchGoods.power_on = isTrue(powerData.kc_booking);
        stockList.add(matchGoods);

        LocalPowerData badGoods = new LocalPowerData();
        badGoods.power_name = "报损单";
        badGoods.power_type = 3;
        badGoods.power_on = isTrue(powerData.kc_bug);
        stockList.add(badGoods);

        LocalPowerData inventeryGoods = new LocalPowerData();
        inventeryGoods.power_name = "盘点单";
        inventeryGoods.power_type = 3;
        inventeryGoods.power_on = isTrue(powerData.kc_update);
        stockList.add(inventeryGoods);

        ArrayList<LocalPowerData> purchaseList = new ArrayList<>();
        LocalPowerData supplier = new LocalPowerData();
        supplier.power_name = "供应商";
        supplier.power_type = 4;
        supplier.power_on = isTrue(powerData.factory);
        purchaseList.add(supplier);

        LocalPowerData purchasecontract = new LocalPowerData();
        purchasecontract.power_name = "采购合同";
        purchasecontract.power_type = 4;
        purchasecontract.power_on = isTrue(powerData.factory_contract);
        purchaseList.add(purchasecontract);

        LocalPowerData dinghuohui = new LocalPowerData();
        dinghuohui.power_name = "订货会";
        dinghuohui.power_type = 4;
        dinghuohui.power_on = isTrue(powerData.order_quote);
        purchaseList.add(dinghuohui);

        ArrayList<LocalPowerData> reportList = new ArrayList<>();
        LocalPowerData bossTotal = new LocalPowerData();
        bossTotal.power_name = "主页统计";
        bossTotal.power_type = 5;
        bossTotal.power_on = isTrue(powerData.indextotal);
        reportList.add(bossTotal);

        LocalPowerData saleReport = new LocalPowerData();
        saleReport.power_name = "销售统计";
        saleReport.power_type = 5;
        saleReport.power_on = isTrue(powerData.total_sales);
        reportList.add(saleReport);

        LocalPowerData goodsReport = new LocalPowerData();
        goodsReport.power_name = "商品统计";
        goodsReport.power_type = 5;
        goodsReport.power_on = isTrue(powerData.total_product);
        reportList.add(goodsReport);

        LocalPowerData assignReport = new LocalPowerData();
        assignReport.power_name = "业绩统计";
        assignReport.power_type = 5;
        assignReport.power_on = isTrue(powerData.total_czy);
        reportList.add(assignReport);

        LocalPowerData monthProfit = new LocalPowerData();
        monthProfit.power_name = "月损益表";
        monthProfit.power_type = 5;
        monthProfit.power_on = isTrue(powerData.total_shop);
        reportList.add(monthProfit);

        ArrayList<LocalPowerData> shopList = new ArrayList<>();
        LocalPowerData shopSet = new LocalPowerData();
        shopSet.power_name = "店铺设置";
        shopSet.power_type = 6;
        shopSet.power_on = isTrue(powerData.shop_seting);
        shopList.add(shopSet);

        LocalPowerData userSet = new LocalPowerData();
        userSet.power_name = "用户设置";
        userSet.power_type = 6;
        userSet.power_on = isTrue(powerData.user_seting);
        shopList.add(userSet);

        LocalPowerData powerSet = new LocalPowerData();
        powerSet.power_name = "权限设置";
        powerSet.power_type = 6;
        powerSet.power_on = isTrue(powerData.user_position);
        shopList.add(powerSet);

        power_list.add(goodsList);
        power_list.add(memberList);
        power_list.add(orderList);
        power_list.add(stockList);
        power_list.add(purchaseList);
        power_list.add(reportList);
        power_list.add(shopList);

        goodsAll.is_all_on = true;
        for (LocalPowerData power : goodsList){
            if (!power.power_on){
                goodsAll.is_all_on = false;
                break;
            }
        }

        memAll.is_all_on = true;
        for (LocalPowerData power : memberList){
            if (!power.power_on){
                memAll.is_all_on = false;
                break;
            }
        }

        orderAll.is_all_on = true;
        for (LocalPowerData power : orderList){
            if (!power.power_on){
                orderAll.is_all_on = false;
                break;
            }
        }

        stockAll.is_all_on = true;
        for (LocalPowerData power : stockList){
            if (!power.power_on){
                stockAll.is_all_on = false;
                break;
            }
        }

        purchaseAll.is_all_on = true;
        for (LocalPowerData power : purchaseList){
            if (!power.power_on){
                purchaseAll.is_all_on = false;
                break;
            }
        }

        reportAll.is_all_on = true;
        for (LocalPowerData power : reportList){
            if (!power.power_on){
                reportAll.is_all_on = false;
                break;
            }
        }

        shopAll.is_all_on = true;
        for (LocalPowerData power : shopList){
            if (!power.power_on){
                shopAll.is_all_on = false;
                break;
            }
        }
    }

    //确定
    private void onOkClick(){
        String name = edit_permission_name.getText().toString();
        if (name == null || name.equals("")){
            Toast.show(context,"请输入职位名称");
            edit_permission_name.requestFocus();
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

        powerData.product_add = zeroOrOne(power_list.get(0).get(0).power_on);
        powerData.product_edit = zeroOrOne(power_list.get(0).get(1).power_on);
        powerData.product_del = zeroOrOne(power_list.get(0).get(2).power_on);

        powerData.client_add = zeroOrOne(power_list.get(1).get(0).power_on);
        powerData.client_edit = zeroOrOne(power_list.get(1).get(1).power_on);
        powerData.client_del = zeroOrOne(power_list.get(1).get(2).power_on);
        powerData.client_payment = zeroOrOne(power_list.get(1).get(3).power_on);

        powerData.order_add = zeroOrOne(power_list.get(2).get(0).power_on);
        powerData.order_edit = zeroOrOne(power_list.get(2).get(1).power_on);
        powerData.order_del = zeroOrOne(power_list.get(2).get(2).power_on);
        powerData.order_fh = zeroOrOne(power_list.get(2).get(3).power_on);
        powerData.order_query = zeroOrOne(power_list.get(2).get(4).power_on);
        powerData.order_discount = zeroOrOne(power_list.get(2).get(5).power_on);

        powerData.kc_add = zeroOrOne(power_list.get(3).get(0).power_on);
        powerData.kc_del = zeroOrOne(power_list.get(3).get(1).power_on);
        powerData.kc_back = zeroOrOne(power_list.get(3).get(2).power_on);
        powerData.kc_booking = zeroOrOne(power_list.get(3).get(3).power_on);
        powerData.kc_bug = zeroOrOne(power_list.get(3).get(4).power_on);
        powerData.kc_update = zeroOrOne(power_list.get(3).get(5).power_on);

        powerData.factory = zeroOrOne(power_list.get(4).get(0).power_on);
        powerData.factory_contract = zeroOrOne(power_list.get(4).get(1).power_on);
        powerData.order_quote = zeroOrOne(power_list.get(4).get(2).power_on);

        powerData.indextotal = zeroOrOne(power_list.get(5).get(0).power_on);
        powerData.total_sales = zeroOrOne(power_list.get(5).get(1).power_on);
        powerData.total_product = zeroOrOne(power_list.get(5).get(2).power_on);
        powerData.total_czy = zeroOrOne(power_list.get(5).get(3).power_on);
        powerData.total_shop = zeroOrOne(power_list.get(5).get(4).power_on);

        powerData.shop_seting = zeroOrOne(power_list.get(6).get(0).power_on);
        powerData.user_seting = zeroOrOne(power_list.get(6).get(1).power_on);
        powerData.user_position = zeroOrOne(power_list.get(6).get(2).power_on);

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

        powerData.product_add = zeroOrOne(power_list.get(0).get(0).power_on);
        powerData.product_edit = zeroOrOne(power_list.get(0).get(1).power_on);
        powerData.product_del = zeroOrOne(power_list.get(0).get(2).power_on);

        powerData.client_add = zeroOrOne(power_list.get(1).get(0).power_on);
        powerData.client_edit = zeroOrOne(power_list.get(1).get(1).power_on);
        powerData.client_del = zeroOrOne(power_list.get(1).get(2).power_on);
        powerData.client_payment = zeroOrOne(power_list.get(1).get(3).power_on);

        powerData.order_add = zeroOrOne(power_list.get(2).get(0).power_on);
        powerData.order_edit = zeroOrOne(power_list.get(2).get(1).power_on);
        powerData.order_del = zeroOrOne(power_list.get(2).get(2).power_on);
        powerData.order_fh = zeroOrOne(power_list.get(2).get(3).power_on);
        powerData.order_query = zeroOrOne(power_list.get(2).get(4).power_on);
        powerData.order_discount = zeroOrOne(power_list.get(2).get(5).power_on);

        powerData.kc_add = zeroOrOne(power_list.get(3).get(0).power_on);
        powerData.kc_del = zeroOrOne(power_list.get(3).get(1).power_on);
        powerData.kc_back = zeroOrOne(power_list.get(3).get(2).power_on);
        powerData.kc_booking = zeroOrOne(power_list.get(3).get(3).power_on);
        powerData.kc_bug = zeroOrOne(power_list.get(3).get(4).power_on);
        powerData.kc_update = zeroOrOne(power_list.get(3).get(5).power_on);

        powerData.factory = zeroOrOne(power_list.get(4).get(0).power_on);
        powerData.factory_contract = zeroOrOne(power_list.get(4).get(1).power_on);
        powerData.order_quote = zeroOrOne(power_list.get(4).get(2).power_on);

        powerData.indextotal = zeroOrOne(power_list.get(5).get(0).power_on);
        powerData.total_sales = zeroOrOne(power_list.get(5).get(1).power_on);
        powerData.total_product = zeroOrOne(power_list.get(5).get(2).power_on);
        powerData.total_czy = zeroOrOne(power_list.get(5).get(3).power_on);
        powerData.total_shop = zeroOrOne(power_list.get(5).get(4).power_on);

        powerData.shop_seting = zeroOrOne(power_list.get(6).get(0).power_on);
        powerData.user_seting = zeroOrOne(power_list.get(6).get(1).power_on);
        powerData.user_position = zeroOrOne(power_list.get(6).get(2).power_on);

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

    //判断全选状态
    private void selectAll(int type){
        ArrayList<LocalPowerData> temp = power_list.get(type);
        power_catalog.get(type).is_all_on = true;
        for (LocalPowerData power : temp){
            if (!power.power_on){
                power_catalog.get(type).is_all_on = false;
                break;
            }
        }
    }
}
