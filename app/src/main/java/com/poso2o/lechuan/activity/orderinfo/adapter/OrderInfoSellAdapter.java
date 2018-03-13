package com.poso2o.lechuan.activity.orderinfo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;

/**
 * Created by ${cbf} on 2018/3/13 0013.
 * 销售界面的适配器列表
 */

public class OrderInfoSellAdapter extends RecyclerView.Adapter<OrderInfoSellAdapter.Vholder> {

    @Override
    public OrderInfoSellAdapter.Vholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderinfo_sell, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(OrderInfoSellAdapter.Vholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class Vholder extends RecyclerView.ViewHolder {

        private ImageView ivPaperHead;//头像
        //商品名称 价格  数量 描述
        private TextView tvOrderGName, tvOrderGPrice, tvOrderGCount, tvOrderGDes;

        public Vholder(View itemView) {
            super(itemView);
            ivPaperHead = (ImageView) itemView.findViewById(R.id.iv_order_sell_head);
            tvOrderGName = (TextView) itemView.findViewById(R.id.tv_order_goodsName);
            tvOrderGPrice = (TextView) itemView.findViewById(R.id.tv_order_goods_price);
            tvOrderGCount = (TextView) itemView.findViewById(R.id.tv_order_goods_count);
            tvOrderGDes = (TextView) itemView.findViewById(R.id.tv_order_goodsName_des);
        }
    }
}
