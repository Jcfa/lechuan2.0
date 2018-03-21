package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.oa.TemplateBean;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/2/6.
 */

public class ItemOaGroupModelAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<TemplateBean> mDatas;

    public ItemOaGroupModelAdapter(Context context) {
        this.context = context;
    }

    public void notifyData(ArrayList<TemplateBean> templateBeen){
        mDatas = templateBeen;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_model_group_list,parent,false);
        return new GroupModelVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TemplateBean bean = mDatas.get(position);
        if (bean == null)return;
        GroupModelVH vh = (GroupModelVH) holder;
        Glide.with(context).load(bean.pic).placeholder(R.mipmap.background_image).error(R.mipmap.background_image).into(vh.group_model_pic);
        vh.group_model_name.setText(bean.template_name);
        if (bean.has_default == 1){
            vh.group_model_item.setSelected(true);
        }else {
            vh.group_model_item.setSelected(false);
        }

        vh.group_model_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onModelListener != null)onModelListener.onModelClick(bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 0;
        return mDatas.size();
    }

    private OnModelListener onModelListener;
    public interface OnModelListener{
        void onModelClick(TemplateBean templateBean);
    }
    public void setOnModelListener(OnModelListener onModelListener){
        this.onModelListener = onModelListener;
    }

    class GroupModelVH extends RecyclerView.ViewHolder{

        LinearLayout group_model_item;
        ImageView group_model_pic;
        TextView group_model_name;

        public GroupModelVH(View itemView) {
            super(itemView);
            group_model_item = (LinearLayout) itemView.findViewById(R.id.group_model_item);
            group_model_pic = (ImageView) itemView.findViewById(R.id.group_model_pic);
            group_model_name = (TextView) itemView.findViewById(R.id.group_model_name);
        }
    }
}
