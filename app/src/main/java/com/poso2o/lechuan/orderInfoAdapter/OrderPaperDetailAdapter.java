package com.poso2o.lechuan.orderInfoAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.orderInfo.OrderPaperDetailBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/16 0016.
 * 库存详情界面
 */

public class OrderPaperDetailAdapter extends RecyclerView.Adapter<OrderPaperDetailAdapter.Vholder> {
    private List<OrderPaperDetailBean.ListsBean> data;
    private int type;
    private String fprice;

    public OrderPaperDetailAdapter(List<OrderPaperDetailBean.ListsBean> lists, String fprice, int type) {
        this.data = lists;
        this.type = type;
        this.fprice = fprice;
        notifyDataSetChanged();
    }

    @Override
    public OrderPaperDetailAdapter.Vholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_paper_detail, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(OrderPaperDetailAdapter.Vholder holder, int position) {
        OrderPaperDetailBean.ListsBean listsBean = data.get(position);
        double v = Double.parseDouble(fprice) * Double.parseDouble(listsBean.getNum());
        BigDecimal bg = new BigDecimal(v);
        double money = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String format = df.format(money);
        if (v == 0) {
            holder.tvPrice.setText("0.00");//成本价格
        } else {
            holder.tvPrice.setText(format + "");//成本价格
        }

        holder.tvKc.setText(listsBean.getNum());
        holder.tvShouc.setText(listsBean.getSales_num());
        holder.tvSpg.setText(listsBean.getColorid() + "/" + listsBean.getSizeid());
        holder.tvQchu.setVisibility(View.GONE);


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
