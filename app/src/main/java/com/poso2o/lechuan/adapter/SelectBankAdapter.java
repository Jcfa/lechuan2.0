package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.supplier.SupplierBank;
import com.poso2o.lechuan.dialog.SelectBankDialog;
import com.poso2o.lechuan.util.TextUtil;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

/**
 * 选择目录列表适配器
 * Created by tenac on 2017/1/7.
 */
public class SelectBankAdapter extends RecyclerView.Adapter<SelectBankAdapter.GoodsCatalogSelectHolder> {

    private Context context;

    private ArrayList<SupplierBank> data;

    private String selectBank;

    private SelectBankDialog dialog;

    public SelectBankAdapter(Context context, SelectBankDialog dialog, ArrayList<SupplierBank> data) {
        this.context = context;
        this.dialog = dialog;
        this.data = new ArrayList<>();
        if (data != null) {
            this.data.addAll(data);
        }
    }

    @Override
    public GoodsCatalogSelectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsCatalogSelectHolder(LayoutInflater.from(context).inflate(R.layout.view_bank_select_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GoodsCatalogSelectHolder holder, int position) {
            holder.bank_select_item_line.setVisibility(VISIBLE);
            final SupplierBank item = data.get(position);
            holder.bank_select_item_name.setText(TextUtil.trimToEmpty(item.bank_name));
            if (selectBank != null && TextUtil.equals(selectBank, item.bank_name)) {
                holder.bank_select_item_name.setTextColor(0xffFE7E01);
            } else {
                holder.bank_select_item_name.setTextColor(0xff000000);
            }
            holder.bank_select_item_group.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.selectName(item);
                }
            });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void notifyDataSetChanged(ArrayList<SupplierBank> banks) {
        this.data.clear();
        if (banks != null) {
            this.data.addAll(banks);
        }
        notifyDataSetChanged();
    }

    public void setSelectBank(String selectBank) {
        this.selectBank = selectBank;
    }

    class GoodsCatalogSelectHolder extends RecyclerView.ViewHolder {

        TextView bank_select_item_name;

        View bank_select_item_group;

        View bank_select_item_line;

        GoodsCatalogSelectHolder(View itemView) {
            super(itemView);
            bank_select_item_name = (TextView) itemView.findViewById(R.id.bank_select_item_name);
            bank_select_item_group = itemView.findViewById(R.id.bank_select_item_group);
            bank_select_item_line = itemView.findViewById(R.id.bank_select_item_line);
        }
    }
}
