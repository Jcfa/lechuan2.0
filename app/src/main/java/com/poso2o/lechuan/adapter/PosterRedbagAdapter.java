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
import com.poso2o.lechuan.bean.poster.PosterRedBagDTO;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.glide.GlideRoundTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.time.DateTimeUtil;

import java.util.ArrayList;

/**
 * 红包用户列表适配器
 * Created by Luo on 2017/02/10.
 */
public class PosterRedbagAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemListener onItemListener;
    private ArrayList<PosterRedBagDTO> fansBeen;
    private RequestManager glideRequest;

    public PosterRedbagAdapter(Context context, OnItemListener onItemListener) {
        this.context = context;
        this.onItemListener = onItemListener;
        this.fansBeen = new ArrayList<>();
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
        glideRequest = Glide.with(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterHolder(LayoutInflater.from(context).inflate(R.layout.view_red_bag_details_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return fansBeen.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PosterHolder) {
            PosterHolder posterHolder = (PosterHolder) holder;

            if (!fansBeen.get(position).logo.isEmpty()){
                glideRequest.load("" + fansBeen.get(position).logo).transform(new GlideCircleTransform(context)).into(posterHolder.red_bag_details_item_img);
                //glideRequest.load("" + fansBeen.get(position).logo).transform(new GlideRoundTransform(context, 5)).into(posterHolder.red_bag_details_item_img);
            }else{
                posterHolder.red_bag_details_item_img.setImageResource(R.mipmap.logo_c);
            }

            posterHolder.red_bag_details_item_name.setText("" + fansBeen.get(position).nick);
            String date = DateTimeUtil.LongToData(fansBeen.get(position).build_time, "yyyy-MM-dd HH:mm:ss");
            posterHolder.red_bag_details_item_dat.setText("" + date);
            posterHolder.red_bag_details_item_price.setText("" + fansBeen.get(position).amount);

            posterHolder.red_bag_details_item_group.setTag(fansBeen.get(position));
            posterHolder.red_bag_details_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener!=null){
                        onItemListener.onClickItem((PosterRedBagDTO)v.getTag());
                    }
                }
            });
        }
    }

    public void notifyData(ArrayList<PosterRedBagDTO> fansBeen) {
        this.fansBeen.clear();
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onClickItem(PosterRedBagDTO fansBeen);
        void onClickReward(PosterRedBagDTO fansBeen);
    }

    public class PosterHolder extends RecyclerView.ViewHolder {

        View red_bag_details_item_group;
        ImageView red_bag_details_item_img;
        TextView red_bag_details_item_name;
        TextView red_bag_details_item_dat;
        TextView red_bag_details_item_price;

        PosterHolder(View itemView) {
            super(itemView);
            red_bag_details_item_group = itemView.findViewById(R.id.red_bag_details_item_group);

            red_bag_details_item_img = (ImageView) itemView.findViewById(R.id.red_bag_details_item_img);
            red_bag_details_item_name = (TextView) itemView.findViewById(R.id.red_bag_details_item_name);
            red_bag_details_item_dat = (TextView) itemView.findViewById(R.id.red_bag_details_item_dat);
            red_bag_details_item_price = (TextView) itemView.findViewById(R.id.red_bag_details_item_price);
        }
    }
}
