package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.tool.edit.TextUtils;

import java.util.ArrayList;

/**
 * 文章筛选列表适配器
 *
 * Created by Jaydon on 2018/1/29.
 */
public class ArticleFiltrateAdapter extends BaseAdapter<ArticleFiltrateAdapter.FiltrateHolder, String> {

    private ArrayList<String> selects = new ArrayList<>();

    public ArticleFiltrateAdapter(Context context, ArrayList<String> data) {
        super(context, data);
    }

    @Override
    public FiltrateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FiltrateHolder(getItemView(R.layout.view_filtrate_item, parent));
    }

    @Override
    public void initItemView(FiltrateHolder holder, final String item, int position) {
        holder.filtrate_name.setText(item);
        holder.filtrate_name.setSelected(findSelect(item) != null);
        holder.filtrate_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = findSelect(item);
                if (str == null) {
                    selects.add(item);
                } else {
                    selects.remove(str);
                }
                notifyDataSetChanged();
            }
        });
    }

    public ArrayList<String> getSelects() {
        return selects;
    }

    private String findSelect(String item) {
        for (String str : selects) {
            if (TextUtils.equals(str, item)) {
                return str;
            }
        }
        return null;
    }

    class FiltrateHolder extends BaseViewHolder {

        TextView filtrate_name;

        public FiltrateHolder(View itemView) {
            super(itemView);
            filtrate_name = getView(R.id.filtrate_name);
        }
    }
}
