package com.poso2o.lechuan.orderInfoAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoPaperBean;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/15 0015.
 */

public class OrderInfoPaperAdapter extends RecyclerView.Adapter<OrderInfoPaperAdapter.Vholder> implements View.OnClickListener {
    private Context context;
    private List<OrderInfoPaperBean.DataBean> lists;
    private RecyclerViewOnItemClickListener onItemClickListener;

    public void setData(List<OrderInfoPaperBean.DataBean> data) {
        this.lists = data;
        notifyDataSetChanged();
    }

    /**
     * 所有到内容后进行刷新视图
     */
    public void updateSearchListView(List<OrderInfoPaperBean.DataBean> searchlists) {
        this.lists = searchlists;
        notifyDataSetChanged();
    }

    /**
     * 排序更新视图
     * */
    public void updateSorttView(List<OrderInfoPaperBean.DataBean>  lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    //点击事件
    public interface RecyclerViewOnItemClickListener {

        void onItemClickListener(View view, int position);

    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClickListener(v, (Integer) v.getTag());

    }

    public OrderInfoPaperAdapter(Context activity, List<OrderInfoPaperBean.DataBean> data) {
        this.context = activity;
        this.lists = data;
        notifyDataSetChanged();
    }

    @Override
    public OrderInfoPaperAdapter.Vholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderinfo_paper, parent, false);
        Vholder vholder = new Vholder(view);
        view.setOnClickListener(this);
        return vholder;
    }

    @Override
    public void onBindViewHolder(OrderInfoPaperAdapter.Vholder holder, final int position) {
        OrderInfoPaperBean.DataBean dataBean = lists.get(position);
        Glide.with(context).load(dataBean.getImage222()).error(R.drawable.ex_shop_logo).into(holder.iv_order_sell_head);
        holder.tv_order_paperName.setText(dataBean.getName());
        holder.tv_order_paper_price.setText(dataBean.getFprice());
        holder.tv_order_paper_moeny.setText(dataBean.getTotalamount());
        holder.tv_order_paper_count.setText(dataBean.getTotalnum());
        //设置Tag值来进行点击的事件的触发 也就是相应的位置
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {

        private final ImageView iv_order_sell_head;
        private final TextView tv_order_paperName, tv_order_paper_count, tv_order_paper_price, tv_order_paper_moeny;
        public View root;

        public Vholder(View itemView) {
            super(itemView);
            this.root = itemView;
            iv_order_sell_head = (ImageView) itemView.findViewById(R.id.iv_order_sell_head);
            tv_order_paperName = (TextView) itemView.findViewById(R.id.tv_order_paperName);
            tv_order_paper_price = (TextView) itemView.findViewById(R.id.tv_order_paper_price);
            tv_order_paper_moeny = (TextView) itemView.findViewById(R.id.tv_order_paper_moeny);
            tv_order_paper_count = (TextView) itemView.findViewById(R.id.tv_order_paper_count);
        }
    }
}
