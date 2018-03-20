package com.poso2o.lechuan.orderInfoAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.orderInfo.OrderMemberDetailBean;
import com.poso2o.lechuan.bean.orderInfo.OrderPaperDetailBean;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/16 0016.
 * 会员详情界面
 */

public class OrderMemberDetailAdapter extends RecyclerView.Adapter<OrderMemberDetailAdapter.Vholder> {
    private List<OrderMemberDetailBean.ListsBean> data;

    public OrderMemberDetailAdapter(List<OrderMemberDetailBean.ListsBean> lists) {
        this.data = lists;
        notifyDataSetChanged();

    }


    @Override
    public OrderMemberDetailAdapter.Vholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_paper_detail, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(OrderMemberDetailAdapter.Vholder holder, int position) {

        OrderMemberDetailBean.ListsBean listsBean = data.get(position);
        String order_date = listsBean.getOrder_date();
        String time = order_date.substring(0, 10);
        holder.tvSpg.setText(time);
        holder.tvSpg.setTextSize(10);
        holder.tvQchu.setVisibility(View.GONE);
        holder.tvShouc.setText(listsBean.getOrder_id());
        holder.tvShouc.setTextSize(10);
        String num = listsBean.getOrder_num();
        holder.tvKc.setText(num);
        holder.tvKc.setWidth(2);
        holder.tvShouc.setTextSize(10);
        holder.tvKc.setTextSize(10);
        String paymentAmount = listsBean.getPayment_amount();
        holder.tvPrice.setText(paymentAmount);
        holder.tvPrice.setTextSize(10);

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
