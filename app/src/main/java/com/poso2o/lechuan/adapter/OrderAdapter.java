package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.oldorder.SaleOrderDate;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.view.LoadMoreViewHolder;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/1.
 */

public class OrderAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<SaleOrderDate> mDatas;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void notifyAdapter(ArrayList<SaleOrderDate> orders){
        mDatas = orders;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.item_load_more,parent,false);
            return new LoadMoreViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_list,parent,false);
        return new OrderVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==1 && onOrderClickListener != null){
            LoadMoreViewHolder vh = (LoadMoreViewHolder) holder;
            onOrderClickListener.onLoadMore(vh.load_tips,vh.load_progress);
        }else {
            final SaleOrderDate saleOrderDate = mDatas.get(position);
            if (saleOrderDate == null)return;
            OrderVH vh = (OrderVH) holder;
            vh.item_order_id.setText(saleOrderDate.order_id);
            vh.item_order_count.setText(saleOrderDate.order_num);
            vh.item_order_amount.setText(saleOrderDate.payment_amount);
            vh.item_order_staff.setText(saleOrderDate.salesname);
            vh.item_order_root.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if(onOrderClickListener != null){
                        onOrderClickListener.onOrderClick(saleOrderDate);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) return 0;
        return mDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas == null || position == mDatas.size()){
            return 1;
        }
        return 0;
    }

    class OrderVH extends RecyclerView.ViewHolder{

        LinearLayout item_order_root;

        TextView item_order_id;

        TextView item_order_count;

        TextView item_order_amount;

        TextView item_order_staff;

        public OrderVH(View itemView) {
            super(itemView);

            item_order_root = (LinearLayout) itemView.findViewById(R.id.item_order_root);

            item_order_id = (TextView) itemView.findViewById(R.id.item_order_id);

            item_order_count = (TextView) itemView.findViewById(R.id.item_order_count);

            item_order_amount = (TextView) itemView.findViewById(R.id.item_order_amount);

            item_order_staff = (TextView) itemView.findViewById(R.id.item_order_staff);
        }
    }

    private OnOrderClickListener onOrderClickListener;
    public interface OnOrderClickListener{
        void onOrderClick(SaleOrderDate saleOrderDate);
        void onLoadMore(TextView textView, ProgressBar progressBar);
    }
    public void setOnOrderClickListener(OnOrderClickListener onOrderClickListener){
        this.onOrderClickListener = onOrderClickListener;
    }
}
