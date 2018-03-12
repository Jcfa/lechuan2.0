package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.poster.MyFansDTO;
import com.poso2o.lechuan.bean.poster.MyFlowsDTO;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;

import java.util.ArrayList;

/**
 * 粉丝列表适配器
 * Created by Luo on 2017/02/10.
 */
public class MyFansAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemListener onItemListener;
    public ArrayList<MyFansDTO> fansBeen = new ArrayList<>();
    private RequestManager glideRequest;
    private int user_type = 0;
    private String contextType = "";

    public MyFansAdapter(Context context, String open, OnItemListener onItemListener) {
        this.context = context;
        this.onItemListener = onItemListener;
        this.fansBeen = new ArrayList<>();
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
        glideRequest = Glide.with(context);
        user_type = SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE);
        contextType = open;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterHolder(LayoutInflater.from(context).inflate(R.layout.view_my_fans_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return fansBeen.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PosterHolder) {
            PosterHolder posterHolder = (PosterHolder) holder;
            final MyFansDTO bean = fansBeen.get(position);
            if (!bean.logo.isEmpty()) {
                glideRequest.load("" + bean.logo).transform(new GlideCircleTransform(context)).into(posterHolder.my_follow_item_img);
                //glideRequest.load("" + fansBeen.get(position).flow_logo).transform(new GlideRoundTransform(context, 5)).into(posterHolder.my_follow_item_img);
            } else {
                posterHolder.my_follow_item_img.setImageResource(R.mipmap.logo_e);
            }
            if (!bean.nick.isEmpty()) {
                posterHolder.my_follow_item_name.setText(bean.nick);
            } else {
                posterHolder.my_follow_item_name.setText(bean.uid+"");
            }
            //posterHolder.poster_item_type;0=商家,1=分销商
            if (bean.user_type == 0) {
                posterHolder.my_follow_item_type.setVisibility(View.VISIBLE);
                posterHolder.my_follow_item_type.setImageResource(R.mipmap.poster_seller_icon);
            } else if (bean.user_type == 1) {
                posterHolder.my_follow_item_type.setVisibility(View.VISIBLE);
                posterHolder.my_follow_item_type.setImageResource(R.mipmap.poster_seller_icon_b);
            } else {
                posterHolder.my_follow_item_type.setVisibility(View.GONE);
            }
//            if (!fansBeen.get(position).remark.isEmpty()) {
//                posterHolder.my_follow_item_remark.setText("" + fansBeen.get(position).user_remark);
//            } else {
//                posterHolder.my_follow_item_remark.setText("这家伙很懒,什么都没有留下");
//            }
            posterHolder.my_follow_item_publish.setText(bean.news_number + "");
            posterHolder.my_follow_item_fans.setText(bean.fans_number + "");
            if (contextType.contains("MyFansActivity")) {
                if (user_type == Constant.MERCHANT_TYPE) {//0=商家,1=分销商,2=商家+分销商
                    if (bean.has_distributor == 0) {//0=不是我的分销商，1=已是我的分销商
                        posterHolder.my_follow_item_distribution_btn.setVisibility(View.VISIBLE);
                        posterHolder.my_follow_item_distribution_btn.setTextColor(Color.parseColor("#5E5E5E"));
                        posterHolder.my_follow_item_distribution_btn.setOnClickListener(new NoDoubleClickListener() {
                            @Override
                            public void onNoDoubleClick(View v) {
                                if (onItemListener != null) {
                                    onItemListener.onClickReward(bean);
                                }
                            }
                        });
                    } else {
                        posterHolder.my_follow_item_distribution_btn.setVisibility(View.GONE);
                        posterHolder.my_follow_item_distribution_btn.setTextColor(Color.parseColor("#FF6537"));
                    }
                } else {
                    //0=没有相互关注，1=已相互关注
                    /*if (fansBeen.get(position).mutual_flow == 0){
                        posterHolder.my_follow_item_had_follow.setVisibility(View.GONE);
                    }else{
                        posterHolder.my_follow_item_had_follow.setVisibility(View.VISIBLE);
                    }*/
                }
            }


            posterHolder.my_follow_item_group.setTag(bean);
            posterHolder.my_follow_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickItem((MyFansDTO) v.getTag());
                    }
                }
            });

        }
    }

    public void notifyData(ArrayList<MyFansDTO> fansBeen) {
        this.fansBeen.clear();
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<MyFansDTO> posterData) {
        if (posterData != null) {
            this.fansBeen.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onClickItem(MyFansDTO fansBeen);

        void onClickReward(MyFansDTO fansBeen);
    }

    public class PosterHolder extends RecyclerView.ViewHolder {

        View my_follow_item_group;
        ImageView my_follow_item_img;
        TextView my_follow_item_name;
        ImageView my_follow_item_type;
        TextView my_follow_item_remark;
        ImageView my_follow_item_had_follow;
        TextView my_follow_item_distribution_btn;
        TextView my_follow_item_publish;
        TextView my_follow_item_fans;

        PosterHolder(View itemView) {
            super(itemView);
            my_follow_item_group = itemView.findViewById(R.id.my_follow_item_group);

            my_follow_item_img = (ImageView) itemView.findViewById(R.id.my_follow_item_img);
            my_follow_item_name = (TextView) itemView.findViewById(R.id.my_follow_item_name);
            my_follow_item_type = (ImageView) itemView.findViewById(R.id.my_follow_item_type);
            my_follow_item_remark = (TextView) itemView.findViewById(R.id.my_follow_item_remark);
            my_follow_item_had_follow = (ImageView) itemView.findViewById(R.id.my_follow_item_had_follow);
            my_follow_item_distribution_btn = (TextView) itemView.findViewById(R.id.my_follow_item_distribution_btn);
            my_follow_item_fans = (TextView) itemView.findViewById(R.id.tv_fans_num);
            my_follow_item_publish = (TextView) itemView.findViewById(R.id.tv_publish_num);
        }
    }
}
