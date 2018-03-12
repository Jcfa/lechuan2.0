package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.TextUtil;

/**
 * 选择商品适配器
 * Created by Jaydon on 2017/12/4.
 */
public class SelectGoodsAdapter extends BaseAdapter<SelectGoodsAdapter.SelectGoodsHolder, Goods> {

    public SelectGoodsAdapter(Context context) {
        super(context, null);
    }

    @Override
    public void initItemView(SelectGoodsHolder holder, Goods item, int position) {
        Glide.with(context).load(item.main_picture).into(holder.select_goods_item_icon);
        holder.select_goods_item_name.setText(TextUtil.trimToEmpty(item.goods_name));
        holder.select_goods_item_number.setText(TextUtil.trimToEmpty(item.goods_no));
        holder.select_goods_item_price.setText(TextUtil.trimToEmpty(item.goods_price_section));
        holder.select_goods_item_brokerage.setText(NumberFormatUtils.format(item.commission_amount));
    }

    @Override
    public SelectGoodsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectGoodsHolder(getItemView(R.layout.item_select_goods, parent));
    }

    class SelectGoodsHolder extends BaseViewHolder {

        ImageView select_goods_item_icon;// 图片
        TextView select_goods_item_name;// 名称
        TextView select_goods_item_number;// 货号
        TextView select_goods_item_price;// 单价
        TextView select_goods_item_brokerage;// 佣金

        SelectGoodsHolder(View itemView) {
            super(itemView);
            select_goods_item_icon = getView(R.id.select_goods_item_icon);
            select_goods_item_name = getView(R.id.select_goods_item_name);
            select_goods_item_number = getView(R.id.select_goods_item_number);
            select_goods_item_price = getView(R.id.select_goods_item_price);
            select_goods_item_brokerage = getView(R.id.select_goods_item_brokerage);
        }
    }
}
