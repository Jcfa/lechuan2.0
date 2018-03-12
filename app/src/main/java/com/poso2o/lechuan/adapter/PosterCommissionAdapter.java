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
import com.poso2o.lechuan.bean.poster.PosterCommissionDTO;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.glide.GlideRoundTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.time.DateTimeUtil;

import java.util.ArrayList;

/**
 * 佣金详情列表适配器
 * Created by Luo on 2017/02/10.
 */
public class PosterCommissionAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemListener onItemListener;
    private ArrayList<PosterCommissionDTO> fansBeen;
    private RequestManager glideRequest;

    public PosterCommissionAdapter(Context context, OnItemListener onItemListener) {
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
        return new PosterHolder(LayoutInflater.from(context).inflate(R.layout.view_commission_details_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return fansBeen.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PosterHolder) {
            PosterHolder posterHolder = (PosterHolder) holder;

            if (!fansBeen.get(position).member_logo.isEmpty()){
                glideRequest.load("" + fansBeen.get(position).member_logo).transform(new GlideCircleTransform(context)).into(posterHolder.commission_details_item_img);
                //glideRequest.load("" + fansBeen.get(position).member_logo).transform(new GlideRoundTransform(context, 5)).into(posterHolder.commission_details_item_img);
            }else{
                posterHolder.commission_details_item_img.setImageResource(R.mipmap.logo_e);
            }

            posterHolder.commission_details_item_name.setText("" + fansBeen.get(position).member_name);
            String date = DateTimeUtil.LongToData(fansBeen.get(position).order_time, "yyyy-MM-dd HH:mm:ss");
            posterHolder.commission_details_item_dat.setText("" + date);
            posterHolder.commission_details_item_price.setText("" + fansBeen.get(position).order_amount);
            posterHolder.commission_details_item_rad.setText("" + fansBeen.get(position).commission_amount);

            posterHolder.commission_details_item_group.setTag(fansBeen.get(position));
            posterHolder.commission_details_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener!=null){
                        onItemListener.onClickItem((PosterCommissionDTO)v.getTag());
                    }
                }
            });
        }
    }

    public void notifyData(ArrayList<PosterCommissionDTO> fansBeen) {
        this.fansBeen.clear();
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onClickItem(PosterCommissionDTO fansBeen);
        void onClickReward(PosterCommissionDTO fansBeen);
    }

    public class PosterHolder extends RecyclerView.ViewHolder {

        View commission_details_item_group;
        ImageView commission_details_item_img;
        TextView commission_details_item_name;
        TextView commission_details_item_dat;
        TextView commission_details_item_price;
        TextView commission_details_item_rad;

        PosterHolder(View itemView) {
            super(itemView);
            commission_details_item_group = itemView.findViewById(R.id.commission_details_item_group);
            commission_details_item_img = (ImageView) itemView.findViewById(R.id.commission_details_item_img);
            commission_details_item_name = (TextView) itemView.findViewById(R.id.commission_details_item_name);
            commission_details_item_dat = (TextView) itemView.findViewById(R.id.commission_details_item_dat);
            commission_details_item_price = (TextView) itemView.findViewById(R.id.commission_details_item_price);
            commission_details_item_rad = (TextView) itemView.findViewById(R.id.commission_details_item_rad);
        }
    }
}
