package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.transfer.Transfer;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.ArithmeticUtils;
import com.poso2o.lechuan.util.DateTimeUtil;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.view.LoadMoreViewHolder;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/7.
 */

public class CashierListAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Transfer> mDatas;

    public CashierListAdapter(Context context) {
        this.context = context;
    }

    public void notifyAdapter(ArrayList<Transfer> transfers){
        mDatas = transfers;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.item_load_more,parent,false);
            return new LoadMoreViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_cashier_list, parent, false);
        return new CashierVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==1 && onCashierClickListener != null){
            LoadMoreViewHolder vh = (LoadMoreViewHolder) holder;
            onCashierClickListener.onLoadMore(vh.load_tips,vh.load_progress);
        }else {
            final Transfer transfer = mDatas.get(position);
            if (transfer == null)return;
            CashierVH vh = (CashierVH) holder;
            vh.item_cashier_time.setText(transfer.dat);
            vh.item_cashier_cashier.setText(transfer.czyname);
            vh.cashier_value_input.setText(NumberFormatUtils.format(ArithmeticUtils.add(Double.parseDouble(transfer.amount_cash),transfer.other_amount_total)));
            vh.cashier_value_refund.setText(transfer.b_amount_back);
            vh.cashier_value_commit.setText(NumberFormatUtils.format(transfer.amounts_success));
            vh.cashier_value_obligate.setText(NumberFormatUtils.format(transfer.transfe_reservefund_last));
            vh.cashier_value_spare.setText(NumberFormatUtils.format(transfer.transfe_reservefund));
            vh.item_cashier_root.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onCashierClickListener == null) return;
                    onCashierClickListener.onCashierClick(transfer);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.size() == 0)return 0;
        return mDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas == null || position == mDatas.size())return 1;
        return 0;
    }

    //收银交接项
    class CashierVH extends RecyclerView.ViewHolder {

        RelativeLayout item_cashier_root;
        TextView item_cashier_time;
        TextView item_cashier_cashier;
        TextView cashier_value_input;
        TextView cashier_value_refund;
        TextView cashier_value_commit;
        TextView cashier_value_obligate;
        TextView cashier_value_spare;

        public CashierVH(View itemView) {
            super(itemView);
            item_cashier_root = (RelativeLayout) itemView.findViewById(R.id.item_cashier_root);
            item_cashier_time = (TextView) itemView.findViewById(R.id.item_cashier_time);
            item_cashier_cashier = (TextView) itemView.findViewById(R.id.item_cashier_cashier);
            cashier_value_input = (TextView) itemView.findViewById(R.id.cashier_value_input);
            cashier_value_refund = (TextView) itemView.findViewById(R.id.cashier_value_refund);
            cashier_value_commit = (TextView) itemView.findViewById(R.id.cashier_value_commit);
            cashier_value_obligate = (TextView) itemView.findViewById(R.id.cashier_value_obligate);
            cashier_value_spare = (TextView) itemView.findViewById(R.id.cashier_value_spare);
        }
    }

    private OnCashierClickListener onCashierClickListener;

    public interface OnCashierClickListener {
        void onCashierClick(Transfer transfer);
        void onLoadMore(TextView textView, ProgressBar progressBar);
    }

    public void setOnCashierClickListener(OnCashierClickListener onCashierClickListener) {
        this.onCashierClickListener = onCashierClickListener;
    }
}
