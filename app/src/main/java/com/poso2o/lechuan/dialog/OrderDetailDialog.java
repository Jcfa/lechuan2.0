package com.poso2o.lechuan.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.oldorder.SaleOrderDate;
import com.poso2o.lechuan.bean.oldorder.SaleOrderGood;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.util.NumberFormatUtils;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/1.
 */

public class OrderDetailDialog extends BaseDialog {

    private View contentView;
    private Context context;

    //关闭弹窗
    private ImageView order_detail_close;
    //店铺名称
    private TextView shop_name;
    //订单号
    private TextView order_id;
    //商品列表
    private LinearLayout goods_list;
    //合计数量
    private TextView order_total_count;
    //合计金额
    private TextView order_total_amount;
    //开单时间
    private TextView order_time;
    //整单打折
    private TextView order_discount;
    //支付方式
    private TextView order_paid_type;
    //应收金额
    private TextView order_should_paid;
    //实收金额
    private TextView order_paid;
    //收银员
    private TextView order_cashier;
    //营业员
    private TextView order_sale_man;

    public OrderDetailDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_order_detail,null);
        return contentView;
    }

    @Override
    public void initView() {
        order_detail_close = (ImageView) contentView.findViewById(R.id.order_detail_close);

        shop_name = (TextView) contentView.findViewById(R.id.shop_name);

        order_id = (TextView) contentView.findViewById(R.id.order_id);

        goods_list = (LinearLayout) contentView.findViewById(R.id.goods_list);

        order_total_count = (TextView) contentView.findViewById(R.id.order_total_count);

        order_total_amount = (TextView) contentView.findViewById(R.id.order_total_amount);

        order_time = (TextView) contentView.findViewById(R.id.order_time);

        order_discount = (TextView) contentView.findViewById(R.id.order_discount);

        order_paid_type = (TextView) contentView.findViewById(R.id.order_paid_type);

        order_should_paid = (TextView) contentView.findViewById(R.id.order_should_paid);

        order_paid = (TextView) contentView.findViewById(R.id.order_paid);

        order_cashier = (TextView) contentView.findViewById(R.id.order_cashier);

        order_sale_man = (TextView) contentView.findViewById(R.id.order_sale_man);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        setWindowDispalay(1.0f,0.7f);
        setCancelable(true);
        getWindow().setWindowAnimations(R.style.BottomInAnimation);
    }

    @Override
    public void initListener() {
        order_detail_close.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
    }

    public void setOrderData(SaleOrderDate saleOrder){
        if(saleOrder == null)return;
        shop_name.setText(saleOrder.shopname);
        order_id.setText(saleOrder.order_id);
        order_total_count.setText(saleOrder.order_num);
        order_total_amount.setText(NumberFormatUtils.format(saleOrder.payment_amount));
        String date = saleOrder.order_date;
        if (date.contains(".0"))date = date.substring(0,date.length()-2);
        order_time.setText(date);
        order_discount.setText(NumberFormatUtils.format(saleOrder.order_discount) + "%");
        order_paid_type.setText(saleOrder.payment_jsfs);
        order_should_paid.setText(NumberFormatUtils.format(saleOrder.order_amount));
        order_paid.setText(NumberFormatUtils.format(saleOrder.payment_amount));
        order_cashier.setText(saleOrder.salesname);
        order_sale_man.setText(saleOrder.czyname);
        initGoodsData(saleOrder.products);
    }

    private void initGoodsData(ArrayList<SaleOrderGood> orderGoodses){
        if(orderGoodses == null)return;
        for (SaleOrderGood orderGoods : orderGoodses){
            View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail_goods,goods_list,false);
            ImageView order_detail_goods_image = (ImageView) view.findViewById(R.id.order_detail_goods_image);
            TextView goods_name = (TextView) view.findViewById(R.id.order_detail_goods_name);
            TextView goods_price = (TextView) view.findViewById(R.id.order_detail_goods_price);
            TextView goods_spec = (TextView) view.findViewById(R.id.order_detail_goods_spec);
            TextView goods_count = (TextView) view.findViewById(R.id.order_detail_goods_count);
            TextView goods_amount = (TextView) view.findViewById(R.id.order_detail_goods_amount);

            goods_list.addView(view);

            if (orderGoods.image222 != null && !orderGoods.image222.equals(""))
                Glide.with(context).load(orderGoods.image222).placeholder(R.mipmap.background_image).into(order_detail_goods_image);
            goods_name.setText(orderGoods.name);
            goods_price.setText("¥ " + orderGoods.price);
            goods_spec.setText(orderGoods.colorid + "/" + orderGoods.sizeid);
            goods_count.setText(orderGoods.num);
            goods_amount.setText(NumberFormatUtils.format(orderGoods.amount));
        }
    }

}
