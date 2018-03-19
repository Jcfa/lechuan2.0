package com.poso2o.lechuan.orderInfoAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.orderInfo.OrderIOnfoStaffDetailBean;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/19 0019.
 */

public class OrderInfoStaffDetailAdapter extends RecyclerView.Adapter<OrderInfoStaffDetailAdapter.Vholder> {
    private List<OrderIOnfoStaffDetailBean.ListsBean> data;

    public OrderInfoStaffDetailAdapter(List<OrderIOnfoStaffDetailBean.ListsBean> list) {
        this.data = list;
        notifyDataSetChanged();

    }

    @Override
    public OrderInfoStaffDetailAdapter.Vholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_staff_detail, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(OrderInfoStaffDetailAdapter.Vholder holder, int position) {
        OrderIOnfoStaffDetailBean.ListsBean bean = data.get(position);
        holder.tvStaffTime.setText(bean.getMonths());
        holder.tvstaffrw.setText(bean.getAssignments());
        holder.tvStaffdbl.setText(bean.getCompletion_rate() + "%");
        holder.tvStaffxse.setText(bean.getOrder_amounts());

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {

        private final TextView tvStaffTime, tvStaffxse, tvstaffrw, tvStaffdbl;

        public Vholder(View itemView) {
            super(itemView);
            tvStaffTime = (TextView) itemView.findViewById(R.id.tv_staff_time);
            tvStaffxse = (TextView) itemView.findViewById(R.id.tv_staff_xse);
            tvstaffrw = (TextView) itemView.findViewById(R.id.tv_staff_rw2);
            tvStaffdbl = (TextView) itemView.findViewById(R.id.tv_staff_dbl);
        }
    }
}
