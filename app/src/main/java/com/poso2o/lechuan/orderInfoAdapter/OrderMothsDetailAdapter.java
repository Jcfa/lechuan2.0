package com.poso2o.lechuan.orderInfoAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.orderInfo.OrderMothsDetailBean;
import com.poso2o.lechuan.bean.orderInfo.OrderPaperDetailBean;

import java.math.BigDecimal;
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
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        BigDecimal sales = new BigDecimal(Double.parseDouble(listsBean.getSales_amount()));
        double income = sales.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        String icomes = df.format(income);
        if (income == 0.0) {
            holder.tvQchu.setText("0.00");
        } else
            holder.tvQchu.setText(icomes);
        String delAmount = listsBean.getDel_amount();
        BigDecimal bg3 = new BigDecimal(Double.parseDouble(delAmount));
        double value = bg3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        String format = df.format(value);
        if (value == 0.0) {
            holder.tvShouc.setText("0.00");
        } else
            holder.tvShouc.setText(format);
        BigDecimal primcost = new BigDecimal(Double.parseDouble(listsBean.getPrimecost_amount()));
        double cost = primcost.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        String costs = df.format(cost);
        if (cost == 0.0) {
            holder.tvKc.setText("0.00");
        } else
            holder.tvKc.setText(costs);
        double profit = new BigDecimal(listsBean.getClear_profit()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        String profits = df.format(profit);
        if (profit == 0.0) {
            holder.tvPrice.setText("0.00");
        } else
            holder.tvPrice.setText(profits);
        holder.tvQchu.setVisibility(View.VISIBLE);


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
