package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.oa.OaLablesBean;
import com.poso2o.lechuan.tool.edit.TextUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/3/16.
 *
 * 文章标签适配器
 */

public class ArticleLabelAdapter extends BaseAdapter<ArticleLabelAdapter.OaLaybelHolder,OaLablesBean> {

    private ArrayList<String> selects = new ArrayList<>();

    public ArticleLabelAdapter(Context context, ArrayList<OaLablesBean> data) {
        super(context, data);
        initSelect();
    }

    @Override
    public void initItemView(OaLaybelHolder holder,final OaLablesBean item, int position) {
        holder.filtrate_name.setText(item.label_name);
        holder.filtrate_name.setSelected(findSelect(item));
        holder.filtrate_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (findSelect(item)) {
                    selects.remove(item.label_name);
                } else {
                    selects.add(item.label_name);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public OaLaybelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OaLaybelHolder(getItemView(R.layout.view_filtrate_item, parent));
    }

    public ArrayList<String> getSelects() {
        return selects;
    }

    private boolean findSelect(OaLablesBean item) {
        for (String str : selects) {
            if (TextUtils.equals(str, item.label_name)) {
                return true;
            }
        }
        return false;
    }

    class OaLaybelHolder extends BaseViewHolder {

        TextView filtrate_name;

        public OaLaybelHolder(View itemView) {
            super(itemView);
            filtrate_name = getView(R.id.filtrate_name);
        }
    }

    private void initSelect(){
        String types = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_OA_LAYBELS);
        if (TextUtil.isNotEmpty(types)){
            String[] str = types.split(",");
            for (int i=0;i<str.length;i++){
                selects.add(str[i]);
            }
        }
    }
}
