package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.MDGPromotionAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsSpec;
import com.poso2o.lechuan.bean.marketdata.DiscountGoodsData;
import com.poso2o.lechuan.bean.marketdata.MarketingDGData;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.dialog.MDiscountInputDialog;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.DateTimeUtil;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/11/4.
 * 折扣商品活动主界面
 */

public class DiscountGoodsActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //选择折扣商品页面跳转请求码
    public static final int SELECT_DG_CODE = 1000;

    //返回
    private ImageView discount_goods_back;
    //确定
    private Button discount_goods_ok;

    //活动名称
    private EditText discount_goods_name;

    //开始时间
    private TextView discount_goods_begin;
    //结束时间
    private TextView discount_goods_end;

    //折扣商品数量
    private TextView discount_goods_num;
    //添加折扣商品
    private TextView discount_goods_add;

    //批量打折
    private Button batch_discount;

    //折扣商品列表
    private RecyclerView discount_goods_recycler;
    //列表适配器
    private MDGPromotionAdapter promotionAdapter;
    //折扣商品列表数据
    private ArrayList<DiscountGoodsData> discountGoodsDatas = new ArrayList<>();

//    private SharedPreferencesUtil sharedPreferencesUtil;

    //点击的是开始时间还是结束时间
    private boolean isBeginTime;
    //开始时间
    private String beginTime;
    //结束时间
    private String endTime;

    //编辑页时传递进来的数据
    private MarketingDGData marketingDGData;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_discount_goods;
    }

    @Override
    public void initView() {
        context = this;

        discount_goods_back = (ImageView) findViewById(R.id.discount_goods_back);
        discount_goods_ok = (Button) findViewById(R.id.discount_goods_ok);

        discount_goods_name = (EditText) findViewById(R.id.discount_goods_name);

        discount_goods_begin = (TextView) findViewById(R.id.discount_goods_begin);
        discount_goods_end = (TextView) findViewById(R.id.discount_goods_end);

        discount_goods_num = (TextView) findViewById(R.id.discount_goods_num);
        discount_goods_add = (TextView) findViewById(R.id.discount_goods_add);

        batch_discount = (Button) findViewById(R.id.batch_discount);

        discount_goods_recycler = (RecyclerView) findViewById(R.id.discount_goods_recycler);
        discount_goods_ok.setSelected(true);
        batch_discount.setSelected(true);
    }

    @Override
    public void initData() {
//        sharedPreferencesUtil = new SharedPreferencesUtil(context, LoginMemory.LOGIN_INFO);
        initAdapter();
        initEditData();
    }

    @Override
    public void initListener() {
        discount_goods_back.setOnClickListener(this);
        discount_goods_ok.setOnClickListener(this);

        discount_goods_begin.setOnClickListener(this);
        discount_goods_end.setOnClickListener(this);

        discount_goods_add.setOnClickListener(this);

        batch_discount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.discount_goods_back:
                finish();
                break;
            case R.id.discount_goods_ok:
                clickOK();
                break;
            case R.id.discount_goods_begin:
                isBeginTime = true;
                showCalendar();
                break;
            case R.id.discount_goods_end:
                isBeginTime = false;
                showCalendar();
                break;
            case R.id.discount_goods_add:
                Intent intent = new Intent();
                intent.setClass(context,MSelectDGActivity.class);
                startActivityForResult(intent,SELECT_DG_CODE);
                break;
            case R.id.batch_discount:
                batchDiscount();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SELECT_DG_CODE){
            ArrayList<Goods> goods = (ArrayList<Goods>) data.getExtras().get(MSelectDGActivity.DG_SELECT_DATA);
            notifyPromotion(goods);
        }
    }

    //选择时间
    private void showCalendar(){
        final CalendarDialog calendarDialog = new CalendarDialog(context);
        calendarDialog.show();
        calendarDialog.setOnDateSelectListener(new CalendarDialog.OnDateSelectListener() {
            @Override
            public void onDateSelect(CustomDate date) {
                if (date == null) return;
                String dateT = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                if (isBeginTime) {
                    String str = discount_goods_end.getText().toString();
                    if (str == null || str.equals("")) {
                        discount_goods_begin.setText(dateT);
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        calendarDialog.dismiss();
                    } else if (CalendarUtil.TimeCompare(dateT, str)) {
                        discount_goods_begin.setText(dateT);
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        calendarDialog.dismiss();
                    } else {
                        Toast.show(context, "选择的时间范围不正确");
                    }
                } else {
                    String str = discount_goods_begin.getText().toString();
                    if (str == null || str.equals("")) {
                        discount_goods_end.setText(dateT);
                        endTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                        calendarDialog.dismiss();
                    } else if (CalendarUtil.TimeCompare(str, dateT)) {
                        discount_goods_end.setText(dateT);
                        endTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                        calendarDialog.dismiss();
                    } else {
                        Toast.show(context, "选择的时间范围不正确");
                    }
                }
            }
        });
    }

    private void initAdapter(){
        promotionAdapter = new MDGPromotionAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        discount_goods_recycler.setLayoutManager(linearLayoutManager);
        discount_goods_recycler.setAdapter(promotionAdapter);

        promotionAdapter.setOnMDGPListener(new MDGPromotionAdapter.OnMDGPListener() {
            @Override
            public void onMDGPDel(DiscountGoodsData discountGoodsData) {
                discountGoodsDatas.remove(discountGoodsData);
                promotionAdapter.notifyDataSetChanged();
                discount_goods_num.setText("打折商品（共" + discountGoodsDatas.size() + "件）");
            }
        });
    }

    //刷新商品数据
    private void notifyPromotion(ArrayList<Goods> goodses){
        if (goodses == null){
            return;
        }
        ArrayList<DiscountGoodsData> temp = new ArrayList<>();
        for (Goods item : goodses){
            boolean isIn = false;
            for (DiscountGoodsData goodsData : discountGoodsDatas){
                if (item.goods_id.equals(goodsData.goods_id)){
                    isIn = true;
                    break ;
                }
            }
            if (!isIn){
                DiscountGoodsData discountGoodsData = new DiscountGoodsData();
                discountGoodsData.goods_id = item.goods_id;
                discountGoodsData.main_picture = item.main_picture;
                discountGoodsData.goods_name = item.goods_name;
                discountGoodsData.goods_no = item.goods_no;
                discountGoodsData.goods_discount = NumberFormatUtils.format(item.goods_discount);
                discountGoodsData.goods_price_section_mix = NumberFormatUtils.format(caculateMix(item.goods_spec));
                discountGoodsData.goods_price_section_max = NumberFormatUtils.format(caculateMax(item.goods_spec));
                discountGoodsData.goods_price_section = item.goods_price_section;
                discountGoodsData.sale_amount = item.goods_sale_number + "";
                temp.add(discountGoodsData);
            }
        }
        discountGoodsDatas.addAll(temp);
        if (promotionAdapter != null)promotionAdapter.notifyData(discountGoodsDatas);
        discount_goods_num.setText("打折商品（共" + discountGoodsDatas.size() + "件）");
    }

    private double caculateMix(ArrayList<GoodsSpec> specs){
        if (specs == null)return 0;
        if (specs.size() == 1)return specs.get(0).spec_price;
        double temp = specs.get(0).spec_price;
        for (int i = 1;i<specs.size();i++){
            if (temp > specs.get(i).spec_price){
                temp = specs.get(i).spec_price;
            }
        }
        return temp;
    }

    private double caculateMax(ArrayList<GoodsSpec> specs){
        if (specs == null)return 0;
        if (specs.size() == 1)return specs.get(0).spec_price;
        double temp = specs.get(0).spec_price;
        for (int i = 1;i<specs.size();i++){
            if (temp < specs.get(i).spec_price){
                temp = specs.get(i).spec_price;
            }
        }
        return temp;
    }

    //添加折扣商品活动
    private void clickOK(){
        String name = discount_goods_name.getText().toString();
        if (name == null || name.equals("")){
            Toast.show(context, "请输入活动名称");
            discount_goods_name.requestFocus();
            return;
        }
        if (beginTime == null || beginTime.equals("")){
            Toast.show(context, "请选择开始时间");
            return;
        }
        if (endTime == null || endTime.equals("")){
            Toast.show(context, "请选择结束时间");
            return;
        }
        if (marketingDGData == null){
            add(name);
        }else {
            edit(name);
        }
    }

    //添加
    private void add(String name){
        /*showReadDialog();
        DiscountGoodsManager.getDiscountGoodsManager().addDiscountGoods(this, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), name, beginTime, endTime, discountGoodsDatas, new DiscountGoodsManager.OnAddDGListener() {
            @Override
            public void onAddDGSuccess(BaseActivity baseActivity) {
                dismissReadDialog();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onAddDGFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(baseActivity,failStr).show();
            }
        });*/
    }

    //编辑
    private void edit(String name){
        /*showReadDialog();
        DiscountGoodsManager.getDiscountGoodsManager().editDiscountGoods(this, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), marketingDGData.promotion_id, name, beginTime, endTime,discountGoodsDatas, new DiscountGoodsManager.OnEditDGListener() {
            @Override
            public void onEditDGSuccess(BaseActivity baseActivity) {
                dismissReadDialog();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onEditDGFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(baseActivity,failStr).show();
            }
        });*/
    }

    //判断当前是添加还是编辑
    private void initEditData(){
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)return;
        marketingDGData = (MarketingDGData) bundle.get(RMarketActivity.DISCOUNT_GOODS_DATA);
        if (marketingDGData == null)return;
        discount_goods_name.setText(marketingDGData.promotion_name);
        discount_goods_name.setSelection(marketingDGData.promotion_name.length());
        discount_goods_begin.setText(DateTimeUtil.StringToData(marketingDGData.begin_time,"yyyy-MM-dd"));
        discount_goods_end.setText(DateTimeUtil.StringToData(marketingDGData.end_time,"yyyy-MM-dd"));
        beginTime = marketingDGData.begin_time;
        endTime = marketingDGData.end_time;
        discountGoodsDatas = marketingDGData.promotionGoodsDetails;
        discount_goods_num.setText("打折商品（共" + discountGoodsDatas.size() + "件）");
        if (promotionAdapter != null)promotionAdapter.notifyData(discountGoodsDatas);
    }

    //批量折扣
    private void batchDiscount(){
        if (discountGoodsDatas.size() == 0){
            Toast.show(context,"请先选择商品");
            return;
        }
        MDiscountInputDialog inputDialog = new MDiscountInputDialog(context);
        inputDialog.show();
        inputDialog.setOnDiscountListener(new MDiscountInputDialog.OnDiscountListener() {
            @Override
            public void onDiscount(double discount) {
                for (DiscountGoodsData goodsData : discountGoodsDatas){
                    goodsData.goods_discount = discount + "";
                }
                if (promotionAdapter != null)promotionAdapter.notifyData(discountGoodsDatas);
            }
        });
    }
}
