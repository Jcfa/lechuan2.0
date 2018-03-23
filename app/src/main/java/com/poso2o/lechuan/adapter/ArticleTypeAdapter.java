package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.oa.OaTypeBean;
import com.poso2o.lechuan.tool.edit.TextUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;

import java.util.ArrayList;

/**
 * 文章类型列表适配器
 *
 */
public class ArticleTypeAdapter extends BaseAdapter<ArticleTypeAdapter.OaTypeHolder, OaTypeBean> {

    private ArrayList<String> selects = new ArrayList<>();

    public ArticleTypeAdapter(Context context, ArrayList<OaTypeBean> data) {
        super(context, data);
        initSelect();
    }

    @Override
    public OaTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OaTypeHolder(getItemView(R.layout.view_filtrate_item, parent));
    }

    @Override
    public void initItemView(OaTypeHolder holder, final OaTypeBean item, int position) {
        holder.filtrate_name.setText(item.type_name);
        holder.filtrate_name.setSelected(findSelect(item));
        holder.filtrate_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (findSelect(item)) {
                    selects.remove(item.type_id);
                } else {
                    selects.add(item.type_id);
                }
                notifyDataSetChanged();
            }
        });
    }

    public ArrayList<String> getSelects() {
        return selects;
    }

    private boolean findSelect(OaTypeBean item) {
        for (String str : selects) {
            if (TextUtils.equals(str, item.type_id)) {
                return true;
            }
        }
        return false;
    }

    class OaTypeHolder extends BaseViewHolder {

        TextView filtrate_name;

        public OaTypeHolder(View itemView) {
            super(itemView);
            filtrate_name = getView(R.id.filtrate_name);
        }
    }

    private void initSelect(){
        String types = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_OA_TYPES);
        if (TextUtil.isNotEmpty(types)){
            String[] str = types.split(",");
            for (int i=0;i<str.length;i++){
                selects.add(str[i]);
            }
        }
    }
}
