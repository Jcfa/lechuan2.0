package com.poso2o.lechuan.orderInfoAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoPoplStaffBean;
import com.poso2o.lechuan.dialog.OrderInfoStaffDetailDialog;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/19 0019.
 */

public class OrderInfoPoplStaffAdapter extends RecyclerView.Adapter<OrderInfoPoplStaffAdapter.Vholder> {
    private List<OrderInfoPoplStaffBean.ListBean> data;
    private Context context;

    public OrderInfoPoplStaffAdapter(Context contexs, List<OrderInfoPoplStaffBean.ListBean> list) {
        this.data = list;
        this.context = contexs;
        notifyDataSetChanged();
    }

    @Override
    public OrderInfoPoplStaffAdapter.Vholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderinfo_poplstaff, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(OrderInfoPoplStaffAdapter.Vholder holder, int position) {
        final OrderInfoPoplStaffBean.ListBean bean = data.get(position);
        holder.tvOrderName.setText(bean.getCzy());
        holder.tvOrderyyy.setText(bean.getRealname());
        holder.tvSellMany.setText(bean.getOrder_amounts());
        holder.tvOrderRenwu.setText(bean.getAssignments());
        holder.tvOrderzm.setText(bean.getCompletion_rate() + "%");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderInfoStaffDetailDialog dialog = new OrderInfoStaffDetailDialog(context);
                dialog.show();
                dialog.setDiaLogData(bean.getRealname(), bean.getCzy());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {
        //排名 营业员 销售最高 任务 达标最高
        private final TextView tvOrderName, tvOrderyyy, tvSellMany, tvOrderRenwu, tvOrderzm;

        public Vholder(View itemView) {
            super(itemView);
            tvOrderName = (TextView) itemView.findViewById(R.id.tv_order_name);
            tvOrderyyy = (TextView) itemView.findViewById(R.id.tv_order_yyy);
            tvSellMany = (TextView) itemView.findViewById(R.id.tv_order_sell_many);
            tvOrderRenwu = (TextView) itemView.findViewById(R.id.tv_order_renwu);
            tvOrderzm = (TextView) itemView.findViewById(R.id.tv_order_zm);
        }
    }
}
