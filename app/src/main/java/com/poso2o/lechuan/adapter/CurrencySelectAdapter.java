package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.print.CurrencySelectActivity;
import com.poso2o.lechuan.bean.printer.CurrencyItemData;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

import java.util.ArrayList;

/**
 * 选择货币
 * Created by luo on 2017/09/19.
 */
public class CurrencySelectAdapter extends RecyclerView.Adapter<CurrencySelectAdapter.CurrencySelectHolder> {
    private Context context;
    private ArrayList<CurrencyItemData> mCurrencyItemData;
    private CurrencySelectActivity mCurrencySelectActivity;

    private ImageView SelectedItem;

    public CurrencySelectAdapter(Context context, ArrayList<CurrencyItemData> currencyItemDatas) {
        this.context = context;
        this.mCurrencyItemData = currencyItemDatas;
        mCurrencySelectActivity=(CurrencySelectActivity)context;

    }

    @Override
    public CurrencySelectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrencySelectHolder(LayoutInflater.from(context).inflate(R.layout.recycle_currency_select_item, null));
    }

    @Override
    public void onBindViewHolder(final CurrencySelectHolder holder, int position) {
        if (holder instanceof CurrencySelectHolder) {
            CurrencyItemData currencyItemData = mCurrencyItemData.get(position);
            holder.size_item_text.setText(currencyItemData.currency_id + currencyItemData.currency_name);

            if (currencyItemData.currency_ing){
                holder.size_item_select.setSelected(true);
                SelectedItem = holder.size_item_select;
            }else{
                holder.size_item_select.setSelected(false);
            }

            holder.size_item_group.setTag(currencyItemData);
            holder.size_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (SelectedItem != null){
                        SelectedItem.setSelected(false);
                    }
                    SelectedItem = holder.size_item_select;
                    holder.size_item_select.setSelected(true);
                    if (v.getTag() instanceof CurrencyItemData){
                        mCurrencySelectActivity.selectCurrency((CurrencyItemData)v.getTag());
                    }
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return mCurrencyItemData == null ? 0 : mCurrencyItemData.size();
    }

    public void notify(ArrayList<CurrencyItemData> currencyItemDatas) {
        this.mCurrencyItemData=currencyItemDatas;
        notifyDataSetChanged();
    }

    class CurrencySelectHolder extends RecyclerView.ViewHolder {
        public LinearLayout size_item_group;
        public TextView size_item_text;
        public ImageView size_item_select;

        public CurrencySelectHolder(View itemView) {
            super(itemView);
            size_item_group = (LinearLayout) itemView.findViewById(R.id.size_item_group);
            size_item_text = (TextView) itemView.findViewById(R.id.size_item_text);
            size_item_select = (ImageView) itemView.findViewById(R.id.size_item_select);
        }
    }

}
