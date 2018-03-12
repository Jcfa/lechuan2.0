package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.poster.MyFlowsDTO;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.glide.GlideRoundTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

import java.util.ArrayList;

/**
 * 红包用户列表适配器
 * Created by Luo on 2017/02/10.
 */
public class MyFollowAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemListener onItemListener;
    public ArrayList<MyFlowsDTO> fansBeen=new ArrayList<>();
    private RequestManager glideRequest;
    private String contextType = "";

    public MyFollowAdapter(Context context,String open, OnItemListener onItemListener) {
        this.context = context;
        this.onItemListener = onItemListener;
        this.fansBeen = new ArrayList<>();
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
        glideRequest = Glide.with(context);
        contextType = open;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterHolder(LayoutInflater.from(context).inflate(R.layout.view_my_follow_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return fansBeen.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PosterHolder) {
            PosterHolder posterHolder = (PosterHolder) holder;

            if (!fansBeen.get(position).flow_logo.isEmpty()){
                glideRequest.load("" + fansBeen.get(position).flow_logo).transform(new GlideCircleTransform(context)).into(posterHolder.my_follow_item_img);
                //glideRequest.load("" + fansBeen.get(position).flow_logo).transform(new GlideRoundTransform(context, 5)).into(posterHolder.my_follow_item_img);
            }else{
                posterHolder.my_follow_item_img.setImageResource(R.mipmap.logo_e);
            }
            if (!fansBeen.get(position).flow_nick.isEmpty()){
                posterHolder.my_follow_item_name.setText("" + fansBeen.get(position).flow_nick);
            }else{
                posterHolder.my_follow_item_name.setText("用户名称");
            }
            //posterHolder.poster_item_type;0=商家,1=分销商
            if (fansBeen.get(position).flow_user_type == Constant.MERCHANT_TYPE){
                posterHolder.my_follow_item_type.setVisibility(View.VISIBLE);
                posterHolder.my_follow_item_type.setImageResource(R.mipmap.poster_seller_icon);
            }else if (fansBeen.get(position).flow_user_type == Constant.DISTRIBUTION_TYPE){
                posterHolder.my_follow_item_type.setVisibility(View.VISIBLE);
                posterHolder.my_follow_item_type.setImageResource(R.mipmap.poster_seller_icon_b);
            }else{
                posterHolder.my_follow_item_type.setVisibility(View.GONE);
            }
            if (!fansBeen.get(position).remark.isEmpty()){
                posterHolder.my_follow_item_remark.setText("" + fansBeen.get(position).remark);
            }else{
                posterHolder.my_follow_item_remark.setText("这家伙很懒,什么都没有留下");
            }

            if (contextType.contains("MyFollowActivity")){
                //0=没有相互关注，1=已相互关注
                if (fansBeen.get(position).mutual_flow == 0){
                    posterHolder.my_follow_item_had_follow.setVisibility(View.GONE);
                }else{
                    posterHolder.my_follow_item_had_follow.setVisibility(View.VISIBLE);
                }
            }

            posterHolder.my_follow_item_group.setTag(fansBeen.get(position));
            posterHolder.my_follow_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener!=null){
                        onItemListener.onClickItem((MyFlowsDTO)v.getTag());
                    }
                }
            });
        }
    }

    public void notifyData(ArrayList<MyFlowsDTO> fansBeen) {
        this.fansBeen.clear();
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<MyFlowsDTO> posterData) {
        if (posterData != null) {
            this.fansBeen.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onClickItem(MyFlowsDTO fansBeen);
        void onClickReward(MyFlowsDTO fansBeen);
    }

    public class PosterHolder extends RecyclerView.ViewHolder {

        View my_follow_item_group;
        ImageView my_follow_item_img;
        TextView my_follow_item_name;
        ImageView my_follow_item_type;
        TextView my_follow_item_remark;
        ImageView my_follow_item_had_follow;

        PosterHolder(View itemView) {
            super(itemView);
            my_follow_item_group = itemView.findViewById(R.id.my_follow_item_group);

            my_follow_item_img = (ImageView) itemView.findViewById(R.id.my_follow_item_img);
            my_follow_item_name = (TextView) itemView.findViewById(R.id.my_follow_item_name);
            my_follow_item_type = (ImageView) itemView.findViewById(R.id.my_follow_item_type);
            my_follow_item_remark = (TextView) itemView.findViewById(R.id.my_follow_item_remark);
            my_follow_item_had_follow = (ImageView) itemView.findViewById(R.id.my_follow_item_had_follow);
        }
    }
}
