package com.poso2o.lechuan.orderInfoAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoSellBean;
import com.poso2o.lechuan.dialog.OrderInfoSellDialog;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by ${cbf} on 2018/3/15 0015.
 */

public class OrderInfoSellAdapter extends RecyclerView.Adapter<OrderInfoSellAdapter.Vholder> implements View.OnClickListener {
    private Context context;
    private List<OrderInfoSellBean.DataBean> dataBeen;
    private String beginTs, endTs;

    public OrderInfoSellAdapter(Context activity, List<OrderInfoSellBean.DataBean> data, String beginTs, String endTs) {
        this.dataBeen = data;
        this.context = activity;
        notifyDataSetChanged();
    }

    public void setData(List<OrderInfoSellBean.DataBean> data) {
        this.dataBeen = data;
        notifyDataSetChanged();
    }

    public void addData(List<OrderInfoSellBean.DataBean> data) {
        this.dataBeen.addAll(data);
        notifyDataSetChanged();
    }

    private OrderInfoPaperAdapter.RecyclerViewOnItemClickListener onItemClickListener;


    //点击事件
    public interface RecyclerViewOnItemClickListener {

        void onItemClickListener(View view, int position);

    }

    public void setOnItemClickListener(OrderInfoPaperAdapter.RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClickListener(v, (Integer) v.getTag());

    }

    @Override
    public OrderInfoSellAdapter.Vholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderinfo_sell, parent, false);
        view.setOnClickListener(this);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(Vholder holder, final int position) {
        OrderInfoSellBean.DataBean dataBean = dataBeen.get(position);
        holder.tvOrderGCount.setText(dataBean.getTotalnum());
        holder.tvOrderGDes.setText(dataBean.getColorid() + "/" + dataBean.getSizeid());
        holder.tvOrderGPrice.setText(dataBean.getPrice());
        holder.tvOrderGName.setText(dataBean.getName());
        Glide.with(context).load(dataBean.getImage222()).into(holder.ivPaperHead);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderInfoSellDialog dialog = new OrderInfoSellDialog(context);
                dialog.show();
                dialog.setData(beginTs, endTs,
                        dataBeen.get(position).getGuid(),
                        dataBeen.get(position).getColorid(),
                        dataBeen.get(position).getSizeid());
            }
        });

    }


    @Override
    public int getItemCount() {
        return dataBeen == null ? 0 : dataBeen.size();
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
