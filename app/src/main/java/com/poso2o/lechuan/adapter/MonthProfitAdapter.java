package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.monthprofit.MonthProfitData;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.ArithmeticUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/5.
 */

public class MonthProfitAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<MonthProfitData> mDatas;
    private double totalInput;
    private double totalCost;
    private double totalMao;
    private double totalNet;

    private boolean is_online;

    public MonthProfitAdapter(Context context,boolean is_online) {
        this.context = context;
        this.is_online = is_online;
    }

    public void notifyAdapter(ArrayList<MonthProfitData> profitDatas){
        mDatas = profitDatas;
        totalInput = 0;
        totalCost = 0;
        totalMao = 0;
        totalNet = 0;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.item_month_profit_total,parent,false);
            return new TotalProfitVH(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_month_profit,parent,false);
        return new MonthProfitVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (is_online){
            //微店
            if (getItemViewType(position) == 1){
                TotalProfitVH vh = (TotalProfitVH) holder;
                vh.total_profit_input.setText(NumberFormatUtils.format(totalInput));
                vh.total_profit_cost.setText(NumberFormatUtils.format(totalCost));
                vh.total_profit_gross_margin.setText(NumberFormatUtils.format(totalMao));
                vh.total_profit_retained.setText(NumberFormatUtils.format(totalNet));
            }else {
                final MonthProfitData profitData = mDatas.get(position);
                if (profitData == null)return;

                totalInput = ArithmeticUtils.add(totalInput,profitData.total_amount);
                totalCost = ArithmeticUtils.add(totalCost,profitData.total_cost);
                totalMao = ArithmeticUtils.add(totalMao,profitData.total_profit);
                totalNet = ArithmeticUtils.add(totalNet,profitData.net_profit);

                MonthProfitVH vh = (MonthProfitVH) holder;

                vh.item_profit_time.setText(profitData.months);
                vh.item_profit_input.setText(NumberFormatUtils.format(profitData.total_amount));
                vh.item_profit_cost.setText(NumberFormatUtils.format(profitData.total_cost));
                vh.item_profit_gross_margin.setText(NumberFormatUtils.format(profitData.total_profit));
                vh.item_profit_retained.setText(NumberFormatUtils.format(profitData.net_profit));

                vh.item_profit_root.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        if(onMonthProfitListener == null)return;
                        onMonthProfitListener.onMonthProfitClick(profitData);
                    }
                });
            }
        }else {
            if (getItemViewType(position) == 1){
                TotalProfitVH vh = (TotalProfitVH) holder;
                vh.total_profit_input.setText(NumberFormatUtils.format(totalInput));
                vh.total_profit_cost.setText(NumberFormatUtils.format(totalCost));
                vh.total_profit_gross_margin.setText(NumberFormatUtils.format(totalMao));
                vh.total_profit_retained.setText(NumberFormatUtils.format(totalNet));
            }else {
                final MonthProfitData profitData = mDatas.get(position);
                if (profitData == null)return;

                totalInput = ArithmeticUtils.add(totalInput,Double.parseDouble(profitData.sales_amount));
                totalCost = ArithmeticUtils.add(totalCost,Double.parseDouble(profitData.primecost_amount));
                totalMao = ArithmeticUtils.add(totalMao,Double.parseDouble(profitData.del_amount));
                totalNet = ArithmeticUtils.add(totalNet,Double.parseDouble(profitData.clear_profit));

                MonthProfitVH vh = (MonthProfitVH) holder;

                vh.item_profit_time.setText(profitData.months);
                vh.item_profit_input.setText(profitData.sales_amount);
                vh.item_profit_cost.setText(profitData.primecost_amount);
                vh.item_profit_gross_margin.setText(profitData.del_amount);
                vh.item_profit_retained.setText(profitData.clear_profit);

                vh.item_profit_root.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        if(onMonthProfitListener == null)return;
                        onMonthProfitListener.onMonthProfitClick(profitData);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 1;
        return mDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas == null || position == mDatas.size())
            return 1;
        return 0;
    }

    class MonthProfitVH extends RecyclerView.ViewHolder{

        LinearLayout item_profit_root;
        TextView item_profit_time;
        TextView item_profit_input;
        TextView item_profit_cost;
        TextView item_profit_gross_margin;
        TextView item_profit_retained;

        public MonthProfitVH(View itemView) {
            super(itemView);
            item_profit_root = (LinearLayout) itemView.findViewById(R.id.item_profit_root);
            item_profit_time = (TextView) itemView.findViewById(R.id.item_profit_time);
            item_profit_input = (TextView) itemView.findViewById(R.id.item_profit_input);
            item_profit_cost = (TextView) itemView.findViewById(R.id.item_profit_cost);
            item_profit_gross_margin = (TextView) itemView.findViewById(R.id.item_profit_gross_margin);
            item_profit_retained = (TextView) itemView.findViewById(R.id.item_profit_retained);
        }
    }

    class TotalProfitVH extends RecyclerView.ViewHolder{

        TextView total_profit_input;
        TextView total_profit_cost;
        TextView total_profit_gross_margin;
        TextView total_profit_retained;

        public TotalProfitVH(View itemView) {
            super(itemView);
            total_profit_input = (TextView) itemView.findViewById(R.id.total_profit_input);
            total_profit_cost = (TextView) itemView.findViewById(R.id.total_profit_cost);
            total_profit_gross_margin = (TextView) itemView.findViewById(R.id.total_profit_gross_margin);
            total_profit_retained = (TextView) itemView.findViewById(R.id.total_profit_retained);
        }
    }

    private OnMonthProfitListener onMonthProfitListener;
    public interface OnMonthProfitListener{
        void onMonthProfitClick(MonthProfitData profitData);
    }
    public void setOnMonthProfitListener(OnMonthProfitListener onMonthProfitListener){
        this.onMonthProfitListener = onMonthProfitListener;
    }
}
