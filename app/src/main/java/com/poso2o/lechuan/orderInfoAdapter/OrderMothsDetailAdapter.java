package com.poso2o.lechuan.orderInfoAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.orderInfo.OrderMothsDetailBean;
import com.poso2o.lechuan.bean.orderInfo.OrderPaperDetailBean;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/16 0016.
 * 月损益表详情
 */

public class OrderMothsDetailAdapter extends RecyclerView.Adapter<OrderMothsDetailAdapter.Vholder> {
    private List<OrderMothsDetailBean.ListBean> data;
    private int type;

    public OrderMothsDetailAdapter(List<OrderMothsDetailBean.ListBean> list, int type) {
        this.data = list;
        this.type = type;
        notifyDataSetChanged();
    }

    @Override
    public OrderMothsDetailAdapter.Vholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_paper_detail, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(OrderMothsDetailAdapter.Vholder holder, int position) {
        OrderMothsDetailBean.ListBean listsBean = data.get(position);
        holder.tvSpg.setText(listsBean.getCreate_date());
        holder.tvQchu.setText(listsBean.getSales_amount());
        holder.tvShouc.setText(listsBean.getDel_amount());
        holder.tvKc.setText(listsBean.getPrimecost_amount());
        holder.tvPrice.setText(listsBean.getClear_profit());


    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {

        private final TextView tvPrice, tvKc, tvShouc, tvQchu, tvSpg;

        public Vholder(View itemView) {
            super(itemView);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvKc = (TextView) itemView.findViewById(R.id.tv_kc);
            tvShouc = (TextView) itemView.findViewById(R.id.tv_shouc);
            tvQchu = (TextView) itemView.findViewById(R.id.tv_qchu);
            tvSpg = (TextView) itemView.findViewById(R.id.tv_spg);
        }
    }
}
