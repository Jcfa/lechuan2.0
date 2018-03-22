package com.poso2o.lechuan.orderInfoAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoMemberBean;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/16 0016.
 * 会员管理
 */

public class OrderInfoMemberAdapter extends RecyclerView.Adapter<OrderInfoMemberAdapter.Vholder> implements View.OnClickListener {
    public List<OrderInfoMemberBean.DataBean> list;
    private RecyclerViewOnItemClickListener onItemClickListener;

    public OrderInfoMemberAdapter(List<OrderInfoMemberBean.DataBean> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    //点击事件
    public interface RecyclerViewOnItemClickListener {

        void onItemClickListener(View view, int position);

    }

    public void setOnItemClickListener(OrderInfoMemberAdapter.RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public OrderInfoMemberAdapter.Vholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderinfo_member, parent, false);
        view.setOnClickListener(this);
        return new Vholder(view);
    }


    @Override
    public void onClick(View v) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClickListener(v, (Integer) v.getTag());

    }


    @Override
    public void onBindViewHolder(OrderInfoMemberAdapter.Vholder holder, int position) {
        OrderInfoMemberBean.DataBean dataBean = list.get(position);
        holder.tvXuhao.setText(dataBean.getNick());
        holder.tvName.setText(dataBean.getOrdernum());
        holder.tvCjs.setText(dataBean.getOrderamount());
        holder.tvCje.setText(dataBean.getAmounts());
        holder.tvYe.setText(dataBean.getPoints());//积分
        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {
        //序号 姓名  成交数  成交额 余额
        private final TextView tvXuhao, tvName, tvCjs, tvCje, tvYe;

        public Vholder(View itemView) {
            super(itemView);
            tvXuhao = (TextView) itemView.findViewById(R.id.tv_member_xuhao);
            tvName = (TextView) itemView.findViewById(R.id.tv_member_name);
            tvCjs = (TextView) itemView.findViewById(R.id.tv_member_cjs);
            tvCje = (TextView) itemView.findViewById(R.id.tv_member_cje);
            tvYe = (TextView) itemView.findViewById(R.id.tv_member_ye);
        }
    }
}
