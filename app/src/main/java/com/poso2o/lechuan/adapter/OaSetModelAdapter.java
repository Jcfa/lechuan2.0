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
        if (type == 1) {
            //全部
            vh.oa_set_model_group_num.setText(group.templates.size() + "");
            if (group.has_buy.equals("1")) {
                //已购买,显示已购买，价格、订购和续订隐藏
                vh.oa_set_model_buy.setVisibility(View.GONE);
                vh.oa_model_group_bought.setVisibility(View.VISIBLE);
                vh.oa_set_model_price.setVisibility(View.INVISIBLE);
            } else {
                //没购买，显示价格和购买，隐藏续订和已购买
                vh.oa_set_model_price.setVisibility(View.VISIBLE);
                vh.oa_set_model_price.setText("¥" + NumberFormatUtils.format(group.amount));
                vh.oa_set_model_buy.setVisibility(View.VISIBLE);
                vh.oa_model_group_bought.setVisibility(View.GONE);
            }
        } else {
            //已购买
            //描述布局隐藏，到期时间显示，价格和购买和已购买隐藏，续订显示
            vh.oa_set_model_group_des.setVisibility(View.GONE);
            vh.oa_set_model_group_timeout.setVisibility(View.VISIBLE);

            vh.oa_set_model_price.setVisibility(View.GONE);
            vh.oa_set_model_buy.setVisibility(View.GONE);
            vh.oa_model_group_bought.setVisibility(View.GONE);
            vh.oa_model_group_continue.setVisibility(View.VISIBLE);

            if (TextUtil.isEmpty(group.unit))group.unit = "0";
            vh.oa_model_time_out_date.setText(DateTimeUtil.StringToData(group.unit,"yyyy-MM-dd"));
            vh.oa_mode_time_less.setText(group.template_service_day);
        }

        for(int i =0;i<group.templates.size();i++){
            if (i == 0){
                Glide.with(context).load(group.templates.get(i).pic).placeholder(R.mipmap.background_image).error(R.mipmap.background_image).into(vh.item_model_one);
                vh.item_model_one_des.setText(group.templates.get(i).template_name);
            }else if (i == 1){
                Glide.with(context).load(group.templates.get(i).pic).placeholder(R.mipmap.background_image).error(R.mipmap.background_image).into(vh.item_model_two);
                vh.item_model_two_des.setText(group.templates.get(i).template_name);
            }else if (i == 2){
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

        vh.oa_set_model_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onModelGroupListener == null) return;
                onModelGroupListener.onModelGroupBuy(group);
            }
        });

        vh.oa_model_group_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onModelGroupListener == null) return;
                onModelGroupListener.onModelGroupContinue(group);
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
        //描述布局
        private LinearLayout oa_set_model_group_des;
        //模板组数量
        private TextView oa_set_model_group_num;

        //到期布局
        private LinearLayout oa_set_model_group_timeout;
        //到期时间
        private TextView oa_model_time_out_date;
        //剩余天数
        private TextView oa_mode_time_less;

        //模板组价格
        private TextView oa_set_model_price;
        //购买
        private TextView oa_set_model_buy;

        //续费
        private TextView oa_model_group_continue;

        //已购买
        private TextView oa_model_group_bought;

        //模板一图片
        private ImageView item_model_one;
        //模板二图片
        private ImageView item_model_two;
        //模板三图片
        private ImageView item_model_three;

        //模板一描述
        private TextView item_model_one_des;
        //模板二描述
        private TextView item_model_two_des;
        //模板三描述
        private TextView item_model_three_des;

        public SetModelVH(View itemView) {
            super(itemView);

            model_group_view = (LinearLayout) itemView.findViewById(R.id.model_group_view);

            oa_set_model_group = (TextView) itemView.findViewById(R.id.oa_set_model_group);
            oa_set_model_group_des = (LinearLayout) itemView.findViewById(R.id.oa_set_model_group_des);
            oa_set_model_group_num = (TextView) itemView.findViewById(R.id.oa_set_model_group_num);

            oa_set_model_group_timeout = (LinearLayout) itemView.findViewById(R.id.oa_set_model_group_timeout);
            oa_model_time_out_date = (TextView) itemView.findViewById(R.id.oa_model_time_out_date);
            oa_mode_time_less = (TextView) itemView.findViewById(R.id.oa_mode_time_less);

            oa_set_model_price = (TextView) itemView.findViewById(R.id.oa_set_model_price);
            oa_set_model_buy = (TextView) itemView.findViewById(R.id.oa_set_model_buy);

            oa_model_group_continue = (TextView) itemView.findViewById(R.id.oa_model_group_continue);

            oa_model_group_bought = (TextView) itemView.findViewById(R.id.oa_model_group_bought);

            item_model_one = (ImageView) itemView.findViewById(R.id.item_model_one);
            item_model_two = (ImageView) itemView.findViewById(R.id.item_model_two);
            item_model_three = (ImageView) itemView.findViewById(R.id.item_model_three);

            item_model_one_des = (TextView) itemView.findViewById(R.id.item_model_one_des);
            item_model_two_des = (TextView) itemView.findViewById(R.id.item_model_two_des);
            item_model_three_des = (TextView) itemView.findViewById(R.id.item_model_three_des);
        }
    }
}
