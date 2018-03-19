package com.poso2o.lechuan.orderInfoAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoPrimeCostBean;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/16 0016.
 */

public class OrderInfoPrimeAdapter extends RecyclerView.Adapter<OrderInfoPrimeAdapter.Vholder>implements View.OnClickListener {
    private List<OrderInfoPrimeCostBean.DataBean> list;

    public OrderInfoPrimeAdapter(List<OrderInfoPrimeCostBean.DataBean> data) {
        this.list = data;
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
    public OrderInfoPrimeAdapter.Vholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderinfo_prime, parent, false);
        view.setOnClickListener(this);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(OrderInfoPrimeAdapter.Vholder holder, int position) {
        OrderInfoPrimeCostBean.DataBean dataBean = list.get(position);
        holder.tvname.setText(dataBean.getShopname());
        holder.tvPM.setText("1");
        holder.tvicome.setText(dataBean.getAdd_amounts());
        holder.tvspend.setText(dataBean.getDel_amount());
        holder.tvlr.setText(dataBean.getTotal_profit());
        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {
        //排名 店铺名 收入 支出 利润
        private final TextView tvPM, tvname, tvicome, tvspend, tvlr;

        public Vholder(View itemView) {
            super(itemView);
            tvPM = (TextView) itemView.findViewById(R.id.tv_order_prime_pm);
            tvname = (TextView) itemView.findViewById(R.id.tv_order_prime_name);
            tvicome = (TextView) itemView.findViewById(R.id.tv_order_prime_income);
            tvspend = (TextView) itemView.findViewById(R.id.tv_order_prime_spend);
            tvlr = (TextView) itemView.findViewById(R.id.tv_order_prime_lr);
        }
    }
}
