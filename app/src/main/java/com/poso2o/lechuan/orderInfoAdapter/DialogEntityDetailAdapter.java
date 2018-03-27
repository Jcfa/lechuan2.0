package com.poso2o.lechuan.orderInfoAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoEntityDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/22 0022.
 */

public class DialogEntityDetailAdapter extends RecyclerView.Adapter<DialogEntityDetailAdapter.Vholder> {

    private List<OrderInfoEntityDetailBean.ProductsBean> data;
    private Context context;

    public DialogEntityDetailAdapter(Context context, List<OrderInfoEntityDetailBean.ProductsBean> data) {
        this.context = context;
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public DialogEntityDetailAdapter.Vholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_order_entity, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(DialogEntityDetailAdapter.Vholder holder, int position) {
        OrderInfoEntityDetailBean.ProductsBean bean = data.get(position);
        Glide.with(context).load(bean.getImage222()).error(R.drawable.expicture).into(holder.ivHead);
        holder.tvEntityName.setText(bean.getName());
        holder.tvEntityNum.setText(bean.getNum());
        holder.tvEntityMoney.setText(bean.getPrice());
        holder.tvEntityHH.setText(bean.getBh());
        holder.tv_entity_sizeid.setText(bean.getColorid() + "/" + bean.getSizeid());
        holder.tvEntityPrice.setText("售价:" + bean.getPrice() + "      成本:" + bean.getFprice());

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {

        private final ImageView ivHead;
        private final TextView tvEntityName, tvEntityHH, tvEntityPrice, tvEntityNum, tvEntityMoney, tv_entity_sizeid;

        public Vholder(View itemView) {
            super(itemView);
            ivHead = (ImageView) itemView.findViewById(R.id.iv_entity_head);
            tvEntityName = (TextView) itemView.findViewById(R.id.tv_entity_name);
            tvEntityHH = (TextView) itemView.findViewById(R.id.tv_entity_hh);
            tvEntityPrice = (TextView) itemView.findViewById(R.id.tv_entity_price);
            tvEntityNum = (TextView) itemView.findViewById(R.id.tv_entity_num);
            tvEntityMoney = (TextView) itemView.findViewById(R.id.tv_entity_money);
            tv_entity_sizeid = (TextView) itemView.findViewById(R.id.tv_entity_sizeid);
        }
    }
}
