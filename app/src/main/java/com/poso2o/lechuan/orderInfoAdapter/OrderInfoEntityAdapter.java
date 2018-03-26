package com.poso2o.lechuan.orderInfoAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.orderInfo.DataBean;
import com.poso2o.lechuan.dialog.OrderEntityDetailDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/15 0015.
 */

public class OrderInfoEntityAdapter extends RecyclerView.Adapter<OrderInfoEntityAdapter.Vholder> {
    private Context context;
    private List<DataBean> data;

    public OrderInfoEntityAdapter(Context context, List<DataBean> list) {
        this.context = context;
        this.data = list;
        notifyDataSetChanged();
    }

    public void setData(List<DataBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void AddData(List<DataBean> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    //刷新adapter方法  做搜索使用
    public void updateListView(List<DataBean> newlists) {
        data = newlists;  //重新赋值
        notifyDataSetChanged();  //刷新
    }

    @Override
    public OrderInfoEntityAdapter.Vholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_orderinf_entity, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(OrderInfoEntityAdapter.Vholder holder, final int position) {
        DataBean dataBean = data.get(position);
        holder.tv_order_hao.setText(dataBean.getOrder_id());
        holder.tv_order_money.setText(dataBean.getPayment_amount());
        holder.tv_order_num.setText(dataBean.getOrder_num());
        holder.tv_order_salesname.setText(dataBean.getSalesname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderEntityDetailDialog detailDialog = new OrderEntityDetailDialog(context);
                detailDialog.show();
                detailDialog.setNetData(data.get(position).getOrder_id());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public class Vholder extends RecyclerView.ViewHolder {

        private final TextView tv_order_hao, tv_order_num, tv_order_money, tv_order_salesname;

        public Vholder(View itemView) {
            super(itemView);
            tv_order_hao = (TextView) itemView.findViewById(R.id.tv_order_hao);
            tv_order_num = (TextView) itemView.findViewById(R.id.tv_order_num);
            tv_order_money = (TextView) itemView.findViewById(R.id.tv_order_money);
            tv_order_salesname = (TextView) itemView.findViewById(R.id.tv_order_salesname);
        }
    }
}
