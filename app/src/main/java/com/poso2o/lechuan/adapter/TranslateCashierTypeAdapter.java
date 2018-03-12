package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.transfer.TransferDetailOrders;
import com.poso2o.lechuan.util.NumberFormatUtils;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/7.
 */

public class TranslateCashierTypeAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<TransferDetailOrders> mDatas;

    public TranslateCashierTypeAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(ArrayList<TransferDetailOrders> types){
        mDatas = types;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_translate_detail_list,parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TransferDetailOrders transferDetailOrder = mDatas.get(position);
        if (transferDetailOrder == null)return;
        OrderViewHolder vh = (OrderViewHolder) holder;
        vh.translate_order_id.setText(transferDetailOrder.transfer_id);
        vh.translate_order_cashier_type.setText(transferDetailOrder.payment_method_name);
        vh.translate_detail_order_amount.setText(NumberFormatUtils.format(transferDetailOrder.order_amount));
    }

    @Override
    public int getItemCount() {
        if(mDatas == null)return 0;
        return mDatas.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{

        TextView translate_order_id;
        TextView translate_order_cashier_type;
        TextView translate_detail_order_amount;

        public OrderViewHolder(View itemView) {
            super(itemView);
            translate_order_id = (TextView) itemView.findViewById(R.id.translate_order_id);
            translate_order_cashier_type = (TextView) itemView.findViewById(R.id.translate_order_cashier_type);
            translate_detail_order_amount = (TextView) itemView.findViewById(R.id.translate_detail_order_amount);
        }
    }
}
