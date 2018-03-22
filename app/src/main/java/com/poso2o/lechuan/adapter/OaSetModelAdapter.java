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
import com.poso2o.lechuan.bean.oa.TemplateGroup;
import com.poso2o.lechuan.util.DateTimeUtil;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.TextUtil;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/2/5.
 */

public class OaSetModelAdapter extends RecyclerView.Adapter {

    private Context context;
    private int type = 1;  //1是全部，2是已购买，用于显示已购买还是续订
    private ArrayList<TemplateGroup> mDatas;

    public OaSetModelAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public void notifyData(ArrayList<TemplateGroup> groups) {
        mDatas = groups;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_oa_set_model, parent, false);
        return new SetModelVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TemplateGroup group = mDatas.get(position);
        if (group == null) return;
        SetModelVH vh = (SetModelVH) holder;
        vh.oa_set_model_group.setText(group.group_name);

        for(int i =0;i<group.templates.size();i++){
            if (i == 0){
                if (group.templates.get(i).has_default == 1){
                    vh.model_one.setBackgroundResource(R.drawable.bg_oa_packages_select);
                }else {
                    vh.model_one.setBackground(null);
                }
                Glide.with(context).load(group.templates.get(i).pic).placeholder(R.mipmap.background_image).error(R.mipmap.background_image).into(vh.item_model_one);
                vh.item_model_one_des.setText(group.templates.get(i).template_name);
            }else if (i == 1){
                if (group.templates.get(i).has_default == 1){
                    vh.model_two.setBackgroundResource(R.drawable.bg_oa_packages_select);
                }else {
                    vh.model_two.setBackground(null);
                }
                Glide.with(context).load(group.templates.get(i).pic).placeholder(R.mipmap.background_image).error(R.mipmap.background_image).into(vh.item_model_two);
                vh.item_model_two_des.setText(group.templates.get(i).template_name);
            }else if (i == 2){
                if (group.templates.get(i).has_default == 1){
                    vh.model_three.setBackgroundResource(R.drawable.bg_oa_packages_select);
                }else {
                    vh.model_three.setBackground(null);
                }
                Glide.with(context).load(group.templates.get(i).pic).placeholder(R.mipmap.background_image).error(R.mipmap.background_image).into(vh.item_model_three);
                vh.item_model_three_des.setText(group.templates.get(i).template_name);
                break;
            }
        }

        vh.model_group_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onModelGroupListener == null) return;
                onModelGroupListener.onModelGroupClick(group);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) return 0;
        return mDatas.size();
    }

    private OnModelGroupListener onModelGroupListener;

    public interface OnModelGroupListener {
        void onModelGroupClick(TemplateGroup templateGroup);
        void onModelGroupBuy(TemplateGroup templateGroup);
        void onModelGroupContinue(TemplateGroup templateGroup);
    }

    public void setOnModelGroupListener(OnModelGroupListener onModelGroupListener) {
        this.onModelGroupListener = onModelGroupListener;
    }

    class SetModelVH extends RecyclerView.ViewHolder {

        //根布局
        private LinearLayout model_group_view;

        //模板组名称
        private TextView oa_set_model_group;

        //第一个模板
        private LinearLayout model_one;
        //模板一图片
        private ImageView item_model_one;
        //模板一描述
        private TextView item_model_one_des;

        //第二个模板
        private LinearLayout model_two;
        //模板二图片
        private ImageView item_model_two;
        //模板二描述
        private TextView item_model_two_des;

        //第三个模板
        private LinearLayout model_three;
        //模板三图片
        private ImageView item_model_three;
        //模板三描述
        private TextView item_model_three_des;

        public SetModelVH(View itemView) {
            super(itemView);

            model_group_view = (LinearLayout) itemView.findViewById(R.id.model_group_view);

            oa_set_model_group = (TextView) itemView.findViewById(R.id.oa_set_model_group);

            model_one = (LinearLayout) itemView.findViewById(R.id.model_one);
            item_model_one = (ImageView) itemView.findViewById(R.id.item_model_one);
            item_model_one_des = (TextView) itemView.findViewById(R.id.item_model_one_des);

            model_two = (LinearLayout) itemView.findViewById(R.id.model_two);
            item_model_two = (ImageView) itemView.findViewById(R.id.item_model_two);
            item_model_two_des = (TextView) itemView.findViewById(R.id.item_model_two_des);

            model_three = (LinearLayout) itemView.findViewById(R.id.model_three);
            item_model_three = (ImageView) itemView.findViewById(R.id.item_model_three);
            item_model_three_des = (TextView) itemView.findViewById(R.id.item_model_three_des);
        }
    }
}
